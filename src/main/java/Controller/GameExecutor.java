package Controller;

import java.util.concurrent.ExecutorService;

/**
 * lớp quản lý luồng xử lý cho trò chơi.
 * xử dụng singleton để chỉ tạo một thực thể
 */
public final class GameExecutor {
    private final ExecutorService logicExecutor;
    private final ExecutorService audioExecutor;

    private GameExecutor() {
        logicExecutor = newSingle("game-logic");
        audioExecutor = newSingle("game-audio");
    }

    private static ExecutorService newSingle(String name) {
        return java.util.concurrent.Executors.newSingleThreadExecutor(r -> {
            Thread t = new Thread(r, name);
            t.setDaemon(true);
            return t;
        });
    }

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

    private static class Holder {
        static final GameExecutor I = new GameExecutor();
    }

    public static GameExecutor getInstance() {
        return Holder.I;
    }

    public ExecutorService getLogicExecutor() {
        return logicExecutor;
    }

    public ExecutorService getAudioExecutor() {
        return audioExecutor;
    }
}
