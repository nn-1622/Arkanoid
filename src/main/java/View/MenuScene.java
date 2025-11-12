package View;

import Controller.ChangeStateCmd;
import Controller.ExitCmd;
import Model.Button;
import Model.GameModel;
import Model.GameplayModel;
import Model.State;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;
import javafx.scene.input.MouseEvent;

import java.util.Objects;

/**
 * Lớp hiển thị Menu Game.
 */
public class MenuScene extends View implements SceneActions {
    private final Image background;
    Button start;
    Button settings;
    Button exit;
    Button lead;

    /**
     * Hàm hiển thị MenuScene.
     *
     * @param model model gốc của game
     */
    public MenuScene(GameModel model) {
        super(model);

        background = new Image(Objects.requireNonNull(getClass().getResource("/bg.png")).toExternalForm());

        start = new Button(177.9, 274.5, 244.2, 61, new ChangeStateCmd(model, State.PLAY_MODE));
        start.setImgButton("/Start.png");
        start.setImgHoverButton("/StartHover.png");

        settings = new Button(184, 346.8, 231.9, 64.4, new ChangeStateCmd(model, State.SETTING));
        settings.setImgButton("/Setting.png");
        settings.setImgHoverButton("/SettingHover.png");

        exit = new Button(212.5, 411.2, 175, 67.3, new ExitCmd());
        exit.setImgButton("/Exit.png");
        exit.setImgHoverButton("/ExitHover.png");

        lead = new Button(422.1, 259.6, 90.8, 90.8, new ChangeStateCmd(model, State.LEADERBOARD));
        lead.setImgButton("/Lead.png");
        lead.setImgHoverButton("/LeadHover.png");

        buttons.add(start);
        buttons.add(settings);
        buttons.add(exit);
        buttons.add(lead);
    }

    @Override
    public void draw(GraphicsContext render, GameplayModel gameplayModel) {
        render.drawImage(background, 0, 0, 600, 650);
        start.draw(render);
        settings.draw(render);
        exit.draw(render);
        lead.draw(render);
    }

    @Override
    public void checkHover(MouseEvent e) {
        for (Button b : buttons) {
            b.setHovering(e);
        }
    }
}