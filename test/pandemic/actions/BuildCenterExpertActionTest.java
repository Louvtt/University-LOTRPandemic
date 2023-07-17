package pandemic.actions;

import pandemic.City;
import pandemic.Disease;
import pandemic.Player;
import pandemic.roles.Expert;


import static org.junit.Assert.*;

import org.junit.Test;

public class BuildCenterExpertActionTest {
    
    private final static Action a = new BuildCenterExpertAction();

    //Test if the actions of a Expert are implented
    
    @Test
    public void TestExecute() {
        Disease d = new Disease("grippe", 24);
        City c1 = new City("Lille",d);
        City c2 = new City("Paris",d);

        Expert s = new Expert();
        Player p = new Player("player1", s, c1);


        a.execute(p);
        assertTrue(c1.hasResearchedCenter());
        assertFalse(c2.hasResearchedCenter());
    }


    // ---Pour permettre l'execution des tests ----------------------
    public static junit.framework.Test suite() {
        return new junit.framework.JUnit4TestAdapter(pandemic.actions.BuildCenterExpertActionTest.class);
        }
}