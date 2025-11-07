package Controller;

public class ExitCmd implements GameCommand {
    @Override
    public void execute() {
        System.exit(0);
    }
}
