package View;

import Controller.ChangeStateCmd;
import Model.*;
import Model.HighScoreEntry;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;
import javafx.scene.input.MouseEvent;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;

import java.io.File;
import java.io.FileInputStream;
import java.io.ObjectInputStream;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Objects;

public class LeaderBoardView extends View {
    private Image background;
    Button exit;

    private List<HighScoreEntry> topScores = new ArrayList<>();
    private static final String HIGH_SCORE_FILE = "highscores.dat";

    public LeaderBoardView(GameModel model) {
        super(model);
        background = new Image(getClass().getResource("/LeaderBoard.png").toExternalForm());

        exit = new Button(8.8, 582, 150.1, 57.7, new ChangeStateCmd(model, State.MENU));
        exit.setImgButton("/Exit.png");
        exit.setImgHoverButton("/ExitHover.png");

        buttons.add(exit);
    }

    /**
     * Đọc file highscores.dat sắp xếp và lấy top 3
     */
    @SuppressWarnings("unchecked")
    public void refreshScores() {
        List<HighScoreEntry> allScores = new ArrayList<>();
        File file = new File(HIGH_SCORE_FILE);

        if (file.exists()) {
            try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream(file))) {
                allScores = (List<HighScoreEntry>) ois.readObject();
            } catch (Exception e) {
                System.err.println("Lỗi khi đọc file highscores.dat: " + e.getMessage());
            }
        }

        allScores.sort(Comparator.comparingInt(HighScoreEntry::score).reversed());

        this.topScores = allScores.stream().limit(3).toList();
    }

    @Override
    public void draw(GraphicsContext render, GameplayModel gameplayModel) {
        render.drawImage(background, 0, 0, 600, 650);
        exit.draw(render);

        render.setFill(Color.WHITE);
        render.setFont(Font.font("Consolas", FontWeight.BOLD, 28));

        double startX = 100;
        double startY = 250;
        double lineSpacing = 60;

        if (topScores.isEmpty()) {
            render.fillText("Chưa có ai chơi!", startX, startY);
        } else {
            for (int i = 0; i < topScores.size(); i++) {
                HighScoreEntry ps = topScores.get(i);
                String text = String.format("Top %d: %s - %d",
                        i + 1,
                        ps.name(),
                        ps.score()
                );
                // Vẽ tên và điểm
                render.fillText(text, startX, startY + (i * lineSpacing));
            }
        }
    }

    @Override
    public void checkHover(MouseEvent e) {
        for (Button b : buttons) {
            b.setHovering(e);
        }
    }
}