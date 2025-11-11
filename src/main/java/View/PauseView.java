package View;

import Controller.ChangeStateCmd;
import Controller.CheckSaveNameCmd;
import Controller.ResumeGameCmd;
import Model.Button;
import Model.GameModel;
import Model.GameplayModel;
import Model.State;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.input.MouseEvent; // <-- Thêm import này
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;

public class PauseView extends View {
    Button resumeButton;
    Button saveButton;
    Button exitButton;

    // === THÊM BIẾN NÀY ===
    private String saveMessage = "";

    public PauseView(GameModel model) {
        super(model);

        double centerX = 300;
        double btnWidth = 200;
        double btnHeight = 50;

        resumeButton = new Button(centerX - btnWidth/2, 250, btnWidth, btnHeight,
                new ResumeGameCmd(model));
        resumeButton.setImgButton("/Replay.png");
        resumeButton.setImgHoverButton("/ReplayHover.png");

        // === CẬP NHẬT DÒNG NÀY ===
        // Truyền "this" (chính là PauseView) vào
        saveButton = new Button(centerX - btnWidth/2, 320, btnWidth, btnHeight,
                new CheckSaveNameCmd(model, this)); // <-- THÊM ", this"
        saveButton.setImgButton("/Save.png");
        saveButton.setImgHoverButton("/SaveHover.png");

        exitButton = new Button(centerX - btnWidth/2, 390, btnWidth, btnHeight,
                new ChangeStateCmd(model, State.MENU));
        exitButton.setImgButton("/Exit.png");
        exitButton.setImgHoverButton("/ExitHover.png");

        buttons.add(resumeButton);
        buttons.add(saveButton);
        buttons.add(exitButton);
    }

    // === THÊM HÀM MỚI NÀY ===
    /**
     * Được gọi bởi các Lệnh (Commands) để báo cho View này hiển thị xác nhận.
     */
    public void showSaveConfirmation() {
        this.saveMessage = "Đã lưu!";
    }

    @Override
    public void handleClick(MouseEvent e) {
        if (!saveMessage.isEmpty()) {
            saveMessage = "";
        }

        super.handleClick(e);
    }

    @Override
    public void draw(GraphicsContext gc, GameplayModel gameplayModel) {
        gc.setFill(Color.rgb(0, 0, 0, 0.7));
        gc.fillRect(0, 0, 600, 650);

        gc.setFill(Color.WHITE);
        gc.setFont(Font.font("Consolas", FontWeight.BOLD, 50));
        gc.fillText("PAUSED", 210, 180);

        resumeButton.draw(gc);
        saveButton.draw(gc);
        exitButton.draw(gc);

        // === THÊM LOGIC VẼ THÔNG BÁO NÀY ===
        if (!saveMessage.isEmpty()) {
            gc.setFill(Color.GREEN); // Màu xanh lá
            gc.setFont(Font.font("Consolas", FontWeight.BOLD, 20));
            // Căn giữa vị trí của nút Save
            gc.fillText(saveMessage, 265, 380); // (Vị trí Y ở ngay dưới nút Save)
        }
    }
}