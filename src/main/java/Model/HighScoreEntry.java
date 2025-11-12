package Model;

import java.io.Serial;
import java.io.Serializable;

/**
 * đại diện cho một mục trong bảng xếp hạng điểm cao của trò chơi.
 * @param name  tên
 * @param score điểm số
 */
public record HighScoreEntry(String name, int score) implements Serializable {
    @Serial
    private static final long serialVersionUID = 1L;
}