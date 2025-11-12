package Model;

import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;

public class Paddle extends MovableObject implements UltilityValues {
    private double length;
    private final double height;

    private static Paddle paddle;
    private final Image paddleImg;

    private boolean shield = false;
    private double shieldPulse = 0.0;
    private boolean shieldIncreasing = true;

    private Paddle(double x, double y, double length, double height, String path) {
        super(x, y);
        this.length = length;
        this.height = height;
        this.paddleImg = new Image(path);
        setVx(5);
    }

    public static Paddle getPaddle(String path) {
        if (paddle == null) {
            paddle = new Paddle(canvasWidth / 2 - paddleLength / 2,
                    canvasHeight - 140, paddleLength, paddleHeight, path);

        }
        return paddle;
    }

    public void update(double deltaTime) {
        if (shield) {
            if (shieldIncreasing) {
                shieldPulse += deltaTime * 2;
                if (shieldPulse >= 1.0) {
                    shieldPulse = 1.0;
                    shieldIncreasing = false;
                }
            } else {
                shieldPulse -= deltaTime * 2;
                if (shieldPulse <= 0.2) {
                    shieldPulse = 0.2;
                    shieldIncreasing = true;
                }
            }
        }
    }

    @Override
    public void draw(GraphicsContext g) {
        g.drawImage(paddleImg, x, y, length, height);
    }

    public void move(boolean left, boolean right) {
        if (left) {
            x -= this.getVx();
        } else if (right) {
            x += this.getVx();
        }
    }

    public void checkBoundary(double canvasWidth) {
        if (getX() < 0) {
            setX(0);
        } else if (getX() > canvasWidth - getLength()) {
            setX(canvasWidth - getLength());
        }
    }

    public double getLength() {
        return length;
    }

    public void setLength(double length) {
        this.length = length;
    }

    public static Paddle newInstance(double x, double y, double length, double height, String path) {
        return new Paddle(x, y, length, height, path);
    }

    public double getHeight() {
        return height;
    }

    public void setShield(boolean v) {
        shield = v;
    }

    public boolean hasShield() {
        return shield;
    }
}