package Controller;

import java.sql.SQLOutput;

public class AdjustVolumeCmd implements GameCommand {
    private boolean increase;

    public AdjustVolumeCmd(boolean inc) {
        increase = inc;
    }

    @Override
    public void execute() {
        if (increase) {
            SoundManager.increaseVolume();
        } else {
            SoundManager.decreaseVolume();
        }
    }
}
