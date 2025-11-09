package Model;

import javafx.scene.image.Image;

public enum PowerUpType {
    LASER("LaserPU.png"),
    EXPAND("x2LengthPU.png"),
    EXTRA_LIVE("extralive.png"),
    SCORE_X2("x2.png"),
    SHIELD("shield.png"),
    BOMB_BALL("bombball.png"),
    MULTI_BALL("MultiBallPU.png");

    private final Image image;

    PowerUpType(String path) {
        this.image = new Image("/" + path);
    }

    public Image getImage() {
        return image;
    }
}
