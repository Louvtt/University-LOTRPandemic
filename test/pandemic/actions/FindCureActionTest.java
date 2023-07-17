package pandemic.actions;

import pandemic.City;
import pandemic.Disease;
import pandemic.Player;
import pandemic.cards.PlayerCard;
import pandemic.exception.TooManyCardException;
import pandemic.roles.Scientist;

import static org.junit.Assert.*;

import org.junit.Test;

public class FindCureActionTest {
    
    private final static Action a = new FindCureAction();

    // Test the name of the Action
    @Test
    public void TestNameActionIfFindCure() {
        assertEquals("Find cure", a.getName());
    }

    // Test if the actions of a Expert are implented
    
    @Test
    public void TestExecute() {
        Disease d = new Disease("grippe", 24);
        City c1 = new City("Lille",d);
        City c2 = new City("Paris",d);
        City c3 = new City("Marseille",d);
        City c4 = new City("Toulouse",d);
        City c5 = new City("Rennes",d);
        Scientist s = new Scientist();
        Player p = new Player("player1", s, c1);
        PlayerCard card1 = new PlayerCard(1,c1,d);
        PlayerCard card2 = new PlayerCard(2,c2,d);
        PlayerCard card3 = new PlayerCard(3,c3,d);
        PlayerCard card4 = new PlayerCard(4,c4,d);
        PlayerCard card5 = new PlayerCard(5,c5,d);
        try{
            p.drawCard(card1);
            p.drawCard(card2);
            p.drawCard(card3);
            p.drawCard(card4);
            p.drawCard(card5);
        } catch (TooManyCardException e) {
            
        }

        
        assertFalse(d.isCured());
        a.execute(p);
        assertTrue(d.isCured());
    }


    // ---Pour permettre l'execution des tests ----------------------
    public static junit.framework.Test suite() {
        return new junit.framework.JUnit4TestAdapter(pandemic.actions.FindCureActionTest.class);
        }
}