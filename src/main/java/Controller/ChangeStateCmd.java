package Controller;

import Model.GameModel;
import Model.State;

public class ChangeStateCmd implements GameCommand {
    private final GameModel model;
    private final State stateToChange;
    public ChangeStateCmd(GameModel model,  State stateToChange) {
        this.model = model;
        this.stateToChange = stateToChange;
    }
    @Override
    public void execute() {
        model.setGstate(stateToChange);
        if (stateToChange == State.PLAYING) {
            model.CreateNewGame();
        }
        else if (stateToChange == State.TWO_PLAYING) {
            model.CreateTwoGameplay();
        }
    }
    /**
     * Lấy State mà lệnh này sẽ chuyển đến.
     * @return Trạng thái (State) đích.
     */
    public State getState() {
        return this.stateToChange;
    }
}
