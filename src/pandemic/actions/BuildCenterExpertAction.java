package pandemic.actions;

import pandemic.Player;

/**
 * Class for build a center but for the expert
 */
public class BuildCenterExpertAction extends BuildCenterAction {
    
    /**
     * Creates a build center expert action
     */
    public BuildCenterExpertAction() {
        super();
    }

    /**
     * Returns true
     * @param p player that want to execute the action
     * @return true
     */
    public boolean canBeExecuted(Player p) {
        return true;
    }

    /**
     * Execute the Build Center Action for this expert
     * @param p the expert player who build the center
     */
    public void execute(Player p) {
        p.getCity().addResearchCenter();
    }

}
