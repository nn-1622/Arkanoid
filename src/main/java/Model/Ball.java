package Model;

import javafx.scene.image.Image;
import javafx.scene.canvas.GraphicsContext;
import Controller.GameEvent;
import javafx.scene.paint.Color;

import java.util.ArrayList;

/**
 * Đối tượng quả bóng
 * xử lý di chuyển, va chạm.
 */
public class Ball extends MovableObject {
    private final double radius;
    private final Image ballImg;
    private boolean isBomb = false;

    public Ball(double x, double y, double vx, double vy, double radius, String path) {
        super(x, y, vx, vy);
        this.radius = radius;
        ballImg = new Image(path);
    }

    @Override
    public void draw(GraphicsContext render) {
        if (isBomb) {
            render.setFill(Color.ORANGE);
            render.fillOval(x - radius * 1.4, y - radius * 1.4, radius * 2.8, radius * 2.8);
        }
        render.drawImage(ballImg, x - radius, y - radius, radius * 2, radius * 2);
    }

    public void attachToPaddle(Paddle paddle) {
        setX(paddle.getX() + paddle.getLength() / 2);
        setY(paddle.getY() - paddle.getHeight() / 2);
    }

    public void checkWallCollision(double canvasWidth, double canvasHeight, GameplayModel game) {
        if (getEdgeLeft() <= 0 || getEdgeRight() >= canvasWidth) {
            reverseVx();
        }

        if (getEdgeTop() <= 0) {
            reverseVy();
        }

        if (getEdgeBottom() >= canvasHeight) {
            if (game.getPaddle().hasShield()) {
                reverseVy();
            } else {
                game.getEventLoader().loadEvent(GameEvent.BALL_LOST);
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
            double diff = (getCenter() - paddleCenter) / (paddle.getLength() / 2);

            double speed = Math.sqrt(getVx() * getVx() + getVy() * getVy());
            double angle = diff * Math.toRadians(60);

            setVx(speed * Math.sin(angle));
            setVy(-speed * Math.cos(angle));
        }
    }

    private void explodeBricksAround(ArrayList<Brick> bricks, Brick center, GameplayModel game) {
        double blastRadius = 80;

        for (Brick b : bricks) {
            if (!b.isDestroyed()) {
                double dx = (b.getX() + b.getWidth() / 2) - (center.getX() + center.getWidth() / 2);
                double dy = (b.getY() + b.getHeight() / 2) - (center.getY() + center.getHeight() / 2);
                double dist = Math.sqrt(dx * dx + dy * dy);

                if (dist <= blastRadius) {
                    b.hit();
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

    public double getEdgeLeft() {
        return x - radius;
    }

    public double getEdgeRight() {
        return x + radius;
    }

    public double getEdgeTop() {
        return y - radius;
    }

    public double getEdgeBottom() {
        return y + radius;
    }

    public double getRadius() {
        return radius;
    }

    public double getCenter() {
        return x;
    }

    public void setBomb(boolean val) {
        isBomb = val;
    }

    public boolean isBomb() {
        return isBomb;
    }
}