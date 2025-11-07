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
        }
    }

    /**
     * Đặt âm lượng chính cho tất cả các hiệu ứng âm thanh.
     * Âm lượng được giới hạn trong khoảng từ 0.0 (tắt tiếng) đến 1.0 (tối đa).
     * @param volume Giá trị âm lượng mới (từ 0.0 đến 1.0).
     */
    public static void setMasterVolume(double volume) {
        masterVolume = Math.max(0.0, Math.min(1.0, volume));

        hitSound.setVolume(masterVolume);
        winSound.setVolume(masterVolume);
        loseSound.setVolume(masterVolume);
        testSound.setVolume(masterVolume);
    }

    /**
     * Tăng âm lượng chính lên một bậc.
     * Lượng tăng được xác định bởi biến {@code volStep}.
     */
    public static void increaseVolume() {
        setMasterVolume(masterVolume + volStep);
        testSound.play();
    }

    /**
     * Giảm âm lượng chính xuống một bậc.
     * Lượng giảm được xác định bởi biến {@code volStep}.
     */
    public static void decreaseVolume() {
        setMasterVolume(masterVolume - volStep);
        testSound.play();
    }

}