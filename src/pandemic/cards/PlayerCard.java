package pandemic.cards;

import pandemic.*;

/**
 * Represent the player card
 */
public class PlayerCard extends Card {

    /** A city */
    protected City city;

    /** a disease */
    protected Disease disease;

    /**
     * Create the player card
     * @param id the id of the player card
     * @param c the city of the player card
     * @param d the disease of the player card
     */
    public PlayerCard(int id, City c, Disease d) {
        super(id);
        this.city = c;
        this.disease = d;
    }

    /**
     * Return the city of the player card
     * @return the city
     */
    public City getCity() {
        return this.city;
    }

    /**
     * Return the disease of the player card
     * @return the disease
     */
    public Disease getDisease() {
        return this.disease;
    }

    public String toString() {
        return "Player Card ["+this.getId()+"] of "+this.getCity().getName()+" for "+ this.getDisease().getName();
    }
}