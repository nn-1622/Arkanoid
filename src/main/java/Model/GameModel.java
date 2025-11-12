package Model;

import Controller.EventLoader;
import View.*;

import java.io.*;
import java.util.ArrayList;
import java.util.EnumMap;
import java.util.List;
import java.util.Map;
import View.LoadGameView;
import View.View;
import View.TwoPlayerPauseView;

/**
 * Lớp model chính của toàn bộ ứng dụng trò chơi.
 * Quản lý trạng thái tổng thể (MENU, PLAYING, TWO_PLAYING, SETTING, v.v.)
 * và chứa các GameplayModel riêng cho 1P hoặc 2P.
 */
public class GameModel implements UltilityValues {
    private final EventLoader eventLoader;
    private static GameModel model;
    private volatile State gstate;
    private View currentView;
    private GameplayModel gameModel;
    private final Map<State, View> viewMap = new EnumMap<>(State.class);
    private long fadeStartTime = 0;
    private String currentSaveName = null;
    private State stateBeforeAccount;
    private static final String HIGH_SCORE_FILE = "highscores.dat";

    private GameplayModel leftGame;
    private GameplayModel rightGame;

    private long resultStartTime = -1;
    private boolean resultTimerStarted = false;

    private volatile boolean twoPlayerEnded = false;

    public boolean isTwoPlayerEnded() {
        return twoPlayerEnded;
    }

    public void setTwoPlayerEnded(boolean value) {
        this.twoPlayerEnded = value;
    }


    /** Bắt đầu bộ đếm thời gian kết quả */
    public void startResultTimer() {
        resultStartTime = System.nanoTime();
        resultTimerStarted = true;
    }

    /** Kiểm tra xem đã bắt đầu bộ đếm kết quả chưa */
    public boolean isResultTimerStarted() {
        return resultTimerStarted;
    }

    /**
     * Kiểm tra đã trôi qua bao lâu kể từ khi bắt đầu kết quả
     * @param durationMillis số mili-giây chờ (ví dụ 5000 = 5 giây)
     */
    public boolean hasResultTimeElapsed(long durationMillis) {
        if (!resultTimerStarted) return false;
        long elapsed = (System.nanoTime() - resultStartTime) / 1_000_000;
        return elapsed >= durationMillis;
    }

    /** Reset bộ đếm kết quả (nếu cần dùng lại) */
    public void resetResultTimer() {
        resultTimerStarted = false;
        resultStartTime = -1;
    }

    /**
     * Khởi tạo một đối tượng GameModel mới.
     * Trạng thái ban đầu là MENU.
     */
    private GameModel() {
        eventLoader = new EventLoader();

        viewMap.put(State.MENU, new MenuScene(this));
        viewMap.put(State.LOSS, new LoseView(this));
        viewMap.put(State.PLAYING, new GameplayView(this));
        viewMap.put(State.TWO_PLAYING, new TwoPlayerView(this));
        viewMap.put(State.SETTING, new SettingScene(this));
        viewMap.put(State.VICTORY, new VictoryView(this));
        viewMap.put(State.PLAY_MODE, new PlayModeScene(this));
        viewMap.put(State.CHOOSE_PADDLE, new ChoosePaddleView(this));
        viewMap.put(State.CHOOSE_BALL, new ChooseBallView(this));
        viewMap.put(State.CHOOSE_BACKGROUND, new ChooseBackgroundView(this));
        viewMap.put(State.LEADERBOARD, new LeaderBoardView(this));
        viewMap.put(State.THEME, new ThemeView(this));
        viewMap.put(State.HOW_TO_PLAY, new HowToPlayView(this));

        viewMap.put(State.SETTING_ACCOUNT, new AccountView(this));
        viewMap.put(State.SETTING_HOWTOPLAY, new HowToPlayView(this));
        viewMap.put(State.PAUSED, new PauseView(this));
        viewMap.put(State.LOAD_GAME, new LoadGameView(this));
        viewMap.put(State.READY_TO_PLAY, new ReadyView(this));
        viewMap.put(State.TWO_PLAYER_PAUSED, new TwoPlayerPauseView(this));

        setGstate(State.MENU);
    }

    /** Singleton: chỉ có một GameModel duy nhất */
    public static GameModel getGameModel() {
        if (model == null) {
            model = new GameModel();
        }
        return model;
    }

    /**
     * Tạo và khởi tạo một phiên chơi 1 người.
     */
    public void CreateGameplay() {
        gameModel = new GameplayModel(eventLoader);
        leftGame = null;
        rightGame = null;
    }

    /**
     * Tạo và khởi tạo hai phiên chơi riêng biệt cho chế độ 2 người.
     */
    public void CreateTwoGameplay() {
        leftGame = new GameplayModel(eventLoader, true);
        rightGame = new GameplayModel(eventLoader, true);
        resetResultTimer();
    }

    public GameplayModel getLeftGame() { return leftGame; }
    public GameplayModel getRightGame() { return rightGame; }

    public GameplayModel getGameplayModel() { return gameModel; }

    public void setGstate(State gstate) {
        if (gstate == State.LOAD_GAME) {
            View view = viewMap.get(State.LOAD_GAME);
            if (view instanceof LoadGameView) {
                ((LoadGameView) view).refreshSaveSlots();
            }
        }
        if (gstate == State.LEADERBOARD) {
            View view = viewMap.get(State.LEADERBOARD);
            if (view instanceof LeaderBoardView) {
                ((LeaderBoardView) view).refreshScores();
            }
        }
        this.gstate = gstate;
        this.currentView = viewMap.get(gstate);
    }
    public String getCurrentSaveName() {
        return currentSaveName;
    }

    public void setCurrentSaveName(String name) {
        if (name == null) {
            this.currentSaveName = null;
        } else if (!name.endsWith(".sav")) {
            this.currentSaveName = name + ".sav";
        } else {
            this.currentSaveName = name;
        }
    }

    public State getStateBeforeAccount() {
        return (stateBeforeAccount != null) ? stateBeforeAccount : State.MENU;
    }

    public void setStateBeforeAccount(State state) {
        this.stateBeforeAccount = state;
    }

    /**
     * Thu thập dữ liệu từ GameplayModel và lưu vào file.
     * @param fileName Tên file (ví dụ: "player1.sav")
     */
    public void saveGame(String fileName) {
        if (gameModel == null) {
            System.err.println("Không có game để save!");
            return;
        }
        if (gstate != State.PAUSED && gstate != State.PLAYING) {
            System.err.println("Chỉ có thể save khi đang Paused hoặc auto-save!");
        }

        SaveState save = new SaveState();

        save.hasGameProgress = true;
        String baseName = fileName.replace(".sav", "");
        save.playerName = baseName;
        save.level = gameModel.getLevel();
        save.lives = gameModel.getLives();
        save.score = gameModel.getScore();
        save.combo = gameModel.getCombo();

        Paddle p = gameModel.getPaddle();
        save.paddleX = p.getX();
        save.paddleLength = p.getLength();
        save.paddleShield = p.hasShield();

        for (Ball b : gameModel.getBalls()) {
            SaveState.BallData ballData = new SaveState.BallData();
            ballData.x = b.getX();
            ballData.y = b.getY();
            ballData.vx = b.getVx();
            ballData.vy = b.getVy();
            ballData.isBomb = b.isBomb();
            save.balls.add(ballData);
        }

        for (Brick b : gameModel.getBricks()) {
            SaveState.BrickData brickData = new SaveState.BrickData();
            brickData.x = b.getX();
            brickData.y = b.getY();
            brickData.brickType = b.getBrickType();
            brickData.isBreaking = b.isBreaking();
            brickData.frameTimer = b.frameTimer;
            save.bricks.add(brickData);
        }

        for (MovableObject puObj : gameModel.getFallingPowerUps()) {
            if (puObj instanceof PowerUp) {
                PowerUp pu = (PowerUp) puObj;
                SaveState.FallingPowerUpData puData = new SaveState.FallingPowerUpData();
                puData.name = pu.getName();
                puData.x = puObj.getX();
                puData.y = puObj.getY();
                puData.vx = puObj.getVx();
                puData.vy = puObj.getVy();
                save.fallingPowerUps.add(puData);
            }
        }

        for (PowerUp pu : gameModel.getActivePowerUps()) {
            SaveState.ActivePowerUpData puData = new SaveState.ActivePowerUpData();
            puData.name = pu.getName();
            puData.elapsedMs = pu.getElapsedMs();
            save.activePowerUps.add(puData);
        }
        try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream("saves/" + fileName))) {
            oos.writeObject(save);
            System.out.println("Lưu thành công: " + fileName);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Đọc file save và cấu hình GameplayModel
     * @param fileName Tên file (ví dụ: "player1.sav")
     * @return true nếu load thành công
     */
    public boolean loadGame(String fileName) {
        SaveState save;

        try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream("saves/" + fileName))) {
            save = (SaveState) ois.readObject();
        } catch (IOException | ClassNotFoundException e) {
            e.printStackTrace();
            return false;
        }

        if (gameModel == null) {
            CreateGameplay();
        }

        CreateGameplay();
        setCurrentSaveName(fileName);

        if (save.hasGameProgress) {
            System.out.println("Loading game progress from: " + fileName);
            gameModel.configureFromSave(save);
        } else {
            System.out.println("Starting new game in slot: " + fileName);
        }

        return true;
    }

    public State getGstate() {
        return gstate;
    }

    public View getCurrentView() {
        return currentView;
    }

    public void setCurrentView(View currentView) {
        this.currentView = currentView;
    }

    public EventLoader getEventLoader() {
        return eventLoader;
    }

    public void setFadeStartTime(long fadeStartTime) {
        this.fadeStartTime = fadeStartTime;
    }

    public long getFadeStartTime() {
        return fadeStartTime;
    }


    public View getView(State state) {
        return viewMap.get(state);
    }

    public void autoSave() {
        if (currentSaveName != null && gameModel != null) {
            System.out.println("Auto-saving progress to: " + currentSaveName);
            saveGame(currentSaveName);
        } else {
            System.out.println("Currently in a 'New Game' slot, auto-save skipped.");
        }
    }

    public void CreateNewGame() {
        currentSaveName = null;
        CreateGameplay();
    }

    /**
     * Ghi lại điểm số cuối cùng khi game kết thúc (Thắng hoặc Thua).
     */
    public synchronized void recordFinalScore() {
        if (gameModel == null || currentSaveName == null) {
            System.out.println("Không lưu điểm: Không có tên hoặc không có game.");
            return;
        }

        int finalScore = gameModel.getScore();
        String playerName = currentSaveName.replace(".sav", "");

        List<HighScoreEntry> scores = loadHighScores();

        scores.add(new HighScoreEntry(playerName, finalScore));

        saveHighScores(scores);
        System.out.println("Đã lưu điểm cao: " + playerName + " - " + finalScore);
    }

    /**
     * Đọc danh sách điểm từ file highscores.dat
     */
    @SuppressWarnings("unchecked")
    private List<HighScoreEntry> loadHighScores() {
        File file = new File(HIGH_SCORE_FILE);
        if (file.exists()) {
            try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream(file))) {
                return (List<HighScoreEntry>) ois.readObject();
            } catch (Exception e) {
                System.err.println("Lỗi khi đọc highscores.dat: " + e.getMessage());
            }
        }
        return new ArrayList<>();
    }

    /**
     * Hàm hỗ trợ: Ghi (ghi đè) danh sách điểm vào file highscores.dat
     */
    private void saveHighScores(List<HighScoreEntry> scores) {
        try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(HIGH_SCORE_FILE))) {
            oos.writeObject(scores);
        } catch (IOException e) {
            System.err.println("lỗi khi ghi highscores.dat " + e.getMessage());
        }
    }
}
