import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;
import javafx.scene.paint.Color;

public class GameScene {
    private GameplayModel model;
    private MenuScene menuScene;
    private SettingScene settingScene;
    public GameScene(GameplayModel model) {
        this.model = model;
    }

    public void drawGameScene(GraphicsContext gc) {
        gc.setFill(Color.BLACK);
        gc.fillRect(0, 0, gc.getCanvas().getWidth(), gc.getCanvas().getHeight());
        model.getPaddle().draw(gc);
        model.getBall().draw(gc);
    }
}