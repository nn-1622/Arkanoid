package Controller;

import Model.GameModel;

import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;

public class ChangeBackgroundCmd implements GameCommand {
    private static final String CONFIG_FILE = "background_config.txt";
    private final GameModel model;
    private final String path;

    public ChangeBackgroundCmd(GameModel model, String path) {
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
