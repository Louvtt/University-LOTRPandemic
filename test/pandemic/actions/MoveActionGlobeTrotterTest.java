package pandemic.actions;

import pandemic.City;
import pandemic.Disease;
import pandemic.Player;
import pandemic.game.Game;
import pandemic.map.JSONBoard;
import pandemic.roles.Globetrotter;

import static org.junit.Assert.*;

import java.io.FileNotFoundException;
import java.util.List;

import org.junit.Before;
import org.junit.Test;

public class MoveActionGlobeTrotterTest {
    
    private final static Action a = new MoveActionGlobeTrotter();
    //Test if player can move anywhere

    @Before
    public void init() 
    throws FileNotFoundException {
        if(Game.GetInstance() == null || Game.GetInstance().getBoard() == null) {
            Game.Create(new JSONBoard("assets/carte1.json"));
        }
    }
    
    @Test
    public void TestExecute() {
        Disease d = new Disease("grippe", 24);
        City c1 = new City("Lille",d);
        City c2 = new City("Paris",d);
        City c3 = new City("Marseille",d);
        City c4 = new City("Toulouse",d);
        City c5 = new City("Rennes",d);
        Globetrotter s = new Globetrotter();
        List <City> cities = Game.GetInstance().getBoard().getCities();
        cities.add(c2);
        cities.add(c3);
        cities.add(c4);
        cities.add(c5);
        Player p = new Player("player1", s, c1);

        a.execute(p);

        assertTrue(cities.contains(p.getCity()));
    }


    // ---Pour permettre l'execution des tests ----------------------
    public static junit.framework.Test suite() {
        return new junit.framework.JUnit4TestAdapter(pandemic.actions.MoveActionGlobeTrotterTest.class);
        }
}