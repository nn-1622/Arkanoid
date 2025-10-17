package View;

import Model.Button;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;

import javafx.scene.input.MouseEvent;

public class SettingScene {
    private Image settingBg;
    private Image settings;
    private Button exit;
    private Button lowVolume;
    private Button highVolume;

    public SettingScene(double canvasWidth, double canvasHeight) {
        settingBg = new Image(getClass().getResource("/settingBg.png").toExternalForm());

        exit = new Button( 200.1, 523.8, 199.8, 41.9);
        exit.setImgButton("/Exit.png");
        exit.setImgHoverButton("/ExitHover.png");

        lowVolume = new Button( 132.4, 347.3 , 60, 60);
        lowVolume.setImgButton("/left.png");
        lowVolume.setImgHoverButton("/leftHover.png");

        highVolume = new Button(407.1, 347.3, 60, 60);
        highVolume.setImgButton("/right.png");
        highVolume.setImgHoverButton("/rightHover.png");
    }

    public void drawSettingScene(GraphicsContext render) {
        render.drawImage(settingBg,0, 0, 600, 650);
        exit.draw(render);
        lowVolume.draw(render);
        highVolume.draw(render);
    }

    public void checkHover(MouseEvent e) {
        exit.setHovering(e);
        lowVolume.setHovering(e);
        highVolume.setHovering(e);
    }

    public boolean exitClicked(MouseEvent e) {
        return exit.isClicked(e);
    }
    public boolean lowVolumeClicked(MouseEvent e) {
        return lowVolume.isClicked(e);
    }
    public boolean highVolumeClicked(MouseEvent e) {
        return highVolume.isClicked(e);
    }
}