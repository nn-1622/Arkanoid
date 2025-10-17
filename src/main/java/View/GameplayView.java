package View;

import java.util.ArrayList;

import Model.Brick;
import Model.GameplayModel;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;
import javafx.scene.paint.Color;

public class GameplayView {
    public Image healthbar = new Image(getClass().getResource("/healthbar.png").toExternalForm());
    public void drawGameScene(GraphicsContext gc, GameplayModel model) {
        gc.setFill(Color.BLACK);
        gc.fillRect(0, 0, gc.getCanvas().getWidth(), gc.getCanvas().getHeight());
        model.getPaddle().draw(gc);
        model.getBall().draw(gc);
        gc.drawImage(healthbar, 0, 0, 40 * model.getLives(), 40,
                23.7, 600, 40 * model.getLives(), 40);

        ArrayList<Brick> brickMap = model.getBricks();

        for (Brick brick : brickMap) {
            brick.draw(gc);
        }
    }
}