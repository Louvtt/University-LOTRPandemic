package pandemic.cards;

import static org.junit.Assert.assertTrue;
import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.Before;
import org.junit.Test;

import pandemic.Disease;

public class EpidemicCardTest extends CardTest {
    
    private static final Disease RED = new Disease("Red", 0);

    @Before
    public void init() {
        this.card = new EpidemicCard(0, RED);
    }

    @Test
    public void testCreationState() {
        assertTrue(this.card.getId() == 0);

        EpidemicCard eCard = (EpidemicCard)card;
        assertEquals(eCard.getDisease(), RED);
    }
}
