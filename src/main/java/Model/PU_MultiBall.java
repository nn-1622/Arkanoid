package Model;

import java.util.List;
import java.util.ArrayList;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;
import java.awt.*;

public class PU_MultiBall extends MovableObject implements PowerUp {
    private double radius;
    private Image multi_ball;
    private boolean active = false;
    private double originalWidth;

    public PU_MultiBall(double x, double y, double vx, double vy, double radius) {
        super(x, y, vx, vy);
        this.radius = radius;
        this.multi_ball = new Image ("/MultiBallPU.png");
    }

    @Override
    public String getName() {
        return "Multi Ball";
    }

    @Override
    public int getDurationMs() {
        return 0;
    }

    @Override
    public void draw(GraphicsContext g) {
        g.drawImage(multi_ball, x - radius, y - radius, radius *2, radius*2);
    }

    @Override
    public void apply(GameplayModel game) {
        if (active) return;
        active = true;

        List<Ball> currentBalls = game.getBalls();
        List<Ball> newBalls = new ArrayList<>();
        int currentCount = game.getBalls().size();

        for (Ball b : currentBalls) {
            // Nếu đủ 10 bóng thì dừng luôn
            if (currentCount + newBalls.size() >= 10) break;

            Ball ball1 = new Ball(b.getX(), b.getY(), b.getVx(), -b.getVy(), radius);
            if (currentCount + newBalls.size() < 10) newBalls.add(ball1);

            Ball ball2 = new Ball(b.getX(), b.getY(), -b.getVx(), b.getVy(), radius);
            if (currentCount + newBalls.size() < 10) newBalls.add(ball2);
        }

        // Thêm bóng mới vào danh sách bóng trong GameplayModel
        game.getBalls().addAll(newBalls);
    }

    @Override
    public void remove(GameplayModel game) {
        // MultiBall là hiệu ứng tức thời → không cần remove
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

}
