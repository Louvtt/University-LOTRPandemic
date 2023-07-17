package pandemic.actions;

import java.util.*;

import pandemic.*;
import pandemic.game.Log;

/**
 * Class for the move action
 */
public class MoveAction extends Action {

    /**
     * Create the move action
     */
    public MoveAction() {
        super();
    }

    /**
     * Returns the name of the action
     * @return the name of the action
     */
    public String getName() {
        return "Move";
    }

    /**
     * Return true
     * @param player player that want to execute the action
     * @return true
     */
    public boolean canBeExecuted(Player player) {
        return true;
    }
    
    /**
     * Execute the move action on a player
     * @param p the player who move
     */
    public void execute(Player p) {
        // Find usable cards to travel
        List<String> neighbourCities = new ArrayList<String>();
        for(City c : p.getCity().getNeighbours()) {
            neighbourCities.add(c.getName());
        }
        
        if(neighbourCities.size() > 0) {
            int chosenCardIndex = p.choose("Choose which city to travel to", neighbourCities);
            City chosenCity = p.getCity().getNeighbours().get(chosenCardIndex);
            p.setCity(chosenCity);
            Log.Get().log(p.getName() + " moved to " + p.getCity().getName());
        } else {
            Log.Get().error(p.getName() + " cannot move :(");
        }
    }
}
