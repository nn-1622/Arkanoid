package Model;

import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;
import javafx.scene.media.AudioClip;

/**
 * Lớp đại diện cho một đối tượng gạch trong trò chơi.
 * Lớp này quản lý trạng thái của viên gạch, bao gồm loại (độ bền),
 * hoạt ảnh khi bị phá hủy, và logic vẽ viên gạch lên màn hình.
 */
public class Brick extends GameObject{
    private Image image = new Image("/Sprite.png");
    private final double width = 50;
    private final double height = 25;
    private static final double sx = 32, sy = 176, sw = 32, sh = 16;
    private int brickType;
    private int currentFrame;
    private boolean breaking;
    private boolean destroyed;
    public double frameTimer = 0;
    private int totalFrames = 10;
    private double frameDuration = 0.05;

    /**
     * Khởi tạo một đối tượng Brick mới tại một vị trí cụ thể.
     * @param x Tọa độ x ban đầu của viên gạch.
     * @param y Tọa độ y ban đầu của viên gạch.
     */
    public Brick(double x, double y) {
        super(x,y);
        this.brickType = 0;
        this.breaking = false;
        this.destroyed = false;
        currentFrame = 0;
    }

    /**
     * Cập nhật trạng thái của viên gạch, chủ yếu để xử lý hoạt ảnh phá hủy.
     * Nếu viên gạch đang trong trạng thái 'breaking', phương thức này sẽ tiến hành
     * chuyển đổi qua các khung hình của hoạt ảnh dựa trên thời gian đã trôi qua.
     * @param deltaTime Thời gian (tính bằng giây) đã trôi qua kể từ lần cập nhật cuối cùng.
     */
    public void update(double deltaTime) {
        if (breaking) {
            frameTimer += deltaTime;
            if (frameTimer >= frameDuration) {
                frameTimer = 0;
                currentFrame++;
                if (currentFrame >= totalFrames) {
                    destroyed = true;
                    breaking = false;
                }
            }
        }
    }

    /**
     * Xử lý logic khi viên gạch bị bóng va vào.
     * Phương thức này giảm độ bền (brickType) của viên gạch. Nếu độ bền về 0,
     * nó sẽ bắt đầu quá trình hoạt ảnh phá hủy.
     */
    public void hit() {
        brickType--;
        if (brickType == 0 && !breaking) {
            breaking = true;
            currentFrame = 0;
        }
    }

    /**
     * Kiểm tra xem viên gạch đã bị phá hủy hoàn toàn hay chưa.
     * @return true nếu viên gạch đã kết thúc hoạt ảnh và cần được loại bỏ khỏi game, ngược lại trả về false.
     */
    public boolean isDestroyed() {
        return destroyed;
    }

    /**
     * Kiểm tra xem viên gạch có đang trong quá trình hoạt ảnh phá hủy hay không.
     * @return true nếu viên gạch đang thực hiện hoạt ảnh phá hủy, ngược lại trả về false.
     */
    public boolean isBreaking() {
        return breaking;
    }

    /**
     * Lấy loại (độ bền) hiện tại của viên gạch.
     * @return Loại/độ bền của viên gạch.
     */
    public int getBrickType(){
        return this.brickType;
    }

    /**
     * Thiết lập loại (độ bền) cho viên gạch.
     * @param x Loại/độ bền mới.
     */
    public void setBrickType(int x){
        this.brickType = x;
    }

    /**
     * Lấy tọa độ y của cạnh trên của viên gạch.
     * @return Tọa độ y của cạnh trên.
     */
    public double getEdgeTop() {
        return this.y;
    }

    /**
     * Lấy tọa độ y của cạnh dưới của viên gạch.
     * @return Tọa độ y của cạnh dưới.
     */
    public double getEdgeBottom() {
        return this.y + this.height;
    }

    /**
     * Lấy tọa độ x của cạnh trái của viên gạch.
     * @return Tọa độ x của cạnh trái.
     */
    public double getEdgeLeft() {
        return this.x;
    }

    /**
     * Lấy tọa độ x của cạnh phải của viên gạch.
     * @return Tọa độ x của cạnh phải.
     */
    public double getEdgeRight() {
        return this.x + this.width;
    }

    public double getHeight() {
        return height;
    }

    public double getWidth() {
        return width;
    }

    /**
     * {@inheritDoc}
     * Vẽ viên gạch lên canvas.
     * Nếu viên gạch không ở trạng thái 'breaking', nó sẽ vẽ hình ảnh tương ứng với loại gạch.
     * Nếu đang ở trạng thái 'breaking', nó sẽ vẽ khung hình hiện tại của hoạt ảnh phá hủy.
     */
    @Override
    public void draw(GraphicsContext render) {
        if(!isBreaking()) {
            render.drawImage(image, sx, sy + (brickType-1) * 16, sw, sh, x, y, width, height);
        } else {
            render.drawImage(image, sx + 32 * currentFrame, sy, sw, sh, x, y, width, height);
        }
    }
}