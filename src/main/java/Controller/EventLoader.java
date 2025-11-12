package Controller;

import java.util.ArrayList;
import java.util.List;

/**
 * lớp quản lý và phân phối các sự kiện trong trò chơi.
 * cho phép đăng ký và gọi các listener khi có sự kiện xảy ra
 */
public class EventLoader {
    private final List<GameEventListener> listeners = new ArrayList<>();

    public void register(GameEventListener listener) {
        listeners.add(listener);
    }

    public void loadEvent(GameEvent event) {
        for (GameEventListener listener : listeners) {
            listener.onGameEvent(event);
        }
    }
}
