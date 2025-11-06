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
    private SoundManager soundManager;
    private MouseEvent e;
    private boolean leftpressed;
    private boolean rightpressed;
    private long lastUpdateTime = 0;

    /**
     * Khởi tạo một GameController mới với GameModel và GameView được cung cấp.
     * @param gm Model của trò chơi, chứa dữ liệu và trạng thái game.
     * @param gv View của trò chơi, chịu trách nhiệm hiển thị.
     */
    public GameController(GameModel gm, GameView gv) {
        this.model = gm;
        this.view = gv;
        this.soundManager = new SoundManager();
        setInput();
    }

    /**
     * {@inheritDoc}
     * Được gọi khi người chơi hoàn thành một cấp độ.
     * Chuyển trạng thái trò chơi sang FADE để tạo hiệu ứng chuyển cảnh và phát âm thanh chiến thắng.
     */
    @Override
    public void onLevelCompleted() {
        model.setGstate(State.FADE);
        model.setFadeStartTime(System.nanoTime());
        soundManager.playWinSound();
    }

    /**
     * {@inheritDoc}
     * Được gọi khi người chơi thua cuộc (hết mạng).
     * Chuyển trạng thái trò chơi sang LOSS và phát âm thanh thua cuộc.
     */
    @Override
    public void onGameOver() {
        model.setGstate(State.LOSS);
        soundManager.playLoseSound();
    }

    /**
     * {@inheritDoc}
     * Được gọi mỗi khi bóng va vào một viên gạch.
     * Phát âm thanh va chạm.
     */
    @Override
    public void onBrickHit() {
        soundManager.playHitSound();
    }

    /**
     * {@inheritDoc}
     * Được gọi khi người chơi hoàn thành tất cả các cấp độ và chiến thắng trò chơi.
     * Chuyển trạng thái trò chơi sang VICTORY và phát âm thanh chiến thắng.
     */
    @Override
    public void onVictory() {
        soundManager.playWinSound();
        model.setGstate(State.VICTORY);
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
        } else if(model.getGstate() == State.FADE){
            double timeElapsed = (now - model.getFadeStartTime()) / 1_000_000_000.0;
            final double FADE_DURATION = 2.0;
            if(timeElapsed >= FADE_DURATION){
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
            if(model.getGstate() == State.MENU) {
                view.getMenuScene().checkHover(e);
            } else if(model.getGstate() == State.SETTING){
                view.getSettingScene().checkHover(e);
            } else if(model.getGstate() == State.LOSS){
                view.getLoseScene().checkHover(e);
            } else if(model.getGstate() == State.VICTORY){
                view.getVictoryScene().checkHover(e);
            } else if(model.getGstate() == State.HIGHSCORE) { //update xử lý hover
                view.getHighScoreView().checkHover(e); // Gọi checkHover của HighScoreView
            }
        });

        // Xử lý sự kiện nhấp chuột để tương tác với các nút trong các màn hình khác nhau
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
                //Update thêm xử lý phần highscoreview
                else if(view.getLoseScene().checkClickHighScore(e)) {
                    model.setGstate(State.HIGHSCORE);
                }
            }   else if(model.getGstate() == State.VICTORY) {
                if(view.getVictoryScene().checkClickMenu(e)) {
                    model.setGstate(State.MENU);
                } else if (view.getVictoryScene().checkClickReplay(e)) {
                    model.setGstate(State.PLAYING);
                    model.CreateGameplay(this);
                }
            }
            //Thêm phần xử lý khi đang trong highscoreview
            else if(model.getGstate() == State.HIGHSCORE) {
                if(view.getHighScoreView().checkClickMenu(e)) {
                    model.setGstate(State.MENU);  // Quay lại menu
                }
            }
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