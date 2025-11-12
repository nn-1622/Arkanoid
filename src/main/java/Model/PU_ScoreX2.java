package Model;

import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;

public class PU_ScoreX2 extends MovableObject implements PowerUp {
    private static final int DURATION_MS = 10000;
    private final double radius;
    private final Image img;
    private boolean active = true;
    private boolean effectActive = false;
    private int elapsedMs = 0;

    public PU_ScoreX2(double x, double y, double vx, double vy, double radius) {
        super(x, y, vx, vy);
        this.radius = radius;
        this.img = new Image("/x2.png");
    }

    @Override
    public void draw(GraphicsContext g) {
        if (active && !effectActive) {
            g.drawImage(img, x - radius, y - radius, radius * 2, radius * 2);
        }
    }

    @Override
    public void apply(GameplayModel game) {
        if (!active) return;
        active = false;
        effectActive = true;
        elapsedMs = 0;

        game.setCombo(game.getCombo() + 1);
        game.setScoreMultiplier(2);
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
        effectActive = false;
        game.setScoreMultiplier(1);
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
        return "Score x2";
    }

    @Override
    public int getDurationMs() {
        return DURATION_MS;
    }

}
