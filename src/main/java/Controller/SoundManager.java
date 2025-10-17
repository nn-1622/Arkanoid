package Controller;


import javafx.scene.media.AudioClip;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;

public class SoundManager {
    private AudioClip hitSound;
    private AudioClip winSound;
    private AudioClip loseSound;
    private AudioClip testSound;
    private double masterVolume = 0.5;
    private double volStep = 0.2;

    public SoundManager() {
        hitSound = new AudioClip(getClass().getResource("/sound/hit.wav").toExternalForm());
        winSound = new AudioClip(getClass().getResource("/sound/win.wav").toExternalForm());
        loseSound = new AudioClip(getClass().getResource("/sound/lose.wav").toExternalForm());
        testSound = new AudioClip(getClass().getResource("/sound/test.wav").toExternalForm());
    }

    public void playHitSound() {
        hitSound.play();
    }
    public void playWinSound() {
        winSound.play();
    }
    public void playLoseSound() {
        loseSound.play();
    }
    public void playTestSound() {
        testSound.play();
    }

    public void setMasterVolume(double volume) {
        this.masterVolume = Math.max(0.0, Math.min(1.0, volume));

        hitSound.setVolume(masterVolume);
        winSound.setVolume(masterVolume);
        loseSound.setVolume(masterVolume);
        testSound.setVolume(masterVolume);
    }

    public void increaseVolume() {
        setMasterVolume(masterVolume + volStep);
    }

    public void decreaseVolume() {
        setMasterVolume(masterVolume - volStep);
    }

}
