package pandemic.actions;

import pandemic.Player;

/**
 * Class for treat disease action but for a Doctor
 */
public class TreatDiseaseDoctorAction extends TreatDiseaseAction {
    /**
     * Create a treat disease doctor action
     */
    public TreatDiseaseDoctorAction() {
        super();
    }

    /**
     * Execute the treat disease action
     * @param p the player who treat the disease
     */
    public void execute(Player p) {
        while (p.getCity().getInfectionLevel()>0) {
            p.getCity().cure();
        }
    }
}
