package Model;

import java.io.Serial;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class SaveState implements Serializable {
    @Serial
    private static final long serialVersionUID = 1L;

    public boolean hasGameProgress = false;
    public String playerName;

    public int level;
    public int lives;
    public int score;
    public int combo;

    public double paddleX;
    public double paddleLength;
    public boolean paddleShield;

    public List<BallData> balls;
    public List<BrickData> bricks;
    public List<FallingPowerUpData> fallingPowerUps;
    public List<ActivePowerUpData> activePowerUps;

    public static class FallingPowerUpData implements Serializable {
        @Serial
        private static final long serialVersionUID = 1L;
        public String name;
        public double x, y, vx, vy;
    }

    public static class ActivePowerUpData implements Serializable {
        @Serial
        private static final long serialVersionUID = 1L;
        public String name;
        public int elapsedMs;
    }

    public static class BallData implements Serializable {
        @Serial
        private static final long serialVersionUID = 1L;
        public double x, y, vx, vy;
        public boolean isBomb;
    }

    public static class BrickData implements Serializable {
        @Serial
        private static final long serialVersionUID = 1L;
        public double x, y;
        public int brickType;
        public boolean isBreaking;
        public double frameTimer;
    }

    public SaveState() {
        balls = new ArrayList<>();
        bricks = new ArrayList<>();
        fallingPowerUps = new ArrayList<>();
        activePowerUps = new ArrayList<>();
    }
}