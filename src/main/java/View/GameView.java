package View;

import Model.GameModel;
import Model.State;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;

/**
 * Lớp xử lí hiển thị game.
 */
public class GameView extends View {
    private final Scene scene;
    private final GraphicsContext gc;
    private final Canvas canvas;

    /**
     * Hàm khởi tạo.
     *
     * @param gameModel gameModel gốc của game
     */
    public GameView(GameModel gameModel) {
        super(gameModel);
        Group root = new Group();
        scene = new Scene(root, 600, 650);
        canvas = new Canvas(600, 650);
        root.getChildren().add(canvas);
        gc = canvas.getGraphicsContext2D();
    }

    /**
     * Hàm chuyển kích thước màn hình sang các chế độ của game.
     *
     * @param s trạng thái game
     */
    public void ensureSizeForState(State s) {
        double targetW = (s == State.TWO_PLAYING || s == State.TWO_PLAYER_PAUSED) ? 1200 : 600;
        double targetH = 650;

        if (canvas.getWidth() != targetW || canvas.getHeight() != targetH) {
            canvas.setWidth(targetW);
            canvas.setHeight(targetH);

            if (scene.getWindow() != null) {
                scene.getWindow().setWidth(targetW + 16);
                scene.getWindow().setHeight(targetH + 39);
            }
        }

        if (scene.getWindow() != null) {
            scene.getWindow().setWidth(targetW + 16);
            scene.getWindow().setHeight(targetH + 39);
            scene.getWindow().centerOnScreen();
        }
    }

    /**
     * Hàm vẽ màn hình của game.
     *
     * @param model model gốc của game
     */
    public void render(GameModel model) {
        ensureSizeForState(model.getGstate());
        State currentState = model.getGstate();

        if (model.getGstate() == State.FADE) {
            final double fadeTime = 15.0;
            double timeElapsed = (System.nanoTime() - model.getFadeStartTime()) / 1_000_000_000.0;
            double opacity = Math.min(1.0, (timeElapsed / fadeTime));

            gc.setFill(new Color(0, 0, 0, opacity));
            gc.fillRect(0, 0, canvas.getWidth(), canvas.getHeight());
            return;
        }
        if (model.getGstate() == State.PAUSED || model.getGstate() == State.READY_TO_PLAY || model.getGstate() == State.TWO_PLAYER_PAUSED) {
            View backgroundView = model.getView(State.TWO_PLAYING);
            if (backgroundView != null && model.getGameplayModel() != null) {
                backgroundView.draw(gc, model.getGameplayModel());
            }
            View overlayView = model.getCurrentView();
            if (overlayView != null) {
                overlayView.draw(gc, model.getGameplayModel()); // Dùng model 1P
            }
            return;
        }

        if (currentState == State.TWO_PLAYER_PAUSED) {
            View backgroundView = model.getView(State.TWO_PLAYING);
            if (backgroundView != null) {
                backgroundView.draw(gc, null);
            }

            View overlayView = model.getCurrentView();
            if (overlayView != null) {
                overlayView.draw(gc, null);
            }
            return;
        }

        try {
            if (model.getCurrentView() != null) {
                model.getCurrentView().draw(gc, model.getGameplayModel());
            } else if (model.getGameplayModel() != null) {
                new GameplayView(model).draw(gc, model.getGameplayModel());
            } else {
                gc.setFill(Color.BLACK);
                gc.fillRect(0, 0, canvas.getWidth(), canvas.getHeight());
            }
        } catch (Exception e) {
            System.err.println("[Render Warning] " + e.getMessage());
            e.printStackTrace();
            gc.setFill(Color.BLACK);
            gc.fillRect(0, 0, canvas.getWidth(), canvas.getHeight());
        }
    }

    public Scene getScene() {
        return scene;
    }

}