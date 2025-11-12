package View;

import Model.GameModel;
import Model.GameplayModel;
import Model.UltilityValues;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.TextAlignment;

/**
 * Lớp hiển trị chế độ hai người chơi.
 */
public class TwoPlayerView extends View {
    private final GameplayView subView;

    /**
     * Hàm khởi tạo màn Hai người chơi.
     *
     * @param model model gốc
     */
    public TwoPlayerView(GameModel model) {
        super(model);
        this.subView = new GameplayView(model);
    }

    /**
     * Hàm vẽ màn hình đợi người chơi khác hoàn thành màn.
     *
     * @param gc chuyển ngữ cảnh
     * @param text Người chơi số...
     */
    private void drawWaitingOverlay(GraphicsContext gc, String text) {
        gc.save();
        gc.setGlobalAlpha(0.6);
        gc.setFill(Color.BLACK);
        gc.fillRect(0, 0, UltilityValues.canvasWidth, UltilityValues.canvasHeight);
        gc.setGlobalAlpha(1.0);
        gc.setFill(Color.WHITE);
        gc.setFont(new Font("Arial", 28));
        gc.setTextAlign(TextAlignment.CENTER);
        gc.fillText(text,
                UltilityValues.canvasWidth / 2.0,
                UltilityValues.canvasHeight / 2.0);
        gc.restore();
    }

    /**
     * Hàm hiển thị kết quả chơi.
     *
     * @param gc biến chuyển ngữ cảnh
     * @param text Người chơi số...
     * @param color màu bên của người chơi
     * @param score điểm số của người chơi
     */
    private void drawResultOverlay(GraphicsContext gc, String text, Color color, int score) {
        gc.save();
        gc.setGlobalAlpha(0.85);
        gc.setFill(Color.rgb(0, 0, 0, 0.85));
        gc.fillRect(0, 0, UltilityValues.canvasWidth, UltilityValues.canvasHeight);
        gc.setGlobalAlpha(1.0);

        gc.setFill(color);
        gc.setFont(new Font("Arial", 48));
        gc.setTextAlign(TextAlignment.CENTER);
        gc.fillText(text,
                UltilityValues.canvasWidth / 2.0,
                UltilityValues.canvasHeight / 2.0 - 40);

        gc.setFont(new Font("Arial", 24));
        gc.setFill(Color.WHITE);
        gc.fillText("Final Score: " + score,
                UltilityValues.canvasWidth / 2.0,
                UltilityValues.canvasHeight / 2.0 + 5);

        gc.setFont(new Font("Arial", 18));
        gc.setFill(Color.LIGHTGRAY);
        gc.fillText("Press ENTER to replay  |  ESC to return to Menu",
                UltilityValues.canvasWidth / 2.0,
                UltilityValues.canvasHeight / 2.0 + 50);

        gc.restore();
    }

    @Override
    public void draw(GraphicsContext gc, GameplayModel ignore) {
        GameplayModel left = model.getLeftGame();
        GameplayModel right = model.getRightGame();

        gc.setFill(Color.BLACK);
        gc.fillRect(0, 0, UltilityValues.canvasWidth * 2, UltilityValues.canvasHeight);

        if (left != null) {
            gc.save();
            subView.draw(gc, left);

            if (left.isFading()) {
                double fadeTime = 2.0;
                double elapsed = (System.nanoTime() - left.getFadeStartTime()) / 1_000_000_000.0;
                double opacity = Math.min(1.0, elapsed / fadeTime);
                gc.setFill(new Color(0, 0, 0, opacity));
                gc.fillRect(0, 0, UltilityValues.canvasWidth, UltilityValues.canvasHeight);
            }

            if (left.isWaitingForOtherPlayer() && !left.isWinner() && !left.isLoser() && !left.isDraw()) {
                drawWaitingOverlay(gc, "WAITING FOR PLAYER 2...");
            }

            if (left.isWinner()) {
                drawResultOverlay(gc, "PLAYER 1 WINS!", Color.LIMEGREEN, left.getScore());
            } else if (left.isLoser()) {
                drawResultOverlay(gc, "PLAYER 1 LOSES!", Color.RED, left.getScore());
            } else if (left.isDraw()) {
                drawResultOverlay(gc, "DRAW!", Color.GOLD, left.getScore());
            }

            gc.restore();
        }

        gc.setStroke(Color.GRAY);
        gc.setLineWidth(3);
        gc.strokeLine(UltilityValues.canvasWidth, 0, UltilityValues.canvasWidth, UltilityValues.canvasHeight);

        if (right != null) {
            gc.save();
            gc.translate(UltilityValues.canvasWidth, 0);
            subView.draw(gc, right);

            if (right.isFading()) {
                double fadeTime = 2.0;
                double elapsed = (System.nanoTime() - right.getFadeStartTime()) / 1_000_000_000.0;
                double opacity = Math.min(1.0, elapsed / fadeTime);
                gc.setFill(new Color(0, 0, 0, opacity));
                gc.fillRect(0, 0, UltilityValues.canvasWidth, UltilityValues.canvasHeight);
            }

            if (right.isWaitingForOtherPlayer() && !right.isWinner() && !right.isLoser() && !right.isDraw()) {
                drawWaitingOverlay(gc, "WAITING FOR PLAYER 1...");
            }

            if (right.isWinner()) {
                drawResultOverlay(gc, "PLAYER 2 WINS!", Color.LIMEGREEN, right.getScore());
            } else if (right.isLoser()) {
                drawResultOverlay(gc, "PLAYER 2 LOSES!", Color.RED, right.getScore());
            } else if (right.isDraw()) {
                drawResultOverlay(gc, "DRAW!", Color.GOLD, right.getScore());
            }
            gc.restore();
        }
    }
}
