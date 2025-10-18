package View;

import Model.Button;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;
import javafx.scene.input.MouseEvent;

/**
 * Lớp chịu trách nhiệm hiển thị màn hình chiến thắng khi người chơi hoàn thành trò chơi.
 * Lớp này quản lý việc vẽ ảnh nền chiến thắng và các nút tương tác
 * như "Chơi lại" (Replay) và "Menu".
 */
public class VictoryView {
    private Image win = new Image("/win.jpg");
    private Button replay;
    private Button menu;

    /**
     * Khởi tạo một VictoryView mới.
     * Phương thức này thiết lập các nút "Chơi lại" và "Menu", bao gồm vị trí,
     * kích thước và hình ảnh cho trạng thái bình thường cũng như trạng thái khi di chuột qua.
     */
    public VictoryView() {
        replay = new Button(225.6,378.4, 148.8, 65.6);
        menu = new Button(225.6,475.4, 148.8, 65.6);

        replay.setImgButton("/Start.png"); // Sử dụng lại ảnh nút Start cho Replay
        replay.setImgHoverButton("/StartHover.png");
        menu.setImgButton("/Exit.png"); // Sử dụng lại ảnh nút Exit cho Menu
        menu.setImgHoverButton("/ExitHover.png");
    }

    /**
     * Vẽ toàn bộ màn hình chiến thắng lên canvas.
     * @param gc Đối tượng GraphicsContext được sử dụng để thực hiện các thao tác vẽ.
     */
    public void drawWinScene(GraphicsContext gc) {
        gc.drawImage(win, 0, 0,600,650);
        replay.draw(gc);
        menu.draw(gc);
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