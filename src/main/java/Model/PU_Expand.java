package Model;

import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;

public class PU_Expand extends MovableObject implements PowerUp {

    private static final int DURATION_MS = 30_000;

    private double radius;
    private Image expand_paddle;
    private boolean effectActive;
    private double originalWidth;
    private int elapsedMs;

    public PU_Expand(double x, double y, double vx, double vy, double radius) {
        super(x, y, vx, vy);
        this.radius = radius;
        this.expand_paddle = new Image("/x2LengthPU.png");
    }

    @Override
    public String getName() {
        return "Expand Paddle";
    }

    @Override
    public int getDurationMs() {
        return DURATION_MS;
    }

    @Override
    public void draw(GraphicsContext g) {
        if (!effectActive)
            g.drawImage(expand_paddle, x - radius, y - radius, radius * 2, radius * 2);
    }

    @Override
    public void apply(GameplayModel game) {
        if (effectActive) return;
        Paddle paddle = game.getPaddle();
        if (paddle != null) {
            originalWidth = paddle.getLength();
            paddle.setLength(originalWidth * 1.5);
            effectActive = true;
            elapsedMs = 0;
        }
    }

    @Override
    public void update(GameplayModel game, double deltaTime) {
        if (!effectActive) return;
        elapsedMs += (int) (deltaTime * 1000);
        if (elapsedMs >= DURATION_MS) {
            remove(game);
        }
    }

    @Override
    public void remove(GameplayModel game) {
        if (!effectActive) return;
        Paddle paddle = game.getPaddle();
        if (paddle != null) {
            paddle.setLength(originalWidth);
        }
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
}
