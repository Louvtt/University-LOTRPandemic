package pandemic.exception;

/**
 * Represent the exception of no card in Deck
 */
public class NoMoreCardException extends Exception {

    /**
     * Create the no more card Exception
     * @param message message to display
     */
    public NoMoreCardException(String message) {
        super(message);
    }

    /**
     * Create the no more card Exception
     */
    public NoMoreCardException() {
        this("");
    }
}
