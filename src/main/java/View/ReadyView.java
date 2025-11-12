package View;

import Model.GameModel;
import Model.GameplayModel;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.text.TextAlignment;

public class ReadyView extends View {

    public ReadyView(GameModel model) {
        super(model);
    }

    @Override
    public void draw(GraphicsContext gc, GameplayModel gameplayModel) {
        gc.setFill(Color.rgb(0, 0, 0, 0.7));
        gc.fillRect(0, 0, 600, 650);

        gc.setFill(Color.WHITE);
        gc.setFont(Font.font("Consolas", FontWeight.BOLD, 50));
        gc.setTextAlign(TextAlignment.CENTER); // Căn giữa
        gc.fillText("GAME LOADED", 300, 250);

        gc.setFont(Font.font("Consolas", FontWeight.NORMAL, 24));

        long time = System.nanoTime();
        double opacity = (time % 1_000_000_000 < 500_000_000) ? 1.0 : 0.5;
        gc.setFill(Color.rgb(255, 255, 255, opacity));

        gc.fillText("Nhấn [A] hoặc [D] để bắt đầu", 300, 350);

        gc.setTextAlign(TextAlignment.LEFT);
    }
}