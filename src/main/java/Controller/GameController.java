package Controller;

import Model.GameModel;
import Model.GameplayModel;
import Model.State;
import View.GameView;
import View.PauseOverlayView;
import javafx.scene.input.MouseEvent;
import javafx.scene.input.KeyCode;

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
    private PauseOverlayView pauseOverlay;

    /**
     * Khởi tạo một GameController mới với GameModel và GameView được cung cấp.
     * @param gm Model của trò chơi, chứa dữ liệu và trạng thái game.
     * @param gv View của trò chơi, chịu trách nhiệm hiển thị.
     */
    public GameController(GameModel gm, GameView gv) {
        this.model = gm;
        this.view = gv;
        this.soundManager = new SoundManager();
        pauseOverlay = view.getPauseOverlayView(); //update pauseoverlayview
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

        //Chỉ update gameplay khi PLAYING (không update khi PAUSED)
        if (model.getGstate() == State.PLAYING) {
            this.model.getGameplayModel().update(leftpressed, rightpressed, deltaTime);
        }
        // Xử lý FADE
        else if(model.getGstate() == State.FADE){
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
            // Update xử lý hover cho paused
            if(model.getGstate() == State.PAUSED) {
                pauseOverlay.checkHover(e);
            }
            else if(model.getGstate() == State.MENU) {
                view.getMenuScene().checkHover(e);
            } else if(model.getGstate() == State.SETTING){
                view.getSettingScene().checkHover(e);
            } else if(model.getGstate() == State.LOSS){
                view.getLoseScene().checkHover(e);
            } else if(model.getGstate() == State.VICTORY){
                view.getVictoryScene().checkHover(e);
            } else if(model.getGstate() == State.HIGHSCORE) {
                view.getHighScoreView().checkHover(e);
            }
        });

        // Xử lý sự kiện nhấp chuột để tương tác với các nút trong các màn hình khác nhau
        view.getScene().setOnMouseClicked(e -> {
            // Update xử lý click cho pause, resume, save
            if(model.getGstate() == State.PAUSED) {
                if (pauseOverlay.saveClicked(e)) {
                    model.getGameplayModel().savePause();
                    model.setGstate(State.MENU); //về menu sau khi save
                    System.out.println("Game saved and returned to menu");
                } else if (pauseOverlay.menuClicked(e)) {
                    model.setGstate(State.MENU); // về menu mà ko save
                    System.out.println("Returned to menu without saving");
                }
            } else if(model.getGstate() == State.MENU) {
                if(view.getMenuScene().startClick(e)) {
                    model.setGstate(State.PLAYING);
                    model.CreateGameplay(this);
                } else if(view.getMenuScene().settingClick(e)) {
                    model.setGstate(State.SETTING);
                } else if(view.getMenuScene().exitClick(e)) {
                    System.exit(0);
                }
            } else if(model.getGstate() == State.SETTING) {
                if(view.getSettingScene().exitClicked(e)) {
                    model.setGstate(State.MENU);
                } else if (view.getSettingScene().lowVolumeClicked(e)) {
                    soundManager.decreaseVolume();
                    soundManager.playTestSound();
                } else if(view.getSettingScene().highVolumeClicked(e)) {
                    soundManager.increaseVolume();
                    soundManager.playTestSound();
                }
            } else if(model.getGstate() == State.LOSS) {
                if(view.getLoseScene().checkClickMenu(e)) {
                    model.setGstate(State.MENU);
                } else if (view.getLoseScene().checkClickReplay(e)) {
                    model.setGstate(State.PLAYING);
                    model.CreateGameplay(this);
                } else if(view.getLoseScene().checkClickHighScore(e)) {
                    model.setGstate(State.HIGHSCORE);
                }
            } else if(model.getGstate() == State.VICTORY) {
                if(view.getVictoryScene().checkClickMenu(e)) {
                    model.setGstate(State.MENU);
                } else if (view.getVictoryScene().checkClickReplay(e)) {
                    model.setGstate(State.PLAYING);
                    model.CreateGameplay(this);
                }
            } else if(model.getGstate() == State.HIGHSCORE) {
                if(view.getHighScoreView().checkClickMenu(e)) {
                    model.setGstate(State.LOSS);
                }
            }
        });

        // Xử lý sự kiện nhấn phím
        view.getScene().setOnKeyPressed(e -> {
            switch (e.getCode()) {
                case A -> leftpressed = true; // Di chuyển sang trái
                case D -> rightpressed = true; // Di chuyển sang phải
                case SPACE -> {
                    if(model.getGstate() == State.PLAYING) {
                        model.getGameplayModel().launchBall(); // Phóng bóng
                    }
                }
                // ESC cho Pause and Resume
                case ESCAPE -> {
                    if (model.getGstate() == State.PLAYING) {
                        model.setGstate(State.PAUSED);
                        System.out.println("GAME PAUSED!"); //nhấn esc thì pause
                    } else if(model.getGstate() == State.PAUSED) {
                        model.setGstate(State.PLAYING); //nhấn lần 2 thì resume
                        System.out.println("GAME RESUMED!");
                    }
                }
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