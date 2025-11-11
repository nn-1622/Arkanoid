package Controller;

import Model.GameModel;
import Model.State;

/**
 * Lưu lệnh thoát trạng thái paused
 */
public class ResumeGameCmd implements GameCommand {
    private final GameModel model;
    public ResumeGameCmd(GameModel model) {
        this.model = model;
    }
    @Override
    public void execute() {
        model.setGstate(State.PLAYING);
    }
}