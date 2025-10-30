package Controller;

import Model.GameModel;
import Model.GameplayModel;
import Model.State;
import View.GameView;
import javafx.scene.input.MouseEvent;


/**
 * Lớp điều khiển chính của trò chơi.
 * Lớp này chịu trách nhiệm xử lý các đầu vào từ người dùng (bàn phím, chuột),
 * cập nhật trạng thái của trò chơi (Model) và yêu cầu View vẽ lại giao diện.
 * Nó cũng hoạt động như một GameEventListener để xử lý các sự kiện từ GameplayModel.
 */
public class GameController implements GameEventListener {
    private GameModel model;
    private GameView view;
    private MouseEvent e;
    private boolean leftpressed;
    private boolean rightpressed;
    private long lastUpdateTime = 0;

    /**
     * Khởi tạo một GameController mới với GameModel và GameView được cung cấp.
     * @param gm Model của trò chơi, chứa dữ liệu và trạng thái game.
     * @param gv View của trò chơi, chịu trách nhiệm hiển thị.
     */
    public GameController(GameModel gm, GameView gv, SoundManager soundManager) {
        this.model = gm;
        this.view = gv;
        setInput();
        gm.getEventLoader().register(this);
        gm.getEventLoader().register(soundManager);
    }

    @Override
    public void onGameEvent (GameEvent event) {
        switch (event) {
            case GAME_WIN:
                model.setGstate(State.VICTORY);
                break;
            case GAME_LOST:
                model.setGstate(State.LOSS);
                break;
            case LEVEL_COMPLETE:
                model.setFadeStartTime(System.nanoTime());
                model.setGstate(State.FADE);
                break;
        }
    }

    /**
     * Phương thức cập nhật chính của trò chơi, được gọi liên tục bởi AnimationTimer.
     * Phương thức này tính toán delta time, yêu cầu view vẽ lại trạng thái hiện tại của model,
     * và cập nhật logic của game dựa trên trạng thái (PLAYING, FADE, v.v.).
     * @param now Thời gian hệ thống hiện tại tính bằng nano giây.
     */
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
        } else if (model.getGstate() == State.FADE) {
            double timeElapsed = (now - model.getFadeStartTime()) / 1_000_000_000.0;
            final double FADE_DURATION = 2.0;

            if (timeElapsed >= FADE_DURATION) {
                model.getGameplayModel().Initialize();
                model.setGstate(State.PLAYING);
            }
        }
    }

    /**
     * Thiết lập các trình xử lý sự kiện cho đầu vào từ chuột và bàn phím.
     * Các sự kiện này điều khiển các hành động khác nhau tùy thuộc vào trạng thái hiện tại của trò chơi
     * (ví dụ: điều hướng menu, di chuyển thanh trượt, phóng bóng).
     */
    public void setInput() {
        // Xử lý sự kiện di chuyển chuột để tạo hiệu ứng hover cho các nút
        view.getScene().setOnMouseMoved(e -> {
            model.getCurrentView().checkHover(e);
        });

        // Xử lý sự kiện nhấp chuột để tương tác với các nút trong các màn hình khác nhau
        view.getScene().setOnMouseClicked(e -> {
            model.getCurrentView().handleClick(e);
        });

        // Xử lý sự kiện nhấn phím
        view.getScene().setOnKeyPressed(e -> {
            switch (e.getCode()) {
                case A -> leftpressed = true; // Di chuyển sang trái
                case D -> rightpressed = true; // Di chuyển sang phải
                case SPACE -> model.getGameplayModel().launchBall(); // Phóng bóng
            }
        });

        // Xử lý sự kiện nhả phím
        view.getScene().setOnKeyReleased(e -> {
            switch (e.getCode()) {
                case A -> leftpressed = false; // Dừng di chuyển sang trái
                case D -> rightpressed = false; // Dừng di chuyển sang phải
            }
        });
    }
}