package Model;

public interface PowerUp {
    String getName();

    int getDurationMs();

    void apply(GameplayModel object);

    void remove(GameplayModel ctx);

    void update(GameplayModel ctx, double deltaTime);

    boolean isActive();

    int getElapsedMs();

    void setElapsedMs(int ms);
}
