package Model;

import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;

/**
 * Lớp đại diện cho thanh trượt (paddle) của người chơi.
 * Kế thừa từ {@link MovableObject}, lớp này quản lý vị trí, kích thước, hình ảnh
 * và logic di chuyển ngang của thanh trượt dựa trên đầu vào của người dùng.
 */
public class Paddle extends MovableObject implements UltilityValues {
    private static Paddle paddle;
    private double length;
    private double height;
    private Image paddleImg;
    private boolean shield = false;
    private double shieldOpacity = 0.8;
    private double shieldPulse = 0.0;      // để làm hiệu ứng gợn sóng
    private boolean shieldIncreasing = true;

    /**
     * Khởi tạo một đối tượng Paddle mới.
     * @param x Tọa độ x ban đầu của góc trên bên trái thanh trượt.
     * @param y Tọa độ y ban đầu của góc trên bên trái thanh trượt.
     * @param length Chiều dài (chiều rộng) của thanh trượt.
     * @param height Chiều cao của thanh trượt.
     */
    private Paddle(double x, double y, double length, double height) {
        super(x, y);
        this.length = length;
        this.height = height;
        this.paddleImg = new Image("/DefaultPaddle.png");
        setVx(5); // Đặt tốc độ di chuyển mặc định
    }

    public static Paddle getPaddle() {
        if (paddle == null) {
            paddle = new Paddle(canvasWidth / 2 - paddleLength / 2,
                    canvasHeight - 140, paddleLength, paddleHeight);

        }
        return paddle;
    }

    public static Paddle newInstance(double x, double y, double length, double height) {
        return new Paddle(x, y, length, height);
    }


    /**
     * Lấy chiều dài của thanh trượt.
     * @return Chiều dài của thanh trượt.
     */
    public double getLength() {
        return length;
    }
    public void setLength(double length) {
        this.length = length;
    }
    /**
     * Lấy chiều cao của thanh trượt.
     * @return Chiều cao của thanh trượt.
     */
    public double getHeight() {
        return height;
    }

    //public void setLength;

    /**
     * Thiết lập một hình ảnh mới cho thanh trượt.
     * @param paddleImg Đối tượng Image mới để hiển thị cho thanh trượt.
     */
    public void setPaddleImg(Image paddleImg) {
        this.paddleImg = paddleImg;
    }

    public void setShield(boolean v) { shield = v; }
    public boolean hasShield() { return shield; }

    /**
     * {@inheritDoc}
     * Vẽ hình ảnh của thanh trượt lên canvas tại vị trí hiện tại của nó.
     */
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

    /**
     * Di chuyển thanh trượt sang trái hoặc sang phải dựa trên đầu vào của người dùng.
     * Vị trí x của thanh trượt được cập nhật dựa trên vận tốc đã đặt.
     * @param left  true nếu phím di chuyển sang trái được nhấn.
     * @param right true nếu phím di chuyển sang phải được nhấn.
     */
    public void move(boolean left, boolean right){
        if(left){
            x -= this.getVx();
        } else if(right){
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

}