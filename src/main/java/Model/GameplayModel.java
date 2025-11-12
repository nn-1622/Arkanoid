package Model;

import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.*;

import Controller.EventLoader;
import Controller.GameEvent;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;

/**
 * quản lý toàn bộ logic và trạng thái của chế độ chơi chính.
 * bao gồm xử lý bóng, gạch, vật phẩm, điểm, mạng, cấp độ, hiệu ứng và va chạm.
 */
public class GameplayModel implements UltilityValues {
    private Image background;
    private Paddle paddle;
    private BallState currentBallState;

    private double currentVx;

    private ArrayList<Brick> brick;
    private ArrayList<Ball> balls = new ArrayList<>();
    private final ArrayList<MovableObject> fallingPowerUps = new ArrayList<>();
    private final ArrayList<PowerUp> activePowerUps = new ArrayList<>();
    private final ArrayList<LaserShot> lasers = new ArrayList<>();

    private int level;
    private int lives;
    private int score;
    private int combo;
    private int scoreMultiplier = 1;

    private final EventLoader eventLoader;

    private boolean fading = false;
    private long fadeStartTime;

    private final boolean twoPlayerMode;
    private boolean completedAllLevels = false;
    private boolean levelFinished = false;
    private boolean waitingForOtherPlayer = false;
    private boolean isWinner = false;
    private boolean isLoser = false;
    private boolean isDraw = false;

    private static final String CONFIG_FILE = "ball_config.txt";
    private static final String DEFAULT_BALL = "DefaultBall.png";
    private static final String BG_CONFIG_FILE = "background_config.txt";
    private static final String DEFAULT_BG = "/GameBG.png";
    private static final String PADDLE_CONFIG_FILE = "paddle_config.txt";
    private static final String DEFAULT_PADDLE = "/DefaultPaddle.png";
    private String ballPath = "DefaultBall.png";

    /**
     * khởi tạo trò chơi 1 người chơi.
     * tải hình nền, paddle, bóng và bản đồ đầu tiên.
     */
    public GameplayModel(EventLoader eventLoader) {
        this.eventLoader = eventLoader;
        this.twoPlayerMode = false;

        this.ballPath = loadBallPathFromFile();
        this.background = loadBackgroundFromFile();

        paddle = Paddle.getPaddle(loadPaddlePathFromFile());

        Ball ball = new Ball(paddle.x + paddleLength / 2, paddle.y - paddleHeight / 2, 0, 0, 10, ballPath);
        currentBallState = BallState.ATTACHED;
        balls.add(ball);
        lives = 5;
        level = 1;
        score = 0;
        combo = 0;
        currentVx = 0;
        renderMap();
    }

    /**
     * khởi tạo cho chế độ 2 người chơi.
     */
    public GameplayModel(EventLoader eventLoader, double leftBound, double rightBound) {
        this.eventLoader = eventLoader;
        this.twoPlayerMode = true;
        paddle.setX((leftBound + rightBound - paddle.getLength()) / 2);
    }

    /**
     * khởi tạo gameplay có hoặc không ở chế độ 2 người.
     */
    public GameplayModel(EventLoader eventLoader, boolean twoPlayerMode) {
        String paddlePath = loadPaddlePathFromFile();

        this.eventLoader = eventLoader;
        this.twoPlayerMode = twoPlayerMode;
        this.ballPath = loadBallPathFromFile();
        this.background = loadBackgroundFromFile();
        this.paddle = Paddle.getPaddle(loadPaddlePathFromFile());

        this.paddle = Paddle.newInstance(
                UltilityValues.canvasWidth / 2.0 - UltilityValues.paddleLength / 2.0,
                UltilityValues.canvasHeight - 140,
                UltilityValues.paddleLength,
                UltilityValues.paddleHeight,
                paddlePath
        );

        Ball ball = new Ball(
                paddle.x + paddleLength / 2.0,
                paddle.y - paddleHeight / 2.0,
                0, 0, 10, ballPath
        );

        this.currentBallState = BallState.ATTACHED;
        this.balls.add(ball);
        this.lives = 5;
        this.level = 1;
        this.score = 0;
        this.combo = 0;
        this.currentVx = 0;
        renderMap();
    }

    /**
     * tải đường dẫn paddle từ file cấu hình, trả về mặc định nếu không có.
     */
    private String loadPaddlePathFromFile() {
        try {
            Path p = Path.of(PADDLE_CONFIG_FILE);
            if (Files.exists(p)) {
                String s = Files.readString(p, StandardCharsets.UTF_8).trim();
                if (!s.isEmpty()) {
                    System.out.println("Set paddle from config: " + s);
                    return s;
                }
            } else {
                System.out.println("Paddle config not found, use default");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return DEFAULT_PADDLE;
    }

    /**
     * tải hình nền.
     */
    private Image loadBackgroundFromFile() {
        String path = DEFAULT_BG;
        try {
            Path p = Path.of(BG_CONFIG_FILE);
            if (Files.exists(p)) {
                String s = Files.readString(p, StandardCharsets.UTF_8).trim();
                if (!s.isEmpty()) {
                    path = s;
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return new Image(getClass().getResource(path).toExternalForm());
    }

    /**
     * tải đường dẫn bóng.
     */
    private String loadBallPathFromFile() {
        try {
            Path path = Path.of(CONFIG_FILE);
            if (Files.exists(path)) {
                String data = Files.readString(path, StandardCharsets.UTF_8).trim();
                if (!data.isEmpty()) return data;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return DEFAULT_BALL;
    }

    /**
     * phóng bóng ra khỏi paddle
     */
    public void launchBall() {
        if (this.currentBallState == BallState.ATTACHED) {
            this.currentBallState = BallState.LAUNCHED;
            for (Ball ball : balls) {
                ball.setVx(0);
                ball.setVy(-3 - currentVx);
            }
        }
    }

    /**
     * thêm một viên gạch vào bản đồ
     */
    public void addBrickType(int type, double x, double y) {
        Brick newBrick = new Brick(x, y);
        newBrick.setBrickType(type);
        brick.add(newBrick);
    }

    /**
     * tạo lại bản đồ từ file map theo cấp độ hiện tại.
     */
    public void renderMap() {
        this.brick = new ArrayList<>();
        try (InputStream map = getClass().getResourceAsStream(String.format("/map/%d.txt", level));
             Scanner scan = new Scanner(map)) {
            double spawnX = 0;
            double spawnY = 0;
            while (scan.hasNextLine()) {
                String line = scan.nextLine();
                for (char num : line.toCharArray()) {
                    switch (num) {
                        case '0':
                            break;

                        case '1':
                            addBrickType(1, spawnX * 50, spawnY * 25);
                            break;

                        case '2':
                            addBrickType(2, spawnX * 50, spawnY * 25);
                            break;

                        case '3':
                            addBrickType(3, spawnX * 50, spawnY * 25);
                            break;

                        case '4':
                            addBrickType(4, spawnX * 50, spawnY * 25);
                            break;
                    }
                    spawnX++;
                }
                spawnX = 0;
                spawnY++;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * đặt lại ví trí của bóng khi mất mạng.
     */
    public void resetPosition() {
        paddle.setX(canvasWidth / 2 - paddle.getLength() / 2);
        paddle.setY(canvasHeight - 140);
        balls.clear();
        Ball ball = new Ball(paddle.x + paddleLength / 2, paddle.y - paddleHeight / 2, 0, 0, 10, ballPath);
        balls.add(ball);
        currentBallState = BallState.ATTACHED;
    }

    /**
     * sinh ngẫu nhiên một vật phẩm tại vị trí chỉ định.
     */
    public void spawnPowerUp(double x, double y) {
        Random rand = new Random();
        int type = rand.nextInt(7);
        MovableObject pu;
        switch (type) {
            case 1 -> pu = new PU_MultiBall(x, y, 0, 2, 15);
            case 2 -> pu = new PU_Laser(x, y, 0, 2, 15);
            case 3 -> pu = new PU_ExtraLive(x, y, 15);
            case 4 -> pu = new PU_Shield(x, y, 0, 2, 15);
            case 5 -> pu = new PU_BombBall(x, y, 0, 2, 15);
            case 6 -> pu = new PU_ScoreX2(x, y, 0, 2, 15);
            default -> pu = new PU_Expand(x, y, 0, 2, 15);
        }
        fallingPowerUps.add(pu);
    }

    /**
     * kiểm tra va chạm.
     */
    public void checkCollisions() {
        paddle.checkBoundary(canvasWidth);
        for (Ball ball : new ArrayList<>(balls)) {
            if (currentBallState == BallState.ATTACHED) {
                ball.attachToPaddle(paddle);
            } else if (currentBallState == BallState.LAUNCHED) {
                ball.checkWallCollision(canvasWidth, canvasHeight, this);
                ball.checkPaddleCollision(paddle);
                ball.checkBrickCollision(brick, this);
            }
        }
        for (LaserShot l : lasers) {
            l.checkLaser(this);
        }
    }

    /**
     * xóa toàn bộ hiệu ứng và vật phẩm đang hoạt động.
     */
    public void resetPowerUp() {
        for (PowerUp p : activePowerUps) {
            p.remove(this);
        }
        activePowerUps.clear();
        fallingPowerUps.clear();
        lasers.clear();
    }

    /**
     * chuyển sang level kế tiếp và đặt lại trạng thái.
     */
    public void Initialize() {
        if (level >= 5) {
            completedAllLevels = true;
            return;
        }

        level++;
        renderMap();

        paddle.setX(canvasWidth / 2.0 - paddleLength / 2.0);
        paddle.setY(canvasHeight - 140);
        paddle.setLength(paddleLength);

        balls.clear();
        Ball newBall = new Ball(
                paddle.getX() + paddle.getLength() / 2,
                paddle.getY() - paddle.getHeight() / 2,
                0, 0, 10, ballPath
        );
        balls.add(newBall);
        currentBallState = BallState.ATTACHED;

        fallingPowerUps.clear();
        activePowerUps.clear();
        lasers.clear();

        combo = 0;
        scoreMultiplier = 1;
        currentVx = 0;

        levelFinished = false;
        waitingForOtherPlayer = false;
        fading = false;
        fadeStartTime = -1;
        isWinner = false;
        isLoser = false;
        isDraw = false;

        eventLoader.loadEvent(GameEvent.AUTO_SAVE_TRIGGER);
    }

    /**
     * cập nhật logic cho mỗi khung hình
     */
    public void update(boolean left, boolean right, double deltaTime) {
        long frameTime = 16_666_667 / 1_000_000_000;

        if (deltaTime < frameTime) {
            return;
        }

        this.paddle.move(left, right);
        for (Ball x : balls) {
            x.move();
        }

        paddle.update(deltaTime);

        for (Brick b : brick) {
            b.update(deltaTime);
            if (b.isDestroyed()) {
                if (Math.random() <= 0.3) {
                    double dropX = b.getX() + b.getWidth() / 2;
                    double dropY = b.getY() + b.getHeight() / 2;
                    spawnPowerUp(dropX, dropY);
                }
            }
        }

        checkCollisions();
        brick.removeIf(Brick::isDestroyed);

        for (int i = 0; i < fallingPowerUps.size(); i++) {
            MovableObject pu = fallingPowerUps.get(i);
            pu.move();

            boolean overlapX = pu.getX() + pu.getWidth() >= paddle.getX() &&
                    pu.getX() <= paddle.getX() + paddle.getLength();
            boolean overlapY = pu.getY() + pu.getHeight() >= paddle.getY() &&
                    pu.getY() <= paddle.getY() + paddle.getHeight();

            if (overlapX && overlapY && pu instanceof PowerUp powerUp) {
                powerUp.apply(this);
                getEventLoader().loadEvent(GameEvent.POWER_UP);
                activePowerUps.add(powerUp);
                fallingPowerUps.remove(i);
                i--;
                continue;
            }

            if (pu.getY() > canvasHeight) {
                fallingPowerUps.remove(i);
                i--;
            }
        }

        if (!lasers.isEmpty()) {
            for (int i = 0; i < lasers.size(); i++) {
                LaserShot l = lasers.get(i);
                l.update();
                l.checkLaser(this);
                if (l.isDestroyed()) {
                    lasers.remove(i);
                    i--;
                }
            }
        }

        for (int i = 0; i < activePowerUps.size(); i++) {
            PowerUp p = activePowerUps.get(i);
            p.update(this, deltaTime);
            if (!p.isActive()) {
                p.remove(this);
                activePowerUps.remove(i);
                i--;
            }
        }

        if (brick.isEmpty()) {
            if (twoPlayerMode) {
                levelFinished = true;
            } else {
                eventLoader.loadEvent(GameEvent.LEVEL_COMPLETE);
            }
        }

        if (!twoPlayerMode) {
            if (hasCompletedAllLevels()) {
                eventLoader.loadEvent(GameEvent.GAME_WIN);
            }
            if (lives <= 0) {
                eventLoader.loadEvent(GameEvent.GAME_LOST);
            }
        }
    }

    /**
     * thiết lập lại trạng thái game từ dữ liệu lưu.
     */
    public void configureFromSave(SaveState save) {
        this.level = save.level;
        this.lives = save.lives;
        this.score = save.score;
        this.combo = save.combo;

        renderMap();

        Paddle p = getPaddle();
        p.setX(save.paddleX);
        p.setLength(save.paddleLength);
        p.setShield(save.paddleShield);

        balls.clear();
        for (SaveState.BallData ballData : save.balls) {
            Ball b = new Ball(ballData.x, ballData.y, ballData.vx, ballData.vy, 10, "/DefaultBall.png");
            b.setBomb(ballData.isBomb);
            balls.add(b);
        }

        if (!balls.isEmpty() && balls.get(0).getVy() != 0) {
            currentBallState = BallState.LAUNCHED;
        } else {
            currentBallState = BallState.ATTACHED;
        }

        ArrayList<Brick> loadedBricks = new ArrayList<>();

        for (Brick templateBrick : this.brick) {
            boolean foundInSave = false;
            for (SaveState.BrickData savedBrick : save.bricks) {
                if (templateBrick.getX() == savedBrick.x && templateBrick.getY() == savedBrick.y) {
                    templateBrick.setBrickType(savedBrick.brickType);
                    templateBrick.frameTimer = savedBrick.frameTimer;

                    if (savedBrick.isBreaking) {
                        templateBrick.hit();
                    }

                    if (templateBrick.getBrickType() > 0) {
                        loadedBricks.add(templateBrick);
                    }

                    foundInSave = true;
                    break;
                }
            }

            if (!foundInSave && templateBrick.getBrickType() > 0) {
                loadedBricks.add(templateBrick);
            }
        }
        this.brick = loadedBricks;

        activePowerUps.clear();
        fallingPowerUps.clear();
        lasers.clear();

        if (save.fallingPowerUps != null) {
            for (SaveState.FallingPowerUpData puData : save.fallingPowerUps) {
                MovableObject puObj = createPowerUpByName(puData.name, puData.x, puData.y, puData.vx, puData.vy);
                if (puObj != null) {
                    fallingPowerUps.add(puObj);
                }
            }
        }

        if (save.activePowerUps != null) {
            for (SaveState.ActivePowerUpData puData : save.activePowerUps) {
                MovableObject puObj = createPowerUpByName(puData.name, 0, 0, 0, 0);
                if (puObj instanceof PowerUp pu) {

                    pu.apply(this);
                    pu.setElapsedMs(puData.elapsedMs);

                    activePowerUps.add(pu);
                }
            }
        }
    }

    /**
     * xử lý power up theo state
     */
    private MovableObject createPowerUpByName(String name, double x, double y, double vx, double vy) {
        double radius = 15;
        return switch (name) {
            case "Expand" -> new PU_Expand(x, y, vx, vy, radius);
            case "Multi Ball" -> new PU_MultiBall(x, y, vx, vy, radius);
            case "Laser" -> new PU_Laser(x, y, vx, vy, radius);
            case "Extra Live" -> new PU_ExtraLive(x, y, radius);
            case "Shield" -> new PU_Shield(x, y, vx, vy, radius);
            case "BombBall" -> new PU_BombBall(x, y, vx, vy, radius);
            case "Score x2" -> new PU_ScoreX2(x, y, vx, vy, radius);
            default -> {
                System.err.println("CẢNH BÁO: Không nhận diện được tên Power-up khi tải: " + name);
                yield null;
            }
        };
    }

    /**
     * Vẽ biểu tượng của các Power-Up đang hoạt động.
     * @param g
     */
    public void drawActivePowerUps(GraphicsContext g) {
        if (activePowerUps.isEmpty()) return;

        double iconSize = 32;
        double spacing = 10;
        double startX = 25;
        double startY = canvasHeight - 105;

        Set<String> drawn = new HashSet<>();

        for (PowerUp p : activePowerUps) {
            if (!p.isActive()) continue;
            String name = p.getName();
            if (drawn.contains(name)) continue;

            Image icon = PU_Icons.getIcon(name);
            g.drawImage(icon, startX, startY, iconSize, iconSize);

            if (p.getDurationMs() > 0) {
                double remaining = Math.max(0, p.getDurationMs() - p.getElapsedMs()) / 1000.0;
                g.setFill(javafx.scene.paint.Color.WHITE);
                g.setFont(javafx.scene.text.Font.font("Consolas", 10));
                g.fillText(String.format("%.1fs", remaining),
                        startX + 2, startY + iconSize + 12);
            }

            startX += iconSize + spacing;
            drawn.add(name);
        }
    }

    /**
     * Kiểm tra xem Power-Up có đang hoạt động hay không.
     */
    public boolean hasActivePower(String name) {
        for (PowerUp p : activePowerUps) {
            if (p.getName().equalsIgnoreCase(name) && p.isActive()) {
                return true;
            }
        }
        return false;
    }

    /**
     * Vẽ hiệu ứng hình ảnh của các Power-Up.
     * @param g
     */
    public void drawEffects(GraphicsContext g) {
        Paddle paddle = getPaddle();

        if (hasActivePower("Score x2")) {
            g.setGlobalAlpha(0.3);
            g.setFill(javafx.scene.paint.Color.GOLD);
            g.fillRoundRect(paddle.getX() - 5, paddle.getY() - 5,
                    paddle.getLength() + 10, paddle.getHeight() + 10, 15, 15);
            g.setGlobalAlpha(1.0);
        }

        if (hasActivePower("Shield")) {
            g.setStroke(javafx.scene.paint.Color.CYAN);
            g.setLineWidth(3);
            g.strokeRoundRect(paddle.getX() - 5,
                    paddle.getY() + paddle.getHeight() + 5,
                    paddle.getLength() + 10, 10, 10, 10);
        }
    }


    public int getLevel() {
        return level;
    }

    public String getBallPath() {
        return ballPath;
    }

    public boolean isWaitingForOtherPlayer() {
        return waitingForOtherPlayer;
    }

    public void setWaitingForOtherPlayer(boolean waiting) {
        this.waitingForOtherPlayer = waiting;
    }

    public void setWinner(boolean winner) {
        this.isWinner = winner;
        if (winner) {
            this.isLoser = false;
            this.isDraw = false;
        }
    }

    public void setLoser(boolean loser) {
        this.isLoser = loser;
        if (loser) {
            this.isWinner = false;
            this.isDraw = false;
        }
    }

    public void setDraw(boolean draw) {
        this.isDraw = draw;
        if (draw) {
            this.isWinner = false;
            this.isLoser = false;
        }
    }

    public boolean hasCompletedAllLevels() {
        return completedAllLevels;
    }

    public boolean isFading() {
        return fading;
    }

    public long getFadeStartTime() {
        return fadeStartTime;
    }

    public void startFade(long nowNano) {
        this.fading = true;
        this.fadeStartTime = nowNano;
    }

    public void stopFade() {
        this.fading = false;
    }

    public boolean isLevelFinished() {
        return levelFinished;
    }

    public boolean isWinner() {
        return isWinner;
    }

    public boolean isLoser() {
        return isLoser;
    }

    public boolean isDraw() {
        return isDraw;
    }

    public void addLaserBullet(double x, double y) {
        lasers.add(new LaserShot(x, y));
    }

    public Image getBackground() {
        return background;
    }

    public Paddle getPaddle() {
        return paddle;
    }

    public ArrayList<MovableObject> getFallingPowerUps() {
        return fallingPowerUps;
    }

    public ArrayList<PowerUp> getActivePowerUps() {
        return activePowerUps;
    }

    public ArrayList<Ball> getBalls() {
        return balls;
    }

    public void setBalls(ArrayList<Ball> x) {
        this.balls = x;
    }

    public ArrayList<Brick> getBricks() {
        return brick;
    }

    public int getLives() {
        return lives;
    }

    public void setLives(int lives) {
        this.lives = lives;
    }

    public void setScoreMultiplier(int multiplier) {
        this.scoreMultiplier = multiplier;
    }

    public int getScore() {
        return score;
    }

    public void scorePoint(int points) {
        score += points * combo * scoreMultiplier;
    }

    public int getCombo() {
        return combo;
    }

    public EventLoader getEventLoader() {
        return eventLoader;
    }

    public void setCombo(int combo) {
        this.combo = combo;
    }

    public void comboHit() {
        combo++;
    }

    public ArrayList<LaserShot> getLasers() {
        return lasers;
    }

    public void decreaseLives() {
        lives--;
    }
}