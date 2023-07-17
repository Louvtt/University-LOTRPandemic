package pandemic.game;


import static org.junit.Assert.*;

import java.io.FileNotFoundException;

import org.junit.Before;
import org.junit.Test;

import pandemic.Player;
import pandemic.exception.MissingBoardException;
import pandemic.exception.NotAValidNumberOfPlayers;
import pandemic.exception.NotEnoughRolesException;
import pandemic.map.Board;
import pandemic.map.JSONBoard;
import pandemic.roles.Expert;
import pandemic.roles.Scientist;

public class GameConfigTest {

    @Before
    public void init() {
        Game.DeleteInstance();
    }

    @Test (expected = MissingBoardException.class)
    public void testNullMapBuild() 
    throws MissingBoardException,NotAValidNumberOfPlayers, NotEnoughRolesException{
        GameConfig g = new GameConfig();
        g.build();
    }

    @Test (expected = NotAValidNumberOfPlayers.class)
    public void testNotEnoughPlayer() 
    throws MissingBoardException,NotAValidNumberOfPlayers, NotEnoughRolesException,FileNotFoundException {
        GameConfig g = new GameConfig();
        g.setBoard(new JSONBoard("assets/carteTest.json"));
        g.build();
    }

    @Test (expected = NotAValidNumberOfPlayers.class)
    public void testTooManyPlayers() 
    throws MissingBoardException,NotAValidNumberOfPlayers, NotEnoughRolesException,FileNotFoundException {
        GameConfig g = new GameConfig();
        g.setBoard(new JSONBoard("assets/carteTest.json"));
        g.addPlayer("Player1");
        g.addPlayer("Player2");
        g.addPlayer("Player3");
        g.addPlayer("Player4");
        g.addPlayer("Player5"); // the one that is too much
        g.build();
    }

    @Test (expected = NotEnoughRolesException.class)
    public void testNotEnoughRole() 
    throws MissingBoardException,NotAValidNumberOfPlayers, NotEnoughRolesException,FileNotFoundException {
        GameConfig g = new GameConfig();
        g.setBoard(new JSONBoard("assets/carteTest.json"));
        g.addPlayer("Player1");
        g.addPlayer("Player2");
        g.addRole(new Expert());
        g.build();
    }

    @Test
    public void cannotAddTwiceTheSameRole() {
        GameConfig g = new GameConfig();
        assertTrue(g.getRoles().size() == 0);
        g.addRole(new Expert());
        assertTrue(g.getRoles().size() == 1);
        g.addRole(new Expert());
        assertTrue(g.getRoles().size() == 1);
    }

    @Test
    public void testGameStateAfterBuilt() 
    throws MissingBoardException,NotAValidNumberOfPlayers, NotEnoughRolesException,FileNotFoundException {
        GameConfig g = new GameConfig();
        Board b = new JSONBoard("assets/carteTest.json");
        g.setBoard(b);
        g.addHumanPlayer("Player1");
        g.addPlayer("Player2");
        g.addRole(new Expert());
        g.addRole(new Scientist());
        Game builtGame = g.build();

        assertEquals(builtGame.getBoard(), b);
        assertSame(b.getCities().size() + b.getDiseases().size(), builtGame.getPlayerDeck().getDrawPile().size());
        assertSame(b.getCities().size(), builtGame.getInfectionDeck().getDrawPile().size());
        assertSame(2, builtGame.getPlayers().size());
        for(Player p : builtGame.getPlayers()) {
            assertTrue(p.getRole() != null);
            assertTrue(p.getCity() != null);
        }
    }
}
