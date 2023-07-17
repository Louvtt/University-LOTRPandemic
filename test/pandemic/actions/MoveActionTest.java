package pandemic.actions;

import pandemic.City;
import pandemic.Disease;
import pandemic.Player;
import pandemic.cards.PlayerCard;
import pandemic.exception.TooManyCardException;
import pandemic.game.Game;
import pandemic.map.JSONBoard;
import pandemic.roles.Scientist;

import static org.junit.Assert.*;

import org.junit.Test;

public class MoveActionTest {
    
    private final static Action a = new MoveAction();

    // Test the name of the Action
    @Test
    public void TestNameActionIfMove() {
        assertEquals("Move", a.getName());
    }

    //Test if the actions of a Expert are implented
    
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

        c1.addNeighbour(c2);
        c1.addNeighbour(c3);
        c1.addNeighbour(c4);
        c1.addNeighbour(c5);

        try{
            p.drawCard(card1);
            p.drawCard(card2);
        } catch (TooManyCardException e) {}

        try {
            Game.Create(new JSONBoard("assets/carteTest.json"));
        } catch(Exception e) { assertTrue(false);}
        a.execute(p);
        assertTrue(c1.getNeighbours().contains(p.getCity()));
    }


    // ---Pour permettre l'execution des tests ----------------------
    public static junit.framework.Test suite() {
        return new junit.framework.JUnit4TestAdapter(pandemic.actions.MoveActionTest.class);
        }
}