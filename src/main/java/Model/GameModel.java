package Model;


import Controller.GameEventListener;

public class GameModel {
    private State gstate;
    private GameplayModel gameModel;
    private double canvasHeight;
    private double canvasWidth;
    private long fadeStartTime = 0;
    public GameModel(double  canvasHeight, double canvasWidth) {
        gstate = State.MENU;
        this.canvasHeight = canvasHeight;
        this.canvasWidth = canvasWidth;
    }
    public void CreateGameplay(GameEventListener gameEventListener) {
        gameModel = new GameplayModel(this.canvasWidth, this.canvasHeight);
        gameModel.setGameEventListener(gameEventListener);
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
    public void setFadeStartTime(long fadeStartTime) {
        this.fadeStartTime = fadeStartTime;
    }
    public long getFadeStartTime() {
        return fadeStartTime;
    }
}
