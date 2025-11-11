package View;

import Controller.ChangeStateCmd;
import Controller.GameCommand;
import Model.Button;
import Model.GameModel;
import Model.GameplayModel;
import Model.State;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.input.MouseEvent;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;

/**
 * Màn hình Pause RÚT GỌN chỉ dành cho 2 người chơi.
 * Chỉ có nút Resume và Exit.
 */
public class TwoPlayerPauseView extends View {
    Button resumeButton;
    Button exitButton;

    public TwoPlayerPauseView(GameModel model) {
        super(model);

        double centerX = 600; // Căn giữa cho 1200px
        double btnWidth = 200;
        double btnHeight = 50;

        // Nút Resume - Trở về trạng thái TWO_PLAYING
        resumeButton = new Button(centerX - btnWidth/2, 250, btnWidth, btnHeight,
                new GameCommand() { // <-- Sửa thành lệnh mới
                    @Override
                    public void execute() {
                        // Chỉ chuyển trạng thái, không tạo game mới
                        model.setGstate(State.TWO_PLAYING);
                    }
                });
        resumeButton.setImgButton("/Continue.png");
        resumeButton.setImgHoverButton("/ContinueHover.png");
        resumeButton.setImgButton("/Continue.png");
        resumeButton.setImgHoverButton("/ContinueHover.png");

        // Nút Exit - Trở về MENU
        exitButton = new Button(centerX - btnWidth/2, 390, btnWidth, btnHeight,
                new ChangeStateCmd(model, State.MENU));
        exitButton.setImgButton("/Exit.png");
        exitButton.setImgHoverButton("/ExitHover.png");

        buttons.add(resumeButton);
        buttons.add(exitButton);
    }

    @Override
    public void draw(GraphicsContext gc, GameplayModel gameplayModel) {
        // Gọi hàm drawOverlay với mặc định là 2P (1200px)
        drawOverlay(gc, true);
    }

    /**
     * Hàm vẽ chính, có khả năng vẽ cho 1P hoặc 2P.
     * @param gc
     * @param isTwoPlayer Chế độ 2 người chơi (vẽ 1200px)
     */
    public void drawOverlay(GraphicsContext gc, boolean isTwoPlayer) {
        double canvasWidth = isTwoPlayer ? 1200 : 600;
        double centerX = canvasWidth / 2;

        gc.setFill(Color.rgb(0, 0, 0, 0.7));
        gc.fillRect(0, 0, canvasWidth, 650);

        gc.setFill(Color.WHITE);
        gc.setFont(Font.font("Consolas", FontWeight.BOLD, 50));

        // Đặt lại tọa độ X của các nút để căn giữa
        resumeButton.setX(centerX - resumeButton.getWidth()/2);
        exitButton.setX(centerX - exitButton.getWidth()/2);

        resumeButton.draw(gc);
        exitButton.draw(gc);
    }

    @Override
    public void checkHover(MouseEvent e){
        resumeButton.setHovering(e);
        exitButton.setHovering(e);
    }
}