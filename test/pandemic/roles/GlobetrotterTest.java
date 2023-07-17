package pandemic.roles;

import pandemic.actions.*;
import java.util.*;

import static org.junit.Assert.*;
import static org.junit.jupiter.api.Assertions.assertIterableEquals;

import org.junit.Test;

public class GlobetrotterTest {
    
    private final static Globetrotter g = new Globetrotter();

    // Test the name of the Role
    @Test
    public void TestNameRoleIfGlobetrotter() {
        assertEquals("Globetrotter", g.getName());
    }

    // Test if the actions of a Globetrotter are implented
    @Test
    public void TestActionsGlobetrotter() {
        List<Action> act = new ArrayList<Action>();
        act.add(new DoNothingAction());
        act.add(new MoveActionGlobeTrotter());
        act.add(new TreatDiseaseAction());
        act.add(new FindCureAction());
        act.add(new BuildCenterAction());
        assertIterableEquals(act, g.getActions());
    }


    // ---Pour permettre l'execution des tests ----------------------
    public static junit.framework.Test suite() {
        return new junit.framework.JUnit4TestAdapter(pandemic.roles.GlobetrotterTest.class);
        }
}