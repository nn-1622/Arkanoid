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

public class VictoryView extends View {
    private final Image win = new Image("/win.png");
    private final Button replay;
    private final Button menu;

    public VictoryView(GameModel model) {
        super(model);
        replay = new Button(54.7, 559.3, 245.3, 61.3, new ChangeStateCmd(model, State.PLAYING));
        menu = new Button(349.7, 553.4, 190.3, 73.2, new ChangeStateCmd(model, State.MENU));

        replay.setImgButton("/Replay.png"); // Sử dụng lại ảnh nút Start cho Replay
        replay.setImgHoverButton("/ReplayHover.png");
        menu.setImgButton("/Exit.png"); // Sử dụng lại ảnh nút Exit cho Menu
        menu.setImgHoverButton("/ExitHover.png");

        buttons.add(replay);
        buttons.add(menu);
    }

    @Override
    public void draw(GraphicsContext gc, GameplayModel gameplayModel) {
        gc.drawImage(win, 0, 0, 600, 650);
        replay.draw(gc);
        menu.draw(gc);
        if (gameplayModel != null) {
            int score = gameplayModel.getScore();
            gc.setFill(Color.web("#D19C00"));
            gc.setFont(Font.font("Consolas", FontWeight.BOLD, 100));
            gc.fillText("" + score, 385, 500);
        }
    }

    @Override
    public void checkHover(MouseEvent e) {
        replay.setHovering(e);
        menu.setHovering(e);
    }
}