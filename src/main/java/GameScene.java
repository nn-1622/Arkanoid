import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;
import javafx.scene.paint.Color;

enum BallState {
    ATTACHED,
    LAUNCHED
}

public class GameScene {
    private Image background;
    private Ball ball;
    private Paddle paddle;
    private double canvasWidth;
    private double canvasHeight;
    private BallState currentBallState;

    public GameScene(double canvasWidth, double canvasHeight) {
        this.canvasWidth = canvasWidth;
        this.canvasHeight = canvasHeight;
        double paddleLength = 100;
        double paddleHeight = 20;

        paddle = new Paddle(canvasWidth / 2 - paddleLength / 2, 
            canvasHeight - 100, paddleLength, paddleHeight);

        ball = new Ball(paddle.x + paddleLength / 2, paddle.y - paddleHeight / 2, 0, 0, 10);
        currentBallState = BallState.ATTACHED;
    }

    public void launchBall() {
        if (this.currentBallState == BallState.ATTACHED) {
            this.currentBallState = BallState.LAUNCHED;
            ball.setVx(5);
            ball.setVy(-5); 
        }
    }

    public void drawGameScene(GraphicsContext gc) {
        gc.setFill(Color.BLACK);
        gc.fillRect(0, 0, gc.getCanvas().getWidth(), gc.getCanvas().getHeight());
        paddle.draw(gc);
        ball.draw(gc);
    }

    public void resetPosition() {
        paddle.setX(canvasWidth / 2 - paddle.getLength() / 2);
        paddle.setY(canvasHeight - 100);
        ball.setX(paddle.getX() + paddle.getLength() / 2);
        ball.setY(paddle.getY() - paddle.getHeight() / 2);
        ball.setVx(0);
        ball.setVy(0);
        currentBallState = BallState.ATTACHED;
    }

    public void update(boolean left, boolean right) {
        paddle.move(left, right);
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
            ball.move();
            if (ball.getEdgeLeft() <= 0 || ball.getEdgeRight() >= canvasWidth) {
                ball.reverseVx();
            }

            if (ball.getEdgeTop() <= 0) {
                ball.reverseVy();
            }

            if (ball.getEdgeBottom() >= canvasHeight) {
                resetPosition();
                currentBallState = BallState.ATTACHED;
            }

            if (ball.getEdgeBottom() >= paddle.getY() && 
                ball.getEdgeBottom() <= paddle.getY() + paddle.getHeight() && 
                ball.getCenter() >= paddle.getX() &&
                ball.getCenter() <= paddle.getX() + paddle.getLength() &&
                ball.getVy() > 0) {
                ball.reverseVy();
            }
        }
    }
}