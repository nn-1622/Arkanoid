package Controller;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

public class GameExecutor {
    private static GameExecutor instance;
    private ExecutorService logicExecutor; // Luồng dành cho các tác vụ logic trò chơi
    private ExecutorService audioExecutor; // Luồng dành cho các tác vụ âm thanh

    private GameExecutor() {
        logicExecutor = Executors.newSingleThreadExecutor();
        audioExecutor = Executors.newSingleThreadExecutor();
    }

    public static synchronized GameExecutor getInstance() {
        if (instance == null) {
            instance = new GameExecutor();
        }
        return instance;
    }

    public ExecutorService getLogicExecutor() {
        return logicExecutor;
    }

    public void setLogicExecutor(ExecutorService logicExecutor) {
        this.logicExecutor = logicExecutor;
    }

    public ExecutorService getAudioExecutor() {
        return audioExecutor;
    }

    public void setAudioExecutor(ExecutorService audioExecutor) {
        this.audioExecutor = audioExecutor;
    }

    public void shutdown() {
        shutdownExecutor(audioExecutor);
        shutdownExecutor(logicExecutor);
    }
    
    private void shutdownExecutor(ExecutorService executor) {
        if (executor != null && !executor.isShutdown()) {
            executor.shutdown();
            try {
                if (!executor.awaitTermination(5, TimeUnit.SECONDS)) {
                    executor.shutdownNow();
                }
            } catch (InterruptedException e) {
                executor.shutdownNow();
                Thread.currentThread().interrupt();
            }
        }
    }
}
