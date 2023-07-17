package pandemic.roles;

import pandemic.actions.*;
import java.util.*;

import static org.junit.Assert.*;
import static org.junit.jupiter.api.Assertions.assertIterableEquals;

import org.junit.Test;

public class ScientistTest {
    
    private final static Scientist s = new Scientist();

    // Test the name of the Role
    @Test
    public void TestNameRoleIfScientist() {
        assertEquals("Scientist", s.getName());
    }

    // Test if the actions of a Scientist are implented
    @Test
    public void TestActionsScientist() {
        List<Action> act = new ArrayList<Action>();
        act.add(new DoNothingAction());
        act.add(new MoveAction());
        act.add(new TreatDiseaseAction());
        act.add(new FindCureScientistAction());
        act.add(new BuildCenterAction());
        assertIterableEquals(act, s.getActions());
    }


    // ---Pour permettre l'execution des tests ----------------------
    public static junit.framework.Test suite() {
        return new junit.framework.JUnit4TestAdapter(pandemic.roles.ScientistTest.class);
        }
}