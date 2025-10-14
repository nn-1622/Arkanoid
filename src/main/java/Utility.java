public abstract class Utility {
    protected double x;
    protected double y;
    protected double width;
    protected double height;
    public Utility() {
        this.x = 0;
        this.y = 0;
        this.width = 0;
        this.height = 0;
    }
    public double getX() {
        return x;
    }
    public void setX(double x) {
        this.x = x;
    }
    public double getY() {
        return y;
    }
    public void setY(double y) {
        this.y = y;
    }
    public double getWidth() {
        return width;
    }
    public void setWidth(double width) {
        this.width = width;
    }
    public double getHeight() {
        return height;
    }
    public void setHeight(double height) {
        this.height = height;
    }
}
