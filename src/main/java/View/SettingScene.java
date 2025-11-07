package View;

import Controller.AdjustVolumeCmd;
import Controller.ChangeStateCmd;
import Model.Button;
import Model.GameModel;
import Model.GameplayModel;
import Model.State;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;

import javafx.scene.input.MouseEvent;

/**
 * Lớp chịu trách nhiệm quản lý và hiển thị màn hình cài đặt của trò chơi.
 * Lớp này bao gồm ảnh nền và các nút tương tác để điều chỉnh âm lượng
 * và quay trở lại menu chính.
 */
public class SettingScene extends View {
    private Image settingBg;
    private Image settings;
    private Button exit;
    private Button lowVolume;
    private Button highVolume;

    /**
     * Khởi tạo một đối tượng SettingScene mới.
     * Tải ảnh nền và khởi tạo các nút (Thoát, Giảm âm lượng, Tăng âm lượng)
     * với vị trí, kích thước và hình ảnh tương ứng.
     */
    public SettingScene(GameModel model) {
        super(model);
        settingBg = new Image(getClass().getResource("/settingBg.png").toExternalForm());

        exit = new Button( 200.1, 523.8, 199.8, 41.9, new ChangeStateCmd(model, State.MENU));
        exit.setImgButton("/Exit.png");
        exit.setImgHoverButton("/ExitHover.png");

        lowVolume = new Button( 132.4, 347.3 , 60, 60, new AdjustVolumeCmd(false));
        lowVolume.setImgButton("/left.png");
        lowVolume.setImgHoverButton("/leftHover.png");

        highVolume = new Button(407.1, 347.3, 60, 60, new AdjustVolumeCmd(true));
        highVolume.setImgButton("/right.png");
        highVolume.setImgHoverButton("/rightHover.png");

        buttons.add(lowVolume);
        buttons.add(highVolume);
        buttons.add(exit);
    }

    /**
     * Vẽ toàn bộ màn hình cài đặt lên canvas.
     *
     * @param render        Đối tượng GraphicsContext được sử dụng để thực hiện các thao tác vẽ.
     * @param gameplayModel
     */
    @Override
    public void draw(GraphicsContext render, GameplayModel gameplayModel) {
        render.drawImage(settingBg,0, 0, 600, 650);
        exit.draw(render);
        lowVolume.draw(render);
        highVolume.draw(render);
    }

    /**
     * Kiểm tra và cập nhật trạng thái di chuột (hover) cho tất cả các nút trên màn hình cài đặt.
     * @param e Sự kiện chuột (MouseEvent) chứa tọa độ hiện tại của con trỏ.
     */
    @Override
    public void checkHover(MouseEvent e) {
        exit.setHovering(e);
        lowVolume.setHovering(e);
        highVolume.setHovering(e);
    }
}