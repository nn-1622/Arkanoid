package View;

import Controller.ScoreManager;
import Model.Button;
import Model.GameplayModel;
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
    private final Image lose = new Image("/lose.jpg");
    private final Button replay;
    private final Button menu;

    //them diem so
    private int currentScore;
    private int highScore;

    /**
     * Khởi tạo một LoseView mới.
     * Phương thức này thiết lập các nút "Chơi lại" và "Menu", bao gồm vị trí,
     * kích thước và hình ảnh cho trạng thái bình thường cũng như trạng thái khi di chuột qua.
     * @update thêm điểm hiện tại và điểm cao nhất
     */
    public LoseView(int score) {
        this.currentScore = score;
        new ScoreManager(score).saveScore();
        this.highScore = ScoreManager.getHighScore();

        replay = new Button(225.6,378.4, 148.8, 65.6);
        menu = new Button(225.6,475.4, 148.8, 65.6);

        replay.setImgButton("/Start.png"); // Sử dụng lại ảnh nút Start cho Replay
        replay.setImgHoverButton("/StartHover.png");
        menu.setImgButton("/Exit.png"); // Sử dụng lại ảnh nút Exit cho Menu
        menu.setImgHoverButton("/ExitHover.png");
    }

    /**
     * lấy điểm hiện tại
     * @return điểm hiện tại
     */
    public int getCurrentScore() {
        return currentScore;
    }

    /**
     * lấy điểm cao nhất
     * @return điểm hiện tại
     */
    public int getHighScore() {
        return highScore;
    }

    /**
     * update điểm
     * @param score
     */

    public void updateScore(int score) {
        new ScoreManager(score).saveScore();
        this.currentScore = score;
        this.highScore = ScoreManager.getHighScore();
    }

    /**
     * Vẽ toàn bộ màn hình thua cuộc lên canvas.
     * @param gc Đối tượng GraphicsContext được sử dụng để thực hiện các thao tác vẽ.
     */
    public void drawLoseScene(GraphicsContext gc) {
        gc.drawImage(lose, 0, 0,600,650);
        replay.draw(gc);
        menu.draw(gc);

        //vẽ thêm cho chữ
        gc.setFill(Color.WHITE);
        gc.setFont(new Font("Arial", 24));
        gc.fillText("Your Score: " + currentScore, 200, 320);

        gc.setFill(Color.YELLOW);
        gc.setFont(new Font("Arial", 24));
        gc.fillText("High Score: " + getHighScore(), 200, 350);

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