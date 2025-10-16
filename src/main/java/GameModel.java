public class GameModel {
    private State gstate;
    private GameplayModel gameModel;
    public GameModel() {
        gstate = State.MENU;
        this.gameModel = new GameplayModel();
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
