package Controller;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

public final class GameExecutor {
    private GameExecutor() {
        logicExecutor = newSingle("game-logic", true);
        audioExecutor = newSingle("game-audio", true);
    }

    private static ExecutorService newSingle(String name, boolean daemon) {
        return java.util.concurrent.Executors.newSingleThreadExecutor(r -> {
            Thread t = new Thread(r, name);
            t.setDaemon(daemon);
            return t;
        });
    }

    private static class Holder { static final GameExecutor I = new GameExecutor(); }
    public static GameExecutor getInstance() { return Holder.I; }

    private final ExecutorService logicExecutor;
    private final ExecutorService audioExecutor;

    public ExecutorService getLogicExecutor() { return logicExecutor; }
    public ExecutorService getAudioExecutor() { return audioExecutor; }

    public void shutdown() {
        shutdownExecutor(audioExecutor);
        shutdownExecutor(logicExecutor);
    }

    private static void shutdownExecutor(ExecutorService ex) {
        if (ex != null && !ex.isShutdown()) {
            ex.shutdown();
            try {
                if (!ex.awaitTermination(5, java.util.concurrent.TimeUnit.SECONDS)) {
                    ex.shutdownNow();
                }
            } catch (InterruptedException e) {
                ex.shutdownNow();
                Thread.currentThread().interrupt();
            }
        }
    }
}
