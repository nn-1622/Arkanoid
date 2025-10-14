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
    private MenuScene menuScene;
    private SettingScene settingScene;
    private GameScene gameScene;
    private GraphicsContext gc;
    private static State gstate;
    private Canvas canvas;
    private MouseEvent e;
    private boolean leftpressed;
    private boolean rightpressed;

    public GameManager() {
        root = new Group();
        gstate = State.MENU;
        canvas = new Canvas(600, 650);
        gc = canvas.getGraphicsContext2D();
        root.getChildren().add(canvas);
        scene = new Scene(root, 600, 650);
        setInput(canvas);

        gameScene = new GameScene(canvas.getWidth(), canvas.getHeight());
        settingScene = new SettingScene(canvas.getWidth(), canvas.getHeight());
        menuScene = new MenuScene(canvas.getWidth(), canvas.getHeight());
    }

    public void update(){
        render(gc);
        if(gstate==State.PLAYING){
            gameScene.update(leftpressed, rightpressed);
        }
    }

    public void render(GraphicsContext gc){
        if (Objects.requireNonNull(gstate) == State.MENU) {
            menuScene.drawMenuScene(gc);
        } else if(Objects.requireNonNull(gstate) == State.SETTING) {
            settingScene.drawSettingScene(gc);
        } else if(Objects.requireNonNull(gstate)== State.PLAYING) {
            gameScene.drawGameScene(gc);
        } else if(Objects.requireNonNull(gstate == State.PAUSED)) {
            //will add later
        } else if(Objects.requireNonNull(gstate == State.LOSS)) {
            //will add later
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
                } else if(menuScene.startClick(e)) {
                    gstate=State.PLAYING;
                }
            } else if(gstate==State.SETTING) {
                if(settingScene.exitClicked(e)) {
                    gstate=State.MENU;
                }
            }
        });

        scene.setOnKeyPressed(e -> {
            switch (e.getCode()) {
                case A -> leftpressed = true;
                case D -> rightpressed = true;
                case SPACE -> gameScene.launchBall();
            }
        });

        scene.setOnKeyReleased(e -> {
            switch (e.getCode()) {
                case A -> leftpressed = false;
                case D -> rightpressed = false;
            }
        });
    }
}
