import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;
import javafx.scene.paint.Color;

public class GameScene {
    private Image background;
    private Ball ball;
    private Paddle paddle;
    private double canvasWidth;
    private double canvasHeight;

    public GameScene(double canvasWidth, double canvasHeight) {
        this.canvasWidth = canvasWidth;
        this.canvasHeight = canvasHeight;
        double paddleLength = 100;
        double paddleHeight = 20;

        paddle = new Paddle(canvasWidth / 2 - paddleLength / 2, 
            canvasHeight - 100, paddleLength, paddleHeight);

        ball = new Ball(paddle.x + paddleLength / 2, paddle.y - paddleHeight / 2, 5, 5, 10);
    }

    public void drawGameScene(GraphicsContext gc) {
        gc.setFill(Color.BLACK);
        gc.fillRect(0, 0, gc.getCanvas().getWidth(), gc.getCanvas().getHeight());
        paddle.draw(gc);
        ball.draw(gc);
    }

    public void update(boolean left, boolean right) {
        paddle.move(left, right);
        if (paddle.getX() < 0) {
            paddle.setX(0);
        }
        if (paddle.getX() >= canvasWidth - paddle.getLength()) {
            paddle.setX(canvasWidth - paddle.getLength());
        }

        ball.move();
        if (ball.getEdgeLeft() <= 0 || ball.getEdgeRight() >= canvasWidth) {
            ball.reverseVx();
        }
        if (ball.getEdgeTop() <= 0 || ball.getEdgeBottom() >= canvasHeight) {
            ball.reverseVy();
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