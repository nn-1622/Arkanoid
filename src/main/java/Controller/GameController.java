package Controller;

import Model.GameModel;
import Model.GameplayModel;
import Model.State;
import View.GameView;
import javafx.scene.input.MouseEvent;



public class GameController implements GameEventListener {
    private GameModel model;
    private GameView view;
    private SoundManager soundManager;
    private MouseEvent e;
    private boolean leftpressed;
    private boolean rightpressed;
    private long lastUpdateTime = 0;

    public GameController(GameModel gm, GameView gv) {

        this.model = gm;
        this.view = gv;
        this.soundManager = new SoundManager();
        setInput();
    }

    @Override
    public void onLevelCompleted() {
        model.setGstate(State.FADE);
        model.setFadeStartTime(System.nanoTime());
        soundManager.playWinSound();
    }
    @Override
    public void onGameOver() {
        model.setGstate(State.LOSS);
        soundManager.playLoseSound();
    }

    @Override
    public void onBrickHit() {
        soundManager.playHitSound();
    }

    @Override
    public void onVictory() {
        soundManager.playWinSound();
        model.setGstate(State.VICTORY);
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
        } else if(model.getGstate() == State.FADE){
            double timeElapsed = (now - model.getFadeStartTime()) / 1_000_000_000.0;
            final double FADE_DURATION = 2.0;
            if(timeElapsed >= FADE_DURATION){
                model.getGameplayModel().Initialize();
                model.setGstate(State.PLAYING);
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
                    model.CreateGameplay(this);
                } else if(view.getMenuScene().settingClick(e)) {
                    model.setGstate(State.SETTING);
                } else if(view.getMenuScene().exitClick(e)) {
                    System.exit(0);
                }
            }  else if(model.getGstate() == State.SETTING) {
                if(view.getSettingScene().exitClicked(e)) {
                    model.setGstate(State.MENU);
                } else if (view.getSettingScene().lowVolumeClicked(e)) {
                    soundManager.decreaseVolume();
                    soundManager.playTestSound();
                } else if(view.getSettingScene().highVolumeClicked(e)) {
                    soundManager.increaseVolume();
                    soundManager.playTestSound();
                }
            }  else if(model.getGstate() == State.LOSS) {
                if(view.getLoseScene().checkClickMenu(e)) {
                    model.setGstate(State.MENU);
                } else if (view.getLoseScene().checkClickReplay(e)) {
                    model.setGstate(State.PLAYING);
                    model.CreateGameplay(this);
                }
            }   else if(model.getGstate() == State.VICTORY) {
                if(view.getVictoryScene().checkClickMenu(e)) {
                    model.setGstate(State.MENU);
                } else if (view.getVictoryScene().checkClickReplay(e)) {
                    model.setGstate(State.PLAYING);
                    model.CreateGameplay(this);
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
