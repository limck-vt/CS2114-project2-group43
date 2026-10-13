import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import java.util.Random;
import java.util.Scanner;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

public class ChoreAppTest
{
    private PrintStream originalOut;
    private ByteArrayOutputStream output;
    private ChoreManager manager;

    /**
     * A Random that always picks 0, so schedules are predictable.
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
     * Captures console output before each test.
     */
    @Before
    public void setUp()
    {
        originalOut = System.out;
        output = new ByteArrayOutputStream();
        System.setOut(new PrintStream(output));
        manager = new ChoreManager(new FirstRoommateRandom());
    }

    /**
     * Restores console output after each test.
     */
    @After
    public void tearDown()
    {
        System.setOut(originalOut);
    }

    /**
     * Runs the app on the given lines of input.
     *
     * @param lines the user input, one entry per line
     * @return everything the app printed
     */
    private String runWith(String... lines)
    {
        String text = String.join("\n", lines) + "\n";
        new ChoreApp(manager, new Scanner(text)).run();
        return output.toString();
    }

    /**
     * Counts how many times a piece of text appears in another.
     *
     * @param text the text to search
     * @param piece the text to count
     * @return the number of times piece appears
     */
    private int countOf(String text, String piece)
    {
        int count = 0;
        int index = text.indexOf(piece);
        while (index >= 0)
        {
            count++;
            index = text.indexOf(piece, index + piece.length());
        }
        return count;
    }

    /**
     * Tests that the default constructor builds an app without error.
     */
    @Test
    public void testDefaultConstructor()
    {
        ChoreApp app = new ChoreApp();
        app.displayMenu();
        assertTrue(output.toString().contains("1. Add Roommate"));
    }

    /**
     * Tests that main exits cleanly when there is no input.
     */
    @Test
    public void testMainNoInput()
    {
        java.io.InputStream originalIn = System.in;
        System.setIn(new java.io.ByteArrayInputStream(new byte[0]));
        try
        {
            ChoreApp.main(new String[0]);
        }
        finally
        {
            System.setIn(originalIn);
        }
        assertTrue(output.toString().contains("Goodbye!"));
    }

    /**
     * Tests that main runs the menu and exits on 9.
     */
    @Test
    public void testMain()
    {
        java.io.InputStream originalIn = System.in;
        System.setIn(new java.io.ByteArrayInputStream("9\n".getBytes()));
        try
        {
            ChoreApp.main(new String[0]);
        }
        finally
        {
            System.setIn(originalIn);
        }
        assertTrue(output.toString().contains("Welcome to Passing the Plunger!"));
        assertTrue(output.toString().contains("Goodbye!"));
    }

    /**
     * Tests that entering 1 starts the Add Roommate action.
     */
    @Test
    public void testRunValidMenuInput()
    {
        String result = runWith("1", "John", "9");
        assertTrue(result.contains("Roommate name: "));
        assertTrue(result.contains("Added roommate John."));
        assertTrue(manager.hasRoommate("John"));
    }

    /**
     * Tests the spec's valid swap selection: swapAssignments is called and
     * the updated schedule is displayed.
     */
    @Test
    public void testRunValidSwap()
    {
        String result = runWith(
            "1", "John", "1", "Alex",
            "2", "Trash", "Medium",
            "2", "Dishes", "High",
            "3",
            "5", "John", "Alex",
            "9");
        assertTrue(result.contains("Swapped chores between John and Alex."));
        assertTrue(result.contains("John - Trash - Medium"));
        assertTrue(result.contains("Alex - Dishes - High"));
    }

    /**
     * Tests that an unavailable number shows an error and the menu again.
     */
    @Test
    public void testRunInvalidNumber()
    {
        String result = runWith("42", "9");
        assertTrue(result.contains("Invalid choice. Enter a number from 1 to 9."));
        assertEquals(2, countOf(result, "1. Add Roommate"));
    }

    /**
     * Tests that text like "hello" shows an error and the menu again.
     */
    @Test
    public void testRunInvalidText()
    {
        String result = runWith("hello", "9");
        assertTrue(result.contains("Invalid choice. Enter a number from 1 to 9."));
        assertEquals(2, countOf(result, "1. Add Roommate"));
    }

    /**
     * Tests that bad input from the scope does not crash the app, and each
     * error message from the spec is shown.
     */
    @Test
    public void testRunBadInputDoesNotCrash()
    {
        String result = runWith(
            "1", "",
            "1", "John", "1", "John",
            "2", "", "High",
            "2", "Dishes", "Urgent",
            "3",
            "5", "John", "Kevin",
            "6",
            "7", "Kevin",
            "8", "Cooking",
            "9");
        assertTrue(result.contains("Roommate name cannot be empty."));
        assertTrue(result.contains("Roommate already exists."));
        assertTrue(result.contains("Chore cannot be empty."));
        assertTrue(result.contains("Priority must be High, Medium, or Low."));
        assertTrue(result.contains("Add a chore first."));
        assertTrue(result.contains("Create a schedule first."));
        assertTrue(result.contains("Roommate not found."));
        assertTrue(result.contains("Chore not found."));
        assertTrue(result.contains("Goodbye!"));
    }

    /**
     * Tests that the app exits cleanly if input ends without choosing Exit.
     */
    @Test
    public void testRunInputEndsEarly()
    {
        String result = runWith("1");
        assertTrue(result.contains("Goodbye!"));
    }

    /**
     * Tests the full MVP path: add, schedule, view, rotate, remove.
     */
    @Test
    public void testRunMvpPath()
    {
        String result = runWith(
            "1", "John", "1", "Alex",
            "2", "Dishes", "High",
            "2", "Trash", "Low",
            "3", "4", "6",
            "7", "Alex",
            "8", "Trash",
            "9");
        assertTrue(result.contains("Schedule created for week 1."));
        assertTrue(result.contains("Schedule rotated to week 2."));
        assertTrue(result.contains("Removed roommate Alex."));
        assertTrue(result.contains("Removed chore Trash."));
    }

    /**
     * Tests that the menu lists every option.
     */
    @Test
    public void testDisplayMenu()
    {
        new ChoreApp(manager, new Scanner("")).displayMenu();
        String result = output.toString();
        assertTrue(result.contains("1. Add Roommate"));
        assertTrue(result.contains("5. Swap Assignments"));
        assertTrue(result.contains("9. Exit"));
    }

    /**
     * Tests that showing the menu twice prints the same menu twice.
     */
    @Test
    public void testDisplayMenuTwice()
    {
        ChoreApp app = new ChoreApp(manager, new Scanner(""));
        app.displayMenu();
        app.displayMenu();
        assertEquals(2, countOf(output.toString(), "9. Exit"));
    }

    /**
     * Tests that the schedule is shown grouped by priority, High first.
     */
    @Test
    public void testDisplaySchedule()
    {
        manager.addRoommate("John");
        manager.addRoommate("Alex");
        manager.addChore("Trash", "Low");
        manager.addChore("Dishes", "High");
        manager.createSchedule();
        new ChoreApp(manager, new Scanner("")).displaySchedule();
        String result = output.toString();
        assertTrue(result.contains("High priority:"));
        assertTrue(result.contains("Low priority:"));
        assertTrue(result.indexOf("High priority:")
            < result.indexOf("Low priority:"));
        assertTrue(result.indexOf("John - Dishes - High")
            < result.indexOf("Alex - Trash - Low"));
    }

    /**
     * Tests showing the schedule when none exists.
     */
    @Test
    public void testDisplayScheduleEmpty()
    {
        new ChoreApp(manager, new Scanner("")).displaySchedule();
        assertTrue(output.toString().contains(
            "No schedule yet. Create a schedule first."));
    }
}
