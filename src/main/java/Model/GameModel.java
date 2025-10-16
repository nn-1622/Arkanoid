package Model;

public class GameModel {
    private State gstate;
    private GameplayModel gameModel;
    private double canvasHeight;
    private double canvasWidth;
    public GameModel(double  canvasHeight, double canvasWidth) {
        gstate = State.MENU;
        this.canvasHeight = canvasHeight;
        this.canvasWidth = canvasWidth;
        this.gameModel = new GameplayModel(this.canvasWidth, this.canvasHeight);
    }
    public void  setGstate(State gstate) {
        this.gstate = gstate;
    }
    public State getGstate() {
        return gstate;
    }
    public GameplayModel getGameplayModel() {
        return gameModel;
    }
}
