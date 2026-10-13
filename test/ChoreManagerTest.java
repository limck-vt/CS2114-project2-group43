import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;
import java.util.Random;
import org.junit.Before;
import org.junit.Test;

/**
 * Tests for ChoreManager, following the spec's test plan, detailed swap
 * test, and invalid swap tests. Each public method has a normal case and
 * a bad-input or edge case.
 *
 * @author Isaac, Liam, Nathaniel
 * @version October 2026
 */
public class ChoreManagerTest
{
    private ChoreManager manager;

    /**
     * A Random that always picks 0, so the first roommate always gets the
     * first chore and schedules are predictable in tests.
     */
    private static class FirstRoommateRandom extends Random
    {
        private static final long serialVersionUID = 1L;

        /**
         * Always returns 0.
         *
         * @param bound ignored
         * @return 0
         */
        @Override
        public int nextInt(int bound)
        {
            return 0;
        }
    }

    /**
     * Creates a manager with predictable scheduling before each test.
     */
    @Before
    public void setUp()
    {
        manager = new ChoreManager(new FirstRoommateRandom());
    }

    /**
     * Sets up the spec's starting schedule:
     * John - Dishes - High, Alex - Trash - Medium, David - Bathroom - Low.
     * Chores are added in reverse because addChore adds to the front.
     */
    private void setUpSpecSchedule()
    {
        manager.addRoommate("John");
        manager.addRoommate("Alex");
        manager.addRoommate("David");
        manager.addChore("Bathroom", "Low");
        manager.addChore("Trash", "Medium");
        manager.addChore("Dishes", "High");
        manager.createSchedule();
    }

    /**
     * Tests that the default constructor creates an empty manager.
     */
    @Test
    public void testDefaultConstructor()
    {
        ChoreManager fresh = new ChoreManager();
        assertEquals(0, fresh.getRoommateCount());
        assertEquals(0, fresh.getChoreCount());
        assertEquals(0, fresh.getAssignmentCount());
    }

    /**
     * Tests that a real random manager still creates a full schedule.
     */
    @Test
    public void testDefaultConstructorRandomSchedule()
    {
        ChoreManager fresh = new ChoreManager();
        fresh.addRoommate("John");
        fresh.addRoommate("Alex");
        fresh.addChore("Dishes", "High");
        fresh.addChore("Trash", "Low");
        assertTrue(fresh.createSchedule());
        assertEquals(2, fresh.getAssignmentCount());
    }

    /**
     * Tests the constructor that takes a Random.
     */
    @Test
    public void testRandomConstructor()
    {
        ChoreManager seeded = new ChoreManager(new Random(42));
        assertEquals(0, seeded.getAssignmentCount());
        assertEquals("", seeded.getLastMessage());
    }

    /**
     * Tests that two managers with the same seed make the same schedule.
     */
    @Test
    public void testRandomConstructorSameSeed()
    {
        ChoreManager first = new ChoreManager(new Random(7));
        ChoreManager second = new ChoreManager(new Random(7));
        String[] names = {"John", "Alex", "David"};
        for (int i = 0; i < names.length; i++)
        {
            first.addRoommate(names[i]);
            second.addRoommate(names[i]);
        }
        first.addChore("Dishes", "High");
        second.addChore("Dishes", "High");
        first.createSchedule();
        second.createSchedule();
        assertEquals(first.getAssignmentsByPriority("High")[0],
            second.getAssignmentsByPriority("High")[0]);
    }

    /**
     * Tests that a successful action sets a message.
     */
    @Test
    public void testGetLastMessage()
    {
        manager.addRoommate("John");
        assertEquals("Added roommate John.", manager.getLastMessage());
    }

    /**
     * Tests that a failed action sets an error message.
     */
    @Test
    public void testGetLastMessageAfterError()
    {
        manager.addRoommate("John");
        manager.addRoommate("John");
        assertEquals("Roommate already exists.", manager.getLastMessage());
    }

    /**
     * Tests adding a roommate.
     */
    @Test
    public void testAddRoommate()
    {
        assertTrue(manager.addRoommate("John"));
        assertTrue(manager.hasRoommate("John"));
        assertEquals(1, manager.getRoommateCount());
    }

    /**
     * Tests that empty, blank, null, and duplicate names are rejected.
     */
    @Test
    public void testAddRoommateEmptyOrDuplicate()
    {
        assertFalse(manager.addRoommate(""));
        assertEquals("Roommate name cannot be empty.",
            manager.getLastMessage());
        assertFalse(manager.addRoommate("   "));
        assertFalse(manager.addRoommate(null));
        assertTrue(manager.addRoommate("John"));
        assertFalse(manager.addRoommate("John"));
        assertFalse(manager.addRoommate("  John  "));
        assertEquals("Roommate already exists.", manager.getLastMessage());
        assertEquals(1, manager.getRoommateCount());
    }

    /**
     * Tests adding a chore with the default priority.
     */
    @Test
    public void testAddChore()
    {
        assertTrue(manager.addChore("Dishes"));
        assertEquals(1, manager.getChoreCount());
        manager.addRoommate("John");
        manager.createSchedule();
        assertEquals("Medium", manager.findAssignment("John").getPriority());
    }

    /**
     * Tests that an empty chore is rejected and a duplicate is allowed.
     */
    @Test
    public void testAddChoreEmptyAndDuplicate()
    {
        assertFalse(manager.addChore(""));
        assertEquals("Chore cannot be empty.", manager.getLastMessage());
        assertFalse(manager.addChore(null));
        assertTrue(manager.addChore("Dishes"));
        assertTrue(manager.addChore("Dishes"));
        assertEquals(2, manager.getChoreCount());
    }

    /**
     * Tests adding a chore with a priority, in any capitalization.
     */
    @Test
    public void testAddChoreWithPriority()
    {
        assertTrue(manager.addChore("Dishes", "high"));
        manager.addRoommate("John");
        manager.createSchedule();
        assertEquals("High", manager.findAssignment("John").getPriority());
    }

    /**
     * Tests that an invalid priority is rejected.
     */
    @Test
    public void testAddChoreInvalidPriority()
    {
        assertFalse(manager.addChore("Dishes", "Urgent"));
        assertFalse(manager.addChore("Dishes", null));
        assertFalse(manager.addChore("Dishes", ""));
        assertFalse(manager.addChore("", "High"));
        assertEquals(0, manager.getChoreCount());
    }

    /**
     * Tests removing an existing roommate, which also removes their
     * assignment.
     */
    @Test
    public void testRemoveRoommate()
    {
        setUpSpecSchedule();
        assertTrue(manager.removeRoommate("John"));
        assertFalse(manager.hasRoommate("John"));
        assertNull(manager.findAssignment("John"));
        assertEquals(2, manager.getAssignmentCount());
        assertEquals(3, manager.getChoreCount());
    }

    /**
     * Tests removing a roommate who is not there.
     */
    @Test
    public void testRemoveRoommateNonexistent()
    {
        assertFalse(manager.removeRoommate("Kevin"));
        manager.addRoommate("John");
        assertFalse(manager.removeRoommate("Kevin"));
        assertFalse(manager.removeRoommate(null));
        assertEquals("Roommate not found.", manager.getLastMessage());
        assertEquals(1, manager.getRoommateCount());
    }

    /**
     * Tests removing an existing chore, which also removes its assignment.
     */
    @Test
    public void testRemoveChore()
    {
        setUpSpecSchedule();
        assertTrue(manager.removeChore("Dishes"));
        assertEquals(2, manager.getChoreCount());
        assertNull(manager.findAssignment("John"));
        assertEquals(0, manager.getAssignmentsByPriority("High").length);
    }

    /**
     * Tests removing a chore that is not there.
     */
    @Test
    public void testRemoveChoreNonexistent()
    {
        assertFalse(manager.removeChore("Cooking"));
        manager.addChore("Dishes");
        assertFalse(manager.removeChore("Cooking"));
        assertFalse(manager.removeChore(""));
        assertEquals("Chore not found.", manager.getLastMessage());
        assertEquals(1, manager.getChoreCount());
    }

    /**
     * Tests that removing a duplicate chore only removes one copy.
     */
    @Test
    public void testRemoveChoreDuplicate()
    {
        manager.addChore("Dishes");
        manager.addChore("Dishes");
        assertTrue(manager.removeChore("Dishes"));
        assertEquals(1, manager.getChoreCount());
    }

    /**
     * Tests creating a schedule with 3 roommates and 3 chores.
     */
    @Test
    public void testCreateSchedule()
    {
        setUpSpecSchedule();
        assertEquals(3, manager.getAssignmentCount());
        assertEquals("John - Dishes - High",
            manager.findAssignment("John").toString());
        assertEquals("Alex - Trash - Medium",
            manager.findAssignment("Alex").toString());
        assertEquals("David - Bathroom - Low",
            manager.findAssignment("David").toString());
    }

    /**
     * Tests creating a schedule with no roommates or no chores.
     */
    @Test
    public void testCreateScheduleNoRoommatesOrChores()
    {
        assertFalse(manager.createSchedule());
        assertEquals("Add a roommate first.", manager.getLastMessage());
        manager.addRoommate("John");
        assertFalse(manager.createSchedule());
        assertEquals("Add a chore first.", manager.getLastMessage());
        assertEquals(0, manager.getAssignmentCount());
    }

    /**
     * Tests that with more chores than roommates, a roommate gets several
     * chores.
     */
    @Test
    public void testCreateScheduleMoreChoresThanRoommates()
    {
        manager.addRoommate("John");
        manager.addChore("Dishes");
        manager.addChore("Trash");
        manager.addChore("Bathroom");
        assertTrue(manager.createSchedule());
        assertEquals(3, manager.getAssignmentsByPriority("Medium").length);
    }

    /**
     * Tests that with more roommates than chores, some get no chore.
     */
    @Test
    public void testCreateScheduleMoreRoommatesThanChores()
    {
        manager.addRoommate("John");
        manager.addRoommate("Alex");
        manager.addChore("Dishes");
        assertTrue(manager.createSchedule());
        assertNotNull(manager.findAssignment("John"));
        assertNull(manager.findAssignment("Alex"));
    }

    /**
     * Tests the spec's detailed swap test: the chores are exchanged, the
     * priority travels with the chore, and David is unchanged.
     */
    @Test
    public void testSwapAssignments()
    {
        setUpSpecSchedule();
        assertTrue(manager.swapAssignments("John", "Alex"));
        assertEquals("John - Trash - Medium",
            manager.findAssignment("John").toString());
        assertEquals("Alex - Dishes - High",
            manager.findAssignment("Alex").toString());
        assertEquals("David - Bathroom - Low",
            manager.findAssignment("David").toString());
    }

    /**
     * Tests the spec's invalid swaps: nonexistent roommate and the same
     * roommate twice. The schedule must not change.
     */
    @Test
    public void testSwapAssignmentsInvalidRoommates()
    {
        setUpSpecSchedule();
        assertFalse(manager.swapAssignments("John", "Kevin"));
        assertEquals("Roommate not found.", manager.getLastMessage());
        assertFalse(manager.swapAssignments("John", "John"));
        assertEquals("A roommate cannot swap with themselves.",
            manager.getLastMessage());
        assertFalse(manager.swapAssignments(null, "John"));
        assertEquals("John - Dishes - High",
            manager.findAssignment("John").toString());
        assertEquals("Alex - Trash - Medium",
            manager.findAssignment("Alex").toString());
    }

    /**
     * Tests swapping before a schedule exists.
     */
    @Test
    public void testSwapAssignmentsNoSchedule()
    {
        manager.addRoommate("John");
        manager.addRoommate("Alex");
        assertFalse(manager.swapAssignments("John", "Alex"));
        assertEquals("Create a schedule first.", manager.getLastMessage());
    }

    /**
     * Tests swapping with a roommate who has no chore.
     */
    @Test
    public void testSwapAssignmentsRoommateWithoutChore()
    {
        manager.addRoommate("John");
        manager.addRoommate("Alex");
        manager.addChore("Dishes", "High");
        manager.createSchedule();
        assertFalse(manager.swapAssignments("John", "Alex"));
        assertEquals("Both roommates need a chore to swap.",
            manager.getLastMessage());
        assertEquals("John - Dishes - High",
            manager.findAssignment("John").toString());
    }

    /**
     * Tests rotating an existing schedule: each chore moves to the next
     * roommate, and the last roommate's chore goes to the first.
     */
    @Test
    public void testRotateSchedule()
    {
        setUpSpecSchedule();
        assertTrue(manager.rotateSchedule());
        assertEquals("Alex - Dishes - High",
            manager.findAssignment("Alex").toString());
        assertEquals("David - Trash - Medium",
            manager.findAssignment("David").toString());
        assertEquals("John - Bathroom - Low",
            manager.findAssignment("John").toString());
        assertEquals(3, manager.getAssignmentCount());
    }

    /**
     * Tests rotating when no schedule exists.
     */
    @Test
    public void testRotateScheduleNoSchedule()
    {
        assertFalse(manager.rotateSchedule());
        assertEquals("Create a schedule first.", manager.getLastMessage());
    }

    /**
     * Tests finding an existing roommate's assignment.
     */
    @Test
    public void testFindAssignment()
    {
        setUpSpecSchedule();
        assertEquals(new ChoreAssignment("John", "Dishes", "High"),
            manager.findAssignment("John"));
    }

    /**
     * Tests finding a roommate with no assignment.
     */
    @Test
    public void testFindAssignmentMissing()
    {
        assertNull(manager.findAssignment("John"));
        setUpSpecSchedule();
        assertNull(manager.findAssignment("Kevin"));
        assertNull(manager.findAssignment(null));
    }

    /**
     * Tests that only High assignments are returned for "High".
     */
    @Test
    public void testGetAssignmentsByPriority()
    {
        setUpSpecSchedule();
        manager.addChore("Cooking", "High");
        manager.createSchedule();
        ChoreAssignment[] high = manager.getAssignmentsByPriority("High");
        assertEquals(2, high.length);
        for (int i = 0; i < high.length; i++)
        {
            assertEquals("High", high[i].getPriority());
        }
    }

    /**
     * Tests that an invalid priority is rejected, and a valid one with no
     * matches returns an empty array.
     */
    @Test
    public void testGetAssignmentsByPriorityInvalid()
    {
        assertEquals(0, manager.getAssignmentsByPriority("Low").length);
        assertNull(manager.getAssignmentsByPriority("Urgent"));
        assertNull(manager.getAssignmentsByPriority(null));
        assertEquals("Priority must be High, Medium, or Low.",
            manager.getLastMessage());
    }

    /**
     * Tests the roommate count.
     */
    @Test
    public void testGetRoommateCount()
    {
        manager.addRoommate("John");
        manager.addRoommate("Alex");
        assertEquals(2, manager.getRoommateCount());
    }

    /**
     * Tests that rejected roommates are not counted.
     */
    @Test
    public void testGetRoommateCountAfterRejected()
    {
        assertEquals(0, manager.getRoommateCount());
        manager.addRoommate("");
        assertEquals(0, manager.getRoommateCount());
    }

    /**
     * Tests the chore count.
     */
    @Test
    public void testGetChoreCount()
    {
        manager.addChore("Dishes");
        manager.addChore("Trash", "Low");
        assertEquals(2, manager.getChoreCount());
    }

    /**
     * Tests that rejected chores are not counted.
     */
    @Test
    public void testGetChoreCountAfterRejected()
    {
        manager.addChore("Dishes", "Never");
        assertEquals(0, manager.getChoreCount());
    }

    /**
     * Tests the assignment count after a schedule is created.
     */
    @Test
    public void testGetAssignmentCount()
    {
        setUpSpecSchedule();
        assertEquals(3, manager.getAssignmentCount());
    }

    /**
     * Tests the assignment count with no schedule.
     */
    @Test
    public void testGetAssignmentCountEmpty()
    {
        assertEquals(0, manager.getAssignmentCount());
    }

    /**
     * Tests hasRoommate for an existing roommate.
     */
    @Test
    public void testHasRoommate()
    {
        manager.addRoommate("John");
        assertTrue(manager.hasRoommate("John"));
        assertTrue(manager.hasRoommate(" John "));
    }

    /**
     * Tests hasRoommate for a missing, empty, or null name.
     */
    @Test
    public void testHasRoommateMissing()
    {
        assertFalse(manager.hasRoommate("Kevin"));
        assertFalse(manager.hasRoommate(""));
        assertFalse(manager.hasRoommate(null));
    }
}
