package pandemic.actions;
import pandemic.*;

/**
 * Class for treat disease actions
 */
public class TreatDiseaseAction extends Action {

    /**
     * Create the Treat disease action
     */
    public TreatDiseaseAction() {
        super();
    }

    /**
     * Return true
     * @param p player that want to execute the action
     * @return true
     */
    public boolean canBeExecuted(Player p) {
        return true;
    }
    
    /**
     * Execute the treat disease action
     * @param p the player who treat the disease
     */
    public void execute(Player p) {
        if(!(p.getCity().getInfectionType().isCured())) 
            p.getCity().cure();
        else {
            if(p.getCity().getInfectionLevel() > 0) {
                p.getCity().cure();
            }
        }
    }
    
    /**
     * Returns the name of the action
     * @return the name of the action
     */
    public String getName() {
        return "Treat disease";
    }
}
