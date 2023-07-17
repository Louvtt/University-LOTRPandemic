package pandemic.roles;

import pandemic.actions.*;

import java.util.*;

import static org.junit.Assert.*;
import static org.junit.jupiter.api.Assertions.assertIterableEquals;

import org.junit.Test;

public class ExpertTest {
    
    private final static Expert e = new Expert();

    // Test the name of the Role
    @Test
    public void TestNameRoleIfExpert() {
        assertEquals("Expert", e.getName());
    }

    // Test if the actions of a Expert are implented
    @Test
    public void TestActionsExpert() {
        List<Action> act = new ArrayList<Action>();
        act.add(new DoNothingAction());
        act.add(new MoveAction());
        act.add(new TreatDiseaseAction());
        act.add(new FindCureAction());
        act.add(new BuildCenterExpertAction());
        assertIterableEquals(act, e.getActions());
    }


    // ---Pour permettre l'execution des tests ----------------------
    public static junit.framework.Test suite() {
        return new junit.framework.JUnit4TestAdapter(pandemic.roles.ExpertTest.class);
        }
}