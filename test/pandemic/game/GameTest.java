package pandemic.game;

import java.io.FileNotFoundException;
import java.util.*;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.Before;
import org.junit.Test;

import pandemic.*;
import pandemic.cards.*;
import pandemic.exception.*;
import pandemic.map.*;
import pandemic.roles.*;

public class GameTest {
    
    private Board jsonBoardTest;
    private Player player1;

    @Before
    public void init() 
    throws FileNotFoundException {
        jsonBoardTest = new JSONBoard("assets/carte1.json");
        player1 = new Player("FirstPlayer", new Expert(), jsonBoardTest.getCities().get(0));

        Game.Create(jsonBoardTest);

        Log.Get().logToConsole(false);
    }

    @Test
    public void TestInitialTest() 
    throws NoMoreCubeException, TooMuchInfectionSource, FileNotFoundException{
        Game g = Game.GetInstance();
        assertFalse(g.hasGameEnded());
        g.checkCubes();
        g.checkCityFocus();
    }

    /*
    @Test (expected = NoMoreCardException.class)
    public void TestRunWithEmptyDeckThrowsException() throws NoMoreCardException {
        Game g = new Game(new JSONBoard("assets/carte1.json"));
        g.setPlayerDeck(new Deck<Card>());;
        g.addPlayer(player1);
        g.addPlayer(player2);

        g.processTurn(player1);
    }
    */

    @Test (expected = NoMoreCubeException.class)
    public void TestRunWithNoCubeThrowsException() 
    throws NoMoreCubeException, FileNotFoundException {
        Game game = new Game(new JSONBoard("assets/carte1.json"));

        for(Disease d : game.getBoard().getDiseases()) {
            assertTrue(d.hasCubeLeft());
            while(d.hasCubeLeft()) { d.useCube(); }
            assertFalse(d.hasCubeLeft());
        }

        game.checkCubes();
    }

    @Test
    public void TestPlayerAdded(){
        Game g = new Game(null);
        List<Player> playerList = new ArrayList<Player>();
        playerList.add(player1);

        g.addPlayer(player1);

        assertTrue(g.getPlayers().size() == 1);
        assertIterableEquals(g.getPlayers(), playerList);
    }

    @Test
    public void TestCannotAddMoreThan4Players() {
        Game g = new Game(null);
        for(int i = 0; i < 4; i++) {
            g.addPlayer(new Player("Another one", null, null));
            assertTrue(g.getPlayers().size() == i + 1);
        }

        g.addPlayer(new Player("Another one", null, null));
        assertTrue(g.getPlayers().size() == 4);
    }

    @Test
    public void TestLessThanTwoPlayersRunNothing() 
    throws NoMoreCubeException,NoMoreCardException,TooMuchInfectionSource,FileNotFoundException {
        Game g = new Game(new JSONBoard("assets/carte1.json"));
        g.run();
        // nothing has ended the game (exit the while)
        assertFalse(g.hasGameEnded());
        g.checkCubes();
        g.checkCityFocus();
    }

    @Test (expected = TooMuchInfectionSource.class)
    public void TestCityFocusThrowException() 
    throws TooMuchInfectionSource,FileNotFoundException {
        Game g = new Game(new JSONBoard("assets/carte1.json"));
        for(City c : g.getBoard().getCities()) {
            c.infect(new ArrayList<City>());
            c.infect(new ArrayList<City>());
            c.infect(new ArrayList<City>());
            assertTrue(c.getInfectionLevel() == 3);
        }
        g.checkCityFocus();
    }

    @Test
    public void testPlayerDeck() {
        Deck<Card> c = new Deck<Card>();
        Game g = new Game(null);
        g.setPlayerDeck(c);
        assertEquals(c, g.getPlayerDeck());
    }

    @Test
    public void testInfectionDeck() {
        Deck<InfectionCard> c = new Deck<InfectionCard>();
        Game g = new Game(null);
        g.setInfectionDeck(c);
        assertEquals(c, g.getInfectionDeck());
    }

    @Test
    public void testDistributeToPlayer() 
    throws NoMoreCardException, FileNotFoundException {
        List<Card> someCards = new LinkedList<Card>();
        Board b = new JSONBoard("assets/carte1.json");
        Disease d = b.getDiseases().get(0);
        for(int i = 0; i < b.getCities().size(); ++i) {
            someCards.add(new PlayerCard(i, b.getCities().get(i), d));
        }

        Game g = new Game(b);
        g.setPlayerDeck(new Deck<Card>(someCards));
        g.addPlayer(new Player("First", new Expert(), b.getCities().get(0)));
        g.addPlayer(new Player("Second", new Scientist(), b.getCities().get(1)));

        for(Player p : g.getPlayers()) {
            assertTrue(p.getCardNumber() == 0);
        }
        g.distributeToPlayers();
        for(Player p : g.getPlayers()) {
            assertTrue(p.getCardNumber() == 4);
        }
    }

    @Test (expected = NoMoreCardException.class)
    public void testCannotDistributeToPlayerFromEmptyDeck() 
    throws NoMoreCardException,FileNotFoundException {
        Board b = new JSONBoard("assets/carte1.json");

        Game g = new Game(b);
        g.addPlayer(new Player("First", new Expert(), b.getCities().get(0)));
        g.addPlayer(new Player("Second", new Scientist(), b.getCities().get(1)));

        for(Player p : g.getPlayers()) {
            assertTrue(p.getCardNumber() == 0);
        }
        g.distributeToPlayers();
    }

    @Test (expected = TooManyCardException.class)
    public void testTooManyCardsAreDiscarded()
    throws Exception {
        Game g = new GameConfig()
            .addPlayer("First").addPlayer("Second")
            .setBoard(new JSONBoard("assets/carte1.json"))
            .addRole(new Expert())
            .addRole(new Scientist())
            .build();
        g.distributeToPlayers();
        for(Player p : g.getPlayers()) {
            while(true) {
                p.drawCard(new PlayerCard(0, null, null));
            }
        }
    }

    // ---Pour permettre l'execution des tests ----------------------
    public static junit.framework.Test suite() {
        return new junit.framework.JUnit4TestAdapter(pandemic.game.GameTest.class);
    }
}
