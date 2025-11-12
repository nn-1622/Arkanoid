package Controller;

import Model.GameModel;
import Model.State;

public class LoadGameCmd implements GameCommand {
    private final GameModel model;
    private final String fileName;

    public LoadGameCmd(GameModel model, String fileName) {
        this.model = model;
        this.fileName = fileName;
    }

    @Override
    public void execute() {
        if (model.loadGame(fileName)) {
            model.setGstate(State.READY_TO_PLAY);
        } else {
            System.err.println("Lỗi: Không thể tải file " + fileName);
        }
    }

    public String getFileName() {
        return fileName;
    }
}