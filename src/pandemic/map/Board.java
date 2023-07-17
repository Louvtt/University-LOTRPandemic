package pandemic.map;
import pandemic.*;

import java.util.*;

/**
 * Represent the game board
 */
public abstract class Board {

    /** List of cities in the board */
    protected List<City> cities;
    /** List of diseases in the board */
    protected List<Disease> diseases;
    
    /**
     * Create the board
     */
    public Board() {
        this.cities = new ArrayList<City>();
        this.diseases = new ArrayList<Disease>();
    }

    /** 
     * Add a city in the list 'cities'
     * @param c the city to add
     */
    public void addCity(City c) {
        this.cities.add(c);
    }

    /**
     * Return the cities of this board
     * @return the cities of this board
     */
    public List<City> getCities() {
        return this.cities;
    }

    /**
     ** Return the diseases of this board
     * @return the diseases of this board
     */
    public List<Disease> getDiseases() {
        return this.diseases;
    }

    /**
     * Add a disease to the board
     * @param d disease to add to the board
     */
    public void addDisease(Disease d) {
        this.diseases.add(d);
    }


    /**
     * Returns the string representation of this board
     * @return the string representation of this board
     */
    public String toString() {
        String res = "La liste des villes :";
        for (City c : this.cities) {
            res += "\n - " + c;
        }
        res += "\nLa liste des maladies: ";
        for (Disease d : this.diseases) {
            res += "\n - " + d;
        }
        return res;
    }
}
