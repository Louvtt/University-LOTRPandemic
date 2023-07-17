package pandemic.game;

/** Game states */
public enum GameState {

    /** Generic game states */

    /** Main menu */
    IN_MENU,
    /** Waiting? */
    WAITING,

    /** Player turn phases */

    /** Player action */
    PLAYER_ACTIONS,
    /** Player drawing cards */
    PLAYER_DRAW_CARDS,
    /** Player infection */
    PLAYER_INFECTION,

}
