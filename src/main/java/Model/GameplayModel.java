package Model;

import java.io.InputStream;
import java.util.*;

import Controller.EventLoader;
import Controller.GameEvent;
import Controller.GameEventListener;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;


/**
 * Quản lý trạng thái và logic của trò chơi.
 * Lớp này chứa tất cả các đối tượng trong trò chơi như bóng, thanh trượt và gạch,
 * đồng thời chứa các cơ chế cốt lõi của trò chơi bao gồm phát hiện va chạm,
 * tính điểm và tiến trình qua các cấp độ.
 */
public class GameplayModel implements UltilityValues {
    private Image background;
    private Paddle paddle;
    private BallState currentBallState;
    private double currentVx;
    private ArrayList<Brick> brick;
    private ArrayList<Ball> balls = new ArrayList<>();
    private ArrayList<MovableObject> fallingPowerUps = new ArrayList<>();
    private ArrayList<PowerUp> activePowerUps = new ArrayList<>();
    private ArrayList<LaserShot> lasers = new ArrayList<>();
    private int level;
    private int lives;
    private int score;
    private int combo;
    private int scoreMultiplier = 1;
    private EventLoader eventLoader;

    /**
     * Xây dựng một GameplayModel mới và khởi tạo trạng thái trò chơi.
     */
    public GameplayModel(EventLoader eventLoader) {
        this.eventLoader = eventLoader;

        paddle = Paddle.getPaddle();

        Ball ball = new Ball(paddle.x + paddleLength / 2, paddle.y - paddleHeight / 2, 0, 0, 10);
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
     * Phóng quả bóng từ thanh trượt nếu nó hiện đang ở trạng thái ATTACHED (gắn liền).
     * Quả bóng được cung cấp một vận tốc ban đầu theo chiều dọc.
     */
    public void launchBall() {
        if (this.currentBallState == BallState.ATTACHED) {
            this.currentBallState = BallState.LAUNCHED;
            for (Ball ball: balls) {
                ball.setVx(0);
                ball.setVy(-2 - currentVx);
            }
        }
    }

    /**
     * Thêm một viên gạch thuộc loại được chỉ định vào trò chơi tại một vị trí nhất định.
     * @param type Loại của viên gạch, quyết định các thuộc tính của nó.
     * @param x Tọa độ x cho vị trí của viên gạch.
     * @param y Tọa độ y cho vị trí của viên gạch.
     */
    public void addBrickType(int type, double x, double y) {
        Brick newBrick = new Brick(x, y);
        newBrick.setBrickType(type);
        brick.add(newBrick);
    }

    public void addLaserBullet(double x, double y) {
        lasers.add(new LaserShot(x, y));
    }

    /**
     * Đọc bố cục bản đồ cho cấp độ hiện tại từ một tệp tài nguyên
     * và điền vào danh sách gạch tương ứng.
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
     * Đặt lại quả bóng và thanh trượt về vị trí xuất phát ban đầu.
     * Trạng thái của quả bóng được đặt thành ATTACHED (gắn liền).
     */
    public void setBallPosition() {
        for (Ball ball : balls) {
            ball.setX(paddle.getX() + paddle.getLength() / 2);
            ball.setY(paddle.getY() - paddle.getHeight() / 2);
            ball.setVx(0);
            ball.setVy(0);
        }
    }

    public void resetPosition() {
        paddle.setX(canvasWidth / 2 - paddle.getLength() / 2);
        paddle.setY(canvasHeight - 140);
        balls.clear();
        Ball ball = new Ball(paddle.x + paddleLength / 2, paddle.y - paddleHeight / 2, 0, 0, 10);
        balls.add(ball);
        currentBallState = BallState.ATTACHED;
    }

    /**
     * Lấy ảnh nền.
     * @return Ảnh nền.
     */
    public Image getBackground() {
        return background;
    }

    /**
     * Lấy đối tượng thanh trượt.
     * @return Thanh trượt của trò chơi.
     */
    public Paddle getPaddle() {
        return paddle;
    }

    public ArrayList<MovableObject> getFallingPowerUps() {
        return fallingPowerUps;
    }

    public ArrayList<PowerUp> getActivePowerUps() {
        return activePowerUps;
    }

    /**
     * Lấy đối tượng bóng.
     * @return Quả bóng của trò chơi.
     */


    public ArrayList<Ball> getBalls() {
        return balls;
    }

    public void setBalls(ArrayList<Ball> x) {
        this.balls = x;
    }

    /**
     * Lấy danh sách các viên gạch hiện có trong trò chơi.
     * @return Một ArrayList chứa các đối tượng Brick.
     */
    public ArrayList<Brick> getBricks() {
        return brick;
    }

    /**
     * Lấy số mạng còn lại.
     * @return Số mạng hiện tại.
     */
    public int getLives() {
        return lives;
    }

    public void setLives(int lives) {
        this.lives = lives;
    }

    /**
     * Lấy điểm số hiện tại.
     * @return Điểm của người chơi.
     */

    public int getScoreMultiplier() {
        return scoreMultiplier;
    }

    public void setScoreMultiplier(int multiplier) {
        this.scoreMultiplier = multiplier;
    }

    public int getScore() {
        return score;
    }

    /**
     * Cộng điểm vào tổng điểm, được nhân với combo hiện tại.
     * @param points Số điểm cơ bản để cộng.
     */
    public void scorePoint(int points) {
        score += points * combo * scoreMultiplier;
    }

    /**
     * Lấy hệ số nhân combo hiện tại.
     * @return Giá trị combo.
     */
    public int getCombo() {
        return combo;
    }

    public EventLoader getEventLoader() {
        return eventLoader;
    }

    /**
     * Đặt hệ số nhân combo.
     * @param combo Giá trị combo mới.
     */
    public void setCombo(int combo) {
        this.combo = combo;
    }

    /**
     * Tăng hệ số nhân combo lên một.
     */
    public void comboHit() {
        combo++;
    }

    public void spawnPowerUp(double x, double y) {
        Random rand = new Random();
        int type = rand.nextInt(7);
        System.out.println(type);
        MovableObject pu;
        switch (type) {
            case 0 -> pu = new PU_Expand(x, y, 0, 2, 15);
            case 1 -> pu = new PU_MultiBall(x, y, 0, 2, 15);
            case 2 -> pu = new PU_Laser(x, y, 0, 2, 15);
            case 3 -> pu = new PU_ExtraLive(x, y, 0, 2, 15);
            case 4 -> pu = new PU_Shield(x, y, 0, 2, 15);
            case 5 -> pu = new PU_BombBall(x, y, 0, 2, 15);
            case 6 -> pu = new PU_ScoreX2(x, y, 0, 2, 15);
            default -> pu = new PU_Expand(x, y, 0, 2, 15);
        }

        fallingPowerUps.add(pu);
    }


    /**
     * Kiểm tra và xử lý tất cả các va chạm trong trò chơi. Điều này bao gồm
     * va chạm giữa bóng-tường, bóng-thanh trượt, và bóng-gạch. Nó cũng thực thi
     * các giới hạn di chuyển của thanh trượt.
     */

    public void checkCollisions() {
        paddle.checkBoundary(canvasWidth);
        int check = 0;
        System.out.println(balls.size());
        for (Ball ball: new ArrayList<>(balls)) {
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

    public void resetPowerUp() {
        for (PowerUp p : activePowerUps) {
            p.remove(this);
        }
        activePowerUps.clear();
        fallingPowerUps.clear();
        lasers.clear();
    }

    /**
     * Khởi tạo cấp độ tiếp theo của trò chơi. Phương thức này tăng biến đếm cấp độ,
     * kết xuất bản đồ mới, đặt lại vị trí các đối tượng, và đặt lại số mạng và combo.
     */
    public void Initialize() {
        level++;
        if (level <= 5) {
            renderMap();
            resetPosition();
            resetPowerUp();
            currentVx++;
            lives = 5;
            setCombo(0);
        }
    }

    public ArrayList<LaserShot> getLasers() {
        return lasers;
    }

    /**
     * Cập nhật trạng thái trò chơi cho mỗi khung hình. Phương thức này di chuyển thanh trượt và bóng,
     * cập nhật hoạt ảnh của gạch, kiểm tra va chạm, loại bỏ các viên gạch đã bị phá hủy,
     * và kiểm tra các thay đổi trạng thái trò chơi như hoàn thành cấp độ hoặc thua cuộc.
     * @param left      true nếu phím di chuyển sang trái được nhấn.
     * @param right     true nếu phím di chuyển sang phải được nhấn.
     * @param deltaTime Thời gian đã trôi qua kể từ khung hình cuối cùng.
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
                if (Math.random() <= 1) {
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
            pu.move(); // rơi xuống

            // Va chạm với paddle
            boolean overlapX = pu.getX() + pu.getWidth() >= paddle.getX() &&
                    pu.getX() <= paddle.getX() + paddle.getLength();
            boolean overlapY = pu.getY() + pu.getHeight() >= paddle.getY() &&
                    pu.getY() <= paddle.getY() + paddle.getHeight();

            if (overlapX && overlapY && pu instanceof PowerUp powerUp) {
                powerUp.apply(this);// bật hiệu ứng
                System.out.println(powerUp.getName());
                activePowerUps.add(powerUp);         // đưa vào danh sách đang hoạt động
                fallingPowerUps.remove(i);           // xóa icon rơi
                i--;
                continue;
            }

            // Nếu rơi khỏi màn hình thì xóa
            if (pu.getY() > canvasHeight) {
                fallingPowerUps.remove(i);
                i--;
            }
        }

        if (lasers != null && !lasers.isEmpty()) {
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
            eventLoader.loadEvent(GameEvent.LEVEL_COMPLETE);
        }
        if (level == 6) {
            eventLoader.loadEvent(GameEvent.GAME_WIN);
        }
        if (lives <= 0) {
            eventLoader.loadEvent(GameEvent.GAME_LOST);
        }
    }

    public void decreaseLives() {
        lives--;
    }

    public void drawActivePowerUps(GraphicsContext g) {
        if (activePowerUps.isEmpty()) return;

        double iconSize = 32;
        double spacing = 10;
        double startX = 25;                    // ✅ sát mép trái, ngang với tim
        double startY = canvasHeight - 105;     // ✅ nằm ngay trên thanh máu (40 cao + 10 cách)

        Set<String> drawn = new HashSet<>();

        for (PowerUp p : activePowerUps) {
            if (!p.isActive()) continue;
            String name = p.getName();
            if (drawn.contains(name)) continue;

            Image icon = PU_Icons.getIcon(name);

            // Nền mờ nhẹ
            /*
            g.setGlobalAlpha(0.5);
            g.setFill(javafx.scene.paint.Color.BLACK);
            g.fillRoundRect(startX - 3, startY - 3, iconSize + 6, iconSize + 6, 8, 8);
            g.setGlobalAlpha(1.0);
             */

            // Icon
            g.drawImage(icon, startX, startY, iconSize, iconSize);

            // Thời gian còn lại (nếu có)
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


    public boolean hasActivePower(String name) {
        for (PowerUp p : activePowerUps) {
            if (p.getName().equalsIgnoreCase(name) && p.isActive()) {
                return true;
            }
        }
        return false;
    }

    public void drawEffects(GraphicsContext g) {
        Paddle paddle = getPaddle();

        // Hiệu ứng X2 điểm (ánh sáng vàng quanh paddle)
        if (hasActivePower("Score x2")) {
            g.setGlobalAlpha(0.3);
            g.setFill(javafx.scene.paint.Color.GOLD);
            g.fillRoundRect(paddle.getX() - 5, paddle.getY() - 5,
                    paddle.getLength() + 10, paddle.getHeight() + 10, 15, 15);
            g.setGlobalAlpha(1.0);
        }

        // Hiệu ứng Shield (tấm chắn năng lượng dưới paddle)
        if (hasActivePower("Shield")) {
            g.setStroke(javafx.scene.paint.Color.CYAN);
            g.setLineWidth(3);
            g.strokeRoundRect(paddle.getX() - 5,
                    paddle.getY() + paddle.getHeight() + 5,
                    paddle.getLength() + 10, 10, 10, 10);
        }
    }


}