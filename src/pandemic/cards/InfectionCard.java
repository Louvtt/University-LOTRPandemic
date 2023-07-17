package pandemic.cards;

import pandemic.*;

/**
 * Represent the Infection card
 */
public class InfectionCard extends Card {

    /** A city of the map */
    protected City city;

    /** A disease */
    protected Disease disease;

    /**
     * Create the Infection card
     * @param id the id of the card
     * @param c the city of the infection card
     * @param d the disease of the infection card
     */
    public InfectionCard(int id, City c, Disease d) {
        super(id);
        this.city = c;
        this.disease = d;
    }


    /**
     * Return the city of the Infection card
     * @return the city
     */
    public City getCity() {
        return this.city;
    }

    /**
     * Return the disease of the Infection card
     * @return the disease
     */
    public Disease getDisease() {
        return this.disease;
    }

    public String toString() {
        return "Infection Card ["+this.getId()+"] of "+this.getCity().getName()+" for "+ this.getDisease().getName();
    }
}