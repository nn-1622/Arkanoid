package Model;

import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;

/**
 * đối tượng gạch
 */
public class Brick extends GameObject {
    private static final double sx = 32, sy = 176, sw = 32, sh = 16;

    private final Image image = new Image("/Sprite.png");

    private final double width = 50;
    private final double height = 25;

    private int brickType;
    private int currentFrame;
    private boolean breaking;
    private boolean destroyed;
    public double frameTimer = 0;

    public Brick(double x, double y) {
        super(x, y);
        this.brickType = 0;
        this.breaking = false;
        this.destroyed = false;
        currentFrame = 0;
    }

    public void update(double deltaTime) {
        if (breaking) {
            frameTimer += deltaTime;
            double frameDuration = 0.05;
            if (frameTimer >= frameDuration) {
                frameTimer = 0;
                currentFrame++;
                int totalFrames = 10;
                if (currentFrame >= totalFrames) {
                    destroyed = true;
                    breaking = false;
                }
            }
        }
    }

    public void hit() {
        brickType--;
        if (brickType == 0 && !breaking) {
            breaking = true;
            currentFrame = 0;
        }
    }

    @Override
    public void draw(GraphicsContext render) {
        if (!isBreaking()) {
            render.drawImage(image, sx, sy + (brickType - 1) * 16, sw, sh, x, y, width, height);
        } else {
            render.drawImage(image, sx + 32 * currentFrame, sy, sw, sh, x, y, width, height);
        }
    }

    public boolean isDestroyed() {
        return destroyed;
    }

    public boolean isBreaking() {
        return breaking;
    }

    public int getBrickType() {
        return this.brickType;
    }

    public void setBrickType(int x) {
        this.brickType = x;
    }

    public double getEdgeTop() {
        return this.y;
    }

    public double getEdgeBottom() {
        return this.y + this.height;
    }

    public double getEdgeLeft() {
        return this.x;
    }

    public double getEdgeRight() {
        return this.x + this.width;
    }

    public double getHeight() {
        return height;
    }

    public double getWidth() {
        return width;
    }
}