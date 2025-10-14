import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;
import javafx.scene.input.MouseEvent;
import javafx.scene.paint.Color;
import javafx.scene.text.Text;

public class Button extends Utility {
    private Image imgButton;
    private Image imgHoverButton;
    private String text;
    private boolean isHover;
    public Button(String text, double x, double y, double width, double height) {
        this.text = text;
        this.x = x;
        this.y = y;
        this.width = width;
        this.height = height;
    }
    public void setImgButton(String link) {
        this.imgButton = new Image(getClass().getResource(link).toExternalForm());
    }
    public void setImgHoverButton(String link) {
        this.imgHoverButton = new Image(getClass().getResource(link).toExternalForm());
    }
    public void setHover(boolean hover) {
        isHover = hover;
    }
    public void setText(String text) {
        this.text = text;
    }
    public boolean isHover(){
        return isHover;
    }
    public String getText(){
        return text;
    }
    public void setHovering(MouseEvent m){
        isHover = m.getX() > x && m.getX() < x + width && m.getY() > y && m.getY() < y + height;
    }
    public boolean isClicked(MouseEvent m){
        return m.getX() > x && m.getX() < x + width && m.getY() > y && m.getY() < y + height;
    }
    public void draw(GraphicsContext gc){
        Text te = new Text(text);
        if(!isHover){
            gc.drawImage(imgButton, x, y, width, height);
        } else{
            gc.drawImage(imgHoverButton, x, y, width, height);
        }
        gc.setFill(Color.BLACK);
        gc.fillText(text, x + width/2, y + height/2);
    }
}
