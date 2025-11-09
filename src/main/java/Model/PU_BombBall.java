package Model;

import javafx.scene.image.Image;
import javafx.scene.canvas.GraphicsContext;

public class PU_BombBall extends MovableObject implements PowerUp {

    private static final int DURATION_MS = 6000;
    private boolean picked = false;
    private boolean active = false;
    private double radius;
    private Image icon;

    private int elapsedMs = 0;

    public PU_BombBall(double x, double y, double vx, double vy, double radius) {
        super(x, y, vx, vy);
        this.radius = radius;
        icon = new Image("/bombball.png");
    }

    @Override
    public String getName() {
        return "BombBall";
    }

    @Override
    public int getDurationMs() {
        return DURATION_MS;
    }

    @Override
    public void draw(GraphicsContext g) {
        if (!picked) {
            g.drawImage(icon, x - radius, y - radius, radius * 2, radius * 2);
        }
    }

    @Override
    public void apply(GameplayModel game) {
        picked = true;
        active = true;

        // bật chế độ Bomb cho tất cả bóng đang tồn tại
        for (Ball b : game.getBalls()) {
            b.setBomb(true);
        }
        elapsedMs = 0;
    }

    @Override
    public void update(GameplayModel game, double deltaTime) {
        if (!active) return;

        elapsedMs += (int)(deltaTime * 1000);

        if (elapsedMs >= DURATION_MS) {
            remove(game);
        }
    }

    @Override
    public void remove(GameplayModel game) {
        active = false;

        // tắt bomb cho bóng
        for (Ball b : game.getBalls()) {
            b.setBomb(false);
        }
    }

    @Override
    public boolean isActive() {
        return active;
    }

    @Override public double getWidth() {
        return radius * 2;
    }
    @Override public double getHeight() {
        return radius * 2;
    }
    @Override public int getElapsedMs() {
        return elapsedMs;
    }

}
