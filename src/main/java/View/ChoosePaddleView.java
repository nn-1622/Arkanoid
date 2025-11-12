package View;

import Controller.ChangePaddleCmd;
import Controller.ChangeStateCmd;
import Model.Button;
import Model.GameModel;
import Model.GameplayModel;
import Model.State;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;
import javafx.scene.input.MouseEvent;

import java.util.Objects;

public class ChoosePaddleView extends View {
    Image background;
    Button pd1;
    Button pd2;
    Button pd3;
    Button pd4;
    Button exit;

    public ChoosePaddleView(GameModel model) {
        super(model);

        background = new Image(Objects.requireNonNull(getClass().getResource("/choosePaddleBg.png")).toExternalForm());

        pd1 = new Button(142.3, 176.2, 315.4, 70.1, new ChangePaddleCmd(model, "/DefaultPaddle.png"));
        pd1.setImgButton("/DefaultPaddle.png");
        pd1.setImgHoverButton("/DefaultPaddleHover.png");

        pd2 = new Button(142.3, 274.7, 315.4, 70.1, new ChangePaddleCmd(model, "/Pyramid.png"));
        pd2.setImgButton("/Pyramid.png");
        pd2.setImgHoverButton("/PyramidHover.png");

        pd3 = new Button(142.3, 372.7, 315.4, 70.1, new ChangePaddleCmd(model, "/Skateboard.png"));
        pd3.setImgButton("/Skateboard.png");
        pd3.setImgHoverButton("/SkateboardHover.png");

        pd4 = new Button(142.3, 470.8, 315.4, 70.1, new ChangePaddleCmd(model, "/Sword.png"));
        pd4.setImgButton("/Sword.png");
        pd4.setImgHoverButton("/SwordHover.png");

        exit = new Button(5.4, 590, 153.6, 59.1, new ChangeStateCmd(model, State.THEME));
        exit.setImgButton("/Exit.png");
        exit.setImgHoverButton("/ExitHover.png");

        buttons.add(pd1);
        buttons.add(pd2);
        buttons.add(pd3);
        buttons.add(pd4);
        buttons.add(exit);
    }

    @Override
    public void draw(GraphicsContext render, GameplayModel gameplayModel) {
        render.drawImage(background, 0, 0, 600, 650);
        exit.draw(render);
        pd1.draw(render);
        pd2.draw(render);
        pd3.draw(render);
        pd4.draw(render);
    }
    
    @Override
    public void checkHover(MouseEvent e) {
        for (Button b : buttons) {
            b.setHovering(e);
        }
    }
}
