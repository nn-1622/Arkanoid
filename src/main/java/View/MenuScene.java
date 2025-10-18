package View;

import Model.Button;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;
import javafx.scene.input.MouseEvent;

/**
 * Lớp chịu trách nhiệm quản lý và hiển thị màn hình menu chính của trò chơi.
 * Lớp này bao gồm ảnh nền và các nút tương tác như "Bắt đầu", "Cài đặt" và "Thoát".
 */
public class MenuScene {
    private Image background;
    Button start;
    Button settings;
    Button exit;

    /**
     * Khởi tạo một đối tượng MenuScene mới.
     * Tải ảnh nền và khởi tạo các nút (Start, Settings, Exit) với vị trí,
     * kích thước và hình ảnh tương ứng cho trạng thái bình thường và khi di chuột qua.
     * @param canvasWidth  Chiều rộng của canvas, dùng để căn chỉnh vị trí (nếu cần).
     * @param canvasHeight Chiều cao của canvas, dùng để căn chỉnh vị trí (nếu cần).
     */
    public MenuScene(double canvasWidth, double canvasHeight) {
        background = new Image(getClass().getResource("/bg.jpg").toExternalForm());

        start = new Button( 225.6, 377, 148.8, 65.6);
        start.setImgButton("/Start.png");
        start.setImgHoverButton("/StartHover.png");

        settings = new Button( 225.6, 462.4, 148.8, 65.6);
        settings.setImgButton("/Setting.png");
        settings.setImgHoverButton("/SettingHover.png");

        exit = new Button( 225.6, 548, 148.8, 65.6);
        exit.setImgButton("/Exit.png");
        exit.setImgHoverButton("/ExitHover.png");
    }

    /**
     * Vẽ toàn bộ màn hình menu lên canvas.
     * Phương thức này sẽ vẽ ảnh nền trước, sau đó vẽ các nút lên trên.
     * @param render Đối tượng GraphicsContext được sử dụng để thực hiện các thao tác vẽ.
     */
    public void drawMenuScene(GraphicsContext render) {
        render.drawImage(background, 0, 0, 600, 650);
        start.draw(render);
        settings.draw(render);
        exit.draw(render);
    }

    /**
     * Kiểm tra và cập nhật trạng thái di chuột (hover) cho tất cả các nút trên menu.
     * @param e Sự kiện chuột (MouseEvent) chứa tọa độ hiện tại của con trỏ.
     */
    public void checkHover(MouseEvent e) {
        start.setHovering(e);
        settings.setHovering(e);
        exit.setHovering(e);
    }

    /**
     * Kiểm tra xem sự kiện nhấp chuột có xảy ra trên nút "Cài đặt" hay không.
     * @param e Sự kiện chuột (MouseEvent) để kiểm tra.
     * @return true nếu nút "Cài đặt" được nhấp, ngược lại là false.
     */
    public boolean settingClick(MouseEvent e) {
        return settings.isClicked(e);
    }

    /**
     * Kiểm tra xem sự kiện nhấp chuột có xảy ra trên nút "Thoát" hay không.
     * @param e Sự kiện chuột (MouseEvent) để kiểm tra.
     * @return true nếu nút "Thoát" được nhấp, ngược lại là false.
     */
    public boolean exitClick(MouseEvent e) {
        return exit.isClicked(e);
    }

    /**
     * Kiểm tra xem sự kiện nhấp chuột có xảy ra trên nút "Bắt đầu" hay không.
     * @param e Sự kiện chuột (MouseEvent) để kiểm tra.
     * @return true nếu nút "Bắt đầu" được nhấp, ngược lại là false.
     */
    public boolean startClick(MouseEvent e) { 
        return start.isClicked(e); 
    }
}