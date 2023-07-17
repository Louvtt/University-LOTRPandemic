package pandemic.roles;

import pandemic.actions.*;

/**
 * Represent the doctor role
 */
public class Doctor extends Role {
    
    /**
     * Creat a doctor
     */
    public Doctor() {
        super("Doctor");
        
        this.actions.add(new MoveAction());
        this.actions.add(new TreatDiseaseDoctorAction());
        this.actions.add(new FindCureAction());
        this.actions.add(new BuildCenterAction());
    }
}
