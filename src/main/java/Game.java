import Controller.GameController;
import Controller.GameExecutor;
import Controller.SoundManager;
import Model.GameModel;
import Model.State;
import View.GameView;
import javafx.application.Application;
import javafx.stage.Stage;
import javafx.animation.AnimationTimer;

/**
 * Lớp chính của ứng dụng trò chơi Arkanoid.
 * Lớp này là điểm khởi đầu (entry point) cho ứng dụng JavaFX.
 * Nó chịu trách nhiệm khởi tạo các thành phần cốt lõi theo mô hình MVC (Model-View-Controller),
 * thiết lập cửa sổ chính và bắt đầu vòng lặp game.
 */
public class Game extends Application {
    private GameController controller;

    private SoundManager soundManager;
    private GameModel model;
    private GameView view;
    public static State gstate;

    /**
     * Phương thức khởi đầu chính cho tất cả các ứng dụng JavaFX.
     * Phương thức này được gọi sau khi phương thức init() trả về, và sau khi hệ thống
     * đã sẵn sàng để ứng dụng bắt đầu chạy.
     *
     * @param stage Cửa sổ chính (primary stage) cho ứng dụng này, nơi mà
     *              khung cảnh (scene) của ứng dụng có thể được thiết lập.
     */
    @Override
    public void start(Stage stage) {
        model = GameModel.getGameModel();
        soundManager = SoundManager.getInstance();
        view = new GameView(model);
        controller = new GameController(model, view, soundManager);

        // Thiết lập và hiển thị cửa sổ game
        stage.setScene(view.getScene());
        stage.setTitle("Arkanoid!");
        stage.setResizable(false);
        stage.show();

        // Tạo và bắt đầu vòng lặp game chính bằng AnimationTimer
        new AnimationTimer() {
            /**
             * Phương thức này được gọi trong mỗi khung hình (frame) của hoạt ảnh.
             * @param now Dấu thời gian của khung hình hiện tại tính bằng nano giây.
             */
            @Override
            public void handle(long now) {
                // Gọi phương thức cập nhật của controller trong mỗi khung hình
                controller.update(now);
            }
        } .start();
    }

    /**
     * Phương thức được gọi khi ứng dụng kết thúc.
     * Phương thức này chịu trách nhiệm dọn dẹp tài nguyên,
     * chẳng hạn như tắt các luồng worker để tránh rò rỉ tài nguyên.
     *
     * @throws Exception Nếu có lỗi xảy ra trong quá trình tắt ứng dụng.
     */
    @Override
    public void stop() throws Exception {
        // Tắt các luồng worker khi ứng dụng đóng
        GameExecutor.getInstance().shutdown();
        super.stop();
    }

    /**
     * Phương thức main, điểm khởi đầu tiêu chuẩn cho một ứng dụng Java.
     * Phương thức này gọi launch() để khởi chạy JavaFX runtime và ứng dụng.
     * @param args Các đối số dòng lệnh (không được sử dụng trong ứng dụng này).
     */
    public static void main(String[] args) {
        launch();
    }
}