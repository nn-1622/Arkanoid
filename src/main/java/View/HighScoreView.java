package View;

import Controller.ScoreManager;
import Model.Button;
import Model.GameSaveData;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;
import javafx.scene.input.MouseEvent;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;

/**
 * Lớp chịu trách nhiệm hiển thị màn hình điểm cao nhất (High Score Screen)
 * Hiển thị điểm cao nhất từ file savegame.dat và nút để quay lại
 */
public class HighScoreView {
    private Image background = new Image("/lose.jpg"); //Dùng tạm cái hình exit
    private Button exit;

    /**
     * Khởi tạo HighScoreView.
     * Thiết lập nút "exit" với vị trí, kích thước, và hình ảnh tương tự LoseView.
     */
    public HighScoreView() {
        exit = new Button(225.6, 475.4, 148.8, 65.6);
        exit.setImgButton("/Exit.png"); // Sử dụng lại ảnh Exit cho Menu
        exit.setImgHoverButton("/ExitHover.png");
    }

    /**
     * Vẽ màn hình điểm cao nhất lên canvas.
     * Hiển thị nền, điểm cao nhất, và nút exit
     * @param gc Đối tượng GraphicsContext để vẽ
     */
    public void drawHighScoreScene(GraphicsContext gc) {
        // Vẽ nền
        gc.drawImage(background, 0, 0, 600, 650);

        // Vẽ nút Menu
        exit.draw(gc);

        // Lấy điểm cao nhất từ file
        GameSaveData highScoreData = ScoreManager.loadGame();
        int highScore = (highScoreData != null) ? highScoreData.getScore() : 0;
        int level = (highScoreData != null) ? highScoreData.getLevel() : 0;
        int lives = (highScoreData != null) ? highScoreData.getLives() : 0;

        // Hiển thị tiêu đề và điểm cao nhất
        gc.setFill(Color.WHITE);
        gc.setFont(Font.font("Arial", 32));
        gc.fillText("Thành thích xuất sắc nhất", 130, 200);

        gc.setFont(Font.font("Arial", 28));
        gc.fillText("Điểm: " + highScore + "   |   level: " + level + "   |   số mạng: " + lives , 70, 300);

    }

    /**
     * Kiểm tra và cập nhật trạng thái di chuột cho nút exit
     * @param e Sự kiện chuột
     */
    public void checkHover(MouseEvent e) {
        exit.setHovering(e);
    }

    /**
     * Kiểm tra xem nút "exit" có được nhấp không.
     * @param e Mouse event
     * @return true ...
     */
    public boolean checkClickMenu(MouseEvent e) {
        return exit.isClicked(e);
    }
}