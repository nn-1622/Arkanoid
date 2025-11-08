package View;

import Model.GameModel;
import Model.State;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;

/**
 * Lớp View chính, chịu trách nhiệm quản lý và hiển thị các màn hình khác nhau của trò chơi.
 * Lớp này hoạt động như một bộ điều phối, quyết định màn hình nào (menu, game, cài đặt, v.v.)
 * sẽ được vẽ lên canvas dựa trên trạng thái hiện tại của {@link GameModel}.
 */
public class GameView {
    private Group root;
    private Scene scene;
    private MenuScene menuScene;
    private GameModel gameModel;
    private SettingScene settingScene;
    private GameplayView gameplayView;
    private LoseView loseView;
    private VictoryView victoryView;
    private GraphicsContext gc;
    private Canvas canvas;
    private HighScoreView highScoreView; //update highscore view
    private PauseOverlayView pauseOverlayView; //update pause overlay
    private ContinueOverlayView continueOverlayView; //update continue overlay

    /**
     * Khởi tạo GameView.
     * Thiết lập các thành phần cơ bản của JavaFX như Scene, Group, Canvas và GraphicsContext.
     * Đồng thời, khởi tạo tất cả các đối tượng view con (sub-views) cho các màn hình khác nhau.
     */
    public GameView() {
        root = new Group();
        scene = new Scene(root, 600,650);
        canvas = new Canvas(600,650);
        root.getChildren().add(canvas);
        gc  = canvas.getGraphicsContext2D();

        menuScene = new MenuScene(canvas.getWidth(),canvas.getHeight());
        settingScene = new SettingScene(canvas.getWidth(),canvas.getHeight());
        gameplayView = new GameplayView();
        loseView = new LoseView();
        victoryView = new VictoryView();
        highScoreView = new HighScoreView();
        pauseOverlayView = new PauseOverlayView(canvas.getWidth(), canvas.getHeight());
        continueOverlayView = new ContinueOverlayView(canvas.getWidth(), canvas.getHeight());
    }

    /**
     * Lấy đối tượng Scene chính của ứng dụng.
     * @return Scene chính.
     */
    public Scene getScene() {
        return scene;
    }

    /**
     * Phương thức kết xuất (render) chính, được gọi liên tục trong vòng lặp game.
     * Dựa vào trạng thái (State) từ GameModel, phương thức này gọi phương thức vẽ
     * tương ứng từ các đối tượng view con.
     * @param model Đối tượng GameModel chứa trạng thái hiện tại của game.
     */
    public void render(GameModel model) {
        if(model.getGstate() == State.MENU){
            menuScene.drawMenuScene(gc);
        } else if(model.getGstate() == State.SETTING){
            settingScene.drawSettingScene(gc);
        } else if(model.getGstate() == State.PLAYING){
            gameplayView.drawGameScene(gc, model.getGameplayModel());
        } else if(model.getGstate() == State.LOSS){
            //Update truyền điểm hiện tại vào LoseView để kiểm tra kỷ lục mới
            loseView.setScore(model.getGameplayModel().getScore());
            loseView.drawLoseScene(gc);
        } else if(model.getGstate() == State.VICTORY){
            victoryView.drawWinScene(gc);
        } else if(model.getGstate() == State.FADE){
            // Xử lý hiệu ứng chuyển cảnh mờ dần
            final double fadeTime = 2.0;
            double timeElapsed = (System.nanoTime() - model.getFadeStartTime()) / 1_000_000_000.0;
            double opacity = Math.min(1.0, (timeElapsed / fadeTime));
            gc.setFill(new Color(0,0,0,opacity));
            gc.fillRect(0,0, canvas.getWidth(), canvas.getHeight());
        }
        //Update thêm xử lý highscoreview
        else if(model.getGstate() == State.HIGHSCORE){
            highScoreView.drawHighScoreScene(gc);
        }
        //Update thêm phần xử lý pauseOverlay
        else if(model.getGstate() == State.PAUSED) {
            gameplayView.drawGameScene(gc, model.getGameplayModel());
            pauseOverlayView.draw(gc, canvas.getWidth(), canvas.getHeight());
        }
        //Update thêm phần xử lý continueOverlay view
        else if(model.getGstate() == State.CONTINUE_SCREEN) {
            System.out.println("vẽ CONTINUE_SCREEN"); //DEBUG
            menuScene.drawMenuScene(gc);
            continueOverlayView.draw(gc, canvas.getWidth(), canvas.getHeight());
        }
    }

    /**
     * Lấy đối tượng highscoreview từ màn hình loseview
     * @return màn hình highscoreview
     */
    public HighScoreView getHighScoreView() {
        return highScoreView;
    }

    /**
     * Lấy đối tượng view của màn hình menu.
     * @return Đối tượng MenuScene.
     */
    public MenuScene getMenuScene() {
        return menuScene;
    }

    /**
     * Lấy đối tượng view của màn hình cài đặt.
     * @return Đối tượng SettingScene.
     */
    public SettingScene getSettingScene() {
        return settingScene;
    }

    /**
     * Lấy đối tượng view của màn hình chơi game.
     * @return Đối tượng GameplayView.
     */
    public GameplayView getGameScene() {
        return gameplayView;
    }

    /**
     * Lấy đối tượng view của màn hình thua cuộc.
     * @return Đối tượng LoseView.
     */
    public LoseView getLoseScene() {
        return loseView;
    }

    /**
     * Lấy đối tượng view của màn hình chiến thắng.
     * @return Đối tượng VictoryView.
     */
    public  VictoryView getVictoryScene() {
        return victoryView;
    }

    //Update getter pauseview
    /**
     * Lấy đối tượng paused overlay view
     * @return pauseoverlayview
     */
    public PauseOverlayView getPauseOverlayView() {
        return pauseOverlayView;
    }
    /**
     * Lấy đối tượng Continue Overlay
     * @return đối tượng continueoverlay
     */
    public ContinueOverlayView getContinueOverlayView() {
        return continueOverlayView;
    }


}