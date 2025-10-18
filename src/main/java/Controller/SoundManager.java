package Controller;


import javafx.scene.media.AudioClip;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;

/**
 * Quản lý tất cả các hiệu ứng âm thanh trong trò chơi.
 * Lớp này chịu trách nhiệm tải, phát và điều chỉnh âm lượng
 * của các đoạn âm thanh ngắn (AudioClip) như tiếng va chạm, thắng, thua.
 */
public class SoundManager {
    private AudioClip hitSound;
    private AudioClip winSound;
    private AudioClip loseSound;
    private AudioClip testSound;
    private double masterVolume = 0.5;
    private double volStep = 0.2;

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

    /**
     * Phát âm thanh va chạm (khi bóng trúng gạch).
     */
    public void playHitSound() {
        hitSound.play();
    }

    /**
     * Phát âm thanh chiến thắng (khi hoàn thành một cấp độ hoặc cả trò chơi).
     */
    public void playWinSound() {
        winSound.play();
    }

    /**
     * Phát âm thanh thua cuộc (khi người chơi hết mạng).
     */
    public void playLoseSound() {
        loseSound.play();
    }

    /**
     * Phát một âm thanh thử nghiệm, thường được sử dụng khi điều chỉnh âm lượng trong menu cài đặt.
     */
    public void playTestSound() {
        testSound.play();
    }

    /**
     * Đặt âm lượng chính cho tất cả các hiệu ứng âm thanh.
     * Âm lượng được giới hạn trong khoảng từ 0.0 (tắt tiếng) đến 1.0 (tối đa).
     * @param volume Giá trị âm lượng mới (từ 0.0 đến 1.0).
     */
    public void setMasterVolume(double volume) {
        this.masterVolume = Math.max(0.0, Math.min(1.0, volume));

        hitSound.setVolume(masterVolume);
        winSound.setVolume(masterVolume);
        loseSound.setVolume(masterVolume);
        testSound.setVolume(masterVolume);
    }

    /**
     * Tăng âm lượng chính lên một bậc.
     * Lượng tăng được xác định bởi biến {@code volStep}.
     */
    public void increaseVolume() {
        setMasterVolume(masterVolume + volStep);
    }

    /**
     * Giảm âm lượng chính xuống một bậc.
     * Lượng giảm được xác định bởi biến {@code volStep}.
     */
    public void decreaseVolume() {
        setMasterVolume(masterVolume - volStep);
    }

}