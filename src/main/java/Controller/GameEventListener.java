package Controller;

/**
 * Một interface định nghĩa các phương thức lắng nghe cho các sự kiện quan trọng trong game.
 * Các lớp (ví dụ: GameController) có thể triển khai interface này để nhận thông báo
 * và xử lý khi các sự kiện này xảy ra trong GameplayModel.
 */
public interface GameEventListener {
    void onGameEvent (GameEvent event);
}