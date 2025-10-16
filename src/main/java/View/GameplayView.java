package View;

import java.util.ArrayList;

import Model.Brick;
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

        if (!model.getRendered()) {
            model.renderMap(model.getBricks());
            model.setRendered(true);
            System.out.println("Rendered");
        }

        ArrayList<Brick> brickMap = model.getBricks();

        for (Brick brick : brickMap) {
            brick.draw(gc);
        }
    }
}