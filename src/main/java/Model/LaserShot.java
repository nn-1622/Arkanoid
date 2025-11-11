package Model;

import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;

public class LaserShot extends MovableObject {
    private static final double SPEED = -10;
    private static final double WIDTH = 4;
    private static final double HEIGHT = 15;
    private boolean destroyed = false;

    public LaserShot(double x, double y) {
        super(x, y, 0, SPEED);
    }

    @Override
    public void draw(GraphicsContext g) {
        g.setFill(Color.RED);
        g.fillRect(x, y, WIDTH, HEIGHT);
    }

    @Override
    public double getWidth() { return WIDTH; }

    @Override
    public double getHeight() { return HEIGHT; }

    public boolean isDestroyed() { return destroyed; }

    public void update() {
        y += this.getVy();
        if (y + HEIGHT < 0) destroyed = true;
    }

    public void checkLaser(GameplayModel game) {
        for (Brick brick : game.getBricks()) {
            if (brick.isBreaking()) continue;

            double laserLeft = x;
            double laserRight = x + WIDTH;
            double laserTop = y;
            double laserBottom = y + HEIGHT;

            double brickLeft = brick.getX();
            double brickRight = brick.getX() + brick.getWidth();
            double brickTop = brick.getY();
            double brickBottom = brick.getY() + brick.getHeight();

            boolean overlapX = laserRight >= brickLeft && laserLeft <= brickRight;
            boolean overlapY = laserBottom >= brickTop && laserTop <= brickBottom;

            if (overlapX && overlapY) {
                brick.hit();
                destroyed = true;
                game.scorePoint(1);
                break;
            }
        }
    }
}
