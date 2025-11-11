package View;

import Controller.ChangeStateCmd;
import Model.Button;
import Model.GameModel;
import Model.GameplayModel;
import Model.State;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;

public class HowToPlayView extends View {
    private Button exitButton;

    public HowToPlayView(GameModel model) {
        super(model);
        exitButton = new Button( 200.1, 523.8, 199.8, 41.9, new ChangeStateCmd(model, State.SETTING));
        exitButton.setImgButton("/Exit.png");
        exitButton.setImgHoverButton("/ExitHover.png");
        buttons.add(exitButton);
    }

    @Override
    public void draw(GraphicsContext render, GameplayModel gameplayModel) {
        render.setFill(Color.BLACK);
        render.fillRect(0, 0, 600, 650);

        render.setFill(Color.WHITE);
        render.setFont(Font.font("Consolas", 20));

        render.fillText("HOW TO PLAY", 240, 150);
        render.fillText("Player 1:", 100, 220);
        render.fillText("Move: A / D", 120, 250);
        render.fillText("Launch Ball: SPACE", 120, 280);

        render.fillText("Player 2:", 100, 350);
        render.fillText("Move: LEFT / RIGHT", 120, 380);
        render.fillText("Launch Ball: ENTER", 120, 410);

        exitButton.draw(render);
    }
}