package Controller;

/**
 * Một interface định nghĩa các phương thức lắng nghe cho các sự kiện quan trọng trong game.
 * Các lớp (ví dụ: GameController) có thể triển khai interface này để nhận thông báo
 * và xử lý khi các sự kiện này xảy ra trong GameplayModel.
 */
public interface GameEventListener {

    /**
     * Được gọi khi người chơi phá vỡ tất cả các viên gạch và hoàn thành cấp độ hiện tại.
     */
    void onLevelCompleted();

    /**
     * Được gọi khi người chơi hết mạng và trò chơi kết thúc.
     */
    void onGameOver();

    /**
     * Được gọi mỗi khi quả bóng va chạm với một viên gạch.
     */
    void onBrickHit();

    /**
     * Được gọi khi người chơi hoàn thành tất cả các cấp độ và chiến thắng trò chơi.
     */
    void onVictory();
}