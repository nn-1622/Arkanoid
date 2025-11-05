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
 * Lớp chịu trách nhiệm hiển thị màn hình thua cuộc (Game Over).
 * Lớp này quản lý việc vẽ ảnh nền thua cuộc và các nút tương tác
 * như "Chơi lại" (Replay) và "Menu".
 */
public class LoseView {
    private Image lose = new Image("/lose.jpg");
    private Button replay;
    private Button menu;

    // update thêm hiển thị highscore
    private int currentScore;
    private boolean isNewHighScore = false;

    /**
     * Khởi tạo một LoseView mới.
     * Phương thức này thiết lập các nút "Chơi lại" và "Menu", bao gồm vị trí,
     * kích thước và hình ảnh cho trạng thái bình thường cũng như trạng thái khi di chuột qua.
     */
    public LoseView() {
        replay = new Button(225.6,378.4, 148.8, 65.6);
        menu = new Button(225.6,475.4, 148.8, 65.6);

        replay.setImgButton("/Start.png"); // Sử dụng lại ảnh nút Start cho Replay
        replay.setImgHoverButton("/StartHover.png");
        menu.setImgButton("/Exit.png"); // Sử dụng lại ảnh nút Exit cho Menu
        menu.setImgHoverButton("/ExitHover.png");
    }

    /**
     * Thiết lập điểm của người chơi và kiểm tra xem có phải điểm cao mới hay không.
     * @param score Điểm đạt được sau khi thua cuộc.
     */
    public void setScore(int score) {
        this.currentScore = score;
        GameSaveData saved = ScoreManager.loadGame();

        System.out.println("Current Score: " + score); // Debug điểm hiện tại
        System.out.println("Saved Data: " + saved); // Debug dữ liệu từ file

        if (saved == null || score > saved.getScore()) {
            // Cập nhật điểm cao mới
            ScoreManager.saveGame(new GameSaveData(score, 1, 5));
            isNewHighScore = true;
        } else {
            isNewHighScore = false;
        }
    }

    /**
     * Vẽ toàn bộ màn hình thua cuộc lên canvas.
     * @param gc Đối tượng GraphicsContext được sử dụng để thực hiện các thao tác vẽ.
     */
    public void drawLoseScene(GraphicsContext gc) {
        gc.drawImage(lose, 0, 0,600,650);
        replay.draw(gc);
        menu.draw(gc);

        gc.setFill(Color.WHITE);
        gc.setFont(Font.font("Arial", 28));

        // Hiển thị điểm người chơi
        gc.fillText("Điểm của bạn: " + currentScore, 200, 300);

        GameSaveData highScoreData = ScoreManager.loadGame();
        int highScore = (highScoreData != null) ? highScoreData.getScore() : 0;

        // Thêm dòng hiển thị điểm cao nhất
        gc.fillText("Điểm cao nhất: " + highScore, 200, 330);

        //Thêm dòng này vào để debug lỗi
        System.out.println("Drawing - Current Score: " + currentScore + ", High Score: " + highScore + ", isNewHighScore: " + isNewHighScore);

        // Nếu đạt điểm cao mới
        if (isNewHighScore) {
            gc.setFill(Color.YELLOW);
            gc.setFont(Font.font("Arial", 30));
            gc.fillText("Chúc mừng kỷ lục mới!", 130, 340);
        }
    }

    /**
     * Kiểm tra và cập nhật trạng thái di chuột (hover) cho các nút trên màn hình.
     * @param e Sự kiện chuột (MouseEvent) chứa tọa độ hiện tại của con trỏ.
     */
    public void checkHover(MouseEvent e){
        replay.setHovering(e);
        menu.setHovering(e);
    }

    /**
     * Kiểm tra xem sự kiện nhấp chuột có xảy ra trên nút "Chơi lại" hay không.
     * @param e Sự kiện chuột (MouseEvent) để kiểm tra.
     * @return true nếu nút "Chơi lại" được nhấp, ngược lại là false.
     */
    public boolean checkClickReplay(MouseEvent e){
        return replay.isClicked(e);
    }

    /**
     * Kiểm tra xem sự kiện nhấp chuột có xảy ra trên nút "Menu" hay không.
     * @param e Sự kiện chuột (MouseEvent) để kiểm tra.
     * @return true nếu nút "Menu" được nhấp, ngược lại là false.
     */
    public boolean checkClickMenu(MouseEvent e){
        return menu.isClicked(e);
    }
}