package Model;
import java.io.Serial;
import java.io.Serializable;
import java.util.ArrayList;


/**
 * Class này để lưu dữ liệu khi Pause game
 */
public class PauseData implements Serializable {
    @Serial
    private static final long serialVersionUID = 1L;

    private int score, level, lives;
    private double ballX, ballY, ballVx, ballVy;
    private double paddleX;
    private ArrayList<BrickData> bricks;  // Lưu state từng brick

    // constructer
    public PauseData(GameplayModel model) {
        this.score = model.getScore();
        this.level = model.getLevel();
        this.lives = model.getLives();
        this.ballX = model.getBall().getX();
        this.ballY = model.getBall().getY();
        this.ballVx = model.getBall().getVx();
        this.ballVy = model.getBall().getVy();
        this.paddleX = model.getPaddle().getX();

        // Lưu toàn bộ bricks
        this.bricks = new ArrayList<>();
        for (Brick b : model.getBricks()) {
            bricks.add(new BrickData(b));
        }
    }

    // getter
    public int getScore() { return score; }
    public int getLevel() { return level; }
    public int getLives() { return lives; }
    public double getBallX() { return ballX; }
    public double getBallY() { return ballY; }
    public double getBallVx() { return ballVx; }
    public double getBallVy() { return ballVy; }
    public double getPaddleX() { return paddleX; }
    public ArrayList<BrickData> getBricks() { return bricks; }

    /**
     * Class lưu state cho 1 brick
     */
    public static class BrickData implements Serializable {
        @Serial
        private static final long serialVersionUID = 1L;
        private double x, y;
        private int type;
        private boolean breaking, destroyed;
        private int currentFrame;
        private double frameTimer;

        public BrickData(Brick b) {
            this.x = b.getX();
            this.y = b.getY();
            this.type = b.getBrickType();
            this.breaking = b.isBreaking();
            this.destroyed = b.isDestroyed();
            this.currentFrame = b.getCurrentFrame();
            this.frameTimer = b.getFrameTimer();
        }

        // Getters cho BrickData
        public double getX() { return x; }
        public double getY() { return y; }
        public int getType() { return type; }
        public boolean isBreaking() { return breaking; }
        public boolean isDestroyed() { return destroyed; }
        public int getCurrentFrame() { return currentFrame; }
        public double getFrameTimer() { return frameTimer; }
    }
}