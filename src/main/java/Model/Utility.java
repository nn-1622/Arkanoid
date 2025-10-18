package Model;

/**
 * Lớp trừu tượng (abstract class) cơ sở cho các thành phần tiện ích,
 * chẳng hạn như các yếu tố giao diện người dùng (ví dụ: Button).
 * Lớp này cung cấp các thuộc tính chung và các phương thức truy cập
 * cho vị trí (x, y) và kích thước (width, height) của một đối tượng hình chữ nhật.
 */
public abstract class Utility {
    protected double x;
    protected double y;
    protected double width;
    protected double height;

    /**
     * Khởi tạo một đối tượng Utility.
     * Tất cả các thuộc tính vị trí và kích thước được đặt thành 0 theo mặc định.
     */
    public Utility() {
        this.x = 0;
        this.y = 0;
        this.width = 0;
        this.height = 0;
    }

    /**
     * Lấy tọa độ theo trục x.
     * @return Tọa độ x hiện tại.
     */
    public double getX() {
        return x;
    }

    /**
     * Đặt tọa độ theo trục x.
     * @param x Tọa độ x mới.
     */
    public void setX(double x) {
        this.x = x;
    }

    /**
     * Lấy tọa độ theo trục y.
     * @return Tọa độ y hiện tại.
     */
    public double getY() {
        return y;
    }

    /**
     * Đặt tọa độ theo trục y.
     * @param y Tọa độ y mới.
     */
    public void setY(double y) {
        this.y = y;
    }

    /**
     * Lấy chiều rộng của đối tượng.
     * @return Chiều rộng hiện tại.
     */
    public double getWidth() {
        return width;
    }

    /**
     * Đặt chiều rộng của đối tượng.
     * @param width Chiều rộng mới.
     */
    public void setWidth(double width) {
        this.width = width;
    }

    /**
     * Lấy chiều cao của đối tượng.
     * @return Chiều cao hiện tại.
     */
    public double getHeight() {
        return height;
    }

    /**
     * Đặt chiều cao của đối tượng.
     * @param height Chiều cao mới.
     */
    public void setHeight(double height) {
        this.height = height;
    }
}