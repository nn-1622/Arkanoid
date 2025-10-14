import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.stage.Stage;
import javafx.scene.image.Image;
import javafx.scene.input.MouseEvent;

public class MenuScene {
    private Image background;
    private Image logo;
    Button start;
    Button settings;
    Button exit;
    public MenuScene() {
        background = new Image(getClass().getResource("/bg.jpg").toExternalForm());
        logo = new Image(getClass().getResource("/logo.png").toExternalForm());

        start = new Button("Start!", 200.1, 378, 199.8, 41.9);
        start.setImgButton("/Start.png");
        start.setImgHoverButton("/StartHover.jpg");

        settings = new Button("Settings", 200.1, 450.8, 199.8, 41.9);
        settings.setImgButton("/Start.png");
        settings.setImgHoverButton("/StartHover.jpg");

        exit = new Button("Exit", 200.1, 523.8, 199.8, 41.9);
        exit.setImgButton("/Start.png");
        exit.setImgHoverButton("/StartHover.jpg");
    }
    public void drawMenuScene(GraphicsContext render) {
        render.drawImage(background, 0, 0, 600, 650);
        render.drawImage(logo, 144.7, 84.2, 310.6, 200.1);
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
}
