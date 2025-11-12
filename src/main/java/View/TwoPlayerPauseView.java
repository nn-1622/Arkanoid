package View;

import Controller.ChangeStateCmd;
import Model.Button;
import Model.GameModel;
import Model.GameplayModel;
import Model.State;
import Model.UltilityValues;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.input.MouseEvent;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;

public class TwoPlayerPauseView extends View {
    private final Button resumeButton;
    private final Button exitButton;

    public TwoPlayerPauseView(GameModel model) {
        super(model);

        double centerX = UltilityValues.canvasWidth;
        double btnWidth = 200;
        double btnHeight = 50;

        resumeButton = new Button(centerX - btnWidth / 2, 250, btnWidth, btnHeight,
                () -> model.setGstate(State.TWO_PLAYING));
        resumeButton.setImgButton("/Continue.png");
        resumeButton.setImgHoverButton("/ContinueHover.png");

        exitButton = new Button(centerX - btnWidth / 2, 390, btnWidth, btnHeight,
                new ChangeStateCmd(model, State.MENU));
        exitButton.setImgButton("/Exit.png");
        exitButton.setImgHoverButton("/ExitHover.png");

        buttons.add(resumeButton);
        buttons.add(exitButton);
    }

    @Override
    public void draw(GraphicsContext gc, GameplayModel gameplayModel) {
        drawOverlay(gc, true);
    }

    public void drawOverlay(GraphicsContext gc, boolean isTwoPlayer) {
        double canvasWidth = UltilityValues.canvasWidth * (isTwoPlayer ? 2 : 1);
        double canvasHeight = UltilityValues.canvasHeight;
        double centerX = canvasWidth / 2;

        gc.save();

        gc.setGlobalAlpha(0.6);
        gc.setFill(Color.BLACK);
        gc.fillRect(0, 0, canvasWidth, canvasHeight);

        gc.setGlobalAlpha(1.0);

        gc.setFill(Color.WHITE);
        gc.setFont(Font.font("Consolas", FontWeight.BOLD, 50));
        gc.fillText("PAUSED", centerX - 80, 180);

        resumeButton.setX(centerX - resumeButton.getWidth() / 2);
        exitButton.setX(centerX - exitButton.getWidth() / 2);

        resumeButton.draw(gc);
        exitButton.draw(gc);

        gc.restore();
    }

    @Override
    public void checkHover(MouseEvent e) {
        resumeButton.setHovering(e);
        exitButton.setHovering(e);
    }
}
