package Model;

import Controller.EventLoader;
import View.*;

import java.util.EnumMap;
import java.util.Map;

/**
 * Lớp model chính của toàn bộ ứng dụng trò chơi.
 * Quản lý trạng thái tổng thể (MENU, PLAYING, TWO_PLAYING, SETTING, v.v.)
 * và chứa các GameplayModel riêng cho 1P hoặc 2P.
 */
public class GameModel implements UltilityValues {
    private final EventLoader eventLoader;
    private static GameModel model;
    private State gstate;
    private View currentView;
    private GameplayModel gameModel;
    private final Map<State, View> viewMap = new EnumMap<>(State.class);
    private long fadeStartTime = 0;

    // Hai người chơi (2P)
    private GameplayModel leftGame;
    private GameplayModel rightGame;

    // Bộ đếm thời gian hiển thị kết quả (endgame)
    private long resultStartTime = -1;
    private boolean resultTimerStarted = false;

    private boolean twoPlayerEnded = false;

    public boolean isTwoPlayerEnded() {
        return twoPlayerEnded;
    }

    public void setTwoPlayerEnded(boolean value) {
        this.twoPlayerEnded = value;
    }


    /** Bắt đầu bộ đếm thời gian kết quả */
    public void startResultTimer() {
        resultStartTime = System.nanoTime();
        resultTimerStarted = true;
    }

    /** Kiểm tra xem đã bắt đầu bộ đếm kết quả chưa */
    public boolean isResultTimerStarted() {
        return resultTimerStarted;
    }

    /**
     * Kiểm tra đã trôi qua bao lâu kể từ khi bắt đầu kết quả
     * @param durationMillis số mili-giây chờ (ví dụ 5000 = 5 giây)
     */
    public boolean hasResultTimeElapsed(long durationMillis) {
        if (!resultTimerStarted) return false;
        long elapsed = (System.nanoTime() - resultStartTime) / 1_000_000;
        return elapsed >= durationMillis;
    }

    /** Reset bộ đếm kết quả (nếu cần dùng lại) */
    public void resetResultTimer() {
        resultTimerStarted = false;
        resultStartTime = -1;
    }

    /**
     * Khởi tạo một đối tượng GameModel mới.
     * Trạng thái ban đầu là MENU.
     */
    private GameModel() {
        eventLoader = new EventLoader();

        // Gán các view tương ứng với từng trạng thái
        viewMap.put(State.MENU, new MenuScene(this));
        viewMap.put(State.LOSS, new LoseView(this));
        viewMap.put(State.PLAYING, new GameplayView(this));
        viewMap.put(State.TWO_PLAYING, new TwoPlayerView(this));
        viewMap.put(State.SETTING, new SettingScene(this));
        viewMap.put(State.VICTORY, new VictoryView(this));
        viewMap.put(State.PLAY_MODE, new PlayModeScene(this));
        viewMap.put(State.CHOOSE_PADDLE, new ChoosePaddleView(this));
        viewMap.put(State.CHOOSE_BALL, new ChooseBallView(this));
        viewMap.put(State.CHOOSE_BACKGROUND, new ChooseBackgroundView(this));
        viewMap.put(State.LEADERBOARD, new LeaderBoardView(this));
        viewMap.put(State.THEME, new ThemeView(this));
        viewMap.put(State.HOW_TO_PLAY, new HowToPlayView(this));
        setGstate(State.MENU);
    }

    /** Singleton: chỉ có một GameModel duy nhất */
    public static GameModel getGameModel() {
        if (model == null) {
            model = new GameModel();
        }
        return model;
    }

    /**
     * Tạo và khởi tạo một phiên chơi 1 người.
     */
    public void CreateGameplay() {
        gameModel = new GameplayModel(eventLoader);
        leftGame = null;
        rightGame = null;
    }

    /**
     * Tạo và khởi tạo hai phiên chơi riêng biệt cho chế độ 2 người.
     */
    public void CreateTwoGameplay() {
        leftGame = new GameplayModel(eventLoader, true);
        rightGame = new GameplayModel(eventLoader, true);
        resetResultTimer();
    }

    // --------- Getter / Setter cơ bản ---------
    public GameplayModel getLeftGame() { return leftGame; }
    public GameplayModel getRightGame() { return rightGame; }

    public GameplayModel getGameplayModel() { return gameModel; }

    public void setGstate(State gstate) {
        this.gstate = gstate;
        this.currentView = viewMap.get(gstate);
    }

    public State getGstate() {
        return gstate;
    }

    public View getCurrentView() {
        return currentView;
    }

    public void setCurrentView(View currentView) {
        this.currentView = currentView;
    }

    public EventLoader getEventLoader() {
        return eventLoader;
    }

    public void setFadeStartTime(long fadeStartTime) {
        this.fadeStartTime = fadeStartTime;
    }

    public long getFadeStartTime() {
        return fadeStartTime;
    }
}
