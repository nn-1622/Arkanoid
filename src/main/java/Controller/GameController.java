package Controller;

import Model.GameModel;
import Model.State;
import View.GameView;
import javafx.scene.input.MouseEvent;



public class GameController {
    private boolean leftpressed;
    private boolean rightpressed;
    private GameModel model;
    private GameView view;
    private MouseEvent e;
    public GameController(GameModel gm, GameView gv) {
        this.model = gm;
        this.view = gv;
        setInput();
    }
    public void update() {
        view.render(model);
        if (model.getGstate() == State.PLAYING) {
            this.model.getGameplayModel().getPaddle().move(leftpressed,rightpressed);
            this.model.getGameplayModel().update();
        }
    }
    public void setInput() {
        view.getScene().setOnMouseMoved(e -> {
            if(model.getGstate() == State.MENU) {
                view.getMenuScene().checkHover(e);
            } else if(model.getGstate() == State.SETTING){
                view.getSettingScene().checkHover(e);
            }
        });
        view.getScene().setOnMouseClicked(e -> {
            if(model.getGstate() == State.MENU) {
                if(view.getMenuScene().startClick(e)) {
                    model.setGstate(State.PLAYING);
                } else if(view.getMenuScene().settingClick(e)) {
                    model.setGstate(State.SETTING);
                } else if(view.getMenuScene().exitClick(e)) {
                    System.exit(0);
                }
            }  else if(model.getGstate() == State.SETTING) {
                if(view.getMenuScene().startClick(e)) {
                    model.setGstate(State.MENU);
                }
            }
        });
        view.getScene().setOnKeyPressed(e -> {
            switch (e.getCode()) {
                case A -> leftpressed = true;
                case D -> rightpressed = true;
                case SPACE -> model.getGameplayModel().launchBall();
            }
        });

        view.getScene().setOnKeyReleased(e -> {
            switch (e.getCode()) {
                case A -> leftpressed = false;
                case D -> rightpressed = false;
            }
        });
    }
}
