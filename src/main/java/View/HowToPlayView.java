package View;

import Controller.ChangeStateCmd;
import Model.Button;
import Model.GameModel;
import Model.GameplayModel;
import Model.State;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;
import javafx.scene.input.MouseEvent;

import java.util.Objects;

public class HowToPlayView extends View {
    private final Image background;
    Button exit;

    public HowToPlayView(GameModel model) {
        super(model);

        background = new Image(Objects.requireNonNull(getClass().getResource("/HowtoplayBg.png")).toExternalForm());
        exit = new Button(412.4, 26.4, 175, 67.3, new ChangeStateCmd(model, State.SETTING));
        exit.setImgButton("/Exit.png");
        exit.setImgHoverButton("/ExitHover.png");

        buttons.add(exit);
    }

    @Override
    public void draw(GraphicsContext render, GameplayModel gameplayModel) {
        render.drawImage(background, 0, 0, 600, 650);
        exit.draw(render);
    }

    @Override
    public void checkHover(MouseEvent e) {
        for (Button b : buttons) {
            b.setHovering(e);
        }
    }
}
