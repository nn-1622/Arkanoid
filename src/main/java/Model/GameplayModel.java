package Model;

//import 2 class để save game
import Controller.ScoreManager;
import Model.GameSaveData;


import java.io.*;
import java.util.ArrayList;
import java.util.Scanner;
import Controller.GameEventListener;
import javafx.scene.image.Image;
import java.io.File;
import java.io.FileInputStream;
import java.io.ObjectInputStream;

/**
 * Quản lý trạng thái và logic của trò chơi.
 * Lớp này chứa tất cả các đối tượng trong trò chơi như bóng, thanh trượt và gạch,
 * đồng thời chứa các cơ chế cốt lõi của trò chơi bao gồm phát hiện va chạm,
 * tính điểm và tiến trình qua các cấp độ.
 */
public class GameplayModel {
    private Image background;
    private Ball ball;
    private Paddle paddle;
    private double canvasWidth;
    private double canvasHeight;
    private BallState currentBallState;
    private double currentVx;
    private ArrayList<Brick> brick;
    private int level;
    private int lives;
    private int score;
    private int combo;

    private GameEventListener gameEventListener;

    /**
     * Xây dựng một GameplayModel mới và khởi tạo trạng thái trò chơi.
     * @param canvasWidth  Chiều rộng của khung vẽ trò chơi.
     * @param canvasHeight Chiều cao của khung vẽ trò chơi.
     */
    public GameplayModel(double canvasWidth, double canvasHeight) {
        this.canvasWidth = canvasWidth;
        this.canvasHeight = canvasHeight;
        double paddleLength = 110;
        double paddleHeight = 20;

        paddle = new Paddle(canvasWidth / 2 - paddleLength / 2,
                canvasHeight - 100, paddleLength, paddleHeight);

        ball = new Ball(paddle.x + paddleLength / 2, paddle.y - paddleHeight / 2, 0, 0, 10);
        currentBallState = BallState.ATTACHED;
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

    /**
     * Lấy đối tượng bóng.
     * @return Quả bóng của trò chơi.
     */
    public Ball getBall() {
        return ball;
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

    public int getLevel() {
        return level;
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
                double angle = diff * Math.toRadians(75);

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
                    if(gameEventListener != null) {
                        gameEventListener.onBrickHit();
                    }

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

    /**
     * SaveGame
     */
    public void saveGame() {
        GameSaveData data = new GameSaveData(getScore(), getLevel(), getLives());
        ScoreManager.saveGame(data);
    }

    /**
     * LoadGame
     */
    public void loadGame() {
        GameSaveData saved = ScoreManager.loadGame();
        if (saved != null) {
            this.score = saved.getScore();
            this.level = saved.getLevel();
            this.lives = saved.getLives();

            System.out.println("Downloading " + saved);
            renderMap();       // Vẽ lại bản đồ theo level
            resetPosition();   // Đặt lại bóng và thanh trượt
        } else {
            System.out.println("Not Found Saved Game");
        }
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
        this.getPaddle().move(left, right);
        ball.move();
        for (Brick b : brick) {
            b.update(deltaTime);
        }
        checkCollisions();
        brick.removeIf(Brick::isDestroyed);
        if (brick.isEmpty()) {
            if(gameEventListener != null) {
                gameEventListener.onLevelCompleted();
                //Lưu điểm khi qua màn
                saveGame();
            }
        }
        if (level == 6) {
            if(gameEventListener != null) {
                gameEventListener.onVictory();
                //Lưu điểm khi thắng
                saveGame();
            }
        }
        if (lives <= 0) {
            if(gameEventListener != null) {
                gameEventListener.onGameOver();
                //Lưu điểm khi thua
                saveGame();
            }
        }
    }

    /**
     * Đặt bộ lắng nghe cho các sự kiện của trò chơi.
     * @param gameEventListener Bộ lắng nghe sẽ được thông báo về các sự kiện của trò chơi.
     */
    public void setGameEventListener(GameEventListener gameEventListener) {
        this.gameEventListener = gameEventListener;
    }


    //UPDATE THÊM METHOD PAUSED GAME
    //Lưu pause vào file pause.dat
    public void savePause() {
        PauseData pauseData = new PauseData(this);
        try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream("pause.dat"))) {
            oos.writeObject(pauseData);
            System.out.println("Pause saved!");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    // THÊM MỚI: Load pause từ file và khôi phục chính xác
    public void loadPause() {
        try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream("pause.dat"))) {
            PauseData pauseData = (PauseData) ois.readObject();

            // Khôi phục data cơ bản
            setScore(pauseData.getScore());
            setLevel(pauseData.getLevel());
            setLives(pauseData.getLives());

            // Khôi phục paddle
            getPaddle().setX(pauseData.getPaddleX());

            // Khôi phục ball
            getBall().setX(pauseData.getBallX());
            getBall().setY(pauseData.getBallY());
            getBall().setVx(pauseData.getBallVx());
            getBall().setVy(pauseData.getBallVy());

            // Khôi phục toàn bộ bricks
            brick.clear();
            for (PauseData.BrickData bd : pauseData.getBricks()) {
                Brick b = new Brick(bd.getX(), bd.getY());
                b.setBrickType(bd.getType());
                b.setBreaking(bd.isBreaking()); // Sử dụng setter
                b.setDestroyed(bd.isDestroyed()); // Sử dụng setter
                b.setCurrentFrame(bd.getCurrentFrame()); // Sử dụng setter
                b.setFrameTimer(bd.getFrameTimer()); // Sử dụng setter
                brick.add(b);
            }
            // Khôi phục trạng thái bóng
            if (pauseData.getBallVy() != 0){
                currentBallState = BallState.LAUNCHED;
            } else{
                currentBallState = BallState.ATTACHED;
            }

            System.out.println("Pause loaded!");
        } catch (Exception e) {
            System.out.println("No pause file found.");
        }
    }
    // THÊM setters

    /**
     * Setter cho Score
     * @param score diem
     */
    private void setScore(int score) { this.score = score; }

    /**
     * Setter cho Level
     * @param level man
     */
    private void setLevel(int level) { this.level = level; }
    /**
     * setter cho lives
     * @param lives so mang
     */
    private void setLives(int lives) { this.lives = lives; }

    /**
     * Check xem file pause có hợp lệ
     * @return boolean hasPausedata
     */
    public boolean hasPauseData() {
        File pauseFile = new File("pause.dat");
        if (!pauseFile.exists()) {
            return false;
        }

        try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream("pause.dat"))) {
            ois.readObject();
            return true;
        } catch (Exception e) {
            System.err.println("file bị lỗi, đang xoá");
            pauseFile.delete();
            return false;
        }
    }
}