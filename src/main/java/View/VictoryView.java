package View;

import Controller.ChangeStateCmd;
import Model.Button;
import Model.GameModel;
import Model.GameplayModel;
import Model.State;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;
import javafx.scene.input.MouseEvent;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.text.TextAlignment;

/**
 * Lớp chịu trách nhiệm hiển thị màn hình chiến thắng khi người chơi hoàn thành trò chơi.
 * Lớp này quản lý việc vẽ ảnh nền chiến thắng và các nút tương tác
 * như "Chơi lại" (Replay) và "Menu".
 */
public class VictoryView extends View {
    private Image win = new Image("/win.png");
    private Button replay;
    private Button menu;

    /**
     * Khởi tạo một VictoryView mới.
     * Phương thức này thiết lập các nút "Chơi lại" và "Menu", bao gồm vị trí,
     * kích thước và hình ảnh cho trạng thái bình thường cũng như trạng thái khi di chuột qua.
     */
    public VictoryView(GameModel model) {
        super(model);


        replay = new Button(54.7, 559.3,245.3, 61.3, new ChangeStateCmd(model, State.PLAYING));
        menu = new Button(349.7,553.4, 190.3, 73.2, new ChangeStateCmd(model, State.MENU));

        replay.setImgButton("/Replay.png"); // Sử dụng lại ảnh nút Start cho Replay
        replay.setImgHoverButton("/ReplayHover.png");
        menu.setImgButton("/Exit.png"); // Sử dụng lại ảnh nút Exit cho Menu
        menu.setImgHoverButton("/ExitHover.png");

        buttons.add(replay);
        buttons.add(menu);
    }

    /**
     * Vẽ toàn bộ màn hình chiến thắng lên canvas.
     *
     * @param gc            Đối tượng GraphicsContext được sử dụng để thực hiện các thao tác vẽ.
     * @param gameplayModel
     */
    @Override
    public void draw(GraphicsContext gc, GameplayModel gameplayModel) {
        gc.drawImage(win, 0, 0,600,650);
        replay.draw(gc);
        menu.draw(gc);
        if (gameplayModel != null) {
            int score = gameplayModel.getScore();
            gc.setFill(Color.web("#D19C00"));
            gc.setFont(Font.font("Consolas", FontWeight.BOLD, 100));
            double centerX = 300;
            double posY = 380;
            gc.fillText("" + score, 350, 370);
        }
    }

    /**
     * Kiểm tra và cập nhật trạng thái di chuột (hover) cho các nút trên màn hình.
     * @param e Sự kiện chuột (MouseEvent) chứa tọa độ hiện tại của con trỏ.
     */
    @Override
    public void checkHover(MouseEvent e){
        replay.setHovering(e);
        menu.setHovering(e);
    }
}