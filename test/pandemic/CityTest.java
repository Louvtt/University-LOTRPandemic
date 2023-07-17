package pandemic;

import static org.junit.Assert.*;

import java.util.*;

import org.junit.Test;

public class CityTest {
    //testing is the addNeighbour method works
    private final static Disease RED = new Disease("choléra", 24);
    @Test
    public void cityHasNeighbor() {
        City Paris = new City("Paris", RED);
        City Lyon = new City("Lyon", RED);
        Paris.addNeighbour(Lyon);
        assertEquals(Lyon,Paris.getNeighbours().get(0));
    }

    @Test
    public void testEqualDifferentObjectIsFalse() {
        City Paris = new City("Paris", RED);
        assertNotEquals(Paris, RED);
    }

    //testing if the infection level increases when infect() is called
    @Test
    public void cityIsInfected(){
        City Lille = new City("Lille", RED);
        List<City> infectedCities = new ArrayList<City>();
        Lille.infect(infectedCities);
        assertEquals(1,Lille.getInfectionLevel());
    }

    @Test
    public void cityCantBeCuredByCuredDisease() {
        Disease curedDisease = new Disease("Nothing", 0);
        curedDisease.hasFoundCure();

        City Paris = new City("Paris", curedDisease);
        Paris.infect(new ArrayList<City>());
        Paris.infect(new ArrayList<City>());
        Paris.infect(new ArrayList<City>());
        assertTrue(Paris.getInfectionLevel() == 3);
        Paris.cure();
        assertTrue(Paris.getInfectionLevel() == 0);
    }

    //testing if city is cured when cure() is called
    @Test 
    public void cityIsCured() {
        City Noxus = new City("Noxus", RED);
        List<City> infectedCities = new ArrayList<City>();
        Noxus.infect(infectedCities);
        Noxus.cure();
        assertEquals(0,Noxus.getInfectionLevel());
    }


    //Testing the rule stating that a city can not be infected after being cured
    @Test 
    public void cityCantBeInfectedAgain() {
        City Ionia = new City("Ionia", RED);
        List<City> infectedCities = new ArrayList<City>();
        Ionia.infect(infectedCities);
        Ionia.cure();
        Ionia.infect(infectedCities);
        assertEquals(0,Ionia.getInfectionLevel());
    }


    //Testing if the rule of when a city has been infected 3 times, the neighboring ones will be infected as well
    @Test 
    public void neighboursInfected() {
        City Ishtal = new City("Ishtal", RED);
        City Bilgewater = new City("Bilgewater", RED);
        List<City> infectedCities = new ArrayList<City>();
        Ishtal.addNeighbour(Bilgewater);
        assertEquals(0,Bilgewater.getInfectionLevel());
        for(int i = 0; i<4; i++){
            Ishtal.infect(infectedCities); 
        }
        assertEquals(1, Bilgewater.getInfectionLevel());        
    }

    @Test
    public void reserchCenterBuildingAddAndRemove() {
        City ionia = new City("ionia", RED);
        assertFalse(ionia.hasResearchedCenter());
        ionia.addResearchCenter();
        assertTrue(ionia.hasResearchedCenter());
        ionia.removeResearchCenter();
        assertFalse(ionia.hasResearchedCenter());
    }
    

    // ---Pour permettre l'execution des tests ----------------------
    public static junit.framework.Test suite() {
        return new junit.framework.JUnit4TestAdapter(pandemic.CityTest.class);
    }


}
