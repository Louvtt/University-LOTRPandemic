package pandemic.roles;

import pandemic.actions.*;

/**
 * Represent the scientist role
 */
public class Scientist extends Role {
    /**
     * Create a scientist
     */
    public Scientist() {
        super("Scientist");
        
        this.actions.add(new MoveAction());
        this.actions.add(new TreatDiseaseAction());
        this.actions.add(new FindCureScientistAction());
        this.actions.add(new BuildCenterAction());
    }
}

