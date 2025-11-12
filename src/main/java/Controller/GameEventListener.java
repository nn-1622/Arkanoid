package Controller;

/**
 * bộ lắng nghe các sự kiện trong game.
 */
public interface GameEventListener {
    void onGameEvent(GameEvent event);
}