import javafx.scene.image.Image;
import javafx.scene.canvas.GraphicsContext;

public class Ball extends MovableObject {
    private double radius;
    private Image ballImg;

    public Ball(double x, double y,  double radius) {
        super(x,y);
        this.radius = radius;
        ballImg = new Image("/DefaultBall.png");
    }

    public void setBallImg(Image ballImg) {
        this.ballImg = ballImg;
    }

    @Override
    public void draw(GraphicsContext render) {
        render.drawImage(ballImg, x - radius, y-radius, radius *2, radius*2);
    }
}
