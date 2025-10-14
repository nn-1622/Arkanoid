import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;

import javafx.scene.input.MouseEvent;
import java.awt.image.BufferedImage;

public class SettingScene {
    private Image settingBg;
    private Image settings;
    private Button exit;

    public SettingScene(double canvasWidth, double canvasHeight) {
        settingBg = new Image(getClass().getResource("/settingBg.png").toExternalForm());

        exit = new Button( 200.1, 523.8, 199.8, 41.9);
        
        exit.setImgButton("/Start.png");
        exit.setImgHoverButton("/StartHover.png");
    }

    public void drawSettingScene(GraphicsContext render) {
        render.drawImage(settingBg,0, 0, 600, 650);
        exit.draw(render);
    }

    public void checkHover(MouseEvent e) {
        exit.setHovering(e);
    }

    public boolean exitClicked(MouseEvent e) {
        return exit.isClicked(e);
    }
}
