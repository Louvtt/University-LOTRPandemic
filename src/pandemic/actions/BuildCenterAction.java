package pandemic.actions;

import pandemic.cards.PlayerCard;
import pandemic.*;
import pandemic.game.Log;

/**
 * Class for build a center
 */
public class BuildCenterAction extends Action {
    
    /**
     * Create the Build Center Action
     */
    public BuildCenterAction() {
        super();
    }

    /**
     * Returns true if the player has the correct card, false otherwise
     * @param p player that want to execute the action
     * @return true if the player has the correct card, false otherwise
     */
    public boolean canBeExecuted(Player p) {
        PlayerCard c = null;
        for(PlayerCard card : p.getHand()){
            if(card.getCity().equals(p.getCity())) {
                c = card;
                break;
            }
        }

        return c != null && !p.getCity().hasResearchedCenter();
    }
    
    /**
     * Execute the Build Center Action
     * @param p the player who build the center
     */
    public void execute(Player p) {
        PlayerCard c = null;
        for(PlayerCard card : p.getHand()){
            if(card.getCity().equals(p.getCity())) {
                c = card;
                break;
            }
        }

        if(c != null && !p.getCity().hasResearchedCenter()) {
            p.getHand().remove(c);
            p.getCity().addResearchCenter();
        } else {
            Log.Get().error("Cannot build center here");
        }
    }

    /**
     * Returns the name of this action
     * @return the name of this action
     */
    public String getName() {
        return "Build center";
    }
}
