package View;

import Model.GameModel;
import Model.State;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;

public class GameView {
    private Group root;
    private Scene scene;
    private MenuScene menuScene;
    private GameModel gameModel;
    private SettingScene settingScene;
    private GameplayView gameplayView;
    private LoseView loseView;
    private VictoryView victoryView;
    private GraphicsContext gc;
    private Canvas canvas;
    public GameView() {
        root = new Group();
        scene = new Scene(root, 600,650);
        canvas = new Canvas(600,650);
        root.getChildren().add(canvas);
        gc  = canvas.getGraphicsContext2D();

        menuScene = new MenuScene(canvas.getWidth(),canvas.getHeight());
        settingScene = new SettingScene(canvas.getWidth(),canvas.getHeight());
        gameplayView = new GameplayView();
        loseView = new LoseView();
        victoryView = new VictoryView();
    }
    public Scene getScene() {
        return scene;
    }
    public void render(GameModel model) {
        if(model.getGstate() == State.MENU){
            menuScene.drawMenuScene(gc);
        } else if(model.getGstate() == State.SETTING){
            settingScene.drawSettingScene(gc);
        } else if(model.getGstate() == State.PLAYING){
            gameplayView.drawGameScene(gc, model.getGameplayModel());
        } else if(model.getGstate() == State.LOSS){
            loseView.drawLoseScene(gc);
        } else if(model.getGstate() == State.VICTORY){
            victoryView.drawWinScene(gc);
        }
    }
    public MenuScene getMenuScene() {
        return menuScene;
    }
    public SettingScene getSettingScene() {
        return settingScene;
    }
    public GameplayView getGameScene() {
        return gameplayView;
    }
    public LoseView getLoseScene() {
        return loseView;
    }
    public  VictoryView getVictoryScene() {
        return victoryView;
    }
}
