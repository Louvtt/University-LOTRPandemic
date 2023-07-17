package pandemic.actions;

import pandemic.City;
import pandemic.Disease;
import pandemic.Player;
import pandemic.roles.Doctor;

import static org.junit.Assert.*;

import org.junit.Test;

public class TreatDiseaseDoctorActionTest {
    
    private final static Action a = new TreatDiseaseDoctorAction();


    //Test if the actions of a Expert are implented
    
    @Test
    public void TestExecute() {
        Disease d = new Disease("grippe", 24);
        City c1 = new City("Lille",d);

        Doctor s = new Doctor();
        Player p = new Player("player1", s, c1);

        c1.infect();
        c1.infect();
        c1.infect();


        a.execute(p);   
        assertEquals(c1.getInfectionLevel(),0);
    }


    // ---Pour permettre l'execution des tests ----------------------
    public static junit.framework.Test suite() {
        return new junit.framework.JUnit4TestAdapter(pandemic.actions.TreatDiseaseDoctorActionTest.class);
        }
}