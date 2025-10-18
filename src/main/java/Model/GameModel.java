package Model;


import Controller.GameEventListener;

/**
 * Lớp model chính của toàn bộ ứng dụng trò chơi.
 * Lớp này quản lý trạng thái tổng thể của ứng dụng (ví dụ: đang ở MENU, đang PLAYING, SETTING, v.v.)
 * và chứa một đối tượng {@link GameplayModel} để xử lý logic cụ thể của màn chơi.
 */
public class GameModel {
    private State gstate;
    private GameplayModel gameModel;
    private double canvasHeight;
    private double canvasWidth;
    private long fadeStartTime = 0;

    /**
     * Khởi tạo một đối tượng GameModel mới.
     * Trạng thái ban đầu của trò chơi được đặt là MENU.
     * @param canvasHeight Chiều cao của khu vực vẽ (canvas).
     * @param canvasWidth  Chiều rộng của khu vực vẽ (canvas).
     */
    public GameModel(double  canvasHeight, double canvasWidth) {
        gstate = State.MENU;
        this.canvasHeight = canvasHeight;
        this.canvasWidth = canvasWidth;
    }

    /**
     * Tạo và khởi tạo một phiên chơi mới (GameplayModel).
     * Phương thức này được gọi khi bắt đầu một màn chơi mới hoặc chơi lại.
     * @param gameEventListener Đối tượng lắng nghe sự kiện (thường là GameController) để nhận các thông báo từ màn chơi.
     */
    public void CreateGameplay(GameEventListener gameEventListener) {
        gameModel = new GameplayModel(this.canvasWidth, this.canvasHeight);
        gameModel.setGameEventListener(gameEventListener);
    }

    /**
     * Thiết lập trạng thái hiện tại của trò chơi.
     * @param gstate Trạng thái mới (ví dụ: State.PLAYING, State.MENU).
     */
    public void  setGstate(State gstate) {
        this.gstate = gstate;
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
}