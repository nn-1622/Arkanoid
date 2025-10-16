package Model;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.Scanner;

import javafx.scene.image.Image;

public class GameplayModel {
    private Image background;
    private Ball ball;
    private Paddle paddle;
    private double canvasWidth;
    private double canvasHeight;
    private BallState currentBallState;
    private boolean rendered = false;
    private ArrayList<Brick> brick = new ArrayList<>();
    private ArrayList<Brick> toRemove = new ArrayList<>();
    public GameplayModel(double canvasWidth, double canvasHeight) {
        this.canvasWidth = canvasWidth;
        this.canvasHeight = canvasHeight;
        double paddleLength = 110;
        double paddleHeight = 20;

        paddle = new Paddle(canvasWidth / 2 - paddleLength / 2,
                canvasHeight - 100, paddleLength, paddleHeight);

        ball = new Ball(paddle.x + paddleLength / 2, paddle.y - paddleHeight / 2, 0, 0, 10);
        currentBallState = BallState.ATTACHED;
    }
    public void launchBall() {
        if (this.currentBallState == BallState.ATTACHED) {
            this.currentBallState = BallState.LAUNCHED;
            ball.setVx(0);
            ball.setVy(-5);
        }
    }
    public void addBrickType(int type, double x, double y) {
        Brick newBrick = new Brick(x, y);
        newBrick.setBrickType(type);
        brick.add(newBrick);
    }
    public void renderMap() {
        try (InputStream map = getClass().getResourceAsStream("/map/4.txt");
            Scanner scan = new Scanner(map)) {
            double spawnX = 0;
            double spawnY = 0;
            while (scan.hasNextLine()) {
                String line = scan.nextLine();
                for (char num : line.toCharArray()) {
                    switch (num) {
                        case '0':
                        break;

                        case '1':
                        addBrickType(1, spawnX * 50, spawnY * 25);
                        break;

                        case '2':
                        addBrickType(2, spawnX * 50, spawnY * 25);
                        break;

                        case '3':
                        addBrickType(3, spawnX * 50, spawnY * 25);
                        break;

                        case '4':
                        addBrickType(4, spawnX * 50, spawnY * 25);
                        break;
                    }
                    spawnX++;
                }
                spawnX = 0;
                spawnY++;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    public void resetPosition() {
        paddle.setX(canvasWidth / 2 - paddle.getLength() / 2);
        paddle.setY(canvasHeight - 100);
        ball.setX(paddle.getX() + paddle.getLength() / 2);
        ball.setY(paddle.getY() - paddle.getHeight() / 2);
        ball.setVx(0);
        ball.setVy(0);
        currentBallState = BallState.ATTACHED;
    }
    public Image getBackground() {
        return background;
    }
    public Paddle getPaddle() {
        return paddle;
    }
    public Ball getBall() {
        return ball;
    }
    public boolean getRendered() {
        return rendered;
    }
    public void setRendered(boolean rendered) {
        this.rendered = rendered;
    }
    public ArrayList<Brick> getBricks() {
        return brick;
    }
    public void hitReg(Brick b) {
        b.setBrickType(b.getBrickType() - 1);
        if (b.getBrickType() <= 0) {
            b.setBrickType(0);
            toRemove.add(b);
        }
    }
    public void checkCollisions() {
        if (paddle.getX() < 0) {
            paddle.setX(0);
        }
        if (paddle.getX() >= canvasWidth - paddle.getLength()) {
            paddle.setX(canvasWidth - paddle.getLength());
        }
        if (currentBallState == BallState.ATTACHED) {
            ball.setX(paddle.getX() + paddle.getLength() / 2);
            ball.setY(paddle.getY() - paddle.getHeight() / 2);
        } else if (currentBallState == BallState.LAUNCHED) {
            if (ball.getEdgeLeft() <= 0 || ball.getEdgeRight() >= canvasWidth) {
                ball.reverseVx();
            }

            if (ball.getEdgeTop() <= 0) {
                ball.reverseVy();
            }

            if (ball.getEdgeBottom() >= canvasHeight) {
                resetPosition();
            }

            if (ball.getEdgeBottom() >= paddle.getY() &&
                ball.getEdgeTop() <= paddle.getY() + paddle.getHeight() &&
                ball.getCenter() >= paddle.getX() &&
                ball.getCenter() <= paddle.getX() + paddle.getLength() &&
                ball.getVy() > 0) {

                double paddleCenter = paddle.getX() + paddle.getLength() / 2;
                double diff = (ball.getCenter() - paddleCenter) / (paddle.getLength() / 2);

                double speed = Math.sqrt(ball.getVx() * ball.getVx() + ball.getVy() * ball.getVy());
                double angle = diff * Math.toRadians(75);

                ball.setVx(speed * Math.sin(angle));
                ball.setVy(-speed * Math.cos(angle));
            }
            
            for (Brick b : brick) {
                if (b.getEdgeBottom() > ball.getEdgeTop() &&
                    b.getEdgeTop() < ball.getEdgeBottom() && 
                    b.getEdgeRight() > ball.getEdgeLeft() && 
                    b.getEdgeLeft() < ball.getEdgeRight()) {

                    double overlapX = Math.min(ball.getEdgeRight() - b.getEdgeLeft(), b.getEdgeRight() - ball.getEdgeLeft());
                    double overlapY = Math.min(ball.getEdgeBottom() - b.getEdgeTop(), b.getEdgeBottom() - ball.getEdgeTop());

                    if (overlapX < overlapY) {
                        hitReg(b);
                        if (ball.getX() < b.getX()) {
                            ball.setX(b.getEdgeLeft() - ball.getRadius());
                        } else {
                            ball.setX(b.getEdgeRight() + ball.getRadius());
                        }
                        ball.reverseVx();
                    } else if (overlapY < overlapX) {
                        hitReg(b);
                        if (ball.getY() < b.getY()) {
                            ball.setY(b.getEdgeTop() - ball.getRadius());
                        } else {
                            ball.setY(b.getEdgeBottom() + ball.getRadius());
                        }
                        ball.reverseVy();
                    }
                    break;
                }
            }
            brick.removeAll(toRemove);
            toRemove.clear();
        }
    }
    public void update(boolean left, boolean right) {
        this.getPaddle().move(left, right);
        ball.move();
        checkCollisions();
    }
}