package View;

import Model.Button;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;
import javafx.scene.input.MouseEvent;
import javafx.scene.paint.Color;

public class PauseOverlayView {
    private Button menu;
    private Button save;
    private Image pause;

    public PauseOverlayView(double canvasWidth, double canvasHeight) {
        // Nút Resume (góc phải)
        menu = new Button(canvasWidth - 300, 200, 150, 60);
        menu.setImgButton("/Start.png");
        menu.setImgHoverButton("/StartHover.png"); //Dùng tạm

        // Nút Save (góc phải dưới)
        save = new Button(canvasWidth - 300, 300, 150, 60);
        save.setImgButton("/Exit.png");  //lấy tạm
        save.setImgHoverButton("/ExitHover.png"); //lấy tạm bao giờ nhờ tiến vẽ

        pause = new Image("/Setting.png");  //Lấy tạm
    }

    public void draw(GraphicsContext gc, double canvasWidth, double canvasHeight) {
        // Overlay bán trong suốt
        gc.setFill(new Color(0, 0, 0, 0.7));
        gc.fillRect(0, 0, canvasWidth, canvasHeight);

        // Icon pause lớn giữa màn hình
        gc.drawImage(pause, canvasWidth/2 - 50, canvasHeight/2 - 50, 100, 100);

        // Nút
        menu.draw(gc);
        save.draw(gc);
    }

    public void checkHover(MouseEvent e) {
        menu.setHovering(e);
        save.setHovering(e);
    }

    public boolean menuClicked(MouseEvent e) {
        return menu.isClicked(e);
    }

    public boolean saveClicked(MouseEvent e) {
        return save.isClicked(e);
    }
}