package View;

import Model.Button;
import Model.GameModel;
import Model.GameplayModel;
import Model.UltilityValues;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;

import java.util.ArrayList;

public class View implements UltilityValues, SceneActions {
    protected GameModel model;
    protected ArrayList<Button> buttons;

    public View(GameModel model) {
        this.model = model;
        buttons = new ArrayList<>();
    }

    public void draw(GraphicsContext gc, GameplayModel gameplayModel) {
    }

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

    public void handleKeyInput(KeyEvent e) {
    }

    public GameModel getModel() {
        return model;
    }

}
