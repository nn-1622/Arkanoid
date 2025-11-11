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

        ArrayList<Ball> currentBalls = game.getBalls();
        ArrayList<Ball> newBalls = new ArrayList<>();
        int currentCount = game.getBalls().size();

        for (Ball b : currentBalls) {
            if (currentCount + newBalls.size() > 16) break;

            double x = b.getX();
            double y = b.getY();
            double vx = b.getVx();
            double vy = b.getVy();
            double r = b.getRadius();

            double speed = Math.sqrt(vx * vx + vy * vy);
            double angle = Math.atan2(vy, vx);

            double angle1 = angle + Math.toRadians(15);
            double angle2 = angle - Math.toRadians(15);

            Ball ball1 = new Ball(x, y, speed * Math.cos(angle1), speed * Math.sin(angle1), r);
            Ball ball2 = new Ball(x, y, speed * Math.cos(angle2), speed * Math.sin(angle2), r);

            newBalls.add(ball1);
            newBalls.add(ball2);

        }
        game.setBalls(newBalls);
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

}
