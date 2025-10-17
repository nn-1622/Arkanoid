package Controller;

public interface GameEventListener {
    void onLevelCompleted();
    void onGameOver();
    void onBrickHit();
    void onVictory();
}
