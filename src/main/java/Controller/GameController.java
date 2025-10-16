package Controller;

import Model.GameModel;
import Model.GameplayModel;
import Model.State;
import View.GameView;
import javafx.scene.input.MouseEvent;



public class GameController {
    private GameModel model;
    private GameView view;
    private MouseEvent e;
    private boolean leftpressed;
    private boolean rightpressed;
    private long lastUpdateTime = 0;
    public GameController(GameModel gm, GameView gv) {
        this.model = gm;
        this.view = gv;
        setInput();
    }
    public void update(long now) {
        if (lastUpdateTime == 0) {
            lastUpdateTime = now;
        }

        long elapsed = now - lastUpdateTime;
        double deltaTime = elapsed / 1_000_000_000.0;
        lastUpdateTime = now;

        view.render(model);
        if (model.getGstate() == State.PLAYING) {
            this.model.getGameplayModel().update(leftpressed,rightpressed,deltaTime);
            if(model.getGameplayModel().checkWin()){
                model.getGameplayModel().Initialize();
            }
            if(model.getGameplayModel().checkLose()){
                model.setGstate(State.LOSS);
            }
            if(model.getGameplayModel().checkVictory()){
                model.setGstate(State.VICTORY);
                System.out.println("you won");
            }
        }
    }
    public void setInput() {
        view.getScene().setOnMouseMoved(e -> {
            if(model.getGstate() == State.MENU) {
                view.getMenuScene().checkHover(e);
            } else if(model.getGstate() == State.SETTING){
                view.getSettingScene().checkHover(e);
            } else if(model.getGstate() == State.LOSS){
                view.getLoseScene().checkHover(e);
            } else if(model.getGstate() == State.VICTORY){
                view.getVictoryScene().checkHover(e);
            }
        });
        view.getScene().setOnMouseClicked(e -> {
            if(model.getGstate() == State.MENU) {
                if(view.getMenuScene().startClick(e)) {
                    model.setGstate(State.PLAYING);
                    model.CreateGameplay();
                } else if(view.getMenuScene().settingClick(e)) {
                    model.setGstate(State.SETTING);
                } else if(view.getMenuScene().exitClick(e)) {
                    System.exit(0);
                }
            }  else if(model.getGstate() == State.SETTING) {
                if(view.getMenuScene().startClick(e)) {
                    model.setGstate(State.MENU);
                }
            }  else if(model.getGstate() == State.LOSS) {
                if(view.getLoseScene().checkClickMenu(e)) {
                    model.setGstate(State.MENU);
                } else if (view.getLoseScene().checkClickReplay(e)) {
                    model.setGstate(State.PLAYING);
                    model.CreateGameplay();
                }
            }   else if(model.getGstate() == State.VICTORY) {
                if(view.getVictoryScene().checkClickMenu(e)) {
                    model.setGstate(State.MENU);
                } else if (view.getVictoryScene().checkClickReplay(e)) {
                    model.setGstate(State.PLAYING);
                    model.CreateGameplay();
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
