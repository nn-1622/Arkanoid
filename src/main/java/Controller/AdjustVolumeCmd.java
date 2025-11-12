package Controller;

/**
 * lệnh điều chỉnh âm lượng của trò chơi.
 */
public class AdjustVolumeCmd implements GameCommand {
    private final boolean increase;

    public AdjustVolumeCmd(boolean inc) {
        this.increase = inc;
    }

    @Override
    public void execute() {
        SoundManager soundManager = SoundManager.getInstance();
        if (increase) {
            soundManager.increaseVolume();
        } else {
            soundManager.decreaseVolume();
        }
    }
}