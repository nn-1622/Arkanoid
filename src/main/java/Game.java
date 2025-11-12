import Controller.GameController;
import Controller.GameExecutor;
import Controller.SoundManager;
import Model.GameModel;
import View.GameView;
import javafx.application.Application;
import javafx.stage.Stage;
import javafx.animation.AnimationTimer;

public class Game extends Application {
    private GameController controller;

    @Override
    public void start(Stage stage) {
        GameModel model = GameModel.getGameModel();
        SoundManager soundManager = SoundManager.getInstance();
        GameView view = new GameView(model);
        controller = new GameController(model, view, soundManager);

        stage.setScene(view.getScene());
        stage.setTitle("Arkanoid!");
        stage.setResizable(false);
        stage.show();

        new AnimationTimer() {
            @Override
            public void handle(long now) {
                controller.update(now);
            }
        }.start();
    }

    @Override
    public void stop() throws Exception {
        GameExecutor.getInstance().shutdown();
        super.stop();
    }

    public static void main(String[] args) {
        launch();
    }
}