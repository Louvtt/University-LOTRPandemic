package pandemic;

import static org.junit.Assert.*;

import org.junit.Test;

public class DiseaseTest {

    private static final int CUBES = 4;
    private static final String NAME = "Maladie";

    @Test
    public void validStateAtCreationTest()    {
        Disease d = new Disease(NAME, CUBES);
        assertTrue(d.hasCubeLeft());
        assertEquals(NAME, d.getName());
    }

    @Test
    public void usingCubesRemoveCubesTest() {
        Disease d = new Disease(NAME, 1);
        assertTrue(d.hasCubeLeft());
        d.useCube();
        assertFalse(d.hasCubeLeft());
    }

    @Test
    public void recoveringCubesAddCubesTest() {
        Disease d = new Disease(NAME, 0);
        assertFalse(d.hasCubeLeft());
        d.recoverCube();
        assertTrue(d.hasCubeLeft());
    }

    // ---Pour permettre l'execution des tests ----------------------
    public static junit.framework.Test suite() {
        return new junit.framework.JUnit4TestAdapter(pandemic.DiseaseTest.class);
    }
}