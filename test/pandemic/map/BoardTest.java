package pandemic.map;

import static org.junit.Assert.assertTrue;

import org.junit.Test;

import pandemic.City;
import pandemic.Disease;

public abstract class BoardTest {
    
    protected Board board;

    @Test
    public void testBoardAddDisease() {
        int size = board.getDiseases().size();

        board.addDisease(new Disease("test", 5));

        assertTrue(board.getDiseases().size() > size);
    }

    @Test
    public void testAddCity() {
        int size = board.getCities().size();

        board.addCity(new City("ville-en-plus", null));

        assertTrue(board.getCities().size() > size);
    }
}
