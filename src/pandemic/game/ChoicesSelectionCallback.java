package pandemic.game;

import java.util.List;

/** 
 * Choices selection Callback 
 */
@FunctionalInterface
public interface ChoicesSelectionCallback {
    /**
     * Choose an option from a list of option
     * @param message message for this selection
     * @param options options to choose from
     * @return the chosen option (from cli)
     */
    public int choose(String message, List<String> options);
}
