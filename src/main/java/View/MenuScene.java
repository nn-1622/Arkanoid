package View;

import Controller.ChangeStateCmd;
import Controller.ExitCmd;
import Model.Button;
import Model.GameModel;
import Model.GameplayModel;
import Model.State;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;
import javafx.scene.input.MouseEvent;

/**
 * Lớp chịu trách nhiệm quản lý và hiển thị màn hình menu chính của trò chơi.
 * Lớp này bao gồm ảnh nền và các nút tương tác như "Bắt đầu", "Cài đặt" và "Thoát".
 */
public class MenuScene extends View implements SceneActions {
    private Image background;
    Button start;
    Button settings;
    Button exit;

    /**
     * Khởi tạo một đối tượng MenuScene mới.
     * Tải ảnh nền và khởi tạo các nút (Start, Settings, Exit) với vị trí,
     * kích thước và hình ảnh tương ứng cho trạng thái bình thường và khi di chuột qua.
     */
    public MenuScene(GameModel model) {
        super(model);


        background = new Image(getClass().getResource("/bg.png").toExternalForm());

        start = new Button( 225.6, 377, 148.8, 65.6, new ChangeStateCmd(model, State.PLAYING));
        start.setImgButton("/Start.png");
        start.setImgHoverButton("/StartHover.png");

        settings = new Button( 225.6, 462.4, 148.8, 65.6, new ChangeStateCmd(model, State.SETTING));
        settings.setImgButton("/Setting.png");
        settings.setImgHoverButton("/SettingHover.png");

        exit = new Button( 225.6, 548, 148.8, 65.6, new ExitCmd());
        exit.setImgButton("/Exit.png");
        exit.setImgHoverButton("/ExitHover.png");

        buttons.add(start);
        buttons.add(settings);
        buttons.add(exit);
    }

    /**
     * Vẽ toàn bộ màn hình menu lên canvas.
     * Phương thức này sẽ vẽ ảnh nền trước, sau đó vẽ các nút lên trên.
     *
     * @param render        Đối tượng GraphicsContext được sử dụng để thực hiện các thao tác vẽ.
     * @param gameplayModel
     */
    @Override
    public void draw(GraphicsContext render, GameplayModel gameplayModel) {
        render.drawImage(background, 0, 0, 600, 650);
        start.draw(render);
        settings.draw(render);
        exit.draw(render);
    }

    /**
     * Kiểm tra và cập nhật trạng thái di chuột (hover) cho tất cả các nút trên menu.
     * @param e Sự kiện chuột (MouseEvent) chứa tọa độ hiện tại của con trỏ.
     */
    @Override
    public void checkHover(MouseEvent e) {
        for (Button b : buttons) {
            b.setHovering(e);
        }
    }
}