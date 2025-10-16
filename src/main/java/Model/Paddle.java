package Model;

import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;

public class Paddle extends MovableObject {
    private double length;
    private double height;
    private Image paddleImg;

    public Paddle(double x, double y, double length, double height) {
        super(x, y);
        this.length = length;
        this.height = height;
        this.paddleImg = new Image("/DefaultPaddle.png");
        setVx(5);
    }

    public double getLength() {
        return length;
    }

    public double getHeight() {
        return height;
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
            x -= this.getVx();
        } else if(right){
            x += this.getVx();
        }
    }
}
