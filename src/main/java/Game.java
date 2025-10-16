import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;
import javafx.animation.AnimationTimer;

public class Game extends Application {
    private GameController controller;
    private GameModel model;
    private GameView view;
    public static State gstate;

    @Override
    public void start(Stage stage) {
        model = new GameModel();
        view = new GameView(model);
        controller = new GameController(model, view);
        stage.setScene(view.getScene());
        stage.setTitle("Arkanoid!");
        stage.show();
        new AnimationTimer() {
            @Override
            public void handle(long now) {
                controller.update();
            }
        } .start();
    }
    public static void main(String[] args) {
        launch();
    }
}
