package Controller;

import Model.GameModel;

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.charset.StandardCharsets;

/**
 * lệnh thay đổi quả bóng trong trò chơi.
 */
public class ChangeBallCmd implements GameCommand {
    private static final String CONFIG_FILE = "ball_config.txt";
    private final GameModel model;
    private final String path;

    public ChangeBallCmd(GameModel model, String path) {
        this.model = model;
        this.path = path;
    }

    @Override
    public void execute() {
        try {
            Files.writeString(Path.of(CONFIG_FILE), path, StandardCharsets.UTF_8);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
