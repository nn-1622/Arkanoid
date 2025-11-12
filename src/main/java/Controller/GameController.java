package Controller;

import Model.GameModel;
import Model.GameplayModel;
import Model.State;
import View.GameView;
import View.GameplayView;
import View.LoseView;
import View.MenuScene;
import View.TwoPlayerView;
import View.VictoryView;
import javafx.scene.input.KeyCode;
import javafx.application.Platform;

import java.util.concurrent.Future;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutorService;

public class GameController implements GameEventListener {
    private static final double FADE_DURATION = 1.1;
    private final GameModel model;
    private final GameView view;

    private long lastUpdateTime = 0;
    private State lastState;

    private final SoundManager soundManager;

    private boolean leftPressed, rightPressed;
    private boolean left2Pressed, right2Pressed;

    private final ExecutorService logicExecutor;
    private Future<Void> gameLogicUpdateTask;

    public GameController(GameModel gm, GameView gv, SoundManager soundManager) {
        this.model = gm;
        this.view = gv;
        this.logicExecutor = GameExecutor.getInstance().getLogicExecutor();
        setInput();
        this.soundManager = soundManager;

        gm.getEventLoader().register(this);
        gm.getEventLoader().register(soundManager);

        this.lastState = model.getGstate();
        handleStateChange(lastState);
    }

    private void handleStateChange(State newState) {
        if (newState == null) return;

        if (newState == State.PLAYING || newState == State.TWO_PLAYING) {
            soundManager.playGameplayBgm();
            return;
        }

        if (newState == State.PAUSED || newState == State.TWO_PLAYER_PAUSED) {
            soundManager.pauseGameplayBgm();
            return;
        }

        if (newState == State.MENU
                || newState == State.SETTING
                || newState == State.VICTORY
                || newState == State.LOSS) {
            soundManager.playMenuBgm();
        }
    }

    @Override
    public void onGameEvent(GameEvent event) {
        switch (event) {
            case GAME_WIN -> {
                model.recordFinalScore();
                model.setGstate(State.VICTORY);
            }
            case GAME_LOST -> {
                model.recordFinalScore();
                model.setGstate(State.LOSS);
            }
            case LEVEL_COMPLETE -> {
                model.setFadeStartTime(System.nanoTime());
                model.setGstate(State.FADE);
            }
            case AUTO_SAVE_TRIGGER -> model.autoSave();
        }
    }

    public void update(long now) {
        if (lastUpdateTime == 0) lastUpdateTime = now;
        double deltaTime = (now - lastUpdateTime) / 1_000_000_000.0;
        lastUpdateTime = now;

        State current = model.getGstate();
        if (current != lastState) {
            handleStateChange(current);
            lastState = current;
        }

        if (model.getGstate() == State.PAUSED) {
            view.render(model);
            return;
        }
        Platform.runLater(() -> view.render(model));

        State currentState = model.getGstate();

        if (currentState == State.PAUSED || currentState == State.READY_TO_PLAY) {
            return;
        }

        if (gameLogicUpdateTask == null || gameLogicUpdateTask.isDone()) {
            Callable<Void> logicTask = () -> {
                synchronized (this) {
                    switch (currentState) {
                        case PLAYING:
                            handleSinglePlayer(deltaTime);
                            break;
                        case TWO_PLAYING:
                            handleTwoPlayer(now, deltaTime);
                            break;
                        case FADE:
                            handleFade(now);
                            break;
                        case VICTORY, LOSS: {
                        }
                        default: {
                        }
                    }
                }
                return null;
            };
            gameLogicUpdateTask = logicExecutor.submit(logicTask);
        }
    }

    private void handleSinglePlayer(double deltaTime) {
        if (!(model.getCurrentView() instanceof GameplayView))
            Platform.runLater(() -> model.setCurrentView(new GameplayView(model)));

        GameplayModel g = model.getGameplayModel();
        if (g == null) return;

        g.update(leftPressed, rightPressed, deltaTime);
        if (g.hasCompletedAllLevels()) {
            model.setGstate(State.VICTORY);
            Platform.runLater(() -> model.setCurrentView(new VictoryView(model)));
        } else if (g.getLives() <= 0) {
            model.setGstate(State.LOSS);
            Platform.runLater(() -> model.setCurrentView(new LoseView(model)));
        }
    }

    private void handleTwoPlayer(long now, double deltaTime) {
        if (!(model.getCurrentView() instanceof TwoPlayerView)) {
            Platform.runLater(() -> {
                model.setCurrentView(new TwoPlayerView(model));
                javafx.stage.Stage stage = (javafx.stage.Stage) view.getScene().getWindow();
                stage.sizeToScene();
                stage.centerOnScreen();
            });
        }

        GameplayModel left = model.getLeftGame(), right = model.getRightGame();
        if (left == null || right == null) return;

        boolean leftDead = left.getLives() <= 0;
        boolean rightDead = right.getLives() <= 0;
        boolean leftDone = left.hasCompletedAllLevels();
        boolean rightDone = right.hasCompletedAllLevels();

        boolean leftFinish = left.isLevelFinished() || leftDone;
        boolean rightFinish = right.isLevelFinished() || rightDone;

        boolean bothDead = leftDead && rightDead;
        boolean bothDone = leftDone && rightDone;
        boolean oneFinishOtherDead = (leftFinish && rightDead) || (rightFinish && leftDead);

        if (bothDead || bothDone || oneFinishOtherDead) {
            int leftScore = left.getScore(), rightScore = right.getScore();

            if (leftScore > rightScore) {
                left.setWinner(true);
                right.setLoser(true);
            } else if (rightScore > leftScore) {
                right.setWinner(true);
                left.setLoser(true);
            } else {
                left.setDraw(true);
                right.setDraw(true);
            }

            left.setWaitingForOtherPlayer(false);
            right.setWaitingForOtherPlayer(false);

            model.setTwoPlayerEnded(true);
            model.setCurrentView(new TwoPlayerView(model));
            return;
        }

        left.setWaitingForOtherPlayer((leftFinish || leftDead) && !rightFinish);
        right.setWaitingForOtherPlayer((rightFinish || rightDead) && !leftFinish);

        if (!leftDead && !leftDone && !left.isWaitingForOtherPlayer())
            left.update(leftPressed, rightPressed, deltaTime);

        if (!rightDead && !rightDone && !right.isWaitingForOtherPlayer())
            right.update(left2Pressed, right2Pressed, deltaTime);

        if (leftFinish && rightFinish) {
            if (!left.isFading()) left.startFade(now);
            if (!right.isFading()) right.startFade(now);

            double leftFade = (now - left.getFadeStartTime()) / 1_000_000_000.0;
            double rightFade = (now - right.getFadeStartTime()) / 1_000_000_000.0;

            if (leftFade >= FADE_DURATION && rightFade >= FADE_DURATION) {
                left.stopFade();
                right.stopFade();
                if (!leftDone) left.Initialize();
                if (!rightDone) right.Initialize();
                lastUpdateTime = now;
            }
        }
    }

    private void handleFade(long now) {
        double elapsed = (now - model.getFadeStartTime()) / 1_000_000_000.0;
        if (elapsed >= FADE_DURATION && model.getGameplayModel() != null) {
            model.getGameplayModel().Initialize();
            model.setGstate(State.PLAYING);
        }
    }

    public void setInput() {
        view.getScene().setOnMouseMoved(e -> {
            if (model.getCurrentView() != null) model.getCurrentView().checkHover(e);
        });
        view.getScene().setOnMouseClicked(this::handleMouseClick);
        view.getScene().setOnKeyPressed(this::handleKeyPressed);
        view.getScene().setOnKeyReleased(this::handleKeyReleased);
    }

    private void handleMouseClick(javafx.scene.input.MouseEvent e) {
        if (model.getCurrentView() != null)
            model.getCurrentView().handleClick(e);
    }

    private void handleKeyPressed(javafx.scene.input.KeyEvent e) {
        State state = model.getGstate();
        KeyCode code = e.getCode();

        switch (state) {
            case READY_TO_PLAY:
                if (code == KeyCode.A || code == KeyCode.D) {
                    model.setGstate(State.PLAYING);
                }
                return;
            case PLAYING:
                if (code == KeyCode.ESCAPE) {
                    model.setGstate(State.PAUSED);
                    return;
                }
                break;
            case TWO_PLAYING:
                if (code == KeyCode.ESCAPE) {
                    model.setGstate(State.TWO_PLAYER_PAUSED);
                    return;
                }
                break;
            case PAUSED:
                if (code == KeyCode.ESCAPE) {
                    new ResumeGameCmd(model).execute(); // Resume về PLAYING
                }
                return;
            case TWO_PLAYER_PAUSED:
                if (code == KeyCode.ESCAPE) {
                    model.setGstate(State.TWO_PLAYING);
                }
                return;
            case SETTING_ACCOUNT:
                if (model.getCurrentView() != null)
                    model.getCurrentView().handleKeyInput(e);
                return;
            default:
                break;
        }

        if (model.isTwoPlayerEnded()) {
            if (code == KeyCode.ENTER) {
                model.CreateTwoGameplay();
                model.setTwoPlayerEnded(false);
                model.setGstate(State.TWO_PLAYING);
                model.setCurrentView(new TwoPlayerView(model));
            } else if (code == KeyCode.ESCAPE) {
                model.setTwoPlayerEnded(false);
                model.setGstate(State.MENU);
                model.setCurrentView(new MenuScene(model));
            }
            return;
        }

        switch (code) {
            case A:
                leftPressed = true;
                break;
            case D:
                rightPressed = true;
                break;
            case SPACE:
                if (state == State.PLAYING && model.getGameplayModel() != null) {
                    model.getGameplayModel().launchBall();
                } else if (state == State.TWO_PLAYING && model.getLeftGame() != null) {
                    model.getLeftGame().launchBall();
                }
                break;
            case LEFT:
                if (state == State.TWO_PLAYING) left2Pressed = true;
                else if (state == State.PLAYING) leftPressed = true;
                break;
            case RIGHT:
                if (state == State.TWO_PLAYING) right2Pressed = true;
                else if (state == State.PLAYING) rightPressed = true;
                break;
            case ENTER:
                if (state == State.TWO_PLAYING && model.getRightGame() != null)
                    model.getRightGame().launchBall();
                break;
            default:
                break;
        }
    }

    private void handleKeyReleased(javafx.scene.input.KeyEvent e) {
        switch (e.getCode()) {
            case A:
                leftPressed = false;
                break;
            case D:
                rightPressed = false;
                break;
            case LEFT:
                if (model.getGstate() == State.PLAYING) {
                    leftPressed = false;
                } else if (model.getGstate() == State.TWO_PLAYING) {
                    left2Pressed = false;
                }
                break;
            case RIGHT:
                if (model.getGstate() == State.PLAYING) {
                    rightPressed = false;
                } else if (model.getGstate() == State.TWO_PLAYING) {
                    right2Pressed = false;
                }
                break;
        }
    }
}
