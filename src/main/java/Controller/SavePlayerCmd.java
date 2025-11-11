package Controller;

import Model.GameModel;
import Model.SaveState;
import Model.State;
import View.AccountView;
import View.PauseView;
import View.View;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.util.Arrays;

public class SavePlayerCmd implements GameCommand {
    private AccountView view;
    private GameModel model;

    public SavePlayerCmd(AccountView view) {
        this.view = view;
        this.model = view.getModel();
    }

    /**
     * check xem trong file có quá 4 slot ko
     * @param limit 4 slot được lưu
     */
    private void checkAndDeleteOldestSlot(int limit) {
        File saveDir = new File("saves");
        if (!saveDir.exists()) {
            saveDir.mkdir();
            return;
        }

        File[] saveFiles = saveDir.listFiles((dir, name) -> name.endsWith(".sav"));

        if (saveFiles != null && saveFiles.length >= limit) {
            Arrays.sort(saveFiles, (f1, f2) -> Long.compare(f1.lastModified(), f2.lastModified()));
            File oldestFile = saveFiles[0];
            System.out.println("đã đạt giới hạn slot xóa slot cũ nhất " + oldestFile.getName());
            oldestFile.delete();
        }
    }

    @Override
    public void execute() {
        String name = view.getPlayerName();
        if (name == null || name.trim().isEmpty()) {
            System.out.println("tên người chơi trống, không lưu");
            return;
        }
        String fileName = name + ".sav";

        State previousState = model.getStateBeforeAccount();

        if (previousState == State.PAUSED) {

            model.setCurrentSaveName(fileName);
            System.out.println("Lưu game vao slot " + fileName);
            model.saveGame(fileName);

            View pauseViewInstance = model.getView(State.PAUSED);
            if (pauseViewInstance instanceof PauseView) {
                ((PauseView) pauseViewInstance).showSaveConfirmation();
            }

            model.setGstate(State.PAUSED);

        } else {

            checkAndDeleteOldestSlot(4);

            model.setCurrentSaveName(fileName);
            System.out.println("Creating new empty save slot: " + fileName);
            SaveState emptySave = new SaveState();
            emptySave.hasGameProgress = false;
            emptySave.playerName = name;

            try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream("saves/" + fileName))) {
                oos.writeObject(emptySave);
            } catch (IOException e) {
                e.printStackTrace();
            }

            if (previousState == State.LOAD_GAME) {
                model.CreateGameplay();
                model.setGstate(State.PLAYING);
            } else {
                view.showSaveConfirmation();
                model.setGstate(previousState);
            }
        }
    }
}