package View;

import Controller.ChangeStateCmd;
import Controller.ExitCmd;
import Model.Button;
import Model.GameModel;
import Model.GameplayModel;
import Model.State;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;
import javafx.scene.input.MouseEvent;

public class PlayModeScene extends View implements SceneActions {
    private Image background;
    Button One_Player;
    Button Two_Player;

    public PlayModeScene(GameModel model) {
        super(model);

        background = new javafx.scene.image.Image(getClass().getResource("/bg.png").toExternalForm());

        One_Player = new Button(225.6, 377, 148.8, 65.6, new ChangeStateCmd(model, State.PLAYING));
        One_Player.setImgButton("/p1.png");
        One_Player.setImgHoverButton("/p1.png");

        Two_Player = new Button(225.6, 462.4, 148.8, 65.6, new ChangeStateCmd(model, State.PLAYING));
        Two_Player.setImgButton("/p2.png");
        Two_Player.setImgHoverButton("/p2.png");

        buttons.add(One_Player);
        buttons.add(Two_Player);
    }

    @Override
    public void draw(GraphicsContext render, GameplayModel gameplayModel) {
        render.drawImage(background, 0, 0, 600, 650);
        One_Player.draw(render);
        Two_Player.draw(render);
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
