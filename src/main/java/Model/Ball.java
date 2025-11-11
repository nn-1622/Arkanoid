package Model;

import javafx.scene.image.Image;
import javafx.scene.canvas.GraphicsContext;
import Controller.GameEvent;
import javafx.scene.paint.Color;

import java.util.ArrayList;

/**
 * Lớp đại diện cho đối tượng quả bóng trong trò chơi.
 * Kế thừa từ {@link MovableObject}, lớp Ball có thêm thuộc tính bán kính (radius)
 * và hình ảnh riêng để hiển thị.
 */
public class Ball extends MovableObject {
    private double radius;
    private Image ballImg;
    private boolean isBomb = false;

    /**
     * Khởi tạo một đối tượng Ball mới.
     * @param x Tọa độ x ban đầu của tâm bóng.
     * @param y Tọa độ y ban đầu của tâm bóng.
     * @param vx Vận tốc ban đầu theo trục x.
     * @param vy Vận tốc ban đầu theo trục y.
     * @param radius Bán kính của quả bóng.
     */
    public Ball(double x, double y, double vx, double vy, double radius) {
        super(x,y,vx,vy);
        this.radius = radius;
        ballImg = new Image("/DefaultBall.png");
    }

    /**
     * Lấy tọa độ x của cạnh trái của quả bóng.
     * @return Tọa độ x của cạnh trái.
     */
    public double getEdgeLeft() {
        return x - radius;
    }

    /**
     * Lấy tọa độ x của cạnh phải của quả bóng.
     * @return Tọa độ x của cạnh phải.
     */
    public double getEdgeRight() {
        return x + radius;
    }

    /**
     * Lấy tọa độ y của cạnh trên của quả bóng.
     * @return Tọa độ y của cạnh trên.
     */
    public double getEdgeTop() {
        return y - radius;
    }

    /**
     * Lấy tọa độ y của cạnh dưới của quả bóng.
     * @return Tọa độ y của cạnh dưới.
     */
    public double getEdgeBottom() {
        return y + radius;
    }

    /**
     * Lấy bán kính của quả bóng.
     * @return Bán kính.
     */
    public double getRadius() {
        return radius;
    }

    /**
     * Lấy tọa độ x của tâm quả bóng.
     * @return Tọa độ x của tâm.
     */
    public double getCenterX() {
        return x;
    }

    /**
     * Lấy tọa độ x của tâm quả bóng.
     * @return Tọa độ y của tâm.
     */
    public double getCenterY() {
        return y;
    }

    /**
     * Đặt một hình ảnh mới cho quả bóng.
     * @param ballImg Đối tượng Image mới để hiển thị cho quả bóng.
     */
    public void setBallImg(Image ballImg) {
        this.ballImg = ballImg;
    }

    public void setBomb(boolean val) { isBomb = val; }
    public boolean isBomb() { return isBomb; }


    /**
     * {@inheritDoc}
     * Vẽ hình ảnh của quả bóng lên canvas tại vị trí hiện tại.
     * Hình ảnh được vẽ sao cho tâm của nó trùng với tọa độ (x, y) của đối tượng Ball.
     */
    @Override
    public void draw(GraphicsContext render) {
        if (isBomb) {
            render.setFill(Color.ORANGE);
            render.fillOval(x - radius * 1.4, y - radius * 1.4, radius * 2.8, radius * 2.8);
        }

        render.drawImage(ballImg, x - radius, y - radius, radius *2, radius*2);
    }

    public void attachToPaddle(Paddle paddle) {
        setX(paddle.getX() + paddle.getLength() / 2);
        setY(paddle.getY() - paddle.getHeight() / 2);
    }

    public void checkWallCollision(double canvasWidth, double canvasHeight, GameplayModel game) {
        if (getEdgeLeft() <= 0 || getEdgeRight() >= canvasWidth) {
            this.setX((getEdgeLeft() <= 0) ? getRadius() : canvasWidth - getRadius());
            reverseVx();
        }

        if (getEdgeTop() <= 0) {
            this.setY(getRadius());
            reverseVy();
        }

        if (getEdgeBottom() >= canvasHeight) {
            this.setY(canvasHeight + getRadius());
            if (game.getPaddle().hasShield()) {
                reverseVy();
            } else {
                if (game.getBalls().size() == 1) {
                    game.resetPosition();
                    game.resetPowerUp();
                    game.decreaseLives();
                    game.setCombo(0);
                }
                game.getBalls().removeIf(a -> a.equals(this));
            }
        }
    }

    public void checkPaddleCollision(Paddle paddle) {
        if (getEdgeBottom() >= paddle.getY() &&
                getEdgeTop() <= paddle.getY() + paddle.getHeight() &&
                getEdgeRight() >= paddle.getX() &&
                getEdgeLeft() <= paddle.getX() + paddle.getLength() &&
                getVy() > 0) {

            double paddleCenter = paddle.getX() + paddle.getLength() / 2;
            double diff = (getCenterX() - paddleCenter) / (paddle.getLength() / 2);

            double speed = Math.sqrt(getVx() * getVx() + getVy() * getVy());
            double angle = diff * Math.toRadians(60);

            setVx(speed * Math.sin(angle));
            setVy(-speed * Math.cos(angle));
        }
    }

    private void explodeBricksAround(ArrayList<Brick> bricks, Brick brick, GameplayModel game) {
        double blastRadius = 80; // bán kính nổ

        for (Brick b : bricks) {
            if (!b.isDestroyed()) {
                double dx = (b.getX() + b.getWidth() / 2) - (brick.getX() + brick.getWidth() / 2);
                double dy = (b.getY() + b.getHeight() / 2) - (brick.getY() + brick.getHeight() / 2);
                double dist = Math.sqrt(dx*dx + dy*dy);

                if (dist <= blastRadius) {
                    b.hit(); // phá gạch
                    game.getEventLoader().loadEvent(GameEvent.BALL_HIT);
                }
            }
        }
    }

    public void checkBrickCollision(ArrayList<Brick> bricks, GameplayModel game) {
        for (Brick b : bricks) {
            if (b.isBreaking()) continue;

            boolean intersect = (b.getEdgeBottom() > getEdgeTop() &&
                    b.getEdgeTop() < getEdgeBottom() &&
                    b.getEdgeRight() > getEdgeLeft() &&
                    b.getEdgeLeft() < getEdgeRight());
            if (!intersect) continue;

            double overlapX = Math.min(getEdgeRight() - b.getEdgeLeft(), b.getEdgeRight() - getEdgeLeft());
            double overlapY = Math.min(getEdgeBottom() - b.getEdgeTop(), b.getEdgeBottom() - getEdgeTop());

            if (isBomb) {
                explodeBricksAround(bricks, b, game);
            } else {
                b.hit();
            }

            game.comboHit();
            game.scorePoint(1);
            game.getEventLoader().loadEvent(GameEvent.BALL_HIT);

            if (overlapX < overlapY) {
                if (getX() < b.getX()) {
                    setX(b.getEdgeLeft() - getRadius());
                } else {
                    setX(b.getEdgeRight() + getRadius());
                }
                reverseVx();
            } else if (overlapY < overlapX) {
                if (getY() < b.getY()) {
                    setY(b.getEdgeTop() - getRadius());
                } else {
                    setY(b.getEdgeBottom() + getRadius());
                }
                reverseVy();
            }
            break;
        }
    }
}