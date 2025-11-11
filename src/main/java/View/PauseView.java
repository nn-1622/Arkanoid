package View;

import Controller.ChangeStateCmd;
import Controller.CheckSaveNameCmd;
import Controller.GameCommand;
import Controller.ResumeGameCmd;
import Model.Button;
import Model.GameModel;
import Model.GameplayModel;
import Model.State;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.input.MouseEvent;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;

public class PauseView extends View {
    Button resumeButton;
    Button saveButton;
    Button exitButton;

    private String saveMessage = "";

    public PauseView(GameModel model) {
        super(model);

        double centerX = 300;
        double btnWidth = 200;
        double btnHeight = 50;

        // --- Sửa logic nút Resume ---
        final GameCommand resumeCmd = new ResumeGameCmd(model); // Lệnh gốc
        resumeButton = new Button(centerX - btnWidth/2, 250, btnWidth, btnHeight,
                new GameCommand() { // Lệnh mới (ẩn danh)
                    @Override
                    public void execute() {
                        resetSaveMessage(); // <-- Xóa thông báo
                        resumeCmd.execute(); // <-- Chạy lệnh gốc
                    }
                });
        resumeButton.setImgButton("/Replay.png");
        resumeButton.setImgHoverButton("/ReplayHover.png");


        saveButton = new Button(centerX - btnWidth/2, 320, btnWidth, btnHeight,
                new CheckSaveNameCmd(model, this)); // <-- THÊM ", this"
        saveButton.setImgButton("/Save.png");
        saveButton.setImgHoverButton("/SaveHover.png");

        final GameCommand exitCmd = new ChangeStateCmd(model, State.MENU); // Lệnh gốc
        exitButton = new Button(centerX - btnWidth/2, 390, btnWidth, btnHeight,
                new GameCommand() { // Lệnh mới (ẩn danh)
                    @Override
                    public void execute() {
                        resetSaveMessage(); // <-- Xóa thông báo
                        exitCmd.execute(); // <-- Chạy lệnh gốc
                    }
                });
        exitButton.setImgButton("/Exit.png");
        exitButton.setImgHoverButton("/ExitHover.png");

        buttons.add(resumeButton);
        buttons.add(saveButton);
        buttons.add(exitButton);
    }

    /**
     * Được gọi bởi các Lệnh (Commands) để báo cho View này hiển thị xác nhận
     */
    public void showSaveConfirmation() {
        this.saveMessage = "Đã lưu!";
    }
    /**
     * Xóa thông báo "Đã lưu" để lần tạm dừng sau không hiển thị nữa
     */
    public void resetSaveMessage() {
        this.saveMessage = "";
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

        if (!saveMessage.isEmpty()) {
            gc.setFill(Color.GREEN);
            gc.setFont(Font.font("Consolas", FontWeight.BOLD, 20));
            gc.fillText(saveMessage, 265, 380); // (Vị trí Y ở ngay dưới nút Save)
        }
    }
}