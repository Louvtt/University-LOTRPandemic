package pandemic.actions;
import java.util.ArrayList;
import java.util.List;

import pandemic.*;
import pandemic.cards.*;

/**
 * Class for find a cure
 */
public class FindCureAction extends Action {

    /**
     * Create the Find Cure action
     */
    public FindCureAction() {
        super();
    }

    /**
     * Returns true if the player has enough card to find a cure, false otherwise
     * @param p player that want to execute the action
     * @return true if the player has enough card to find a cure, false otherwise
     */
    public boolean canBeExecuted(Player p) {
        int count = 0;
        Disease d = p.getCity().getInfectionType();
        for(PlayerCard c : p.getHand()) {
            if(c.getDisease().equals(d)) {
                count++;
            }
        }
        return count >= 5;
    }
    
    /**
     * Execute the Find Cure action
     * @param p the player who find the cure 
     */
    public void execute(Player p) {
        int count = 0;
        Disease d = p.getCity().getInfectionType();
        List <PlayerCard> toRemove = new ArrayList<PlayerCard>();
        for(PlayerCard c : p.getHand()) {
            if(c.getDisease().equals(d)) {
                count++;
                toRemove.add(c);
                if(count >= 5) break;
            }
        }
        if(count>=5) {
            p.getHand().removeAll(toRemove);
            d.hasFoundCure();
        }
    }
    
    /**
     * Returns the name of the action
     * @return the name of the action
     */
    public String getName() {
        return "Find cure";
    }
}
