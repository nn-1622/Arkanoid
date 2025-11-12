package Model;

import javafx.scene.canvas.GraphicsContext;

/**
 * Lớp interface đại diện cho một đối tượng cơ bản trong trò chơi.
 * các đối tượng như ball, brick ... đều kế thừa từ lớp này
 */
public abstract class GameObject {
    protected double x;
    protected double y;

    public GameObject(double x, double y) {
        this.x = x;
        this.y = y;
    }

    public abstract void draw(GraphicsContext g);

    public double getX() {
        return x;
    }

    public double getY() {
        return y;
    }

    public void setX(double x) {
        this.x = x;
    }

    public void setY(double y) {
        this.y = y;
    }
}