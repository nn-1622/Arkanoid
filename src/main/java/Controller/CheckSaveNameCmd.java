package Controller;

import Model.GameModel;
import Model.State;
import View.PauseView;

/**
 * lệnh kiểm tra tên file lưu của trò chơi.
 */
public class CheckSaveNameCmd implements GameCommand {
    private final GameModel model;
    private final PauseView view;

    public CheckSaveNameCmd(GameModel model, PauseView view) {
        this.model = model;
        this.view = view;
    }

    @Override
    public void execute() {
        if (model.getCurrentSaveName() == null) {
            model.setStateBeforeAccount(State.PAUSED);
            model.setGstate(State.SETTING_ACCOUNT);
        } else {
            model.saveGame(model.getCurrentSaveName());
        }
    }
}