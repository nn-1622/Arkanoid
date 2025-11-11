package Controller;

import Model.GameModel;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.charset.StandardCharsets;

public class ChangePaddleCmd implements GameCommand {
    private final GameModel model;
    private final String path;

    private static final String CONFIG_FILE = "paddle_config.txt";

    public ChangePaddleCmd(GameModel model, String path) {
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
