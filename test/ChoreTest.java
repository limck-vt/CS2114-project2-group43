import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotEquals;
import static org.junit.Assert.assertTrue;
import org.junit.Before;
import org.junit.Test;

public class ChoreTest
{
    private Chore dishes;

    /**
     * Creates a chore before each test.
     */
    @Before
    public void setUp()
    {
        dishes = new Chore("Dishes", "High");
    }

    /**
     * Tests that the constructor and getName store the name.
     */
    @Test
    public void testGetName()
    {
        assertEquals("Dishes", dishes.getName());
    }

    /**
     * Tests that two chores with the same name keep separate names.
     */
    @Test
    public void testGetNameSeparateObjects()
    {
        Chore trash = new Chore("Trash", "High");
        assertEquals("Trash", trash.getName());
        assertEquals("Dishes", dishes.getName());
    }

    /**
     * Tests that getPriority returns the priority.
     */
    @Test
    public void testGetPriority()
    {
        assertEquals("High", dishes.getPriority());
    }

    /**
     * Tests that the same chore name can have a different priority.
     */
    @Test
    public void testGetPriorityDifferentPriority()
    {
        assertEquals("Low", new Chore("Dishes", "Low").getPriority());
    }

    /**
     * Tests the readable format.
     */
    @Test
    public void testToString()
    {
        assertEquals("Dishes (High)", dishes.toString());
    }

    /**
     * Tests the readable format for a multi-word chore.
     */
    @Test
    public void testToStringMultiWord()
    {
        assertEquals("Take Out Trash (Low)",
            new Chore("Take Out Trash", "Low").toString());
    }

    /**
     * Tests that matching chores are equal and have the same hash code.
     */
    @Test
    public void testEquals()
    {
        Chore same = new Chore("Dishes", "High");
        assertTrue(dishes.equals(same));
        assertTrue(dishes.equals(dishes));
        assertEquals(dishes.hashCode(), same.hashCode());
    }

    /**
     * Tests that different chores, null, and other types are not equal.
     */
    @Test
    public void testEqualsDifferent()
    {
        assertFalse(dishes.equals(new Chore("Trash", "High")));
        assertFalse(dishes.equals(new Chore("Dishes", "Low")));
        assertFalse(dishes.equals(null));
        assertFalse(dishes.equals("Dishes"));
        assertNotEquals(dishes.hashCode(),
            new Chore("Trash", "High").hashCode());
    }
}
