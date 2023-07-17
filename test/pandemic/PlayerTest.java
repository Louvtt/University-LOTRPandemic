package pandemic;

import pandemic.cards.*;
import pandemic.exception.TooManyCardException;
import pandemic.roles.*;
import pandemic.game.GameSettings;

import java.util.*;

import static org.junit.Assert.*;

import org.junit.Test;

public class PlayerTest {
    
    //Creation of a Player
    private final static Role doc = new Doctor();
    private final static Disease d = new Disease("oui", 1);
    private final static City c = new City("Poperingue", d);
    private final static Player p = new Player("Yves", doc, c);

    //Test if the creation looks good
    @Test
    public void TestCreationPlayer() {
        assertEquals(p.getName(), "Yves");
        assertEquals(p.getRole(), doc);
        assertEquals(p.getCity(), c);
    }

    // Test of the fonction setCity()
    @Test
    public void TestSetCity() {
        City c2 = new City("Rambouillet", d);
        assertEquals(p.getCity(), c);
        p.setCity(c2);
        assertEquals(p.getCity(), c2);
        p.setCity(c); // reset
        assertEquals(p.getCity(), c);
    }

    // Test if at the creation of a player, the hand is empty
    @Test
    public void TestEmptyHands() {
        assertTrue(p.getCardNumber() == 0);
        assertTrue(p.getHand().size() == 0);
    }

    //Test of draw and discard in the hand
    @Test
    public void TestDrawPlayerCardAndDiscard() {
        assertTrue(p.getCardNumber() == 0);
        PlayerCard card = new PlayerCard(1, c, d);
        try {
            p.drawCard(card);
        } catch (TooManyCardException e) { }
        List<PlayerCard> hand2 = new ArrayList<PlayerCard>();
        hand2.add(card);
        assertEquals(p.getHand(), hand2);
        assertTrue(p.getCardNumber() == 1);
        p.discardCard(0);
        assertFalse(p.getCardNumber() == 1);
        hand2.remove(0);
        assertEquals(p.getHand(), hand2);
    }

    @Test (expected = TooManyCardException.class)
    public void TestDrawTooManyCards() throws TooManyCardException {
        Player p = new Player("Someone", null, null);

        int i = 0;
        for(i = 0; i < GameSettings.getInstance().getMaxCards(); ++i) {
            try {
                p.drawCard(new PlayerCard(i, null, null)); 
            } catch (TooManyCardException e) {}
        }

        // trop de carte
        p.drawCard(new PlayerCard(i, null, null)); 
    }

    // ---Pour permettre l'execution des tests ----------------------
    public static junit.framework.Test suite() {
        return new junit.framework.JUnit4TestAdapter(pandemic.PlayerTest.class);
    }
}