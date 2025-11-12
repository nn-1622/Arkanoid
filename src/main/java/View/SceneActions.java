package View;

import javafx.scene.input.MouseEvent;

public interface SceneActions {
    /**
     * Hàm kiểm tra khi di chuột vào vùng chọn.
     * @param e chuột
     */
    public void checkHover(MouseEvent e);

    /**
     * Hàm kiểm tra click chuột.
     * @param e chuột
     */
    public void handleClick(MouseEvent e);
}
