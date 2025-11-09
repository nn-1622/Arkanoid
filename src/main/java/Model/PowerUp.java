package Model;

import Controller.GameController;

public interface PowerUp {
    String getName();
    int getDurationMs();
    void apply(GameplayModel object);
    void remove(GameplayModel ctx);
    default boolean isInstant() { return getDurationMs() <= 0; }
    void update(GameplayModel ctx, double deltaTime);
    boolean isActive();
}
