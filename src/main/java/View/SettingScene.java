package View;

import Model.Button;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;

import javafx.scene.input.MouseEvent;

/**
 * Lớp chịu trách nhiệm quản lý và hiển thị màn hình cài đặt của trò chơi.
 * Lớp này bao gồm ảnh nền và các nút tương tác để điều chỉnh âm lượng
 * và quay trở lại menu chính.
 */
public class SettingScene {
    private Image settingBg;
    private Image settings;
    private Button exit;
    private Button lowVolume;
    private Button highVolume;

    /**
     * Khởi tạo một đối tượng SettingScene mới.
     * Tải ảnh nền và khởi tạo các nút (Thoát, Giảm âm lượng, Tăng âm lượng)
     * với vị trí, kích thước và hình ảnh tương ứng.
     * @param canvasWidth  Chiều rộng của canvas (hiện không được sử dụng nhưng có thể cần cho việc căn chỉnh trong tương lai).
     * @param canvasHeight Chiều cao của canvas (hiện không được sử dụng nhưng có thể cần cho việc căn chỉnh trong tương lai).
     */
    public SettingScene(double canvasWidth, double canvasHeight) {
        settingBg = new Image(getClass().getResource("/settingBg.png").toExternalForm());

        exit = new Button( 200.1, 523.8, 199.8, 41.9);
        exit.setImgButton("/Exit.png");
        exit.setImgHoverButton("/ExitHover.png");

        lowVolume = new Button( 132.4, 347.3 , 60, 60);
        lowVolume.setImgButton("/left.png");
        lowVolume.setImgHoverButton("/leftHover.png");

        highVolume = new Button(407.1, 347.3, 60, 60);
        highVolume.setImgButton("/right.png");
        highVolume.setImgHoverButton("/rightHover.png");
    }

    /**
     * Vẽ toàn bộ màn hình cài đặt lên canvas.
     * @param render Đối tượng GraphicsContext được sử dụng để thực hiện các thao tác vẽ.
     */
    public void drawSettingScene(GraphicsContext render) {
        render.drawImage(settingBg,0, 0, 600, 650);
        exit.draw(render);
        lowVolume.draw(render);
        highVolume.draw(render);
    }

    /**
     * Kiểm tra và cập nhật trạng thái di chuột (hover) cho tất cả các nút trên màn hình cài đặt.
     * @param e Sự kiện chuột (MouseEvent) chứa tọa độ hiện tại của con trỏ.
     */
    public void checkHover(MouseEvent e) {
        exit.setHovering(e);
        lowVolume.setHovering(e);
        highVolume.setHovering(e);
    }

    /**
     * Kiểm tra xem sự kiện nhấp chuột có xảy ra trên nút "Thoát" hay không.
     * @param e Sự kiện chuột (MouseEvent) để kiểm tra.
     * @return true nếu nút "Thoát" được nhấp, ngược lại là false.
     */
    public boolean exitClicked(MouseEvent e) {
        return exit.isClicked(e);
    }

    /**
     * Kiểm tra xem sự kiện nhấp chuột có xảy ra trên nút "Giảm âm lượng" hay không.
     * @param e Sự kiện chuột (MouseEvent) để kiểm tra.
     * @return true nếu nút "Giảm âm lượng" được nhấp, ngược lại là false.
     */
    public boolean lowVolumeClicked(MouseEvent e) {
        return lowVolume.isClicked(e);
    }

    /**
     * Kiểm tra xem sự kiện nhấp chuột có xảy ra trên nút "Tăng âm lượng" hay không.
     * @param e Sự kiện chuột (MouseEvent) để kiểm tra.
     * @return true nếu nút "Tăng âm lượng" được nhấp, ngược lại là false.
     */
    public boolean highVolumeClicked(MouseEvent e) {
        return highVolume.isClicked(e);
    }
}