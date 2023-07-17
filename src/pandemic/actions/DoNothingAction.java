package pandemic.actions;
import pandemic.*;
import pandemic.game.Log;

/**
 * Class for no actions
 */
public class DoNothingAction extends Action {

    /**
     * Create the action 'do nothing'
     */
    public DoNothingAction() {
        super();
    }

    /**
     * Execute 'do nothing'
     * @param p the player who do nothing
     */
    public void execute(Player p) {
        Log.Get().log("player " + p.getName() + " did nothing");
    }
    
    /**
     * Returns the name of the action
     * @return the name of the action
     */
    public String getName() {
        return "Nothing";
    }

    /**
     * Returns true
     * @param player player that want to execute the action
     * @return true
     */
    public boolean canBeExecuted(Player player) {
        return true;
    }
}
