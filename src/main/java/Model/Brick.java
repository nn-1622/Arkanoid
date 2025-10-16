package Model;

import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;

public class Brick extends GameObject{
    private Image image = new Image("/Sprite.png");
    private final double width = 50;
    private final double height = 25;
    private double sx = 32, sy = 160, sw = 32, sh = 16;
    private int brickType;
    public Brick(double x, double y) {
        super(x,y);
        this.brickType = 0;
    }
    public int getBrickType(){
        return this.brickType;
    }
    public void setBrickType(int x){
        this.brickType = x;
    }
    public double getEdgeTop() {
        return this.y;
    }
    public double getEdgeBottom() {
        return this.y + this.height;
    }
    public double getEdgeLeft() {
        return this.x;
    }
    public double getEdgeRight() {
        return this.x + this.width;
    }
    @Override
    public void draw(GraphicsContext render) {
        render.drawImage(image, sx, sy + brickType * 16, sw, sh, x, y, width, height);
    }
}
