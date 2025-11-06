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
    private Button highScore;

    // update thêm hiển thị highscore
    private int currentScore;
    private boolean isNewHighScore;

    /**
     * Khởi tạo một LoseView mới.
     * Phương thức này thiết lập các nút "Chơi lại" và "Menu", bao gồm vị trí,
     * kích thước và hình ảnh cho trạng thái bình thường cũng như trạng thái khi di chuột qua.
     */
    public LoseView() {
        replay = new Button(225.6,378.4, 148.8, 65.6);
        menu = new Button(225.6,475.4, 148.8, 65.6);
        highScore = new Button(225.6,281.4, 148.8, 65.6);


        replay.setImgButton("/Start.png"); // Sử dụng lại ảnh nút Start cho Replay
        replay.setImgHoverButton("/StartHover.png");
        menu.setImgButton("/Exit.png"); // Sử dụng lại ảnh nút Exit cho Menu
        menu.setImgHoverButton("/ExitHover.png");
        highScore.setImgButton("/Exit.png"); //test nút highscore bằng hình ảnh nút Exit
        highScore.setImgHoverButton("/ExitHover.png");
    }

    /**
     * Thiết lập điểm của người chơi và kiểm tra xem có phải điểm cao mới hay không.
     * @param score Điểm đạt được sau khi thua cuộc.
     */
    public void setScore(int score) {
        this.currentScore = score;
        GameSaveData saved = ScoreManager.loadGame();
    }

    /**
     * Vẽ toàn bộ màn hình thua cuộc lên canvas.
     * @param gc Đối tượng GraphicsContext được sử dụng để thực hiện các thao tác vẽ.
     */
    public void drawLoseScene(GraphicsContext gc) {
        gc.drawImage(lose, 0, 0,600,650);
        replay.draw(gc);
        menu.draw(gc);
        highScore.draw(gc);

        gc.setFill(Color.WHITE);
        gc.setFont(Font.font("Arial", 15.9));



        GameSaveData highScoreData = ScoreManager.loadGame();
        isNewHighScore = ScoreManager.getIsNewHighScore();
        int highScore = (highScoreData != null) ? highScoreData.getScore() : 0;

        // Hiển thị điểm người chơi
        gc.fillText("Điểm của bạn: " + currentScore, 198.7, 149.8);
        // Thêm dòng hiển thị điểm cao nhất
        gc.fillText("Điểm cao nhất: " + highScore, 196.8, 189.8);

        // Nếu đạt điểm cao mới
        if (isNewHighScore) {
            gc.setFill(Color.YELLOW);
            gc.setFont(Font.font("Arial", 18.9));
            gc.fillText("Chúc mừng kỷ lục mới!", 155.4, 229.5);
        }
    }

    /**
     * Kiểm tra và cập nhật trạng thái di chuột (hover) cho các nút trên màn hình.
     * @param e Sự kiện chuột (MouseEvent) chứa tọa độ hiện tại của con trỏ.
     */
    public void checkHover(MouseEvent e){
        replay.setHovering(e);
        menu.setHovering(e);
        highScore.setHovering(e);
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

    /**
     *Kiểm tra xem sự kiện nhấp chuột có xảy ra trên nút "highScore" hay ko.
     * @param e Sự kiện chuột
     * @return true ....
     */
    public boolean checkClickHighScore(MouseEvent e){
        return highScore.isClicked(e);
    }
}