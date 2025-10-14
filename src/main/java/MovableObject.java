import javafx.scene.canvas.GraphicsContext;

public abstract class MovableObject extends GameObject {
    private double vx;
    private double vy;

    public MovableObject(double x, double y) {
        super(x,y);
    }

    public MovableObject(double x, double y, double vx, double vy) {
        super(x,y);
        this.vx = vx;
        this.vy = vy;
    }

    public double getVx() {
        return vx;
    }

    public double getVy() {
        return vy;
    }

    public void setVx(double vx) {
        this.vx = vx;
    }

    public void setVy(double vy) {
        this.vy = vy;
    }

    public void move() {
        x += vx;
        y += vy;
    }
    
    public void reverseVx() {
        vx = -vx;
    }
    public void reverseVy() {
        vy = -vy;
    }

    public abstract void draw(GraphicsContext g);
}
