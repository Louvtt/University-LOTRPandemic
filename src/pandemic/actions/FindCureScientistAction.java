package pandemic.actions;

import java.util.ArrayList;
import java.util.List;

import pandemic.Disease;
import pandemic.Player;
import pandemic.cards.PlayerCard;

/**
 * Class for find a cure but for the scientist
 */
public class FindCureScientistAction extends FindCureAction {
    /**
     * Create a find cure scientist action
     */
    public FindCureScientistAction() {
        super();
    }

    public boolean canBeExecuted(Player p) {
        int count = 0;
        Disease d = p.getCity().getInfectionType();
        for(PlayerCard c : p.getHand()) {
            if(c.getDisease().equals(d)) {
                count++;
            }
        }
        return count >= 4;
    }

    /**
     * Execute the find cure action for a player
     * @param p the player that executes this action
     */
    public void execute(Player p) {
        int count = 0;
        Disease d = p.getCity().getInfectionType();
        for(PlayerCard c : p.getHand()) {
            if(c.getDisease().equals(d)) {
                count++;
            }
        }
        
        if(count>=4) {
            List <PlayerCard> toRemove = new ArrayList<PlayerCard>();
            for(PlayerCard c : p.getHand()) {
                if(c.getDisease().equals(d) && count>0) {
                    count--;
                    toRemove.add(c);
                }
            }
            p.getHand().removeAll(toRemove);
            if(count==0) {
                d.hasFoundCure();
            }
        }
    }        
}
