package pandemic.map;

import pandemic.City;
import pandemic.Disease;

import static org.junit.Assert.*;

import org.junit.Before;
import org.junit.Test;

import java.io.FileNotFoundException;
import java.util.*;

public class JSONBoardTest extends BoardTest {

    private static final String filename = "assets/carteTest.json";

    protected Board jsonBoard;

    private JSONBoard openJSONBoard(String filename) {
        try {
            return new JSONBoard(filename);
        } catch(FileNotFoundException e) {
            assertTrue(false);
            return null;
        }
    }

    @Before
    public void init() {
        this.jsonBoard = openJSONBoard(filename);
        this.board = (Board)jsonBoard;
    }

    @Test
    public void TestParseJsonSize() {
        assertEquals(this.jsonBoard.getCities().size(), 4 );
        Board b = openJSONBoard("assets/carte1.json");
        assertEquals(b.getCities().size(),12);
    }

    @Test
    public void TestParseJsonCityName() {
        int i = 1;
        for (City c : this.jsonBoard.getCities()) {
            assertEquals(c.getName(), "ville-"+Integer.toString(i));
            i = i+1;
        }
    }

    @Test
    public void TestDiseaseHaveCubes() {
        Board b = openJSONBoard("assets/carte1.json");
        for(Disease d : b.getDiseases()) {
            assertTrue(d.hasCubeLeft());
        }
    }

    @Test
    public void TestParseJsonListNeighbours() {
        List<List<City>> neighbours = new ArrayList<List<City>>();
        List<City> neigh = new ArrayList<City>();
        neigh.add(this.jsonBoard.getCities().get(1));
        neigh.add(this.jsonBoard.getCities().get(3));
        neighbours.add(neigh);
        neigh = new ArrayList<City>();
        neigh.add(this.jsonBoard.getCities().get(0));
        neighbours.add(neigh);
        neigh = new ArrayList<City>();
        neigh.add(this.jsonBoard.getCities().get(3));
        neighbours.add(neigh);
        neigh = new ArrayList<City>();
        neigh.add(this.jsonBoard.getCities().get(0));
        neigh.add(this.jsonBoard.getCities().get(2));
        neighbours.add(neigh);
        int i = 0;
        for (City c : this.jsonBoard.getCities()) {
            assertEquals(c.getNeighbours(),neighbours.get(i));
            i = i+1;
        }
    }

    @Test (expected = FileNotFoundException.class)
    public void TestOpeningMissingFileThrowsException() 
    throws FileNotFoundException {
        JSONBoard board = new JSONBoard("cartequiexistepasdutout.json");
        assertTrue(board.getCities().size() == 0);
        assertTrue(board.getDiseases().size() == 0);
    }

    // ---Pour permettre l'execution des tests ----------------------
    public static junit.framework.Test suite() {
        return new junit.framework.JUnit4TestAdapter(pandemic.map.JSONBoardTest.class);
    }
}