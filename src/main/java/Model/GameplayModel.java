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
    public GameplayModel(double canvasWidth, double canvasHeight) {
        this.canvasWidth = canvasWidth;
        this.canvasHeight = canvasHeight;
        double paddleLength = 100;
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
    public void renderMap(ArrayList<Brick> brick) {
        try {
            InputStream map = getClass().getResourceAsStream("/map.txt");
            Scanner scan = new Scanner(map);
            int spawnX = 0;
            int spawnY = 0;
            while (scan.hasNextLine()) {
                String line = scan.nextLine();
                for (char num : line.toCharArray()) {
                    switch (num) {
                        case '0':
                        Brick brick0 = new Brick(spawnX * 56, spawnY * 25);
                        brick0.setBrickType(0);
                        brick.add(brick0);
                        break;

                        case '1':
                        Brick brick1 = new Brick(spawnX * 56, spawnY * 25);
                        brick1.setBrickType(1);
                        brick.add(brick1);
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
    public void update() {
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
            ball.move();
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
                double angle = diff * Math.toRadians(60);

                ball.setVx(speed * Math.sin(angle));
                ball.setVy(-speed * Math.cos(angle));
            }
        }
    }
}
