package View;

import Model.Button;
import Model.GameModel;
import Model.GameplayModel;
import Model.UltilityValues;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.input.MouseEvent;
import javafx.scene.input.MouseEvent;
import javafx.scene.input.KeyEvent;

import java.util.ArrayList;

public class View implements UltilityValues, SceneActions {
    protected GameModel model;
    protected ArrayList<Button> buttons;

    public View(GameModel model) {
        this.model = model;
        buttons = new ArrayList<>();
    };

    public void draw(GraphicsContext gc, GameplayModel gameplayModel) {};

    public void checkHover(MouseEvent e) {};

    @Override
    public void handleClick(MouseEvent e) {
        for (Button button : buttons) {
            if(button.isClicked(e)) {
                System.out.println("clicked");
                button.activate();
                return;
            }
        }
    }

    /**
     * Xử lý đầu vào bàn phím.
     * Các View con (như AccountView) sẽ override
     * phương thức này để xử lý việc gõ chữ.
     * @param e Sự kiện phím
     */
    public void handleKeyInput(KeyEvent e) {
    }

    public GameModel getModel() { return model; }
}
