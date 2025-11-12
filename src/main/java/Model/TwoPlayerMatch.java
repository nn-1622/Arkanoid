package Model;

import javafx.scene.canvas.GraphicsContext;

public class TwoPlayerMatch {
    private final GameplayModel player1;
    private final GameplayModel player2;

    public TwoPlayerMatch(GameModel root) {
        this.player1 = new GameplayModel(root.getEventLoader());
        this.player2 = new GameplayModel(root.getEventLoader());

        player1.getPaddle().setX(100);
        player2.getPaddle().setX(UltilityValues.playWidth / 2 + 100);
    }

    public void update(boolean left1, boolean right1, boolean left2, boolean right2, double deltaTime) {
        player1.update(left1, right1, deltaTime);
        player2.update(left2, right2, deltaTime);
    }

    public void draw(GraphicsContext g) {
        double midX = UltilityValues.playWidth / 2;

        g.save();
        g.beginPath();
        g.rect(0, 0, midX, UltilityValues.canvasHeight);
        g.clip();
        player1.getPaddle().draw(g);
        g.restore();

        g.save();
        g.beginPath();
        g.rect(midX, 0, midX, UltilityValues.canvasHeight);
        g.clip();
        player2.getPaddle().draw(g);
        g.restore();
    }

    public GameplayModel getPlayer1() { return player1; }
    public GameplayModel getPlayer2() { return player2; }
}
