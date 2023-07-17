package pandemic.exception;

/**
 * Represent when we have not a valid number of players when starting a game
 */
public class NotAValidNumberOfPlayers extends Exception {
    /**
     * Create the no a valid number of players Exception
     * @param message message to display
     */
    public NotAValidNumberOfPlayers(String message) {
        super(message);
    }
}
