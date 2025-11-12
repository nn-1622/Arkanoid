package View;

import Model.Button;
import Model.GameModel;
import Model.GameplayModel;
import Model.UltilityValues;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;

import java.util.ArrayList;

/**
 * Lớp gốc hiển thị hình ảnh.
 */
public class View implements UltilityValues, SceneActions {
    protected GameModel model;// model gốc của game
    protected ArrayList<Button> buttons;// danh sách nút bấm

    /**
     * Hàm khởi tạo, các class View về sau đều lấy từ View.
     *
     * @param model gốc của các view
     */
    public View(GameModel model) {
        this.model = model;
        buttons = new ArrayList<>();
    }

    /**
     * Hàm vẽ các đối tượng trong View.
     *
     * @param gc biến giúp truyền ngữ cảnh
     * @param gameplayModel model của gamePlay
     */
    public void draw(GraphicsContext gc, GameplayModel gameplayModel) {
    }

    /**
     * Hàm kiểm tra chuột khi trong vùng chọn.
     *
     * @param e chuột
     */
    @Override
    public void checkHover(MouseEvent e) {
    }

    @Override
    public void handleClick(MouseEvent e) {
        for (Button button : buttons) {
            if (button.isClicked(e)) {
                button.activate();
                Controller.SoundManager.getInstance().onGameEvent(Controller.GameEvent.CLICK);
                return;
            }
        }
    }

    /**
     * Kiểm tra bấm phím.
     *
     * @param e phím
     */
    public void handleKeyInput(KeyEvent e) {
    }

    public GameModel getModel() {
        return model;
    }

}
