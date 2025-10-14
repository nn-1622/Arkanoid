import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;

public class Paddle extends MovableObject {
    private double length;
    private double height;
    private Image paddleImg;
    private double v;
    public Paddle(double x, double y, double length, double height) {
        super(x, y);
        this.length = length;
        this.height = height;
        this.paddleImg = new Image("/DefaultPaddle.png");
        this.v = 5;
    }
    public double getCenterX(){
        return (x + length )/ 2;
    }
    public void setV(double v){
        this.v = v;
    }
    public void setPaddleImg(Image paddleImg) {
        this.paddleImg = paddleImg;
    }
    @Override
    public void draw(GraphicsContext render) {
        render.drawImage(paddleImg, x, y, length, height);
    }
    public void move(boolean left, boolean right){
        if(left){
            x-=v;
        } else if(right){
            x+=v;
        }
    }
}
