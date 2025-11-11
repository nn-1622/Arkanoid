package View;

import Controller.AdjustVolumeCmd;
import Controller.ChangeStateCmd;
import Controller.SoundManager;
import Model.Button;
import Model.GameModel;
import Model.GameplayModel;
import Model.State;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;


public class VolumeView extends View {
    private Button exitButton;
    private Button lowVolume;
    private Button highVolume;
    private Image settingBg;

    public VolumeView(GameModel model) {
        super(model);
        settingBg = new Image(getClass().getResource("/settingBg.png").toExternalForm());

        exitButton = new Button( 200.1, 523.8, 199.8, 41.9, new ChangeStateCmd(model, State.SETTING));
        exitButton.setImgButton("/Exit.png");
        exitButton.setImgHoverButton("/ExitHover.png");

        lowVolume = new Button( 132.4, 347.3 , 60, 60, new AdjustVolumeCmd(false));
        lowVolume.setImgButton("/left.png");
        lowVolume.setImgHoverButton("/leftHover.png");

        highVolume = new Button(407.1, 347.3, 60, 60, new AdjustVolumeCmd(true));
        highVolume.setImgButton("/right.png");
        highVolume.setImgHoverButton("/rightHover.png");

        buttons.add(lowVolume);
        buttons.add(highVolume);
        buttons.add(exitButton);
    }

    @Override
    public void draw(GraphicsContext render, GameplayModel gameplayModel) {
        render.drawImage(settingBg, 0, 0, 600, 650);

        int totalBlocks = SoundManager.TOTAL_VOLUME_STEPS;
        int activeBlocks = SoundManager.getCurrentVolumeSteps();

        double blockWidth = 50;
        double blockHeight = 40;
        double spacing = 10;
        double totalBarWidth = (totalBlocks * blockWidth) + ((totalBlocks - 1) * spacing);
        double startX = (600 - totalBarWidth) / 2;
        double startY = 250;

        for (int i = 0; i < totalBlocks; i++) {
            double currentX = startX + i * (blockWidth + spacing);

            if (i < activeBlocks) {
                render.setFill(Color.CYAN);
                render.fillRoundRect(currentX, startY, blockWidth, blockHeight, 10, 10);
            } else {
                render.setFill(Color.rgb(100, 100, 100, 0.5));
                render.fillRoundRect(currentX, startY, blockWidth, blockHeight, 10, 10);
            }
        }

        exitButton.draw(render);
        lowVolume.draw(render);
        highVolume.draw(render);
    }
}