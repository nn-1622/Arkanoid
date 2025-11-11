package View;

import Controller.ChangeStateCmd;
import Controller.GameCommand;
import Controller.LoadGameCmd;
import Model.Button;
import Model.GameModel;
import Model.GameplayModel;
import Model.State;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;
import javafx.scene.input.MouseEvent;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;

import java.io.File;
import java.util.Arrays;

public class LoadGameView extends View {
    private Image background;

    public LoadGameView(GameModel model) {
        super(model);
        background = new Image(getClass().getResource("/bg2.png").toExternalForm());

        Button backButton = new Button(20, 580, 150, 50, new ChangeStateCmd(model, State.PLAY_MODE));
        backButton.setImgButton("/Exit.png");
        backButton.setImgHoverButton("/ExitHover.png");
        buttons.add(backButton);

        Button createSlotButton = new Button(175, 150, 250, 60,
                new GameCommand() {
                    @Override
                    public void execute() {
                        model.setStateBeforeAccount(State.LOAD_GAME);
                        model.setGstate(State.SETTING_ACCOUNT);
                    }
                });
        createSlotButton.setImgButton("/NewGame.png");
        createSlotButton.setImgHoverButton("/NewGameHover.png");
        buttons.add(createSlotButton);
    }

    @Override
    public void checkHover(MouseEvent e) {
        for (Button b : buttons) {
            b.setHovering(e);
        }
    }

    /**
     * Xóa các nút save slot cũ, quét lại thư mục và tạo nút mới.
     * Sẽ được gọi bởi GameModel mỗi khi vào State.LOAD_GAME.
     */
    public void refreshSaveSlots() {
        buttons.removeIf(button -> button.getCommand() instanceof LoadGameCmd);
        File saveDir = new File("saves");
        if (!saveDir.exists()) {
            saveDir.mkdir();
        }

        File[] saveFiles = saveDir.listFiles((dir, name) -> name.endsWith(".sav"));

        if (saveFiles != null) {
            Arrays.sort(saveFiles, (f1, f2) -> Long.compare(f2.lastModified(), f1.lastModified()));
        }

        double startY = 230;
        double spacing = 60;

        if (saveFiles != null) {
            for (int i = 0; i < saveFiles.length && i < 4; i++) { // Giới hạn 4 save
                String fileName = saveFiles[i].getName();
                String slotName = fileName.substring(0, fileName.lastIndexOf('.'));

                Button loadSlotBtn = new Button(175, startY + (i * spacing), 262.6, 65.7,
                        new LoadGameCmd(model, fileName));
                loadSlotBtn.setImgButton("/Slot.png");
                loadSlotBtn.setImgHoverButton("/SlotHover.png");
                buttons.add(loadSlotBtn);
            }
        }
    }

    @Override
    public void draw(GraphicsContext gc, GameplayModel gameplayModel) {
        gc.drawImage(background, 0, 0, 600, 650);

        gc.setFont(Font.font("Consolas", 20));
        for (Button b : buttons) {
            b.draw(gc);
            if (b.isHover()) {
                gc.setFill(Color.YELLOW);
            } else {
                gc.setFill(Color.WHITE);
            }

            if (b.getCommand() instanceof LoadGameCmd cmd) {
                String slotName = cmd.getFileName().substring(0, cmd.getFileName().lastIndexOf('.'));
                gc.fillText(slotName, b.getX() + 130, b.getY() + 35);
            }
        }
    }
}