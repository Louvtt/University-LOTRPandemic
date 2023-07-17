package pandemic.actions;

import pandemic.City;
import pandemic.Disease;
import pandemic.Player;
import pandemic.roles.Scientist;

import static org.junit.Assert.*;

import org.junit.Test;

public class TreatDiseaseActionTest {
    
    private final static Action a = new TreatDiseaseAction();

    // Test the name of the Action
    @Test
    public void TestNameActionIfDiseaseTreated() {
        assertEquals("Treat disease", a.getName());
    }

    //Test if the actions of a Expert are implented
    
    @Test
    public void TestExecute() {
        Disease d = new Disease("grippe", 24);
        City c1 = new City("Lille",d);

        Scientist s = new Scientist();
        Player p = new Player("player1", s, c1);

        c1.infect();

        a.execute(p);
        assertEquals(c1.getInfectionLevel(),0);
    }

    @Test
    public void TestExecuteCured() {
        Disease d = new Disease("grippe", 24);
        d.hasFoundCure();
        City c1 = new City("Lille",d);

        Scientist s = new Scientist();
        Player p = new Player("player1", s, c1);

        c1.infect();
        a.execute(p);
        assertEquals(c1.getInfectionLevel(),0);
    }


    // ---Pour permettre l'execution des tests ----------------------
    public static junit.framework.Test suite() {
        return new junit.framework.JUnit4TestAdapter(pandemic.actions.TreatDiseaseActionTest.class);
        }
}