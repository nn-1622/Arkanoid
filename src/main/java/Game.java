import Controller.GameController;
import Model.GameModel;
import Model.State;
import View.GameView;
import javafx.application.Application;
import javafx.stage.Stage;
import javafx.animation.AnimationTimer;

public class Game extends Application {
    private GameController controller;
    private GameModel model;
    private GameView view;
    public static State gstate;

    @Override
    public void start(Stage stage) {
        model = new GameModel(650,600);
        view = new GameView();
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
