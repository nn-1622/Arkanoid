import javafx.application.Platform;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;
import javafx.scene.canvas.GraphicsContext;

import java.util.Objects;

public class GameManager {
    private Group root;
    private Scene scene;
    private MenuScene menuScene = new MenuScene();
    private SettingScene settingScene = new SettingScene();
    private GraphicsContext gc;
    private static State gstate;
    private Canvas canvas;
    private MouseEvent e;
    public GameManager() {
        root = new Group();
        gstate = State.MENU;
        canvas = new Canvas(600, 650);
        gc = canvas.getGraphicsContext2D();
        root.getChildren().add(canvas);
        scene = new Scene(root, 600, 650);
        setInput(canvas);
    }
    public void update(){
        render(gc);
    }
    public void render(GraphicsContext gc){
        if (Objects.requireNonNull(gstate) == State.MENU) {
            menuScene.drawMenuScene(gc);
        } else if(Objects.requireNonNull(gstate) == State.SETTING) {
            settingScene.drawSettingScene(gc);
        }
    }
    public Scene getScene() {
        return scene;
    }
    public void setInput(Canvas canvas) {
        scene.setOnMouseMoved(e -> {
            if(gstate==State.MENU) {
                menuScene.checkHover(e);
            }  else if(gstate==State.SETTING) {
                settingScene.checkHover(e);
            }
        });
        scene.setOnMouseClicked(e -> {
            if(gstate==State.MENU) {
                if(menuScene.settingClick(e)) {
                    gstate=State.SETTING;
                } else if(menuScene.exitClick(e)) {
                    System.exit(0);
                }
            } else if(gstate==State.SETTING) {
                if(settingScene.exitClicked(e)) {
                    gstate=State.MENU;
                }
            }
            
        });
    }
}
