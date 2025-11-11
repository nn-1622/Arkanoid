package Model;

import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;

public class PU_ScoreX2 extends MovableObject implements PowerUp {
    private static final int DURATION_MS = 10000; // hiệu lực 10 giây
    private double radius;
    private Image img;
    private boolean active = true;      // đang rơi
    private boolean effectActive = false; // đang kích hoạt
    private int elapsedMs = 0;

    public PU_ScoreX2(double x, double y, double vx, double vy, double radius) {
        super(x, y, vx, vy);
        this.radius = radius;
        this.img = new Image("/x2.png"); // ảnh vật phẩm (thay bằng ảnh bạn có)
    }

    @Override
    public String getName() {
        return "Score x2";
    }

    @Override
    public int getDurationMs() {
        return DURATION_MS;
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

        // 🔥 bật chế độ nhân đôi điểm
        game.setCombo(game.getCombo() + 1); // combo vẫn tính riêng
        game.setScoreMultiplier(2); // ⚡ thêm biến multiplier trong GameplayModel
    }

    @Override
    public void update(GameplayModel game, double deltaTime) {
        if (!effectActive) return;
        elapsedMs += (int)(deltaTime * 1000);

        // Khi hết thời gian
        if (elapsedMs >= DURATION_MS) {
            remove(game);
        }
    }

    @Override
    public void remove(GameplayModel game) {
        effectActive = false;
        game.setScoreMultiplier(1); // trở lại bình thường
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

}
