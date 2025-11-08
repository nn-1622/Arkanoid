package Model;

import java.awt.*;
import javafx.scene.image.Image;
import javafx.scene.canvas.GraphicsContext;

public class PU_Laser extends MovableObject implements PowerUp{
    private double radius;
    private Image Laser;
    private boolean active = false;
    private double originalWidth;

    public PU_Laser(double x, double y, double vx, double vy, double radius) {
        super(x, y, 0, 3);
        this.radius = radius;
        this.Laser = new Image("/LaserPU.png");
    }

    @Override
    public String getName() {
        return "Laser";
    }

    @Override
    public int getDurationMs() {
        return 10000;
    }

    @Override
    public void draw(GraphicsContext g) {
        g.drawImage(Laser, x - radius, y - radius, radius *2, radius*2);
    }

    @Override
    public void apply(GameplayModel game) {
        if (active) return;
        active = true;

        if(game.getLives() < 5) game.setLives(game.getLives() + 1);
    }

    @Override
    public void remove(GameplayModel game) {
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
