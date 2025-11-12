package View;

import Controller.ChangeStateCmd;
import Model.Button;
import Model.GameModel;
import Model.GameplayModel;
import Model.State;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;
import javafx.scene.input.MouseEvent;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;

/**
 * Lớp hiển thị LoseView của 1P
 */
public class LoseView extends View {
    private final Image lose = new Image("/lose.png");
    private final Button replay;
    private final Button menu;

    /**
     * Hàm khởi tạo.
     *
     * @param model model gốc của game
     */
    public LoseView(GameModel model) {
        super(model);

        replay = new Button(139.3, 428.5, 321.4, 80.3, new ChangeStateCmd(model, State.PLAYING));
        menu = new Button(194.5, 508.9, 211, 81.1, new ChangeStateCmd(model, State.MENU));

        replay.setImgButton("/Replay.png"); // Sử dụng lại ảnh nút Start cho Replay
        replay.setImgHoverButton("/ReplayHover.png");
        menu.setImgButton("/Exit.png"); // Sử dụng lại ảnh nút Exit cho Menu
        menu.setImgHoverButton("/ExitHover.png");

        buttons.add(replay);
        buttons.add(menu);
    }

    @Override
    public void draw(GraphicsContext gc, GameplayModel gameplayModel) {
        gc.drawImage(lose, 0, 0, 600, 650);
        replay.draw(gc);
        menu.draw(gc);

        if (gameplayModel != null) {
            int score = gameplayModel.getScore();
            gc.setFill(Color.web("#E7A3E7"));
            gc.setFont(Font.font("Consolas", FontWeight.BOLD, 100));
            gc.fillText("" + score, 350, 370);
        }
    }

    @Override
    public void checkHover(MouseEvent e) {
        replay.setHovering(e);
        menu.setHovering(e);
    }
}