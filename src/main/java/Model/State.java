package Model;

/**
 * Định nghĩa các trạng thái tổng thể khác nhau mà trò chơi có thể ở trong đó.
 * Enum này được sử dụng bởi {@link GameModel} và {@link Controller.GameController}
 * để quản lý luồng chính của ứng dụng, quyết định logic nào cần được cập nhật
 * và màn hình nào cần được hiển thị.
 */
public enum State {
    MENU,
    PLAYING,
    TWO_PLAYING,
    TWO_PLAYER_ENDGAME,
    PAUSED,
    SETTING,
    LOSS,
    VICTORY,
    FADE,
    PLAY_MODE,
    LEADERBOARD,
    THEME,
    HOW_TO_PLAY,
    CHOOSE_PADDLE,
    CHOOSE_BALL,
    CHOOSE_BACKGROUND
}
