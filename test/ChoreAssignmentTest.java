import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import org.junit.Before;
import org.junit.Test;

public class ChoreAssignmentTest
{
    private ChoreAssignment john;

    /**
     * Creates John - Dishes - High before each test.
     */
    @Before
    public void setUp()
    {
        john = new ChoreAssignment("John", "Dishes", "High");
    }

    /**
     * Tests that the constructor stores all three fields.
     */
    @Test
    public void testConstructor()
    {
        assertEquals("John", john.getRoommate());
        assertEquals("Dishes", john.getChore());
        assertEquals("High", john.getPriority());
    }

    /**
     * Tests that invalid input is rejected by the manager before an
     * assignment is ever constructed.
     */
    @Test
    public void testConstructorInvalidInputRejectedByManager()
    {
        ChoreManager manager = new ChoreManager();
        manager.addRoommate("John");
        assertFalse(manager.addChore("Dishes", "Urgent"));
        assertFalse(manager.createSchedule());
        assertEquals(0, manager.getAssignmentCount());
    }

    /**
     * Tests getRoommate.
     */
    @Test
    public void testGetRoommate()
    {
        assertEquals("John", john.getRoommate());
    }

    /**
     * Tests that the roommate stays the same after a swap.
     */
    @Test
    public void testGetRoommateAfterSwap()
    {
        john.setChore("Trash");
        john.setPriority("Medium");
        assertEquals("John", john.getRoommate());
    }

    /**
     * Tests getChore.
     */
    @Test
    public void testGetChore()
    {
        assertEquals("Dishes", john.getChore());
    }

    /**
     * Tests that getChore returns the new chore after a swap.
     */
    @Test
    public void testGetChoreAfterSwap()
    {
        john.setChore("Trash");
        assertEquals("Trash", john.getChore());
    }

    /**
     * Tests getPriority.
     */
    @Test
    public void testGetPriority()
    {
        assertEquals("High", john.getPriority());
    }

    /**
     * Tests that getPriority returns the priority of the current chore
     * after a swap.
     */
    @Test
    public void testGetPriorityAfterSwap()
    {
        john.setChore("Trash");
        john.setPriority("Medium");
        assertEquals("Medium", john.getPriority());
    }

    /**
     * Tests setChore.
     */
    @Test
    public void testSetChore()
    {
        john.setChore("Trash");
        assertEquals("Trash", john.getChore());
    }

    /**
     * Tests that the manager prevents an invalid (empty) chore.
     */
    @Test
    public void testSetChoreInvalidPreventedByManager()
    {
        ChoreManager manager = new ChoreManager();
        assertFalse(manager.addChore("   ", "High"));
        assertEquals(0, manager.getChoreCount());
    }

    /**
     * Tests setPriority.
     */
    @Test
    public void testSetPriority()
    {
        john.setPriority("Medium");
        assertEquals("Medium", john.getPriority());
    }

    /**
     * Tests that the manager prevents an invalid priority.
     */
    @Test
    public void testSetPriorityInvalidPreventedByManager()
    {
        ChoreManager manager = new ChoreManager();
        assertFalse(manager.addChore("Dishes", "Super"));
        assertEquals("Priority must be High, Medium, or Low.",
            manager.getLastMessage());
    }

    /**
     * Tests the readable format.
     */
    @Test
    public void testToString()
    {
        assertEquals("John - Dishes - High", john.toString());
    }

    /**
     * Tests that toString shows the updated assignment after a swap.
     */
    @Test
    public void testToStringAfterSwap()
    {
        john.setChore("Trash");
        john.setPriority("Medium");
        assertEquals("John - Trash - Medium", john.toString());
    }

    /**
     * Tests that identical assignments are equal.
     */
    @Test
    public void testEquals()
    {
        ChoreAssignment same = new ChoreAssignment("John", "Dishes", "High");
        assertTrue(john.equals(same));
        assertTrue(john.equals(john));
        assertEquals(john.hashCode(), same.hashCode());
    }

    /**
     * Tests that different assignments, null, and other types are not
     * equal.
     */
    @Test
    public void testEqualsDifferent()
    {
        assertFalse(john.equals(new ChoreAssignment("Alex", "Dishes", "High")));
        assertFalse(john.equals(new ChoreAssignment("John", "Trash", "High")));
        assertFalse(john.equals(new ChoreAssignment("John", "Dishes", "Low")));
        assertFalse(john.equals(null));
        assertFalse(john.equals("John - Dishes - High"));
    }
}
