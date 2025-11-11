package View;


import Controller.ChangeStateCmd;
import Model.Button;
import Model.GameModel;
import Model.GameplayModel;
import Model.State;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;
import Controller.GameCommand;
import javafx.scene.input.MouseEvent;

/**
 * Lớp chịu trách nhiệm quản lý và hiển thị màn hình cài đặt của trò chơi.
 * Lớp này bao gồm ảnh nền và các nút tương tác để điều chỉnh âm lượng
 * và quay trở lại menu chính.
 */
public class SettingScene extends View {
    private Image settingBg;
    private Button exit;

    private Button accountButton;
    private Button themeButton;
    private Button volumeButton;
    private Button howToPlayButton;

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

        double btnWidth = 250;
        double btnHeight = 60;
        double startY = 200;
        double spacing = 70;
        double centerX = (600 - btnWidth) / 2;

        accountButton = new Button( centerX, startY, btnWidth, btnHeight,
                new GameCommand() { // Lệnh mới
                    @Override
                    public void execute() {
                        model.setStateBeforeAccount(State.SETTING);
                        model.setGstate(State.SETTING_ACCOUNT);
                    }
                });
        accountButton.setImgButton("/Continue.png");
        accountButton.setImgHoverButton("/ContinueHover.png");

        themeButton = new Button( centerX, startY + spacing, btnWidth, btnHeight,
                new ChangeStateCmd(model, State.SETTING_THEME));
        themeButton.setImgButton("/Continue.png");
        themeButton.setImgHoverButton("/ContinueHover.png");
        volumeButton = new Button( centerX, startY + (spacing * 2), btnWidth, btnHeight,
                new ChangeStateCmd(model, State.SETTING_VOLUME));
        volumeButton.setImgButton("/Continue.png");
        volumeButton.setImgHoverButton("/ContinueHover.png");

        howToPlayButton = new Button( centerX, startY + (spacing * 3), btnWidth, btnHeight,
                new ChangeStateCmd(model, State.SETTING_HOWTOPLAY));
        howToPlayButton.setImgButton("/Continue.png");
        howToPlayButton.setImgHoverButton("/ContinueHover.png");

        buttons.add(exit);
        buttons.add(accountButton);
        buttons.add(themeButton);
        buttons.add(volumeButton);
        buttons.add(howToPlayButton);
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
        accountButton.draw(render);
        themeButton.draw(render);
        volumeButton.draw(render);
        howToPlayButton.draw(render);
        exit.draw(render);
    }

    /**
     * Kiểm tra và cập nhật trạng thái di chuột (hover) cho tất cả các nút trên màn hình cài đặt.
     * @param e Sự kiện chuột (MouseEvent) chứa tọa độ hiện tại của con trỏ.
     */
    @Override
    public void checkHover(MouseEvent e) {
        for (Button b : buttons) {
            b.setHovering(e);
        }
    }
}