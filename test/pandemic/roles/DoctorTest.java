package pandemic.roles;

import pandemic.actions.*;

import java.util.*;

import static org.junit.Assert.*;
import static org.junit.jupiter.api.Assertions.assertIterableEquals;

import org.junit.Test;

public class DoctorTest {
    
    private final static Doctor d = new Doctor();

    // Test the name of the Role
    @Test
    public void TestNameRoleIfDoctor() {
        assertEquals("Doctor", d.getName());
    }

    // Test if the actions of a Doctor are implented
    @Test
    public void TestActionsDoctor() {
        List<Action> act = new ArrayList<Action>();
        act.add(new DoNothingAction());
        act.add(new MoveAction());
        act.add(new TreatDiseaseDoctorAction());
        act.add(new FindCureAction());
        act.add(new BuildCenterAction());
        assertIterableEquals(act, d.getActions());
    }


    // ---Pour permettre l'execution des tests ----------------------
    public static junit.framework.Test suite() {
        return new junit.framework.JUnit4TestAdapter(pandemic.roles.DoctorTest.class);
        }
}