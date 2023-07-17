package pandemic.roles;

import pandemic.actions.*;

/**
 * Represent the GlobeTrotter role
 */
public class Globetrotter extends Role {
    /**
     * Create a globetrotter
     */
    public Globetrotter() {
        super("Globetrotter");
        
        this.actions.add(new MoveActionGlobeTrotter());
        this.actions.add(new TreatDiseaseAction());
        this.actions.add(new FindCureAction());
        this.actions.add(new BuildCenterAction());
    }
}
