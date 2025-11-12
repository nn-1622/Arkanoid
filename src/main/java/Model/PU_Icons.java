package Model;

import javafx.scene.image.Image;

public class PU_Icons {
    private static final Image LASER = new Image("/LaserPU.png");
    private static final Image EXPAND = new Image("/x2LengthPU.png");
    private static final Image EXTRA_LIVE = new Image("/extralive.png");
    private static final Image SCORE_X2 = new Image("/x2.png");
    private static final Image SHIELD = new Image("/shield.png");
    private static final Image BOMB_BALL = new Image("/bombball.png");
    private static final Image MULTI_BALL = new Image("/MultiBallPU.png");
    private static final Image UNKNOWN = new Image("/DefaultBall.png");

    public static Image getIcon(String name) {
        return switch (name) {
            case "Laser" -> LASER;
            case "Expand" -> EXPAND;
            case "Extra Live" -> EXTRA_LIVE;
            case "Score x2" -> SCORE_X2;
            case "Shield" -> SHIELD;
            case "BombBall" -> BOMB_BALL;
            case "Multi Ball" -> MULTI_BALL;
            default -> UNKNOWN;
        };
    }
}
