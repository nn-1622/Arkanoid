package Controller;

import Model.GameModel;
import Model.State;

/**
 * lệnh thay đổi trạng thái của trò chơi.
 */
public class ChangeStateCmd implements GameCommand {
    private final State stateToChange;
    private final GameModel model;

    public ChangeStateCmd(GameModel model, State stateToChange) {
        this.model = model;
        this.stateToChange = stateToChange;
    }

    @Override
    public void execute() {
        model.setGstate(stateToChange);
        if (stateToChange == State.PLAYING) {
            model.CreateGameplay();
        } else if (stateToChange == State.TWO_PLAYING) {
            model.CreateTwoGameplay();
        }
    }
}
