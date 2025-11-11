package View;

import Model.GameModel;
import Model.GameplayModel;
import Model.UltilityValues;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.TextAlignment;


public class TwoPlayerView extends View {
    private final GameplayView subView;

    public TwoPlayerView(GameModel model) {
        super(model);
        // Dùng lại GameplayView để vẽ từng nửa
        this.subView = new GameplayView(model);
    }

    private void drawWaitingOverlay(GraphicsContext gc, String text) {
        gc.save();
        gc.setGlobalAlpha(0.6);
        gc.setFill(Color.BLACK);
        gc.fillRect(0, 0,
                UltilityValues.canvasWidth,
                UltilityValues.canvasHeight);
        gc.setGlobalAlpha(1.0);

        gc.setFill(Color.WHITE);
        gc.setFont(new Font("Arial", 36));
        gc.setTextAlign(TextAlignment.CENTER);
        double time = (System.currentTimeMillis() / 500) % 2;
        if (time < 0.2) {
            gc.fillText(text,
                    UltilityValues.canvasWidth / 2,
                    UltilityValues.canvasHeight / 2);
        }
        gc.restore();
    }

    private void drawResultOverlay(GraphicsContext gc, String text, Color color, int score) {
        gc.save();
        gc.setGlobalAlpha(0.7);
        gc.setFill(Color.BLACK);
        gc.fillRect(0, 0,
                UltilityValues.canvasWidth,
                UltilityValues.canvasHeight);
        gc.setGlobalAlpha(1.0);

        gc.setFill(color);
        gc.setFont(new Font("Arial", 48));
        gc.setTextAlign(TextAlignment.CENTER);
        gc.fillText(text,
                UltilityValues.canvasWidth / 2,
                UltilityValues.canvasHeight / 2 - 30);

        gc.setFont(new Font("Arial", 24));
        gc.setFill(Color.WHITE);
        gc.fillText("Final Score: " + score,
                UltilityValues.canvasWidth / 2,
                UltilityValues.canvasHeight / 2 + 20);

        gc.setFont(new Font("Arial", 22));
        gc.setFill(Color.LIGHTGRAY);
        gc.fillText("Press ENTER or CLICK to return to Menu",
                UltilityValues.canvasWidth / 2,
                UltilityValues.canvasHeight / 2 + 60);

        gc.restore();
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

            if (left.isWaitingForOtherPlayer()) {
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

            if (right.isWaitingForOtherPlayer()) {
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
