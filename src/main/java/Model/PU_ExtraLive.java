package Model;

import javafx.scene.image.Image;
import javafx.scene.canvas.GraphicsContext;

/**
 * hiệu ứng thêm 1 mạng.
 */
public class PU_ExtraLive extends MovableObject implements PowerUp {
    private final double radius;
    private final Image extralive;
    private boolean active = false;

    public PU_ExtraLive(double x, double y, double radius) {
        super(x, y, 0, 3);
        this.radius = radius;
        this.extralive = new Image("/extralive.png");
    }

    @Override
    public void draw(GraphicsContext g) {
        g.drawImage(extralive, x - radius, y - radius, radius * 2, radius * 2);
    }

    @Override
    public void apply(GameplayModel game) {
        if (active) return;
        active = true;

        if (game.getLives() < 5) game.setLives(game.getLives() + 1);
    }

    @Override
    public void remove(GameplayModel game) {
        active = false;
    }

    @Override
    public void update(GameplayModel ctx, double deltaTime) {

    }

    @Override
    public boolean isActive() {
        return false;
    }

    @Override
    public double getWidth() {
        return radius * 2;
    }

    @Override
    public double getHeight() {
        return radius * 2;
    }

    @Override
    public int getElapsedMs() {
        return 0;
    }

    @Override
    public void setElapsedMs(int ms) {

    }

    @Override
    public String getName() {
        return "Extra Live";
    }

    @Override
    public int getDurationMs() {
        return 0;
    }
}
