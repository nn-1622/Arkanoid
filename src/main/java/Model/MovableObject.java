package Model;

import javafx.scene.canvas.GraphicsContext;

/**
 * Lớp trừu tượng (abstract class) mở rộng từ {@link GameObject}.
 * Lớp này là cơ sở cho tất cả các đối tượng trong game có khả năng di chuyển.
 * Nó bổ sung thêm các thuộc tính vận tốc (vx, vy) và các phương thức để
 * cập nhật vị trí và thay đổi hướng di chuyển.
 */
public abstract class MovableObject extends GameObject {
    private double vx;
    private double vy;

    /**
     * Khởi tạo một đối tượng MovableObject chỉ với tọa độ ban đầu.
     * Vận tốc mặc định sẽ là (0, 0).
     * @param x Tọa độ ban đầu theo trục x.
     * @param y Tọa độ ban đầu theo trục y.
     */
    public MovableObject(double x, double y) {
        super(x,y);
    }

    /**
     * Khởi tạo một đối tượng MovableObject với tọa độ và vận tốc ban đầu.
     * @param x Tọa độ ban đầu theo trục x.
     * @param y Tọa độ ban đầu theo trục y.
     * @param vx Vận tốc ban đầu theo trục x.
     * @param vy Vận tốc ban đầu theo trục y.
     */
    public MovableObject(double x, double y, double vx, double vy) {
        super(x,y);
        this.vx = vx;
        this.vy = vy;
    }

    /**
     * Lấy vận tốc hiện tại theo trục x.
     * @return Vận tốc theo trục x.
     */
    public double getVx() {
        return vx;
    }

    /**
     * Lấy vận tốc hiện tại theo trục y.
     * @return Vận tốc theo trục y.
     */
    public double getVy() {
        return vy;
    }

    /**
     * Đặt hoặc cập nhật vận tốc theo trục x.
     * @param vx Vận tốc mới theo trục x.
     */
    public void setVx(double vx) {
        this.vx = vx;
    }

    /**
     * Đặt hoặc cập nhật vận tốc theo trục y.
     * @param vy Vận tốc mới theo trục y.
     */
    public void setVy(double vy) {
        this.vy = vy;
    }

    /**
     * Cập nhật vị trí của đối tượng (x, y) dựa trên vận tốc hiện tại (vx, vy).
     * Phương thức này nên được gọi trong mỗi khung hình (frame) để tạo ra chuyển động.
     */
    public void move() {
        x += vx;
        y += vy;
    }
    
    /**
     * Đảo ngược vận tốc theo trục x.
     * Thường được sử dụng khi đối tượng va chạm với một bề mặt thẳng đứng.
     */
    public void reverseVx() {
        vx = -vx;
    }

    /**
     * Đảo ngược vận tốc theo trục y.
     * Thường được sử dụng khi đối tượng va chạm với một bề mặt nằm ngang.
     */
    public void reverseVy() {
        vy = -vy;
    }

    /**
     * {@inheritDoc}
     * Phương thức trừu tượng để vẽ đối tượng. Các lớp con phải triển khai phương thức này.
     */
    public abstract void draw(GraphicsContext g);
    public double getWidth() { return 0; }
    public double getHeight() { return 0; }

}