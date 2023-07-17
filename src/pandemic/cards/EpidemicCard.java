package pandemic.cards;

import pandemic.*;

/**
 * Reprensent the Epidemic card
 */
public class EpidemicCard extends Card {

    /** A disease */
    protected Disease disease;

    /**
     * Create a epidemic card
     * @param id the id of the card
     * @param d the disease of the card
     */
    public EpidemicCard(int id, Disease d) {
        super(id);
        this.disease = d;
    }
    /**
     * Return the Disease of the epidemic card
     * @return the disease
     */
    public Disease getDisease() {
        return this.disease;
    }

    /**
     * Returns the string representation of this card
     * @return the string representation of this card
     */
    public String toString() {
        return "Epidemic Card [" + this.id + "] of " + this.getDisease().getName();
    }
}