package Model;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.Random;
import java.util.Scanner;

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
    private Ball ball;
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

    private EventLoader eventLoader;

    /**
     * Xây dựng một GameplayModel mới và khởi tạo trạng thái trò chơi.
     */
    public GameplayModel(EventLoader eventLoader) {
        this.eventLoader = eventLoader;

        paddle = Paddle.getPaddle();

        ball = new Ball(paddle.x + paddleLength / 2, paddle.y - paddleHeight / 2, 0, 0, 10);
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
            ball.setVx(0);
            ball.setVy(-5 - currentVx);
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
    public void resetPosition() {
        paddle.setX(canvasWidth / 2 - paddle.getLength() / 2);
        paddle.setY(canvasHeight - 100);
        ball.setX(paddle.getX() + paddle.getLength() / 2);
        ball.setY(paddle.getY() - paddle.getHeight() / 2);
        ball.setVx(0);
        ball.setVy(0);
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
    /**
     * Lấy đối tượng bóng.
     * @return Quả bóng của trò chơi.
     */
    public Ball getBall() {
        return ball;
    }

    public ArrayList<Ball> getBalls() {
        return balls;
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
    public int getScore() {
        return score;
    }

    /**
     * Cộng điểm vào tổng điểm, được nhân với combo hiện tại.
     * @param points Số điểm cơ bản để cộng.
     */
    public void scorePoint(int points) {
        score += points * combo;
    }

    /**
     * Lấy hệ số nhân combo hiện tại.
     * @return Giá trị combo.
     */
    public int getCombo() {
        return combo;
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
        int type = rand.nextInt(4); // 4 loại power-up

        MovableObject pu;
        switch (type) {
            case 0 -> pu = new PU_Expand(x, y, 0, 2, 15);
            case 1 -> pu = new PU_MultiBall(x, y, 0, 2, 15);
            case 2 -> pu = new PU_Laser(x, y, 0, 2, 15);
            case 3 -> pu = new PU_ExtraLive(x, y, 0, 2, 15);
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
        if (paddle.getX() < 0) {
            paddle.setX(0);
        }
        if (paddle.getX() >= canvasWidth - paddle.getLength()) {
            paddle.setX(canvasWidth - paddle.getLength());
        }
        if (currentBallState == BallState.ATTACHED) {
            ball.setX(paddle.getX() + paddle.getLength() / 2);
            ball.setY(paddle.getY() - paddle.getHeight() / 2);
        } else if (currentBallState == BallState.LAUNCHED) {
            if (ball.getEdgeLeft() <= 0 || ball.getEdgeRight() >= canvasWidth) {
                ball.reverseVx();
            }

            if (ball.getEdgeTop() <= 0) {
                ball.reverseVy();
            }

            if (ball.getEdgeBottom() >= canvasHeight) {
                resetPosition();
                lives--;
                setCombo(0);
            }

            if (ball.getEdgeBottom() >= paddle.getY() &&
                ball.getEdgeTop() <= paddle.getY() + paddle.getHeight() &&
                ball.getCenter() >= paddle.getX() &&
                ball.getCenter() <= paddle.getX() + paddle.getLength() &&
                ball.getVy() > 0) {

                double paddleCenter = paddle.getX() + paddle.getLength() / 2;
                double diff = (ball.getCenter() - paddleCenter) / (paddle.getLength() / 2);

                double speed = Math.sqrt(ball.getVx() * ball.getVx() + ball.getVy() * ball.getVy());
                double angle = diff * Math.toRadians(70);

                ball.setVx(speed * Math.sin(angle));
                ball.setVy(-speed * Math.cos(angle));
            }
            
            for (Brick b : brick) {
                if(b.isBreaking()) continue;
                if (b.getEdgeBottom() > ball.getEdgeTop() &&
                    b.getEdgeTop() < ball.getEdgeBottom() && 
                    b.getEdgeRight() > ball.getEdgeLeft() && 
                    b.getEdgeLeft() < ball.getEdgeRight()) {

                    double overlapX = Math.min(ball.getEdgeRight() - b.getEdgeLeft(), b.getEdgeRight() - ball.getEdgeLeft());
                    double overlapY = Math.min(ball.getEdgeBottom() - b.getEdgeTop(), b.getEdgeBottom() - ball.getEdgeTop());

                    b.hit();
                    comboHit();
                    scorePoint(1);
                    eventLoader.loadEvent(GameEvent.BALL_HIT);

                    if (overlapX < overlapY) {
                        if (ball.getX() < b.getX()) {
                            ball.setX(b.getEdgeLeft() - ball.getRadius());
                        } else {
                            ball.setX(b.getEdgeRight() + ball.getRadius());
                        }
                        ball.reverseVx();
                    } else if (overlapY < overlapX) {
                        if (ball.getY() < b.getY()) {
                            ball.setY(b.getEdgeTop() - ball.getRadius());
                        } else {
                            ball.setY(b.getEdgeBottom() + ball.getRadius());
                        }
                        ball.reverseVy();
                    }
                    break;
                }
            }
            for (LaserShot l : lasers) {
                l.checkLaser(this);
            }
        }
    }

    /**
     * Khởi tạo cấp độ tiếp theo của trò chơi. Phương thức này tăng biến đếm cấp độ,
     * kết xuất bản đồ mới, đặt lại vị trí các đối tượng, và đặt lại số mạng và combo.
     */
    public void Initialize(){
        level++;
        if (level <= 5) {
            renderMap();
            resetPosition();
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
        ball.move();
        for (Brick b : brick) {
            b.update(deltaTime);
            if (b.isDestroyed()) {
                if (Math.random() < 0.3) { // 20% xác suất
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
                powerUp.apply(this);                 // bật hiệu ứng
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
}