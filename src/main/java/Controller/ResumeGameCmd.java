package Controller;

import Model.GameModel;
import Model.State;

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