package Model;

import java.io.Serial;
import java.io.Serializable;

public record HighScoreEntry(String name, int score) implements Serializable {
    @Serial
    private static final long serialVersionUID = 1L;
}