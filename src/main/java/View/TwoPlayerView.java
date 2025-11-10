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
        GameplayModel left = model.getLeftGame();
        GameplayModel right = model.getRightGame();

        // nền chung
        gc.setFill(Color.BLACK);
        gc.fillRect(0, 0,
                UltilityValues.canvasWidth * 2,
                UltilityValues.canvasHeight);

        // ====== LEFT HALF ======
        if (left != null) {
            gc.save();
            // không translate, vẽ từ (0,0)
            subView.draw(gc, left);

            if (left.isFading()) {
                double fadeTime = 2.0;
                double elapsed = (System.nanoTime() - left.getFadeStartTime()) / 1_000_000_000.0;
                double opacity = Math.min(1.0, elapsed / fadeTime);

                gc.setFill(new Color(0, 0, 0, opacity));
                gc.fillRect(0, 0,
                        UltilityValues.canvasWidth,
                        UltilityValues.canvasHeight);
            }

            gc.restore();
        }

        // ====== DIVIDER ======
        gc.setStroke(Color.GRAY);
        gc.setLineWidth(2);
        gc.strokeLine(UltilityValues.canvasWidth, 0,
                UltilityValues.canvasWidth, UltilityValues.canvasHeight);

        // ====== RIGHT HALF ======
        if (right != null) {
            gc.save();
            gc.translate(UltilityValues.canvasWidth, 0);

            subView.draw(gc, right);

            if (right.isFading()) {
                double fadeTime = 2.0;
                double elapsed = (System.nanoTime() - right.getFadeStartTime()) / 1_000_000_000.0;
                double opacity = Math.min(1.0, elapsed / fadeTime);

                gc.setFill(new Color(0, 0, 0, opacity));
                gc.fillRect(0, 0,
                        UltilityValues.canvasWidth,
                        UltilityValues.canvasHeight);
            }

            gc.restore();
        }
    }
}
