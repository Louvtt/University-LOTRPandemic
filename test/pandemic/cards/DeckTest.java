package pandemic.cards;

import static org.junit.Assert.*;

import org.junit.Test;
import pandemic.exception.NoMoreCardException;

import java.util.*;

public class DeckTest {

    private static final int NUMBER_OF_CARDS = 10;

    @Test
    public void cardwasDrawed(){
        List<Card> cards = new ArrayList<Card>();
        Card c = new PlayerCard(0, null, null);
        cards.add(c);
        Deck<Card> deck = new Deck<Card>(cards);
        try {
            assertEquals(deck.draw(), c);
        } catch (NoMoreCardException e) {}
    }

    @Test (expected = NoMoreCardException.class)
    public void cannotDrawCardIfDeckEmpty() throws NoMoreCardException {
        Deck<Card> deck = new Deck<Card>();
        deck.draw();
    }

    @Test
    public void drawingCardFromEmptyDrawPileResetTheDeck() {
        Deck<Card> deck = new Deck<Card>();
        deck.discard(new PlayerCard(0, null, null));
        deck.discard(new PlayerCard(1, null, null));

        assertTrue(deck.getDrawPile().size() == 0);
        assertTrue(deck.getDiscardPile().size() == 2);
        try {
            deck.draw();
        } catch (NoMoreCardException e) {}
        assertTrue(deck.getDrawPile().size() == 1);
        assertTrue(deck.getDiscardPile().size() == 0);
    }

    @Test
    public void deckShuffled(){
        List<Card> cards = new ArrayList<Card>();
        for (int i = 0; i < NUMBER_OF_CARDS; i++) {
            cards.add(new PlayerCard(i, null, null));
        }

        Deck<Card> deck = new Deck<Card>(cards);
        List<Card> drawPileBeforeShuffle = new LinkedList<Card>(deck.getDrawPile());
        deck.shuffle();

        assertNotEquals(deck.getDrawPile(), drawPileBeforeShuffle);
    }

    @Test
    public void cardDiscarded(){
        Card card = new PlayerCard(155, null, null);
        Deck<Card> deck = new Deck<Card>();
        deck.discard(card);
        assertEquals(card, deck.getDiscardPile().get(deck.getDiscardPile().size() - 1));
    }

    @Test
    public void discardPileReset(){
        Deck<Card> deck = new Deck<Card>();
        for (int i = 0; i < NUMBER_OF_CARDS; i++) {
            deck.discard(new PlayerCard(i, null, null));
        }
        assertEquals(deck.getDiscardPile().size(), NUMBER_OF_CARDS);

        deck.reset();
        assertEquals(deck.getDiscardPile().size(), 0);
    }

    // ---Pour permettre l'execution des tests ----------------------
    public static junit.framework.Test suite() {
        return new junit.framework.JUnit4TestAdapter(pandemic.cards.DeckTest.class);
    }
}
