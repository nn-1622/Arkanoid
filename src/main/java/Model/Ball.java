package Model;

import javafx.scene.image.Image;
import javafx.scene.canvas.GraphicsContext;

/**
 * Lớp đại diện cho đối tượng quả bóng trong trò chơi.
 * Kế thừa từ {@link MovableObject}, lớp Ball có thêm thuộc tính bán kính (radius)
 * và hình ảnh riêng để hiển thị.
 */
public class Ball extends MovableObject {
    private double radius;
    private Image ballImg;

    /**
     * Khởi tạo một đối tượng Ball mới.
     * @param x Tọa độ x ban đầu của tâm bóng.
     * @param y Tọa độ y ban đầu của tâm bóng.
     * @param vx Vận tốc ban đầu theo trục x.
     * @param vy Vận tốc ban đầu theo trục y.
     * @param radius Bán kính của quả bóng.
     */
    public Ball(double x, double y, double vx, double vy, double radius) {
        super(x,y,vx,vy);
        this.radius = radius;
        ballImg = new Image("/DefaultBall.png");
    }

    /**
     * Lấy tọa độ x của cạnh trái của quả bóng.
     * @return Tọa độ x của cạnh trái.
     */
    public double getEdgeLeft() {
        return x - radius;
    }

    /**
     * Lấy tọa độ x của cạnh phải của quả bóng.
     * @return Tọa độ x của cạnh phải.
     */
    public double getEdgeRight() {
        return x + radius;
    }

    /**
     * Lấy tọa độ y của cạnh trên của quả bóng.
     * @return Tọa độ y của cạnh trên.
     */
    public double getEdgeTop() {
        return y - radius;
    }

    /**
     * Lấy tọa độ y của cạnh dưới của quả bóng.
     * @return Tọa độ y của cạnh dưới.
     */
    public double getEdgeBottom() {
        return y + radius;
    }

    /**
     * Lấy bán kính của quả bóng.
     * @return Bán kính.
     */
    public double getRadius() {
        return radius;
    }

    /**
     * Lấy tọa độ x của tâm quả bóng.
     * @return Tọa độ x của tâm.
     */
    public double getCenter() {
        return x;
    }
    
    /**
     * Đặt một hình ảnh mới cho quả bóng.
     * @param ballImg Đối tượng Image mới để hiển thị cho quả bóng.
     */
    public void setBallImg(Image ballImg) {
        this.ballImg = ballImg;
    }

    /**
     * {@inheritDoc}
     * Vẽ hình ảnh của quả bóng lên canvas tại vị trí hiện tại.
     * Hình ảnh được vẽ sao cho tâm của nó trùng với tọa độ (x, y) của đối tượng Ball.
     */
    @Override
    public void draw(GraphicsContext render) {
        render.drawImage(ballImg, x - radius, y - radius, radius *2, radius*2);
    }
}