package View;

import Controller.ChangeBallCmd;
import Controller.ChangeStateCmd;
import Model.Button;
import Model.GameModel;
import Model.GameplayModel;
import Model.State;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;
import javafx.scene.input.MouseEvent;

import java.util.Objects;

public class ChooseBallView extends View {
    Image background;
    Button ball1;
    Button ball2;
    Button ball3;
    Button ball4;
    Button exit;

    public ChooseBallView(GameModel model) {
        super(model);

        background = new Image(Objects.requireNonNull(getClass().getResource("/chooseBallBg.png")).toExternalForm());

        ball1 = new Button(91.3, 176.6, 123.2, 123.2, new ChangeBallCmd(model, "/DefaultBall.png"));
        ball1.setImgButton("/DefaultBall.png");
        ball1.setImgHoverButton("/DefaultBallHover.png");

        ball2 = new Button(352.3, 176.6, 123.2, 123.2, new ChangeBallCmd(model, "/Football.png"));
        ball2.setImgButton("/Football.png");
        ball2.setImgHoverButton("/FootballHover.png");

        ball3 = new Button(91.3, 379.8, 123.2, 123.2, new ChangeBallCmd(model, "/Pumkin.png"));
        ball3.setImgButton("/Pumkin.png");
        ball3.setImgHoverButton("/PumkinHover.png");

        ball4 = new Button(352.3, 379.8, 123.2, 123.2, new ChangeBallCmd(model, "/Powerball.png"));
        ball4.setImgButton("/Powerball.png");
        ball4.setImgHoverButton("/PowerballHover.png");

        exit = new Button(5.4, 590, 153.6, 59.1, new ChangeStateCmd(model, State.THEME));
        exit.setImgButton("/Exit.png");
        exit.setImgHoverButton("/ExitHover.png");

        buttons.add(ball1);
        buttons.add(ball2);
        buttons.add(ball3);
        buttons.add(ball4);
        buttons.add(exit);
    }

    @Override
    public void draw(GraphicsContext render, GameplayModel gameplayModel) {
        render.drawImage(background, 0, 0, 600, 650);
        exit.draw(render);
        ball1.draw(render);
        ball2.draw(render);
        ball3.draw(render);
        ball4.draw(render);
    }

    @Override
    public void checkHover(MouseEvent e) {
        for (Button b : buttons) {
            b.setHovering(e);
        }
    }
}
