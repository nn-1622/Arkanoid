package View;

import Model.GameModel;
import Model.GameplayModel;
import Model.UltilityValues;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;

public class TwoPlayerView extends View {
    private final GameplayView subView;

    public TwoPlayerView(GameModel model) {
        super(model);
        // Dùng lại GameplayView để vẽ từng nửa
        this.subView = new GameplayView(model);
    }

    @Override
    public void draw(GraphicsContext gc, GameplayModel ignore) {
        // Lấy 2 phiên chơi
        GameplayModel left = model.getLeftGame();
        GameplayModel right = model.getRightGame();

        // Vẽ nền chung (nếu muốn)
        gc.setFill(Color.BLACK);
        gc.fillRect(0, 0, UltilityValues.canvasWidth * 2, UltilityValues.canvasHeight);

        // ====== Vẽ nửa bên trái ======
        if (left != null) {
            subView.draw(gc, left);
        }

        // ====== Vẽ đường chia đôi ======
        gc.setStroke(Color.GRAY);
        gc.setLineWidth(2);
        gc.strokeLine(UltilityValues.canvasWidth, 0,
                UltilityValues.canvasWidth, UltilityValues.canvasHeight);

        // ====== Vẽ nửa bên phải ======
        if (right != null) {
            gc.save();
            gc.translate(UltilityValues.canvasWidth, 0);
            subView.draw(gc, right);
            gc.restore();
        }
    }
}
