package View;

import Controller.ChangeBackgroundCmd;
import Controller.ChangeStateCmd;
import Model.Button;
import Model.GameModel;
import Model.GameplayModel;
import Model.State;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;
import javafx.scene.input.MouseEvent;

import java.util.Objects;

public class ChooseBackgroundView extends View {
    Image background;
    Button bg1;
    Button bg2;
    Button bg3;
    Button bg4;
    Button exit;

    public ChooseBackgroundView(GameModel model) {
        super(model);
        background = new Image(Objects.requireNonNull(getClass().getResource("/chooseBgBg.png")).toExternalForm());

        bg1 = new Button(60, 156.9, 155.2, 168.1, new ChangeBackgroundCmd(model, "/GameBG.png"));
        bg1.setImgButton("/GameBG.png");
        bg1.setImgHoverButton("/GameBG.png");

        bg2 = new Button(384.8, 156.9, 155.2, 168.1, new ChangeBackgroundCmd(model, "/1.jpg"));
        bg2.setImgButton("/1.jpg");
        bg2.setImgHoverButton("/1.jpg");

        bg3 = new Button(60, 421.9, 155.2, 168.1, new ChangeBackgroundCmd(model, "/2.jpg"));
        bg3.setImgButton("/2.jpg");
        bg3.setImgHoverButton("/2.jpg");

        bg4 = new Button(384.8, 421.9, 155.2, 168.1, new ChangeBackgroundCmd(model, "/3.jpg"));
        bg4.setImgButton("/3.jpg");
        bg4.setImgHoverButton("/3.jpg");

        exit = new Button(5.4, 590, 153.6, 59.1, new ChangeStateCmd(model, State.THEME));
        exit.setImgButton("/Exit.png");
        exit.setImgHoverButton("/ExitHover.png");

        buttons.add(bg1);
        buttons.add(bg2);
        buttons.add(bg3);
        buttons.add(bg4);
        buttons.add(exit);
    }

    @Override
    public void draw(GraphicsContext render, GameplayModel gameplayModel) {
        render.drawImage(background, 0, 0, 600, 650);
        exit.draw(render);
        bg1.draw(render);
        bg2.draw(render);
        bg3.draw(render);
        bg4.draw(render);
    }

    @Override
    public void checkHover(MouseEvent e) {
        for (Button b : buttons) {
            b.setHovering(e);
        }
    }
}
