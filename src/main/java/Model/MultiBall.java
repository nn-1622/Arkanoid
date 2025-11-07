package Model;

public class ExpandPaddlePowerUp extends BasePowerUp {
    public ExpandPaddlePowerUp(int x, int y) { super(x, y); }

    @Override
    public void apply(GameplayModel game) {
        game.getPaddle().setLe(game.getPaddle().getWidth() + 40);
        deactivate();
    }
}
