package View;

import Controller.ChangeStateCmd;
import Controller.CheckSaveNameCmd;
import Controller.ResumeGameCmd;
import Model.Button;
import Model.GameModel;
import Model.GameplayModel;
import Model.State;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;
import javafx.scene.input.MouseEvent;

import java.util.Objects;

/**
 * Lớp hiển thị Pause của 1P.
 */
public class PauseView extends View {
    Button resume;
    Button save;
    Button menu;
    Image background;

    /**
     * Hàm khởi tạo pause.
     *
     * @param model model gốc
     */
    public PauseView(GameModel model) {
        super(model);

        background = new Image(Objects.requireNonNull(getClass().getResource("/Pause.png")).toExternalForm());

        resume = new Button(152.2, 351.6, 295.6, 73.9, new ResumeGameCmd(model));
        resume.setImgButton("/Continue.png");
        resume.setImgHoverButton("/ContinueHover.png");


        save = new Button(153.3, 445.9, 293.5, 73.4, new CheckSaveNameCmd(model, this));
        save.setImgButton("/Save.png");
        save.setImgHoverButton("/SaveHover.png");

        menu = new Button(146.2, 539.3, 300.5, 75.1, new ChangeStateCmd(model, State.MENU));
        menu.setImgButton("/Menu.png");
        menu.setImgHoverButton("/MenuHover.png");

        buttons.add(resume);
        buttons.add(save);
        buttons.add(menu);
    }

    @Override
    public void draw(GraphicsContext render, GameplayModel gameplayModel) {
        render.drawImage(background, 0, 0, 600, 650);
        resume.draw(render);
        save.draw(render);
        menu.draw(render);
    }

    @Override
    public void checkHover(MouseEvent e) {
        for (Button b : buttons) {
            b.setHovering(e);
        }
    }
}