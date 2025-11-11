package Model;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * Lớp này chứa tất cả dữ liệu cần thiết để lưu và tải lại 1 màn chơi.
 * Nó phải implement Serializable để có thể ghi/đọc từ file.
 */
public class SaveState implements Serializable {

    // Đảm bảo phiên bản của lớp là nhất quán
    private static final long serialVersionUID = 1L;

    public boolean hasGameProgress = false;
    public String playerName;

    // Dữ liệu GameplayModel
    public int level;
    public int lives;
    public int score;
    public int combo;

    // Dữ liệu Paddle
    public double paddleX;
    public double paddleLength;
    public boolean paddleShield;

    // Dữ liệu các quả bóng
    public List<BallData> balls;

    // Dữ liệu các viên gạch
    // Chúng ta chỉ cần lưu loại gạch và vị trí (thông qua index)
    // Giả sử map không đổi, chỉ cần lưu độ bền
    public List<BrickData> bricks;

    // Dữ liệu Power-ups
    // (Lưu power-up phức tạp, tạm thời bỏ qua, bạn có thể tự thêm sau)
    // public List<PowerUpData> activePowerUps;
    // public List<FallingPowerUpData> fallingPowerUps;
    public List<FallingPowerUpData> fallingPowerUps;
    public List<ActivePowerUpData> activePowerUps;

    // Lớp con để lưu Power-up đang rơi
    public static class FallingPowerUpData implements Serializable {
        private static final long serialVersionUID = 1L;
        public String name; // (ví dụ: "Expand", "Laser")
        public double x, y, vx, vy;
    }
    // Lớp con để lưu Power-up đang kích hoạt
    public static class ActivePowerUpData implements Serializable {
        private static final long serialVersionUID = 1L;
        public String name;
        public int elapsedMs; // Thời gian đã trôi qua
    }
    // Lớp con để lưu dữ liệu Ball
    public static class BallData implements Serializable {
        private static final long serialVersionUID = 1L;
        public double x, y, vx, vy;
        public boolean isBomb;
    }

    // Lớp con để lưu dữ liệu Brick
    public static class BrickData implements Serializable {
        private static final long serialVersionUID = 1L;
        public double x, y; // Dùng toạ độ để xác định gạch
        public int brickType; // Độ bền còn lại
        public boolean isBreaking; // Trạng thái đang vỡ
        public double frameTimer; // Thời gian animation vỡ
    }

    // Constructor rỗng
    public SaveState() {
        balls = new ArrayList<>();
        bricks = new ArrayList<>();
        fallingPowerUps = new ArrayList<>();
        activePowerUps = new ArrayList<>();
    }
}