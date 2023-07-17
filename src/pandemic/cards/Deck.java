package pandemic.cards;

import java.util.List;
import java.util.Random;

import pandemic.exception.NoMoreCardException;

import java.util.LinkedList;

/**
 * Reprensent a deck of card
 * @param <C> type of the card stored
 */
public class Deck<C extends Card> {
    
    /** The list of cards */
    protected List<C> deck;

    /** the list of discard */
    protected List<C> discardPile;

    /** a random number */
    private static final Random RAND = new Random();

    /**
     * Create the Deck
     */
    public Deck() {
        this.deck = new LinkedList<C>();
        this.discardPile = new LinkedList<C>();
    }

    /**
     * Create the Deck with the cards in parameter
     * @param cards the list of cards
     */
    public Deck(List<C> cards) {
        this();
        this.deck = cards;
    }

    /**
     * Return the draw card
     * @return the draw card
     * @throws NoMoreCardException the exception if there are no cards in the Deck
     */
    public C draw() throws NoMoreCardException {
        if(deck.size() > 0) {
            // remove and return first
            return deck.remove(0);
        } else if (discardPile.size() > 0) {
            this.reset();
            return deck.remove(0);
        } 
        // no cards
        throw new NoMoreCardException(); 
    }

    /**
     * Shuffle the deck
     */
    public void shuffle() {
        for(int i = 0; i < deck.size(); ++i) {
            int newI = i + RAND.nextInt(deck.size()-i);
            C tmp = deck.get(newI);
            deck.set(newI, deck.get(i));
            deck.set(i, tmp);
        }
    }

    /**
     * Add the card in parameter to the discard pile
     * @param card the card to dicard
     */
    public void discard(C card) {
        this.discardPile.add(card);
    }

    /**
     * Reset the discard pile
     */
    public void reset() {
        while(this.discardPile.size() > 0) {
            this.deck.add(this.discardPile.remove(0));
        }
        this.shuffle();
    }

    /**
     * Returns the draw pile
     * @return the draw pile
     */
    public List<C> getDrawPile() {
        return deck;
    }

    /**
     * Returns the discard pile
     * @return the discard pile
     */
    public List<C> getDiscardPile(){
        return this.discardPile;
    }

    /**
     * Returns the string representation of this deck
     * @return the string representation of this deck
     */
    public String toString() {
        String res = "Deck [";
        for (C c : this.deck) {
            res += c + " ";
        }
        res += "]\nDiscardPile: [";
        for (C c : this.deck) {
            res += c + " ";
        }
        res += "]";
        return res;
    }
}