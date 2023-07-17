package pandemic.roles;

import pandemic.actions.*;

/**
 * Represent the expert role
 */
public class Expert extends Role {
    /**
     * Create an expert
     */
    public Expert() {
        super("Expert");
        
        this.actions.add(new MoveAction());
        this.actions.add(new TreatDiseaseAction());
        this.actions.add(new FindCureAction());
        this.actions.add(new BuildCenterExpertAction());
    }
}