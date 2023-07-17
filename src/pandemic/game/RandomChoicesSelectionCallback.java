package pandemic.game;

import java.util.List;
import java.util.Random;

/**
 * Random Choice Selection Callback
 */
public class RandomChoicesSelectionCallback implements ChoicesSelectionCallback {

    /** Random instance */
    protected Random rand = new Random();

    /** Create a Random Choice Selection Callback  */
    public RandomChoicesSelectionCallback() {
        super();
    }

    /**
     * Choose a random option among a list of options
     * @param message title of the choice to make
     * @param options options
     * @return random option 
     */
    @Override
    public int choose(String message, List<String> options) {
       return rand.nextInt(options.size());
    }
}
