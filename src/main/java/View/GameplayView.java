package View;

import Model.GameplayModel;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;

public class GameplayView {
    public GameplayView() {
    }

    public void drawGameScene(GraphicsContext gc, GameplayModel model) {
        gc.setFill(Color.BLACK);
        gc.fillRect(0, 0, gc.getCanvas().getWidth(), gc.getCanvas().getHeight());
        model.getPaddle().draw(gc);
        model.getBall().draw(gc);
    }
}