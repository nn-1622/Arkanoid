import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;

public class Paddle extends MovableObject {
    private double x;
    private double y;
    private double length;
    private double height;
    private Image paddleImg;
    public Paddle(double x, double y, double length, double height) {
        super(x, y);
        this.length = length;
        this.height = height;
        this.paddleImg = new Image("Assets/DefaultPaddle.png");
    }
    public void setPaddleImg(Image paddleImg) {
        this.paddleImg = paddleImg;
    }
    @Override
    public void draw(GraphicsContext render) {
        render.drawImage(paddleImg, x, y, length, height);
    }
}
