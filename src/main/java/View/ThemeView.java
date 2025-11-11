package View;

import Controller.ChangeStateCmd;
import Model.Button;
import Model.GameModel;
import Model.GameplayModel;
import Model.State;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;

public class ThemeView extends View {
    private Button exitButton, backgroundButton, ballButton, paddleButton;

    public ThemeView(GameModel model) {
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
        render.setFont(Font.font("Consolas", 30));
        render.fillText("THEME", 250, 200);
        exitButton.draw(render);
    }
}