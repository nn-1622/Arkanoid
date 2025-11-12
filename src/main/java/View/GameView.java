package View;

import Model.GameModel;
import Model.State;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;

import java.util.ArrayList;

/**
 * Lớp View chính, chịu trách nhiệm quản lý và hiển thị các màn hình khác nhau của trò chơi.
 * Lớp này hoạt động như một bộ điều phối, quyết định màn hình nào (menu, game, cài đặt, v.v.)
 * sẽ được vẽ lên canvas dựa trên trạng thái hiện tại của {@link GameModel}.
 */
public class GameView {
    private Group root;
    private Scene scene;
    private GraphicsContext gc;
    private Canvas canvas;

    /**
     * Khởi tạo GameView.
     * Thiết lập các thành phần cơ bản của JavaFX như Scene, Group, Canvas và GraphicsContext.
     * Đồng thời, khởi tạo tất cả các đối tượng view con (sub-views) cho các màn hình khác nhau.
     */
    public GameView(GameModel gameModel) {
        root = new Group();
        scene = new Scene(root, 600,650);
        canvas = new Canvas(600,650);
        root.getChildren().add(canvas);
        gc  = canvas.getGraphicsContext2D();
    }

    public Scene getScene() {
        return scene;
    }

    public javafx.scene.Scene getScene_center() {
        return scene;
    }


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
            ((javafx.stage.Stage) scene.getWindow()).centerOnScreen();
        }

    }

    /**
     * Phương thức kết xuất (render) chính, được gọi liên tục trong vòng lặp game.
     * Dựa vào trạng thái (State) từ GameModel, phương thức này gọi phương thức vẽ
     * tương ứng từ các đối tượng view con.
     * @param model Đối tượng GameModel chứa trạng thái hiện tại của game.
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



}