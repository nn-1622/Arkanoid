package Model;

import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;
import javafx.scene.input.MouseEvent;

/**
 * Lớp đại diện cho một nút bấm (button) có thể tương tác trong giao diện người dùng.
 * Lớp này quản lý vị trí, kích thước, hình ảnh mặc định và hình ảnh khi di chuột qua.
 * Nó cũng cung cấp các phương thức để kiểm tra trạng thái di chuột (hover) và nhấp chuột (click).
 */
public class Button extends Utility {
    private Image imgButton;
    private Image imgHoverButton;
    private boolean isHover;

    /**
     * Khởi tạo một đối tượng Button mới với vị trí và kích thước được chỉ định.
     * @param x Tọa độ x của góc trên bên trái của nút.
     * @param y Tọa độ y của góc trên bên trái của nút.
     * @param width Chiều rộng của nút.
     * @param height Chiều cao của nút.
     */
    public Button(double x, double y, double width, double height) {
        this.x = x;
        this.y = y;
        this.width = width;
        this.height = height;
    }

    /**
     * Thiết lập hình ảnh mặc định cho nút.
     * @param link Đường dẫn đến tệp hình ảnh trong thư mục tài nguyên (resources).
     */
    public void setImgButton(String link) {
        this.imgButton = new Image(getClass().getResource(link).toExternalForm());
    }

    /**
     * Thiết lập hình ảnh cho nút khi con trỏ chuột di chuyển qua.
     * @param link Đường dẫn đến tệp hình ảnh trong thư mục tài nguyên (resources).
     */
    public void setImgHoverButton(String link) {
        this.imgHoverButton = new Image(getClass().getResource(link).toExternalForm());
    }

    /**
     * Đặt trạng thái di chuột (hover) của nút một cách thủ công.
     * @param hover true nếu nút đang được di chuột qua, ngược lại là false.
     */
    public void setHover(boolean hover) {
        isHover = hover;
    }

    /**
     * Kiểm tra xem nút có đang ở trạng thái di chuột qua hay không.
     * @return true nếu con trỏ chuột đang ở trên nút, ngược lại là false.
     */
    public boolean isHover(){
        return isHover;
    }

    /**
     * Cập nhật trạng thái di chuột (hover) của nút dựa trên vị trí của con trỏ chuột.
     * @param m Sự kiện chuột (MouseEvent) chứa tọa độ của con trỏ.
     */
    public void setHovering(MouseEvent m){
        isHover = m.getX() > x && m.getX() < x + width && m.getY() > y && m.getY() < y + height;
    }

    /**
     * Kiểm tra xem một sự kiện nhấp chuột có xảy ra trong phạm vi của nút hay không.
     * @param m Sự kiện chuột (MouseEvent) chứa tọa độ của cú nhấp chuột.
     * @return true nếu cú nhấp chuột nằm trong khu vực của nút, ngược lại là false.
     */
    public boolean isClicked(MouseEvent m){
        return m.getX() > x && m.getX() < x + width && m.getY() > y && m.getY() < y + height;
    }
    
    /**
     * Vẽ nút lên canvas.
     * Phương thức này sẽ vẽ hình ảnh mặc định hoặc hình ảnh khi di chuột qua,
     * tùy thuộc vào trạng thái {@code isHover} hiện tại.
     * @param gc Đối tượng GraphicsContext được sử dụng để vẽ trên canvas.
     */
    public void draw(GraphicsContext gc){
        if(!isHover){
            gc.drawImage(imgButton, x, y, width, height);
        } else{
            gc.drawImage(imgHoverButton, x, y, width, height);
        }
    }
}