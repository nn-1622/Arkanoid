package Model;

import java.awt.*;

public abstract class BasePowerUp implements PowerUp {
    protected int x, y;
    protected int width = 20, height = 20;
    protected int speed = 3;
    protected boolean active = true;

    public BasePowerUp(int x, int y) {
        this.x = x;
        this.y = y;
    }

    @Override
    public void update() {
        y += speed;
    }

    @Override
    public boolean intersects(Rectangle rect) {
        return new Rectangle(x, y, width, height).intersects(rect);
    }

    @Override
    public boolean isActive() {
        return active;
    }

    @Override
    public void deactivate() {
        active = false;
    }

    @Override
    public void draw(Graphics g) {
        g.setColor(Color.ORANGE);
        g.fillOval(x, y, width, height);
    }
}
