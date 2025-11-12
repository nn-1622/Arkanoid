package View;

import java.util.ArrayList;
import java.util.Objects;

import Model.*;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;

public class GameplayView extends View {
    public GameplayView(GameModel model) {
        super(model);
    }

    public Image healthbar = new Image(Objects.requireNonNull(getClass().getResource("/healthbar.png")).toExternalForm());

    public void draw(GraphicsContext gc, GameplayModel model) {
        Image background = model.getBackground();
        gc.drawImage(background, 0, 0, 600, 650);

        model.getPaddle().draw(gc);
        drawUI(gc, model);
        ArrayList<Brick> brickMap = model.getBricks();

        for (Brick brick : brickMap) {
            brick.draw(gc);
        }

        ArrayList<MovableObject> pu = model.getFallingPowerUps();
        for (MovableObject x : pu) {
            x.draw(gc);
        }

        ArrayList<LaserShot> laser = model.getLasers();
        for (LaserShot x : laser) {
            x.draw(gc);
        }

        ArrayList<Ball> balls = model.getBalls();
        for (Ball x : balls) {
            x.draw(gc);
        }

        model.drawActivePowerUps(gc);
        model.drawEffects(gc);
    }

    public void drawUI(GraphicsContext gc, GameplayModel model) {
        double scoreX = UltilityValues.canvasWidth - 170;
        double scoreY = UltilityValues.canvasHeight - 30;

        gc.setFont(Font.font("Consolas", FontWeight.BOLD, 24));
        gc.setFill(Color.WHITE);
        gc.fillText("Score: " + model.getScore(), scoreX, scoreY);

        gc.drawImage(healthbar, 0, 0, 40 * model.getLives(), 40,
                23.7, 600, 40 * model.getLives(), 40);

        if (model.getCombo() > 1) {
            double comboX = scoreX;
            double comboY = scoreY - 30;
            gc.setFont(Font.font("Consolas", FontWeight.BOLD, 24));
            gc.setFill(Color.YELLOW);
            gc.fillText("Combo: x" + model.getCombo(), comboX, comboY);
        }
    }
}