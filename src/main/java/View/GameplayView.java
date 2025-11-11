package View;

import java.util.ArrayList;

import Model.*;
import com.sun.scenario.effect.impl.prism.ps.PPSBlend_ADDPeer;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;

/**
 * Lớp chịu trách nhiệm hiển thị (vẽ) tất cả các thành phần trong màn hình chơi game chính.
 * Lớp này lấy dữ liệu từ {@link GameplayModel} và sử dụng {@link GraphicsContext} để
 * vẽ các đối tượng như thanh trượt, bóng, gạch và giao diện người dùng (UI).
 */
public class GameplayView extends View {

    public GameplayView(GameModel model) {
        super(model);
    }

    /**
     * Hình ảnh được sử dụng để hiển thị thanh máu/mạng sống của người chơi.
     */
    public Image healthbar = new Image(getClass().getResource("/healthbar.png").toExternalForm());
    public Image background = new Image(getClass().getResource("/GameBG.png").toExternalForm());

    /**
     * Vẽ toàn bộ màn hình chơi game.
     * Phương thức này sẽ vẽ nền, thanh trượt, bóng, tất cả các viên gạch, và giao diện người dùng.
     * @param gc    Đối tượng GraphicsContext được sử dụng để thực hiện các thao tác vẽ trên canvas.
     * @param model Đối tượng GameplayModel chứa tất cả dữ liệu và trạng thái của màn chơi cần vẽ.
     */
    public void draw(GraphicsContext gc, GameplayModel model) {

        gc.drawImage(background, 0, 0, 600, 650);
        
        // Vẽ các đối tượng trong game
        model.getPaddle().draw(gc);
        if (model.getLives() <= 0) {
            gc.setGlobalAlpha(0.5);
        }

        drawUI(gc, model);
        ArrayList<Brick> brickMap = model.getBricks();

        for (Brick brick : brickMap) {
            brick.draw(gc);
        }

        ArrayList<MovableObject> pu = model.getFallingPowerUps();
        for (MovableObject x : pu) {
            x.draw(gc);
        }

        ArrayList<LaserShot> laser = model.getLasers();
        for (LaserShot x : laser) {
            x.draw(gc);
        }

        ArrayList<Ball> balls = model.getBalls();
        for (Ball x : balls) {
            x.draw(gc);
        }

        model.drawActivePowerUps(gc);
        model.drawEffects(gc);


    }

    /**
     * Vẽ các thành phần của giao diện người dùng (UI) lên màn hình.
     * Bao gồm điểm số, thanh máu và bộ đếm combo (streak).
     * @param gc    Đối tượng GraphicsContext được sử dụng để vẽ.
     * @param model Đối tượng GameplayModel để lấy dữ liệu UI như điểm, mạng sống, và combo.
     */
    public void drawUI(GraphicsContext gc, GameplayModel model) {
        double w = UltilityValues.canvasWidth;
        double h = UltilityValues.canvasHeight;

        // Ghi điểm & combo ở góc phải mỗi nửa
        double scoreX = w - 170;
        double scoreY = h - 30;

        gc.setFont(Font.font("Consolas", FontWeight.BOLD, 24));
        gc.setFill(Color.WHITE);
        gc.fillText("Score: " + model.getScore(), scoreX, scoreY);

        // Thanh máu
        gc.drawImage(healthbar, 0, 0, 40 * model.getLives(), 40,
                23.7, 600, 40 * model.getLives(), 40);

        // Combo
        if (model.getCombo() > 1) {
            double comboX = scoreX;
            double comboY = scoreY - 30;
            gc.setFont(Font.font("Consolas", FontWeight.BOLD, 24));
            gc.setFill(Color.YELLOW);
            gc.fillText("Streak: x" + model.getCombo(), comboX, comboY);
        }
    }

}