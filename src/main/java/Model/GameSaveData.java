package Model;
import java.io.Serial;
import java.io.Serializable;

/**
 * Class dùng serializable để savedata (bao gồm điểm, level, số mạng)
 */
public class GameSaveData implements Serializable {
    @Serial
    private static final long serialVersionUID = 1L;

    private int score;
    private int level;
    private int lives;

    /**
     * hàm khởi tạo cho gamesavedata
     * @param score
     * @param level
     * @param lives
     */
    public GameSaveData(int score, int level, int lives) {
        this.score = score;
        this.level = level;
        this.lives = lives;
    }

    public int  getScore() {
        return score;
    }

    public int getLevel() {
        return level;
    }

    public int getLives() {
        return lives;
    }

    @Override
    public String toString() {
        return String.format("Score: %d | Level: %d | Lives: %d", score, level, lives);
    }
}
