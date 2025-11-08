package View;

import Model.Button;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;
import javafx.scene.input.MouseEvent;
import javafx.scene.paint.Color;

public class ContinueOverlayView {
    private Button continueBtn;
    private Button newGame;
    private Image questionIcon;

    public ContinueOverlayView(double canvasWidth, double canvasHeight) {
        continueBtn = new Button(canvasWidth / 2 - 175, canvasHeight / 2 - 50, 150, 60);
        continueBtn.setImgButton("/Exit.png");   // Dùng tạm
        continueBtn.setImgHoverButton("/ExitHover.png");  // Dùng tạm

        // Nút Chơi mới (dưới)
        newGame = new Button(canvasWidth / 2 - 175, canvasHeight / 2 + 20, 150, 60);
        newGame.setImgButton("/Start.png");
        newGame.setImgHoverButton("/StartHover.png");

        questionIcon = new Image("/Setting.png");  // Tạm dùng pause.png làm icon hỏi
    }

    public void draw(GraphicsContext gc, double canvasWidth, double canvasHeight) {
        gc.setFill(new Color(0, 0, 0, 0.8));// bán trong suốt
        gc.fillRect(0, 0, canvasWidth, canvasHeight);

        gc.drawImage(questionIcon, canvasWidth/2 - 50, canvasHeight/2 - 150, 100, 100);

        gc.setFill(Color.WHITE);
        gc.setFont(javafx.scene.text.Font.font("Arial", 24));
        gc.fillText("Tiếp tục", canvasWidth/2 - 140, canvasHeight/2 - 80);

        continueBtn.draw(gc);
        newGame.draw(gc);
    }

    public void checkHover(MouseEvent e) {
        continueBtn.setHovering(e);
        newGame.setHovering(e);
    }

    public boolean continueClicked(MouseEvent e) {
        return continueBtn.isClicked(e);
    }

    public boolean newGameClicked(MouseEvent e) {
        return newGame.isClicked(e);
    }
}