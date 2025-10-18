package Model;

import javafx.scene.canvas.GraphicsContext;

/**
 * Lớp trừu tượng (abstract class) cơ sở cho tất cả các đối tượng trong trò chơi.
 * Cung cấp các thuộc tính và hành vi cơ bản mà mọi đối tượng trong game đều có,
 * chẳng hạn như tọa độ (x, y) và một phương thức trừu tượng để vẽ đối tượng đó.
 */
public abstract class GameObject {
    protected double x;
    protected double y;

    /**
     * Khởi tạo một đối tượng GameObject với tọa độ x và y được chỉ định.
     * @param x Tọa độ ban đầu theo trục x.
     * @param y Tọa độ ban đầu theo trục y.
     */
    public GameObject(double x, double y) {
        this.x = x;
        this.y = y;
    }

    /**
     * Lấy tọa độ hiện tại theo trục x của đối tượng.
     * @return Tọa độ x.
     */
    public double getX() {
        return x;
    }

    /**
     * Lấy tọa độ hiện tại theo trục y của đối tượng.
     * @return Tọa độ y.
     */
    public double getY() {
        return y;
    }

    /**
     * Đặt hoặc cập nhật tọa độ theo trục x của đối tượng.
     * @param x Tọa độ x mới.
     */
    public void setX(double x) {
        this.x = x;
    }

    /**
     * Đặt hoặc cập nhật tọa độ theo trục y của đối tượng.
     * @param y Tọa độ y mới.
     */
    public void setY(double y) {
        this.y = y;
    }

    /**
     * Phương thức trừu tượng để vẽ đối tượng lên màn hình.
     * Mọi lớp con kế thừa từ GameObject phải cung cấp cách triển khai (implementation)
     * riêng cho phương thức này để xác định cách chúng được hiển thị.
     * @param g Đối tượng GraphicsContext được sử dụng để thực hiện các thao tác vẽ trên canvas.
     */
    public abstract void draw(GraphicsContext g);
}