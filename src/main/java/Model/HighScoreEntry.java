package Model;

import java.io.Serializable;

/**
 * Một đối tượng  để lưu tên và điểm
 * Implement Serializable để có thể lưu vào file
 */
public record HighScoreEntry(String name, int score) implements Serializable {
    private static final long serialVersionUID = 1L;
}