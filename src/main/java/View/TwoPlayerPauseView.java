package View;

import Controller.ChangeStateCmd;
import Controller.GameCommand;
import Model.Button;
import Model.GameModel;
import Model.GameplayModel;
import Model.State;
import Model.UltilityValues;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.input.MouseEvent;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.text.TextAlignment;

/**
 * Màn hình Pause RÚT GỌN chỉ dành cho 2 người chơi.
 * Chỉ có nút Resume và Exit.
 */
public class TwoPlayerPauseView extends View {
    private final Button resumeButton;
    private final Button exitButton;

    public TwoPlayerPauseView(GameModel model) {
        super(model);

        // ✅ Dùng kích thước canvas từ UltilityValues
        double centerX = UltilityValues.canvasWidth;
        double btnWidth = 200;
        double btnHeight = 50;

        // Nút Resume - Trở về trạng thái TWO_PLAYING
        resumeButton = new Button(centerX - btnWidth / 2, 250, btnWidth, btnHeight,
                new GameCommand() {
                    @Override
                    public void execute() {
                        // Chỉ chuyển trạng thái, không tạo game mới
                        model.setGstate(State.TWO_PLAYING);
                    }
                });
        resumeButton.setImgButton("/Continue.png");
        resumeButton.setImgHoverButton("/ContinueHover.png");

        // Nút Exit - Trở về MENU
        exitButton = new Button(centerX - btnWidth / 2, 390, btnWidth, btnHeight,
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
     * @param isTwoPlayer Chế độ 2 người chơi (vẽ gấp đôi chiều rộng)
     */
    public void drawOverlay(GraphicsContext gc, boolean isTwoPlayer) {
        double canvasWidth = UltilityValues.canvasWidth * (isTwoPlayer ? 2 : 1);
        double canvasHeight = UltilityValues.canvasHeight;
        double centerX = canvasWidth / 2;

        gc.save();

        // 🌌 Nền mờ trong suốt (giống WAITING FOR PLAYER)
        gc.setGlobalAlpha(0.6);
        gc.setFill(Color.BLACK);
        gc.fillRect(0, 0, canvasWidth, canvasHeight);

        // 🔄 Khôi phục alpha = 1 để vẽ nút và chữ rõ ràng
        gc.setGlobalAlpha(1.0);

        // --- Tiêu đề ---
        gc.setFill(Color.WHITE);
        gc.setFont(Font.font("Consolas", FontWeight.BOLD, 50));
        gc.fillText("PAUSED", centerX, 180);

        // --- Nút ---
        resumeButton.setX(centerX - resumeButton.getWidth() / 2);
        exitButton.setX(centerX - exitButton.getWidth() / 2);

        resumeButton.draw(gc);
        exitButton.draw(gc);

        gc.restore();
    }



    @Override
    public void checkHover(MouseEvent e) {
        resumeButton.setHovering(e);
        exitButton.setHovering(e);
    }
}
