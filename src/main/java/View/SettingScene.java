package View;

import Controller.AdjustVolumeCmd;
import Controller.ChangeStateCmd;
import Model.Button;
import Model.GameModel;
import Model.GameplayModel;
import Model.State;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;
import javafx.scene.input.MouseEvent;

import java.util.Objects;

public class SettingScene extends View {
    private final Image settingBg;
    private Image settings;
    private final Button exit;
    private final Button lowVolume;
    private final Button highVolume;
    private final Button theme;
    private final Button how2play;

    public SettingScene(GameModel model) {
        super(model);
        settingBg = new Image(Objects.requireNonNull(getClass().getResource("/settingBg.png")).toExternalForm());

        exit = new Button(412.4, 26.4, 175, 67.3, new ChangeStateCmd(model, State.MENU));
        exit.setImgButton("/Exit.png");
        exit.setImgHoverButton("/ExitHover.png");

        lowVolume = new Button(160, 68.7, 70, 70, new AdjustVolumeCmd(false));
        lowVolume.setImgButton("/left.png");
        lowVolume.setImgHoverButton("/leftHover.png");

        highVolume = new Button(230, 68.7, 70, 70, new AdjustVolumeCmd(true));
        highVolume.setImgButton("/right.png");
        highVolume.setImgHoverButton("/rightHover.png");

        theme = new Button(103.9, 247, 262.6, 65.7, new ChangeStateCmd(model, State.THEME));
        theme.setImgHoverButton("/Theme.png");
        theme.setImgButton("/ThemeHover.png");

        how2play = new Button(103.9, 337.6, 262.6, 65.7, new ChangeStateCmd(model, State.HOW_TO_PLAY));
        how2play.setImgButton("/Howtoplay.png");
        how2play.setImgHoverButton("/HowtoplayHover.png");

        buttons.add(lowVolume);
        buttons.add(highVolume);
        buttons.add(exit);
        buttons.add(theme);
        buttons.add(how2play);
    }

    @Override
    public void draw(GraphicsContext render, GameplayModel gameplayModel) {
        render.drawImage(settingBg, 0, 0, 600, 650);
        exit.draw(render);
        lowVolume.draw(render);
        highVolume.draw(render);
        theme.draw(render);
        how2play.draw(render);
    }

    @Override
    public void checkHover(MouseEvent e) {
        exit.setHovering(e);
        lowVolume.setHovering(e);
        highVolume.setHovering(e);
        theme.setHovering(e);
        how2play.setHovering(e);
    }
}