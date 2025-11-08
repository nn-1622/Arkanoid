package Model;

import java.awt.*;
import javafx.scene.image.Image;
import javafx.scene.canvas.GraphicsContext;

public class PU_Expand extends MovableObject implements PowerUp{
    private double radius;
    private Image expand_paddle;
    private boolean active = false;
    private double originalWidth;

    public PU_Expand(double x, double y, double vx, double vy, double radius) {
        super(x, y, 0, 1);
        this.radius = radius;
        this.expand_paddle = new Image("/x2LengthPU.png");
    }

    @Override
    public String getName() {
        return "Expand Paddle";
    }

    @Override
    public int getDurationMs() {
            return 30000;
    }

    @Override
    public void draw(GraphicsContext g) {
        g.drawImage(expand_paddle, x - radius, y - radius, radius *2, radius*2);
    }

    @Override
    public void apply(GameplayModel game) {
        if (active) return;
        Paddle paddle = game.getPaddle();
        if (paddle != null) {
            originalWidth = paddle.getLength();
            paddle.setLength(originalWidth * 1.5); // tăng 1.5x
            active = true;
        }
    }

    @Override
    public void remove(GameplayModel game) {
        if (!active) return;
        Paddle paddle = game.getPaddle();
        if (paddle != null) {
            paddle.setLength(originalWidth); // khôi phục kích thước ban đầu
        }
        active = false;
    }

    @Override
    public double getWidth() {
        return radius * 2;
    }

    @Override
    public double getHeight() {
        return radius * 2;
    }

}
