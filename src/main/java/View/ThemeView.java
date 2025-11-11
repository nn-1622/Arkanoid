package View;

import Controller.ChangeStateCmd;
import Model.Button;
import Model.GameModel;
import Model.GameplayModel;
import Model.State;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;
import javafx.scene.input.MouseEvent;

public class ThemeView extends View {
    Button background;
    Button paddle;
    Button ball;
    Button exit;
    Image backgroundIMG;

    public ThemeView(GameModel model) {
        super(model);
        backgroundIMG = new Image(getClass().getResource("/bg2.png").toExternalForm());

        paddle = new Button(139.3, 161.1, 321.4, 80.3, new ChangeStateCmd(model, State.CHOOSE_PADDLE));
        paddle.setImgButton("/Spaddle.png");
        paddle.setImgHoverButton("/SpaddleHover.png");

        ball = new Button(139.3, 270.6, 321.4, 80.3, new ChangeStateCmd(model, State.CHOOSE_BALL));
        ball.setImgHoverButton("/Sball.png");
        ball.setImgButton("/SballHover.png");

        background = new Button(139.3, 379.9, 321.4, 80.3, new ChangeStateCmd(model, State.CHOOSE_BACKGROUND));
        background.setImgButton("/Sbackground.png");
        background.setImgHoverButton("/SbackgroundHover.png");

        exit = new Button(30.5, 558.8, 162.4, 62.5, new ChangeStateCmd(model, State.SETTING));
        exit.setImgButton("/Exit.png");
        exit.setImgHoverButton("/ExitHover.png");

        buttons.add(background);
        buttons.add(paddle);
        buttons.add(ball);
        buttons.add(exit);
    }

    @Override
    public void draw(GraphicsContext render, GameplayModel gameplayModel) {
        render.drawImage(backgroundIMG, 0, 0, 600, 650);
        background.draw(render);
        ball.draw(render);
        exit.draw(render);
        paddle.draw(render);
    }

    /**
     * Kiểm tra và cập nhật trạng thái di chuột (hover) cho tất cả các nút trên menu.
     * @param e Sự kiện chuột (MouseEvent) chứa tọa độ hiện tại của con trỏ.
     */
    @Override
    public void checkHover(MouseEvent e) {
        for (Button b : buttons) {
            b.setHovering(e);
        }
    }
}
