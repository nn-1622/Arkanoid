package Controller;

import Model.GameModel;
import Model.GameplayModel;
import Model.State;
import View.GameView;
import View.GameplayView;
import View.LoseView;
import View.MenuScene;
import View.PauseView;
import View.TwoPlayerView;
import View.View;
import View.VictoryView;
import javafx.scene.input.KeyCode;

/**
 * Bộ điều khiển chính cho game Arkanoid (1P & 2P).
 * Xử lý input, điều phối state, cập nhật logic và View.
 */
public class GameController implements GameEventListener {
    private final GameModel model;
    private final GameView view;
    private long lastUpdateTime = 0;

    private boolean leftPressed, rightPressed;     // Player 1
    private boolean left2Pressed, right2Pressed;   // Player 2

    private static final double FADE_DURATION = 1.1;

    public GameController(GameModel gm, GameView gv, SoundManager soundManager) {
        this.model = gm;
        this.view = gv;
        setInput();
        gm.getEventLoader().register(this);
        gm.getEventLoader().register(soundManager);
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

        if (model.getGstate() == State.PAUSED) {
            view.render(model);
            return;
        }
        view.render(model);
        State currentState = model.getGstate();
        if (currentState == State.PAUSED || currentState == State.READY_TO_PLAY) {
            return;
        }

        switch (model.getGstate()) {
            case PLAYING -> handleSinglePlayer(deltaTime);
            case TWO_PLAYING -> handleTwoPlayer(now, deltaTime);
            case FADE -> handleFade(now);
            case VICTORY, LOSS -> { /* giữ nguyên kết quả */ }
            default -> { /* menu hoặc setting không update logic */ }
        }

        view.render(model);
    }

    // ---------------- SINGLE PLAYER ----------------
    private void handleSinglePlayer(double deltaTime) {
        if (!(model.getCurrentView() instanceof GameplayView))
            model.setCurrentView(new GameplayView(model));

        GameplayModel g = model.getGameplayModel();
        if (g == null) return;

        g.update(leftPressed, rightPressed, deltaTime);

        // ✅ Khi người chơi thắng (hoàn tất tất cả level)
        if (g.hasCompletedAllLevels()) {
            model.setGstate(State.VICTORY);
            model.setCurrentView(new VictoryView(model));
        }

        // ✅ Khi người chơi hết mạng
        else if (g.getLives() <= 0) {
            model.setGstate(State.LOSS);
            model.setCurrentView(new LoseView(model));
        }
    }

    // ---------------- TWO PLAYER ----------------
    private void handleTwoPlayer(long now, double deltaTime) {
        if (!(model.getCurrentView() instanceof TwoPlayerView))
            model.setCurrentView(new TwoPlayerView(model));

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
                left.setWinner(true); right.setLoser(true);
            } else if (rightScore > leftScore) {
                right.setWinner(true); left.setLoser(true);
            } else {
                left.setDraw(true); right.setDraw(true);
            }

            left.setWaitingForOtherPlayer(false);
            right.setWaitingForOtherPlayer(false);

            model.setTwoPlayerEnded(true);
            model.setCurrentView(new TwoPlayerView(model));
            return;
        }

        left.setWaitingForOtherPlayer((leftFinish || leftDead) && !(rightFinish || rightDead));
        right.setWaitingForOtherPlayer((rightFinish || rightDead) && !(leftFinish || leftDead));

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
                left.stopFade(); right.stopFade();
                if (!leftDone) left.Initialize();
                if (!rightDone) right.Initialize();
                lastUpdateTime = now;
            }
        }
    }

    // ---------------- FADE ----------------
    private void handleFade(long now) {
        double elapsed = (now - model.getFadeStartTime()) / 1_000_000_000.0;
        if (elapsed >= FADE_DURATION && model.getGameplayModel() != null) {
            model.getGameplayModel().Initialize();
            model.setGstate(State.PLAYING);
        }
    }

    // ---------------- INPUT ----------------
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

    // Tệp: Controller/GameController.java

    private void handleKeyPressed(javafx.scene.input.KeyEvent e) {
        State state = model.getGstate();

        // Xử lý input DỰA TRÊN TRẠNG THÁI (STATE)
        switch (state) {
            case READY_TO_PLAY:
                // Nếu đang ở màn hình "Ready" (vừa load game)
                if (e.getCode() == KeyCode.A || e.getCode() == KeyCode.D) {
                    // Nhấn A hoặc D sẽ bắt đầu game
                    model.setGstate(State.PLAYING);
                    // Không 'return', để cho phím A/D chạy xuống logic di chuyển bên dưới
                } else {
                    return; // Các phím khác thì bỏ qua
                }
                break; // Thoát khỏi switch(state)

            case PLAYING:
            case TWO_PLAYING:
                // Nếu đang chơi (1P hoặc 2P)
                if (e.getCode() == KeyCode.ESCAPE) {
                    // Nhấn ESC sẽ Pause game
                    model.setGstate(State.PAUSED);
                    return; // Đã xử lý xong, thoát
                }
                break; // Thoát khỏi switch(state), chạy logic di chuyển bên dưới

            case PAUSED:
                // Nếu đang Pause
                if (e.getCode() == KeyCode.ESCAPE) {
                    View pauseView = model.getView(State.PAUSED);
                    new ResumeGameCmd(model).execute(); // Lệnh này sẽ set state về PLAYING
                    return;
                }
                return;

            case SETTING_ACCOUNT:
                // Nếu đang ở màn hình nhập tên
                // Chuyển sự kiện phím cho AccountView xử lý (fix từ lần trước)
                if (model.getCurrentView() != null) {
                    model.getCurrentView().handleKeyInput(e);
                }
                return; // Đã xử lý xong, không chạy logic di chuyển

            // Các state khác (MENU, VICTORY,...) không cần xử lý phím bấm ở đây
            default:
                break;
        }

        // --- LOGIC DI CHUYỂN & HÀNH ĐỘNG (CHỈ CHẠY KHI ĐANG CHƠI) ---

        // Xử lý 2 người chơi khi game kết thúc
        // (State vẫn là TWO_PLAYING, nhưng cờ isTwoPlayerEnded = true)
        if (model.isTwoPlayerEnded()) {
            if (e.getCode() == KeyCode.ENTER) {
                model.CreateTwoGameplay();
                model.setTwoPlayerEnded(false);
                model.setGstate(State.TWO_PLAYING);
                model.setCurrentView(new TwoPlayerView(model));
                return;
            } else if (e.getCode() == KeyCode.ESCAPE) {
                model.setTwoPlayerEnded(false);
                model.setGstate(State.MENU);
                model.setCurrentView(new MenuScene(model));
                return;
            }
        }

        // Input di chuyển và phóng bóng
        // (Logic này giờ chỉ chạy khi state là PLAYING, TWO_PLAYING, hoặc READY_TO_PLAY)
        switch (e.getCode()) {
            // Player 1
            case A: leftPressed = true; break;
            case D: rightPressed = true; break;
            case SPACE: {
                if (state == State.TWO_PLAYING && model.getLeftGame() != null)
                    model.getLeftGame().launchBall();
                else if (state == State.PLAYING && model.getGameplayModel() != null)
                    model.getGameplayModel().launchBall();
                else if (state == State.READY_TO_PLAY && model.getGameplayModel() != null) {
                    // Bonus: Cho phép nhấn SPACE để bắt đầu ở màn hình Ready
                    model.setGstate(State.PLAYING);
                    model.getGameplayModel().launchBall();
                }
            }
            break;

            // Player 2
            case LEFT: left2Pressed = true; break;
            case RIGHT: right2Pressed = true; break;
            case ENTER: {
                if (state == State.TWO_PLAYING && model.getRightGame() != null)
                    model.getRightGame().launchBall();
            }
            break;
        }
    }

    private void handleKeyReleased(javafx.scene.input.KeyEvent e) {
        switch (e.getCode()) {
            case A -> leftPressed = false;
            case D -> rightPressed = false;
            case LEFT -> left2Pressed = false;
            case RIGHT -> right2Pressed = false;
        }
    }
}
