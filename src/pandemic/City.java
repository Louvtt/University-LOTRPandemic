package pandemic;

import java.util.*;

/**
* Represent a city
*/
public class City {
    /** Infection level of this city */
    private int infectionLevel;
    /** Type of disease of this city */
    private Disease infectionType;
    /** Name of this city */
    private String name;
    /** The presence of a research center in the city */
    private boolean researchCenter;
    /** The list of city neighbouring this one */
    private List<City> neighbours;
    /** If the city can be infected */
    private boolean stillInfectable;
    
    /**
    * Create a city
    * @param name the name given to the city
    * @param infectionType the Disease type for the city
    */
    public City(String name, Disease infectionType) {
        this.neighbours = new ArrayList<City>();
        this.infectionLevel = 0;
        this.infectionType = infectionType;
        this.name = name;
        this.researchCenter = false;
        this.stillInfectable = true;
    }
    
    /**
    * Returns the name of the city
    * @return the name of the city
    */
    public String getName() {
        return this.name;
    }
    
    /**
    * Returns the list of neighbours of the city
    * @return the list of neighbours of the city
    */
    public List<City> getNeighbours() {
        return this.neighbours;
    }
    
    /**
    * Returns if the city has a research center
    * @return true if it has one, else false
    */
    public boolean hasResearchedCenter() {
        return this.researchCenter;
    }
    
    /**
    * Returns the current level of infection in the city
    * @return the current level of infection in the city
    */
    public int getInfectionLevel() {
        return this.infectionLevel;
    }
    
    /**
    * Returns the disease present in the city
    * @return the disease present in the city
    */
    public Disease getInfectionType() {
        return this.infectionType;
    }
    
    /**
    * Infect the city and it's neighbours if the city infection level is already at max
    * @param alreadyInfected List of already infected cities
    */
    public void infect(List<City> alreadyInfected) {
    	if(this.stillInfectable && !(alreadyInfected.contains(this))) {
    		if(this.infectionLevel == 3) {
                for(City neighbour: this.neighbours) {
                    if(neighbour.getInfectionType().equals(this.infectionType)) {
                        alreadyInfected.add(this);
                        neighbour.infect(alreadyInfected);
                    }
                }
            } else {
                this.infectionLevel += 1;
                this.infectionType.useCube();
            }
    	}
    }

    /**
     * Infect the city and it's neighbours if the city infection level is already at max
     */
    public void infect() {
        this.infect(new ArrayList<City>());
    }
    
    /**
    * Cure the city by one or all points depending on the situation
    */
    public void cure() {
        if(this.infectionType.isCured()) {
            this.infectionLevel=0;
            this.stillInfectable=false;
        } else if(this.stillInfectable && this.infectionLevel>0){
            this.infectionLevel--;
            this.infectionType.recoverCube();
            if(this.infectionLevel==0) {
                this.stillInfectable = false;
            }
        } 
    }
    
    /**
    * Add a neighbour to the city
    * @param city the neighbour to add
    */
    public void addNeighbour(City city) {
        this.neighbours.add(city);
    }
    
    /**
    * Add a research center in the city
    */
    public void addResearchCenter() {
        this.researchCenter = true;
    }
    
    /**
    * Remove a research center from this city
    */
    public void removeResearchCenter() {
        this.researchCenter = false;
    }

    /**
     * Returns true if they have the same name
     * 
     */
    public boolean equals(Object obj) {
        if(!(obj instanceof City)) {
            return false;
        }
        City c = (City)obj;
        return c.getName().equals(this.name);
    }
    
    /**
     * Returns the string representation of a city
     * @return the string representation of a city
     */
    public String toString() {
        String res = "City ";
        res += this.name + " [";
        for(City c : this.neighbours) {
            res += c.getName() + ' ';
        }
        res = res.trim();
        res += "]";
        return res;
    }
}
