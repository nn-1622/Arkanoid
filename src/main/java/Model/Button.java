package Model;

import Controller.GameCommand;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;
import javafx.scene.input.MouseEvent;

import java.util.Objects;

public class Button extends Utility {
    private Image imgButton;
    private Image imgHoverButton;
    private boolean isHover;
    private final GameCommand command;

    public Button(double x, double y, double width, double height, GameCommand command) {
        this.x = x;
        this.y = y;
        this.width = width;
        this.height = height;
        this.command = command;
    }

    public void setImgButton(String link) {
        this.imgButton = new Image(Objects.requireNonNull(getClass().getResource(link)).toExternalForm());
    }

    public void setImgHoverButton(String link) {
        this.imgHoverButton = new Image(getClass().getResource(link).toExternalForm());
    }

    public void setHovering(MouseEvent m) {
        isHover = m.getX() > x && m.getX() < x + width && m.getY() > y && m.getY() < y + height;
    }

    public boolean isClicked(MouseEvent m) {
        return m.getX() > x && m.getX() < x + width && m.getY() > y && m.getY() < y + height;
    }

    public void draw(GraphicsContext gc) {
        if (!isHover) {
            gc.drawImage(imgButton, x, y, width, height);
        } else {
            gc.drawImage(imgHoverButton, x, y, width, height);
        }
    }

    public void setHover(boolean hover) {
        isHover = hover;
    }

    public boolean isHover() {
        return isHover;
    }

    public void activate() {
        command.execute();
    }

    public GameCommand getCommand() {
        return this.command;
    }

}