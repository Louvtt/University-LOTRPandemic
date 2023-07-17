package pandemic.cards;

import static org.junit.Assert.assertTrue;
import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.Before;
import org.junit.Test;

import pandemic.City;
import pandemic.Disease;

public class PlayerCardTest extends CardTest {
    
    private static final Disease RED = new Disease("Red", 0);
    private static final City CITY = new City("bourges", RED);

    @Before
    public void init() {
        this.card = new PlayerCard(0, CITY, RED);
    }

    @Test
    public void testCreationState() {
        assertTrue(this.card.getId() == 0);

        PlayerCard pCard = (PlayerCard)card;
        assertEquals(pCard.getCity(), CITY);
        assertEquals(pCard.getDisease(), RED);
    }
}
