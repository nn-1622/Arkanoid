package Controller;


import javafx.scene.media.AudioClip;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;

/**
 * Quản lý tất cả các hiệu ứng âm thanh trong trò chơi.
 * Lớp này chịu trách nhiệm tải, phát và điều chỉnh âm lượng
 * của các đoạn âm thanh ngắn (AudioClip) như tiếng va chạm, thắng, thua.
 */
public class SoundManager implements GameEventListener {
    private static AudioClip hitSound;
    private static AudioClip winSound;
    private static AudioClip loseSound;
    private static AudioClip testSound;
    private static AudioClip lostBall;
    private static AudioClip powerUp;
    private static AudioClip finishLevel;
    private static MediaPlayer menuBgm;
    private static MediaPlayer gameplayBgm;
    private static double masterVolume = 0.5;
    private static final double volStep = 0.2;

    /**
     * Khởi tạo SoundManager.
     * Tải tất cả các tệp âm thanh cần thiết từ thư mục tài nguyên (resources)
     * và chuẩn bị chúng để phát.
     */
    public SoundManager() {
        hitSound = new AudioClip(getClass().getResource("/sound/hit.wav").toExternalForm());
        winSound = new AudioClip(getClass().getResource("/sound/win.wav").toExternalForm());
        loseSound = new AudioClip(getClass().getResource("/sound/lose.wav").toExternalForm());
        testSound = new AudioClip(getClass().getResource("/sound/test.wav").toExternalForm());
        finishLevel = new AudioClip(getClass().getResource("/sound/FinishALevel.wav").toExternalForm());
        powerUp = new AudioClip(getClass().getResource("/sound/PowerUp.wav").toExternalForm());
        lostBall = new AudioClip(getClass().getResource("/sound/BallLost.wav").toExternalForm());
        this.menuBgm = createLoopPlayer("/sound/MainTheme.mp3");
        this.gameplayBgm = createLoopPlayer("/sound/GameTheme.mp3");
    }

    private MediaPlayer createLoopPlayer(String path) {
        try {
            var url = getClass().getResource(path);
            if (url == null) {
                System.err.println("BGM not found: " + path);
                return null;
            }
            Media media = new Media(url.toExternalForm());
            MediaPlayer player = new MediaPlayer(media);
            player.setCycleCount(MediaPlayer.INDEFINITE);
            return player;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    @Override
    public void onGameEvent (GameEvent event) {
        switch (event) {
            case GAME_WIN:
                winSound.play();
                break;
            case GAME_LOST:
                loseSound.play();
                break;
            case BALL_HIT:
                hitSound.play();
                break;
            case BALL_LOST:
                lostBall.play();
                break;
            case POWER_UP:
                powerUp.play();
                break;
            case LEVEL_COMPLETE:
                finishLevel.play();
                break;
        }
    }

    public void playMenuBgm() {
        stopGameplayBgm();
        if (menuBgm != null) {
            menuBgm.play();
        }
    }

    public void stopMenuBgm() {
        if (menuBgm != null) {
            menuBgm.stop();
        }
    }

    public void stopGameplayBgm() {
        if (gameplayBgm != null) {
            gameplayBgm.stop();
        }
    }

    public void stopAll() {
        stopMenuBgm();
        stopGameplayBgm();
    }
    public void playGameplayBgm() {
        stopMenuBgm();
        if (gameplayBgm != null) {
            gameplayBgm.play();
        }
    }

    public void pauseGameplayBgm() {
        if (gameplayBgm != null
                && gameplayBgm.getStatus() == MediaPlayer.Status.PLAYING) {
            gameplayBgm.pause();
        }
    }

    public void resumeGameplayBgm() {
        if (gameplayBgm != null
                && gameplayBgm.getStatus() == MediaPlayer.Status.PAUSED) {
            gameplayBgm.play();
        }
    }

    /**
     * Đặt âm lượng chính cho tất cả các hiệu ứng âm thanh.
     * Âm lượng được giới hạn trong khoảng từ 0.0 (tắt tiếng) đến 1.0 (tối đa).
     * @param volume Giá trị âm lượng mới (từ 0.0 đến 1.0).
     */
    public static void setMasterVolume(double volume) {
        masterVolume = Math.max(0.0, Math.min(1.0, volume));
        applyMasterVolume();
    }

    public static void increaseVolume() {
        setMasterVolume(masterVolume + volStep);
        playTest();
    }

    public static void decreaseVolume() {
        setMasterVolume(masterVolume - volStep);
        playTest();
    }

    private static void playTest() {
        if (testSound != null) {
            testSound.play();
        }
    }

    private static void applyMasterVolume() {
        if (hitSound != null) hitSound.setVolume(masterVolume);
        if (winSound != null) winSound.setVolume(masterVolume);
        if (loseSound != null) loseSound.setVolume(masterVolume);
        if (testSound != null) testSound.setVolume(masterVolume);
        if (powerUp != null) powerUp.setVolume(masterVolume);
        if (lostBall != null) lostBall.setVolume(masterVolume);
        if (finishLevel != null) finishLevel.setVolume(masterVolume);

        if (menuBgm != null) menuBgm.setVolume(masterVolume);
        if (gameplayBgm != null) gameplayBgm.setVolume(masterVolume);
    }
}