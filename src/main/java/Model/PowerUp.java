package Model;

import java.awt.*;

public interface PowerUp {
    void update();                      // Cập nhật vị trí rơi
    void draw(Graphics g);              // Vẽ vật phẩm
    boolean intersects(Rectangle rect); // Kiểm tra va chạm với paddle
    void apply(GameModel game);              // Áp dụng hiệu ứng
    boolean isActive();                 // Còn hoạt động không
    void deactivate();                  // Hủy vật phẩm
}
