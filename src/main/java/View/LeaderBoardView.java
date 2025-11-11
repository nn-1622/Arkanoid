package View;

import Controller.ChangeStateCmd;
import Model.Button;
import Model.GameModel;
import Model.GameplayModel;
import Model.State;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;
import javafx.scene.input.MouseEvent;

import java.util.Objects;

public class LeaderBoardView extends View {
    private Image background;
    Button exit;

    public  LeaderBoardView(GameModel model) {
        super(model);
        background = new Image(getClass().getResource("/LeaderBoard.png").toExternalForm());

        exit = new Button(8.8, 582, 150.1, 57.7, new ChangeStateCmd(model, State.MENU));
        exit.setImgButton("/Exit.png");
        exit.setImgHoverButton("/ExitHover.png");

        buttons.add(exit);
    }

    @Override
    public void draw(GraphicsContext render, GameplayModel gameplayModel) {
        render.drawImage(background, 0, 0, 600, 650);
        exit.draw(render);
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
