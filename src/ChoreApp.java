import java.util.Scanner;

/**
 * The console interface for Passing the Plunger. Shows the menu, reads
 * user input, calls ChoreManager, and displays results and errors.
 * Invalid menu input shows an error and the menu again.
 *
 * @author Isaac, Liam, Nathaniel
 * @version October 2026
 */
public class ChoreApp
{
    private ChoreManager manager;
    private Scanner input;

    /**
     * Creates an app that reads from the keyboard.
     */
    public ChoreApp()
    {
        this(new ChoreManager(), new Scanner(System.in));
    }

    /**
     * Creates an app with the given manager and input source.
     *
     * @param manager the manager that handles the application logic
     * @param input the Scanner to read user input from
     */
    public ChoreApp(ChoreManager manager, Scanner input)
    {
        this.manager = manager;
        this.input = input;
    }

    /**
     * Starts the program.
     *
     * @param args command-line arguments (not used)
     */
    public static void main(String[] args)
    {
        new ChoreApp().run();
    }

    /**
     * Runs the main menu until the user chooses Exit or input runs out.
     */
    public void run()
    {
        System.out.println("Welcome to Passing the Plunger!");
        boolean running = true;
        while (running)
        {
            displayMenu();
            String line = readLine("Choose an option: ");
            if (line == null)
            {
                running = false;
            }
            else
            {
                running = handleChoice(line);
            }
        }
        System.out.println("Goodbye!");
    }

    /**
     * Displays the menu choices.
     */
    public void displayMenu()
    {
        System.out.println();
        System.out.println("1. Add Roommate");
        System.out.println("2. Add Chore");
        System.out.println("3. Create Random Schedule");
        System.out.println("4. View Schedule");
        System.out.println("5. Swap Assignments");
        System.out.println("6. Rotate Schedule");
        System.out.println("7. Remove Roommate");
        System.out.println("8. Remove Chore");
        System.out.println("9. Exit");
    }

    /**
     * Displays the current schedule organized by priority: High first,
     * then Medium, then Low.
     */
    public void displaySchedule()
    {
        if (manager.getAssignmentCount() == 0)
        {
            System.out.println("No schedule yet. Create a schedule first.");
            return;
        }
        String[] priorities = {"High", "Medium", "Low"};
        for (int p = 0; p < priorities.length; p++)
        {
            ChoreAssignment[] group =
                manager.getAssignmentsByPriority(priorities[p]);
            if (group.length > 0)
            {
                System.out.println(priorities[p] + " priority:");
                for (int i = 0; i < group.length; i++)
                {
                    System.out.println("  " + group[i]);
                }
            }
        }
    }

    /**
     * Runs the action for one menu choice.
     *
     * @param line the text the user typed
     * @return false if the user chose Exit, true otherwise
     */
    private boolean handleChoice(String line)
    {
        int choice;
        try
        {
            choice = Integer.parseInt(line.trim());
        }
        catch (NumberFormatException e)
        {
            System.out.println("Invalid choice. Enter a number from 1 to 9.");
            return true;
        }
        switch (choice)
        {
            case 1:
                manager.addRoommate(readLine("Roommate name: "));
                break;
            case 2:
                String chore = readLine("Chore name: ");
                String priority = readLine("Priority (High/Medium/Low): ");
                manager.addChore(chore, priority);
                break;
            case 3:
                if (manager.createSchedule())
                {
                    System.out.println(manager.getLastMessage());
                    displaySchedule();
                    return true;
                }
                break;
            case 4:
                displaySchedule();
                return true;
            case 5:
                String first = readLine("First roommate: ");
                String second = readLine("Second roommate: ");
                if (manager.swapAssignments(first, second))
                {
                    System.out.println(manager.getLastMessage());
                    displaySchedule();
                    return true;
                }
                break;
            case 6:
                if (manager.rotateSchedule())
                {
                    System.out.println(manager.getLastMessage());
                    displaySchedule();
                    return true;
                }
                break;
            case 7:
                manager.removeRoommate(readLine("Roommate to remove: "));
                break;
            case 8:
                manager.removeChore(readLine("Chore to remove: "));
                break;
            case 9:
                return false;
            default:
                System.out.println("Invalid choice. Enter a number from 1 to 9.");
                return true;
        }
        System.out.println(manager.getLastMessage());
        return true;
    }

    /**
     * Shows a prompt and reads one line.
     *
     * @param prompt the prompt to show
     * @return the line typed, or null if there is no more input
     */
    private String readLine(String prompt)
    {
        System.out.print(prompt);
        if (!input.hasNextLine())
        {
            return null;
        }
        return input.nextLine();
    }
}
