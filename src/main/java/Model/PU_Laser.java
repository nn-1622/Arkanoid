package Model;

import java.awt.*;

import javafx.scene.image.Image;
import javafx.scene.canvas.GraphicsContext;

public class PU_Laser extends MovableObject implements PowerUp {
    private static final int DURATION_MS = 5_000;
    private static final int SHOOT_INTERVAL_MS = 150;

    private final double radius;
    private final Image Laser;

    private boolean active;
    private boolean effectActive;

    private int elapsedMs;
    private int timeSinceLastShotMs;

    public PU_Laser(double x, double y, double vx, double vy, double radius) {
        super(x, y, vx, vy);
        this.radius = radius;
        this.Laser = new Image("/LaserPU.png");
        this.active = true;
    }

    @Override
    public void draw(GraphicsContext g) {
        // dùng khi còn là icon rơi
        if (active && !effectActive) {
            g.drawImage(Laser, x - radius, y - radius, radius * 2, radius * 2);
        }
    }

    public void extendDuration(int extraMs) {
        elapsedMs -= extraMs;
        if (elapsedMs < 0) elapsedMs = 0;
    }

    @Override
    public void apply(GameplayModel game) {
        for (PowerUp p : game.getActivePowerUps()) {
            if (p instanceof PU_Laser && p.isActive()) {
                ((PU_Laser) p).extendDuration(2500);
                return;
            }
        }

        this.active = false;
        this.effectActive = true;
        this.elapsedMs = 0;
        this.timeSinceLastShotMs = 0;
    }

    @Override
    public void update(GameplayModel game, double deltaTime) {
        if (!effectActive) return;

        int dt = (int) (deltaTime * 1000);
        elapsedMs += dt;
        timeSinceLastShotMs += dt;

        if (elapsedMs >= DURATION_MS) {
            remove(game);
            return;
        }

        if (timeSinceLastShotMs >= SHOOT_INTERVAL_MS) {
            timeSinceLastShotMs = 0;

            Paddle paddle = game.getPaddle();
            double y = paddle.getY();

            double leftX = paddle.getX();
            double rightX = paddle.getX() + paddle.getLength();

            game.addLaserBullet(leftX, y);
            game.addLaserBullet(rightX, y);
        }
    }

    @Override
    public void remove(GameplayModel game) {
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
        return "Laser";
    }

    @Override
    public int getDurationMs() {
        return DURATION_MS;
    }
}
