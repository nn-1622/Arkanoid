package View;

import Model.Button;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;
import javafx.scene.input.MouseEvent;

public class MenuScene {
    private Image background;
    Button start;
    Button settings;
    Button exit;
    public MenuScene(double canvasWidth, double canvasHeight) {
        background = new Image(getClass().getResource("/bg.jpg").toExternalForm());

        start = new Button( 225.6, 377, 148.8, 65.6);
        start.setImgButton("/Start.png");
        start.setImgHoverButton("/StartHover.png");

        settings = new Button( 225.6, 462.4, 148.8, 65.6);
        settings.setImgButton("/Setting.png");
        settings.setImgHoverButton("/SettingHover.png");

        exit = new Button( 225.6, 548, 148.8, 65.6);
        exit.setImgButton("/Exit.png");
        exit.setImgHoverButton("/ExitHover.png");
    }
    public void drawMenuScene(GraphicsContext render) {
        render.drawImage(background, 0, 0, 600, 650);
        start.draw(render);
        settings.draw(render);
        exit.draw(render);
    }
    public void checkHover(MouseEvent e) {
        start.setHovering(e);
        settings.setHovering(e);
        exit.setHovering(e);
    }
    public boolean settingClick(MouseEvent e) {
        return settings.isClicked(e);
    }
    public boolean exitClick(MouseEvent e) {
        return exit.isClicked(e);
    }
    public boolean startClick(MouseEvent e) { return start.isClicked(e); }
}
