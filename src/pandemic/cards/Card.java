package pandemic.cards;

/**
* Represent the cards
*/
public abstract class Card {

    /** The id of a card */
    protected int id;

    /**
     * Create a Card with a id
     * @param id the id of the card
     */
    public Card(int id) {
        this.id = id;
    }

    /**
     * Return the id of the card
     * @return the id
     */
    public int getId() {
        return this.id;
    }

    /**
     * Returns the string representation of this card
     * @return the string representation of this card
     */
    public String toString() {
        return "Card " + this.id;
    }

    public boolean equals(Object obj) {
        if(!(obj instanceof Card)) {
            return false;
        }
        Card c = (Card)obj;
        return c.getId() == this.id;
    }
}