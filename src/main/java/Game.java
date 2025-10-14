import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;
import javafx.animation.AnimationTimer;

public class Game extends Application {
    private GameManager gm;
    private GraphicsContext gc;

    @Override
    public void start(Stage stage) {
        gm = new GameManager();
        stage.setScene(gm.getScene());
        stage.setTitle("Arkanoid");
        stage.show();
        new AnimationTimer() {
            @Override
            public void handle(long now) {
                gm.update();
            }
        } .start();
    }
    public static void main(String[] args) {
        launch();
    }
}
