package Model;


import Controller.EventLoader;
import Controller.GameController;
import Controller.GameEventListener;
import View.*;

import java.util.EnumMap;
import java.util.Map;
import java.io.*;
import View.LoadGameView;
import View.View;

/**
 * Lớp model chính của toàn bộ ứng dụng trò chơi.
 * Lớp này quản lý trạng thái tổng thể của ứng dụng (ví dụ: đang ở MENU, đang PLAYING, SETTING, v.v.)
 * và chứa một đối tượng {@link GameplayModel} để xử lý logic cụ thể của màn chơi.
 */
public class GameModel implements UltilityValues {
    private final EventLoader eventLoader;
    private static GameModel model;
    private State gstate;
    private View currentView;
    private GameplayModel gameModel;
    private final Map<State, View> viewMap = new EnumMap<State, View>(State.class);
    private long fadeStartTime = 0;
    private GameplayModel leftGame;
    private GameplayModel rightGame;
    private String currentSaveName = null;
    private State stateBeforeAccount;

    /**
     * Khởi tạo một đối tượng GameModel mới.
     * Trạng thái ban đầu của trò chơi được đặt là MENU.
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
        //update view
        viewMap.put(State.SETTING_ACCOUNT, new AccountView(this));
        viewMap.put(State.SETTING_THEME, new ThemeView(this));
        viewMap.put(State.SETTING_VOLUME, new VolumeView(this));
        viewMap.put(State.SETTING_HOWTOPLAY, new HowToPlayView(this));
        viewMap.put(State.PAUSED, new PauseView(this));
        viewMap.put(State.LOAD_GAME, new LoadGameView(this));
        viewMap.put(State.READY_TO_PLAY, new ReadyView(this));

        setGstate(State.MENU);
    }

    public static GameModel getGameModel() {
        if (model == null) {
            model = new GameModel();
        }
        return model;
    }

    /**
     * Tạo và khởi tạo một phiên chơi mới (GameplayModel).
     * Phương thức này được gọi khi bắt đầu một màn chơi mới hoặc chơi lại.
     */
    public void CreateGameplay() {
        gameModel = new GameplayModel(eventLoader);
    }

    public void CreateTwoGameplay() {
        // Mỗi phiên vẫn chạy tư duy khung 600×650; View hai người sẽ dịch trục vẽ
        Paddle p1 = Paddle.newInstance(
                UltilityValues.canvasWidth / 2.0 - UltilityValues.paddleLength / 2.0,
                UltilityValues.canvasHeight - 140,
                UltilityValues.paddleLength,
                UltilityValues.paddleHeight
        );
        Paddle p2 = Paddle.newInstance(
                UltilityValues.canvasWidth / 2.0 - UltilityValues.paddleLength / 2.0,
                UltilityValues.canvasHeight - 140,
                UltilityValues.paddleLength,
                UltilityValues.paddleHeight
        );
        leftGame = new GameplayModel(eventLoader, p1, true);
        rightGame = new GameplayModel(eventLoader, p2, true);
    }

    public GameplayModel getLeftGame() { return leftGame; }
    public GameplayModel getRightGame() { return rightGame; }

    /**
     * Thiết lập trạng thái hiện tại của trò chơi.
     * @param gstate Trạng thái mới (ví dụ: State.PLAYING, State.MENU).
     */
    public void setGstate(State gstate) {
        // === THÊM ĐOẠN CODE NÀY VÀO ĐẦU HÀM ===
        if (gstate == State.LOAD_GAME) {
            // Lấy LoadGameView từ Map
            View view = viewMap.get(State.LOAD_GAME);

            // Kiểm tra và gọi hàm refresh
            if (view instanceof LoadGameView) {
                ((LoadGameView) view).refreshSaveSlots();
            }
        }
        // === KẾT THÚC ĐOẠN CODE MỚI ===
        // Code cũ của bạn giữ nguyên
        this.gstate = gstate;
        this.currentView = viewMap.get(gstate);
    }
    //Bổ sung getter setter
    public String getCurrentSaveName() {
        return currentSaveName;
    }

    public void setCurrentSaveName(String name) {
        // Đảm bảo tên file hợp lệ (ví dụ: "player1" -> "player1.sav")
        if (name == null) {
            this.currentSaveName = null;
        } else if (!name.endsWith(".sav")) {
            this.currentSaveName = name + ".sav";
        } else {
            this.currentSaveName = name;
        }
    }

    public State getStateBeforeAccount() {
        return (stateBeforeAccount != null) ? stateBeforeAccount : State.MENU; // Mặc định về Menu
    }

    public void setStateBeforeAccount(State state) {
        this.stateBeforeAccount = state;
    }

    /**
     * Thu thập dữ liệu từ GameplayModel và lưu vào file.
     * @param fileName Tên file (ví dụ: "player1.sav")
     */
    public void saveGame(String fileName) {
        // Chỉ save khi đang PAUSED hoặc khi được gọi bởi auto-save
        if (gameModel == null) {
            System.err.println("Không có game để save!");
            return;
        }
        if (gstate != State.PAUSED && gstate != State.PLAYING) {
            System.err.println("Chỉ có thể save khi đang Paused hoặc auto-save!");
            // (Chúng ta cho phép PLAYING để auto-save hoạt động)
        }

        SaveState save = new SaveState();

        save.hasGameProgress = true;
        String baseName = fileName.replace(".sav", "");
        save.playerName = baseName;
        // 1. Lưu dữ liệu GameplayModel
        save.level = gameModel.getLevel();
        save.lives = gameModel.getLives();
        save.score = gameModel.getScore();
        save.combo = gameModel.getCombo();

        // 2. Lưu dữ liệu Paddle
        Paddle p = gameModel.getPaddle();
        save.paddleX = p.getX();
        save.paddleLength = p.getLength();
        save.paddleShield = p.hasShield();

        // 3. Lưu dữ liệu Balls
        for (Ball b : gameModel.getBalls()) {
            SaveState.BallData ballData = new SaveState.BallData();
            ballData.x = b.getX();
            ballData.y = b.getY();
            ballData.vx = b.getVx();
            ballData.vy = b.getVy();
            ballData.isBomb = b.isBomb();
            save.balls.add(ballData);
        }

        // 4. Lưu dữ liệu Bricks
        for (Brick b : gameModel.getBricks()) {
            SaveState.BrickData brickData = new SaveState.BrickData();
            brickData.x = b.getX();
            brickData.y = b.getY();
            brickData.brickType = b.getBrickType();
            brickData.isBreaking = b.isBreaking();
            brickData.frameTimer = b.frameTimer;
            save.bricks.add(brickData);
        }

        // (Bạn tự thêm lưu Power-ups nếu muốn)
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

        // 5b. Lưu các Power-up đang KÍCH HOẠT
        for (PowerUp pu : gameModel.getActivePowerUps()) {
            SaveState.ActivePowerUpData puData = new SaveState.ActivePowerUpData();
            puData.name = pu.getName();
            puData.elapsedMs = pu.getElapsedMs();
            save.activePowerUps.add(puData);
        }
        // 5. Ghi ra file
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

        // 1. Đọc file
        try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream("saves/" + fileName))) {
            save = (SaveState) ois.readObject();
        } catch (IOException | ClassNotFoundException e) {
            e.printStackTrace();
            return false;
        }

        // 2. Cấu hình game
        if (gameModel == null) {
            CreateGameplay(); // Tạo game nếu chưa có
        }

        CreateGameplay();
        setCurrentSaveName(fileName);

        if (save.hasGameProgress) {
            // Path A: File save có tiến trình
            System.out.println("Loading game progress from: " + fileName);
            gameModel.configureFromSave(save); // Tải dữ liệu
        } else {
            // Path B: File save chỉ có tên (từ AccountView)
            System.out.println("Starting new game in slot: " + fileName);
            // Không làm gì cả, CreateGameplay() đã set up Level 1
        }

        return true;
    }

    /**
     * Lấy trạng thái hiện tại của trò chơi.
     * @return Trạng thái hiện tại của trò chơi.
     */
    public State getGstate() {
        return gstate;
    }

    /**
     * Lấy đối tượng model quản lý logic của màn chơi hiện tại.
     * @return Đối tượng GameplayModel.
     */
    public GameplayModel getGameplayModel() {
        return gameModel;
    }

    /**
     * Thiết lập thời điểm bắt đầu hiệu ứng mờ dần (fade).
     * Được sử dụng để tính toán thời gian cho các hiệu ứng chuyển cảnh.
     * @param fadeStartTime Thời gian bắt đầu (tính bằng nano giây).
     */
    public void setFadeStartTime(long fadeStartTime) {
        this.fadeStartTime = fadeStartTime;
    }

    /**
     * Lấy thời điểm bắt đầu hiệu ứng mờ dần (fade).
     * @return Thời gian bắt đầu (tính bằng nano giây).
     */
    public long getFadeStartTime() {
        return fadeStartTime;
    }

    /**
     * trả về view hiện tại
     * @return view
     */
    public View getCurrentView() {
        return currentView;
    }

    public EventLoader getEventLoader() {
        return eventLoader;
    }

    public View getView(State state) {
        return viewMap.get(state);
    }

    public void autoSave() {
        if (currentSaveName != null && gameModel != null) {
            System.out.println("Auto-saving progress to: " + currentSaveName);
            // Dùng hàm saveGame hiện có để lưu
            saveGame(currentSaveName);
        } else {
            System.out.println("Currently in a 'New Game' slot, auto-save skipped.");
        }
    }

    public void CreateNewGame() {
        currentSaveName = null; // Đây là "New Game"
        CreateGameplay();
    }
}