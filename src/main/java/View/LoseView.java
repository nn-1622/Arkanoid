package View;

import Model.Button;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;
import javafx.scene.input.MouseEvent;

public class LoseView {
    private Image lose = new Image("/lose.jpg");
    private Button replay;
    private Button menu;
    public LoseView() {
        replay = new Button(225.6,378.4, 148.8, 65.6);
        menu = new Button(225.6,475.4, 148.8, 65.6);

        replay.setImgButton("/Start.png");
        replay.setImgHoverButton("/StartHover.png");
        menu.setImgButton("/Exit.png");
        menu.setImgHoverButton("/ExitHover.png");
    }
    public void drawLoseScene(GraphicsContext gc) {
        gc.drawImage(lose, 0, 0,600,650);
        replay.draw(gc);
        menu.draw(gc);
    }
    public void checkHover(MouseEvent e){
        replay.setHovering(e);
        menu.setHovering(e);
    }
    public boolean checkClickReplay(MouseEvent e){
        return replay.isClicked(e);
    }
    public boolean checkClickMenu(MouseEvent e){
        return menu.isClicked(e);
    }
}
