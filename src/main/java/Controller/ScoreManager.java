package Controller;

import Model.GameSaveData;
import java.io.*;

/**
 * Lớp quản lý việc lưu và tải điểm số, cấp độ và mạng sống
 * Bổ sung chức năng tự động cập nhật kỷ lục điểm cao (High Score)
 */
public class ScoreManager {
    private static final String SAVE_PATH = "savegame.dat";

    /**
     * Lưu dữ liệu trò chơi
     * Nếu điểm hiện tại cao hơn điểm đã lưu trước đó cập nhật file
     */
    public static void saveGame(GameSaveData newData) {
        try {
            GameSaveData oldData = loadGame();

            if (oldData == null) {
                writeData(newData);
                System.out.println("Game saved for the first time" + newData);
            } else if (newData.getScore() > oldData.getScore()) {
                writeData(newData);
                System.out.println("New highest score" + newData);
            } else {
                System.out.println("GameScore" + newData.getScore() +
                        ", HighestScore" + oldData.getScore());
            }
        } catch (Exception e) {
            System.err.println("Failed to save game");
            e.printStackTrace();
        }
    }

    /**
     * Ghi dữ liệu vào file
     */
    private static void writeData(GameSaveData data) throws IOException {
        try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(SAVE_PATH))) {
            oos.writeObject(data);
        }
    }

    /**
     * Tải dữ liệu game từ file nếu tồn tại
     */
    public static GameSaveData loadGame() {
        try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream(SAVE_PATH))) {
            GameSaveData data = (GameSaveData) ois.readObject();
            return data;
        } catch (IOException | ClassNotFoundException e) {
            return null;
        }
    }

    /**
     * Xem điểm cao hiện tại (nếu có)
     */
    public static void showHighScore() {
        GameSaveData saved = loadGame();
        if (saved != null) {
            System.out.println("Current High Score: " + saved.getScore() +
                    " | Level: " + saved.getLevel() + " | Lives: " + saved.getLives());
        } else {
            System.out.println("No saved game found.");
        }
    }
}
