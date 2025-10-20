package Controller;

import java.io.*;
import java.io.IOException;
import java.io.BufferedWriter;
import java.io.FileWriter;

/**
 * closs để ghi điểm và check điểm cao nhất
 */
public class ScoreManager {
    public static final String FILE_NAME = "score.txt";
    public int score;

    public ScoreManager(int score) {
        this.score = score;
    }

    public int getScore() {
        return score;
    }

    /**
     * ghi diem vao file
     */
    public void saveScore() {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(FILE_NAME, true))) {
            writer.write(getScore() + System.lineSeparator()); //ghi điểm vào file và xuống dòng
        } catch (IOException ignored) {
            // ignore lỗi
        }
    }

    public static int getHighScore(){
        int highScore = 0;
        try (BufferedReader reader = new BufferedReader(new FileReader(FILE_NAME))){
            String line;
            while((line = reader.readLine()) != null){
                line = line.trim();
                if(line.isEmpty()){
                    continue;
                }
                try{
                    int score = Integer.parseInt(line);
                    if(score > highScore){
                        highScore = score;
                    }
                }
                catch (NumberFormatException t){
                    continue;
                }
            }
        }
        catch (IOException e){
            //ko lam gi khi ko co file
        }
        return highScore;
    }



}
