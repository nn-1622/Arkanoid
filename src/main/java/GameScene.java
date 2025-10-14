import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;
import javafx.scene.paint.Color;

public class GameScene {
    private Image background;
    private Ball ball;
    private Paddle paddle;
    public GameScene() {
        //currently a sample, will add later
        paddle = new Paddle(244.4,569.8,111.2,20.2);
        ball = new Ball(300, 558.3, 11.4);
    }
    public void drawGameScene(GraphicsContext gc) {
        gc.setFill(Color.BLACK);
        gc.fillRect(0, 0, gc.getCanvas().getWidth(), gc.getCanvas().getHeight());
        paddle.draw(gc);
        ball.draw(gc);
    }
    public void update(boolean left, boolean right) {
        paddle.move(left, right);
    }
}