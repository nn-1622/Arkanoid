import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;

public class GameView {
    private Group root;
    private Scene scene;
    private MenuScene menuScene;
    private GameplayModel gameplayModel;
    private GameModel gameModel;
    private SettingScene settingScene;
    private GameScene gameScene;
    private GraphicsContext gc;
    private Canvas canvas;
    public GameView(GameModel model) {
        root = new Group();
        scene = new Scene(root, 600,650);
        canvas = new Canvas(600,650);
        root.getChildren().add(canvas);
        gc  = canvas.getGraphicsContext2D();

        this.gameModel = model;
        gameplayModel = new GameplayModel();
        menuScene = new MenuScene(canvas.getWidth(),canvas.getHeight());
        settingScene = new SettingScene(canvas.getWidth(),canvas.getHeight());
        gameScene = new GameScene(gameplayModel);
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
            gameScene.drawGameScene(gc);
        }
    }
    public MenuScene getMenuScene() {
        return menuScene;
    }
    public SettingScene getSettingScene() {
        return settingScene;
    }
    public GameScene getGameScene() {
        return gameScene;
    }
}
