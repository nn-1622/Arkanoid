package Model;

import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;

public class Brick extends GameObject{
    private Image image = new Image("/Sprite.png");
    private final double width = 56;
    private final double height = 25;
    private double sx = 32, sy = 176, sw = 32, sh = 16;
    private int brickType;
    public Brick(double x, double y) {
        super(x,y);
        this.brickType = 0;
    }
    public void setBrickType(int x){
        this.brickType = x;
    }
    @Override
    public void draw(GraphicsContext render) {
        render.drawImage(image, sx, sy+brickType*16, sw, sh, x, y, width, height);
    }
}
