package Model;

import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;

public class PU_Shield extends MovableObject implements PowerUp {
    private final Image icon = new Image("/shield.png");
    private final double radius;
    private static final int DURATION_MS = 10000;
    private boolean effectActive = false;
    private int elapsedMs;

    public PU_Shield(double x, double y, double vx, double vy, double radius) {
        super(x, y, vx, vy);
        this.radius = radius;
    }

    @Override
    public void draw(GraphicsContext g) {
        g.drawImage(icon, x - radius, y - radius, radius * 2, radius * 2);
    }

    @Override
    public void apply(GameplayModel game) {
        effectActive = true;
        game.getPaddle().setShield(true);
        elapsedMs = 0;
    }

    @Override
    public void update(GameplayModel game, double deltaTime) {
        if (!effectActive) return;
        elapsedMs += (int) (deltaTime * 1000);
        if (elapsedMs >= DURATION_MS) remove(game);
    }

    @Override
    public void remove(GameplayModel game) {
        game.getPaddle().setShield(false);
        effectActive = false;
    }

    @Override
    public boolean isActive() {
        return effectActive;
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
        return elapsedMs;
    }

    @Override
    public void setElapsedMs(int ms) {
        this.elapsedMs = ms;
    }

    @Override
    public String getName() {
        return "Shield";
    }

    @Override
    public int getDurationMs() {
        return DURATION_MS;
    }
}
