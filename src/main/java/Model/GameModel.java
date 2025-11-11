package Model;


import Controller.EventLoader;
import Controller.GameController;
import Controller.GameEventListener;
import View.*;

import java.util.EnumMap;
import java.util.Map;

/**
 * Lớp model chính của toàn bộ ứng dụng trò chơi.
 * Lớp này quản lý trạng thái tổng thể của ứng dụng (ví dụ: đang ở MENU, đang PLAYING, SETTING, v.v.)
 * và chứa một đối tượng {@link GameplayModel} để xử lý logic cụ thể của màn chơi.
 */
public class GameModel implements UltilityValues {
    private final EventLoader eventLoader;
    private static GameModel model;
    private State gstate;
    private View currentView;
    private GameplayModel gameModel;
    private final Map<State, View> viewMap = new EnumMap<State, View>(State.class);
    private long fadeStartTime = 0;
    private GameplayModel leftGame;
    private GameplayModel rightGame;
    /**
     * Khởi tạo một đối tượng GameModel mới.
     * Trạng thái ban đầu của trò chơi được đặt là MENU.
     */
    private GameModel() {
        eventLoader = new EventLoader();

        viewMap.put(State.MENU, new MenuScene(this));
        viewMap.put(State.LOSS, new LoseView(this));
        viewMap.put(State.PLAYING, new GameplayView(this));
        viewMap.put(State.TWO_PLAYING, new TwoPlayerView(this));
        viewMap.put(State.SETTING, new SettingScene(this));
        viewMap.put(State.VICTORY, new VictoryView(this));
        viewMap.put(State.PLAY_MODE, new PlayModeScene(this));
        viewMap.put(State.LEADERBOARD, new LeaderBoardView(this));
        viewMap.put(State.THEME, new ThemeView(this));
        viewMap.put(State.CHOOSE_BALL, new ChooseBallView(this));
        viewMap.put(State.CHOOSE_BACKGROUND, new ChooseBackgroundView(this));
        viewMap.put(State.CHOOSE_PADDLE, new ChoosePaddleView(this));
        setGstate(State.MENU);
    }

    public static GameModel getGameModel() {
        if (model == null) {
            model = new GameModel();
        }
        return model;
    }

    /**
     * Tạo và khởi tạo một phiên chơi mới (GameplayModel).
     * Phương thức này được gọi khi bắt đầu một màn chơi mới hoặc chơi lại.
     */
    public void CreateGameplay() {
        gameModel = new GameplayModel(eventLoader);
    }

    public void CreateTwoGameplay() {
        leftGame = new GameplayModel(eventLoader,true);
        rightGame = new GameplayModel(eventLoader, true);
    }

    public GameplayModel getLeftGame() { return leftGame; }
    public GameplayModel getRightGame() { return rightGame; }

    /**
     * Thiết lập trạng thái hiện tại của trò chơi.
     * @param gstate Trạng thái mới (ví dụ: State.PLAYING, State.MENU).
     */
    public void setGstate(State gstate) {
        this.gstate = gstate;

        this.currentView = viewMap.get(gstate);
    }

    /**
     * Lấy trạng thái hiện tại của trò chơi.
     * @return Trạng thái hiện tại của trò chơi.
     */
    public State getGstate() {
        return gstate;
    }

    /**
     * Lấy đối tượng model quản lý logic của màn chơi hiện tại.
     * @return Đối tượng GameplayModel.
     */
    public GameplayModel getGameplayModel() {
        return gameModel;
    }

    /**
     * Thiết lập thời điểm bắt đầu hiệu ứng mờ dần (fade).
     * Được sử dụng để tính toán thời gian cho các hiệu ứng chuyển cảnh.
     * @param fadeStartTime Thời gian bắt đầu (tính bằng nano giây).
     */
    public void setFadeStartTime(long fadeStartTime) {
        this.fadeStartTime = fadeStartTime;
    }

    /**
     * Lấy thời điểm bắt đầu hiệu ứng mờ dần (fade).
     * @return Thời gian bắt đầu (tính bằng nano giây).
     */
    public long getFadeStartTime() {
        return fadeStartTime;
    }

    /**
     * trả về view hiện tại
     * @return view
     */
    public View getCurrentView() {
        return currentView;
    }

    public EventLoader getEventLoader() {
        return eventLoader;
    }
}