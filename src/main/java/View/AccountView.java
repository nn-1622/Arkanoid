package View;

import Controller.GameCommand;
import Controller.SavePlayerCmd;
import Model.Button;
import Model.GameModel;
import Model.GameplayModel;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;


public class AccountView extends View {
    private Button exitButton;
    private Button saveButton;
    private String playerName = "";
    private boolean isTyping = false;
    private boolean showCursor = false;
    private String saveMessage = "";
    private boolean isFirstTyping = false;

    public void setPlayerName(String playerName) {
        this.playerName = playerName;
    }

    public AccountView(GameModel model) {
        super(model);

        saveButton = new Button(202, 356.6, 196.1, 49, new SavePlayerCmd(this));
        saveButton.setImgButton("/Save.png");
        saveButton.setImgHoverButton("/SaveHover.png");

        exitButton = new Button( 5.4, 590, 153.6, 59.1,
                new GameCommand() {
                    @Override
                    public void execute() {
                        model.setGstate(model.getStateBeforeAccount());
                    }
                });
        exitButton.setImgButton("/Exit.png");
        exitButton.setImgHoverButton("/ExitHover.png");

        buttons.add(saveButton);
        buttons.add(exitButton);

        this.playerName = "";
    }

    public String getPlayerName() {
        return playerName;
    }

    @Override
    public void handleClick(MouseEvent e) {
        double boxX = 100, boxY = 280, boxW = 400, boxH = 50;

        if (e.getX() >= boxX && e.getX() <= boxX + boxW &&
                e.getY() >= boxY && e.getY() <= boxY + boxH) {
            isTyping = true;
        } else {
            isTyping = false;
        }

        super.handleClick(e);
    }


    @Override
    public void handleKeyInput(KeyEvent e) {
        if (!isTyping) return;

        if (e.getCode() == KeyCode.BACK_SPACE) {
            if (!playerName.isEmpty()) {
                playerName = playerName.substring(0, playerName.length() - 1);
            }
        }

        else if (e.getCode() == KeyCode.ENTER) {
            saveButton.activate();
            isTyping = false;
        }

        else if (e.getText() != null && !e.getText().isEmpty() && e.getCode() != KeyCode.TAB) {
            if (playerName.length() < 20) {
                playerName += e.getText();
            }
        }
    }

    @Override
    public void draw(GraphicsContext render, GameplayModel gameplayModel) {
        render.setFill(Color.BLACK);
        render.fillRect(0, 0, 600, 650);

        render.setFill(Color.WHITE);
        render.setFont(Font.font("Consolas", 24));
        render.fillText("Enter your name:", 100, 250);

        if (isTyping) {
            render.setStroke(Color.CYAN);
        } else {
            render.setStroke(Color.WHITE);
        }
        render.setLineWidth(2);
        render.strokeRect(100, 280, 400, 50);

        render.setFill(Color.WHITE);
        render.fillText(playerName, 110, 315);

        long time = System.nanoTime();
        if (time % 1_000_000_000 < 500_000_000) {
            showCursor = true;
        } else {
            showCursor = false;
        }

        if (isTyping && showCursor) {

            double textWidth = playerName.length() * 14.4;
            double cursorX = 110 + textWidth;


            if(cursorX < 490) {
                render.setStroke(Color.WHITE);
                render.setLineWidth(2);
                render.strokeLine(cursorX, 290, cursorX, 325);
            }
        }

        saveButton.draw(render);
        exitButton.draw(render);
    }

    /**
     * Kiểm tra và cập nhật trạng thái di chuột (hover) cho tất cả các nút trên menu.
     * @param e Sự kiện chuột (MouseEvent) chứa tọa độ hiện tại của con trỏ.
     */
    @Override
    public void checkHover(MouseEvent e) {
        for (Button b : buttons) {
            b.setHovering(e);
        }
    }

}