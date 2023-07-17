package pandemic.actions;


import static org.junit.Assert.*;

import org.junit.Test;

public class DoNothingActionTest {
    
    private final static Action a = new DoNothingAction();

    // Test the name of the Action
    @Test
    public void TestNameActionIfNothingDone() {
        assertEquals("Nothing", a.getName());
    }


    // ---Pour permettre l'execution des tests ----------------------
    public static junit.framework.Test suite() {
        return new junit.framework.JUnit4TestAdapter(pandemic.actions.DoNothingActionTest.class);
        }
}