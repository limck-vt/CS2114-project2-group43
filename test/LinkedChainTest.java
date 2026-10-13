import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;
import org.junit.Before;
import org.junit.Test;

public class LinkedChainTest
{
    private LinkedChain<String> chain;

    /**
     * Creates an empty chain before each test.
     */
    @Before
    public void setUp()
    {
        chain = new LinkedChain<String>();
    }

    /**
     * Tests that a new chain is empty.
     */
    @Test
    public void testConstructor()
    {
        assertEquals(0, chain.size());
        assertTrue(chain.isEmpty());
        assertEquals("[]", chain.toString());
    }

    /**
     * Tests adding an entry.
     */
    @Test
    public void testAdd()
    {
        assertTrue(chain.add("Dishes"));
        assertEquals(1, chain.size());
        assertTrue(chain.contains("Dishes"));
    }

    /**
     * Tests that adding null throws an exception.
     */
    @Test(expected = IllegalArgumentException.class)
    public void testAddNull()
    {
        chain.add(null);
    }

    /**
     * Tests that addToEnd places the entry at the end.
     */
    @Test
    public void testAddToEnd()
    {
        chain.addToEnd("Dishes");
        chain.addToEnd("Trash");
        assertEquals("Trash", chain.get(1));
    }

    /**
     * Tests that addToEnd with null throws an exception.
     */
    @Test(expected = IllegalArgumentException.class)
    public void testAddToEndNull()
    {
        chain.addToEnd(null);
    }

    /**
     * Tests removing the first entry.
     */
    @Test
    public void testRemoveFirst()
    {
        chain.add("Trash");
        chain.add("Dishes");
        assertEquals("Dishes", chain.remove());
        assertEquals(1, chain.size());
    }

    /**
     * Tests removing from an empty chain.
     */
    @Test
    public void testRemoveFirstEmpty()
    {
        assertNull(chain.remove());
    }

    /**
     * Tests removing a given entry.
     */
    @Test
    public void testRemoveEntry()
    {
        chain.add("Dishes");
        assertTrue(chain.remove("Dishes"));
        assertFalse(chain.contains("Dishes"));
    }

    /**
     * Tests removing an entry that is not there.
     */
    @Test
    public void testRemoveEntryMissing()
    {
        chain.add("Dishes");
        assertFalse(chain.remove("Trash"));
        assertEquals("[Dishes]", chain.toString());
    }

    /**
     * Tests clearing a populated chain.
     */
    @Test
    public void testClear()
    {
        chain.add("Dishes");
        chain.add("Trash");
        chain.clear();
        assertEquals(0, chain.size());
    }

    /**
     * Tests clearing an empty chain.
     */
    @Test
    public void testClearEmpty()
    {
        chain.clear();
        assertTrue(chain.isEmpty());
    }

    /**
     * Tests getting the first entry.
     */
    @Test
    public void testGet()
    {
        chain.add("Dishes");
        assertEquals("Dishes", chain.get(0));
    }

    /**
     * Tests getting an invalid index.
     */
    @Test(expected = IndexOutOfBoundsException.class)
    public void testGetInvalidIndex()
    {
        chain.get(0);
    }

    /**
     * Tests contains for an existing entry.
     */
    @Test
    public void testContains()
    {
        chain.add("Dishes");
        assertTrue(chain.contains("Dishes"));
    }

    /**
     * Tests contains for a missing entry.
     */
    @Test
    public void testContainsMissing()
    {
        chain.add("Dishes");
        assertFalse(chain.contains("Trash"));
    }

    /**
     * Tests counting a duplicate entry.
     */
    @Test
    public void testCount()
    {
        chain.add("Dishes");
        chain.add("Dishes");
        assertEquals(2, chain.count("Dishes"));
    }

    /**
     * Tests counting null.
     */
    @Test
    public void testCountNull()
    {
        chain.add("Dishes");
        assertEquals(0, chain.count(null));
    }

    /**
     * Tests size after two adds.
     */
    @Test
    public void testSize()
    {
        chain.add("Dishes");
        chain.add("Trash");
        assertEquals(2, chain.size());
    }

    /**
     * Tests size of an empty chain.
     */
    @Test
    public void testSizeEmpty()
    {
        assertEquals(0, chain.size());
    }

    /**
     * Tests isEmpty on a new chain.
     */
    @Test
    public void testIsEmpty()
    {
        assertTrue(chain.isEmpty());
    }

    /**
     * Tests isEmpty after adding an entry.
     */
    @Test
    public void testIsEmptyAfterAdd()
    {
        chain.add("Dishes");
        assertFalse(chain.isEmpty());
    }

    /**
     * Tests copying entries to an array of the right size.
     */
    @Test
    public void testToArray()
    {
        chain.addToEnd("Dishes");
        chain.addToEnd("Trash");
        String[] values = chain.toArray(new String[2]);
        assertEquals("Dishes", values[0]);
        assertEquals("Trash", values[1]);
    }

    /**
     * Tests that a null array throws an exception.
     */
    @Test(expected = IllegalArgumentException.class)
    public void testToArrayNull()
    {
        chain.toArray(null);
    }

    /**
     * Tests that a too-small array throws an exception.
     */
    @Test(expected = IllegalArgumentException.class)
    public void testToArrayTooSmall()
    {
        chain.add("Dishes");
        chain.toArray(new String[0]);
    }

    /**
     * Tests the readable format with two entries.
     */
    @Test
    public void testToString()
    {
        chain.addToEnd("A");
        chain.addToEnd("B");
        assertEquals("[A, B]", chain.toString());
    }

    /**
     * Tests the readable format of an empty chain.
     */
    @Test
    public void testToStringEmpty()
    {
        assertEquals("[]", chain.toString());
    }
}
