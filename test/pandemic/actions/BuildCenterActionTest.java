package pandemic.actions;

import pandemic.City;
import pandemic.Disease;
import pandemic.Player;
import pandemic.cards.PlayerCard;
import pandemic.exception.TooManyCardException;
import pandemic.roles.Scientist;

import static org.junit.Assert.*;

import org.junit.Test;

public class BuildCenterActionTest {
    
    private final static Action a = new BuildCenterAction();

    // Test the name of the Action
    @Test
    public void TestNameActionIfBuildCenter() {
        assertEquals("Build center", a.getName());
    }

    //Test if the actions of a Expert are implented
    
    @Test
    public void TestExecute() {
        Disease d = new Disease("grippe", 24);
        City c1 = new City("Lille",d);
        City c2 = new City("Paris",d);

        Scientist s = new Scientist();
        Player p = new Player("player1", s, c1);
        PlayerCard card1 = new PlayerCard(1,c1,d);
        PlayerCard card2 = new PlayerCard(2,c2,d);
        try {
            p.drawCard(card1);
            p.drawCard(card2);
        } catch (TooManyCardException e) {

        }


        a.execute(p);
        assertTrue(c1.hasResearchedCenter());
        assertFalse(c2.hasResearchedCenter());
    }


    // ---Pour permettre l'execution des tests ----------------------
    public static junit.framework.Test suite() {
        return new junit.framework.JUnit4TestAdapter(pandemic.actions.BuildCenterActionTest.class);
        }
}