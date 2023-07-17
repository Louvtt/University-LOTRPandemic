package pandemic.game;

import java.util.*;

/**
 * Interactive Choices Selection Callback
 */
public class InteractiveChoicesSelectionCallback implements ChoicesSelectionCallback {

    /** Scanner */
    private static final Scanner scanner = new Scanner(System.in);

    /** Create a Interactive Choices Selection Callback */
    public InteractiveChoicesSelectionCallback() {
        super();
    }

    /**
     * Choose an option from a list of option
     * @param message title of the choice to make
     * @param options options to choose from
     * @return the chosen option (from cli)
     */
    public int choose(String message, List<String> options) {
        String optionStr = message + "\n";
        for (int i = 0; i < options.size(); ++i) {
            optionStr += String.format("\t%1$3d : %2$s\n", i, options.get(i));
        }
        Log.Get().log(optionStr);

        int chosenOption = -1;
        while(chosenOption < 0 || chosenOption >= options.size()) {
            Log.Get().log(">", ' ');
            try {
                chosenOption = scanner.nextInt();
            } catch (Exception e) {
                scanner.skip(".*");
            }
        } 
        return chosenOption;
    }
}
