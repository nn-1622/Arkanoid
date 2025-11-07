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

import java.util.Objects;

public class PlayModeScene extends View implements SceneActions {
    private Image background;
    Button One_Player;
    Button Two_Player;

    public PlayModeScene(GameModel model) {
        super(model);

        background = new javafx.scene.image.Image(Objects.requireNonNull(getClass().getResource("/bg2.png")).toExternalForm());

        One_Player = new Button(149.2, 169.8, 301.6, 75.4, new ChangeStateCmd(model, State.PLAYING));
        One_Player.setImgButton("/1Player.png");
        One_Player.setImgHoverButton("/1PlayerHover.png");

        Two_Player = new Button(153.3, 288.3, 293.5, 73.4, new ChangeStateCmd(model, State.PLAYING));
        Two_Player.setImgButton("/2Player.png");
        Two_Player.setImgHoverButton("/2PlayerHover.png");

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
