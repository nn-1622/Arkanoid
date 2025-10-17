package Model;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.Scanner;

import Controller.GameEventListener;
import javafx.scene.image.Image;

public class GameplayModel {
    private Image background;
    private Ball ball;
    private Paddle paddle;
    private double canvasWidth;
    private double canvasHeight;
    private BallState currentBallState;
    private double currentVx;
    private boolean rendered = false;
    private ArrayList<Brick> brick;
    private int level;
    private int lives;

    private GameEventListener gameEventListener;

    public GameplayModel(double canvasWidth, double canvasHeight) {
        this.canvasWidth = canvasWidth;
        this.canvasHeight = canvasHeight;
        double paddleLength = 110;
        double paddleHeight = 20;

        paddle = new Paddle(canvasWidth / 2 - paddleLength / 2,
                canvasHeight - 100, paddleLength, paddleHeight);

        ball = new Ball(paddle.x + paddleLength / 2, paddle.y - paddleHeight / 2, 0, 0, 10);
        currentBallState = BallState.ATTACHED;
        lives = 5;
        level = 1;
        currentVx = 0;
        renderMap();
    }
    public void launchBall() {
        if (this.currentBallState == BallState.ATTACHED) {
            this.currentBallState = BallState.LAUNCHED;
            ball.setVx(0);
            ball.setVy(-5 - currentVx);
        }
    }
    public void addBrickType(int type, double x, double y) {
        Brick newBrick = new Brick(x, y);
        newBrick.setBrickType(type);
        brick.add(newBrick);
    }
    public void renderMap() {
        this.brick = new ArrayList<>();
        try (InputStream map = getClass().getResourceAsStream(String.format("/map/%d.txt", level));
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
    public int getLives() {
        return lives;
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
                lives--;
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
                if(b.isBreaking()) continue;
                if (b.getEdgeBottom() > ball.getEdgeTop() &&
                    b.getEdgeTop() < ball.getEdgeBottom() && 
                    b.getEdgeRight() > ball.getEdgeLeft() && 
                    b.getEdgeLeft() < ball.getEdgeRight()) {

                    double overlapX = Math.min(ball.getEdgeRight() - b.getEdgeLeft(), b.getEdgeRight() - ball.getEdgeLeft());
                    double overlapY = Math.min(ball.getEdgeBottom() - b.getEdgeTop(), b.getEdgeBottom() - ball.getEdgeTop());

                    b.hit();
                    if(gameEventListener != null) {
                        gameEventListener.onBrickHit();
                    }

                    if (overlapX < overlapY) {
                        if (ball.getX() < b.getX()) {
                            ball.setX(b.getEdgeLeft() - ball.getRadius());
                        } else {
                            ball.setX(b.getEdgeRight() + ball.getRadius());
                        }
                        ball.reverseVx();
                    } else if (overlapY < overlapX) {
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
        }
    }

    public void Initialize(){
        level++;
        if (level <= 5) {
            renderMap();
            resetPosition();
            currentVx++;
            lives = 5;
        }
    }
    public void update(boolean left, boolean right, double deltaTime) {
        this.getPaddle().move(left, right);
        ball.move();
        for (Brick b : brick) {
            b.update(deltaTime);
        }
        checkCollisions();
        brick.removeIf(Brick::isDestroyed);
        if (brick.isEmpty()) {
            if(gameEventListener != null) {
                gameEventListener.onLevelCompleted();
            }
        }
        if (level ==6) {
            if(gameEventListener != null) {
                gameEventListener.onVictory();
            }
        }
        if (lives <= 0) {
            if(gameEventListener != null) {
                gameEventListener.onGameOver();
            }
        }
    }

    public void setGameEventListener(GameEventListener gameEventListener) {
        this.gameEventListener = gameEventListener;
    }
}
