package Controller;

/**
 * lệnh thoát game.
 */
public class ExitCmd implements GameCommand {
    @Override
    public void execute() {
        System.exit(0);
    }
}
