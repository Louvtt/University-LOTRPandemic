package pandemic.game;

/**
 * The Settings of the Game
 */
public class GameSettings {

    /** The max cards in the hand of a player*/
    private int maxCards   = 7;

    /** the max actions during a turn */
    private int maxActions = 4;
    private int cardsDrawnPerTurn = 2;

    /** Unique instance of the settings */
    private static GameSettings instance = null;

    /**
     * Create GameSettings
     */
    private GameSettings() {}

    /**
     * Return the instance of settings
     * @return the instance
     */
    public static GameSettings getInstance() {
        if (GameSettings.instance != null) return instance;
        instance = new GameSettings();
        return instance; 
    }

    /**
     * Return the max actions
     * @return the max actions
     */
    public int getMaxActions() {
        return maxActions;
    }

    /**
     * Return the max cards in a player hand
     * @return the max cards in a player hand
     */
    public int getMaxCards() {
        return maxCards;
    }

    /**
     * Returns the number of card that must be drawn per turn
     * @return the number of card that must be drawn per turn
     */
    public int getCardsDrawnPerTurn() {
        return cardsDrawnPerTurn;
    }
}
