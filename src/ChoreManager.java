import java.util.Random;

public class ChoreManager
{
    private LinkedChain<ChoreAssignment> assignments;
    private LinkedChain<String> roommates;
    private LinkedChain<Chore> chores;
    private Random random;
    private int week;
    private String lastMessage;

    /**
     * Creates an empty manager with a random number generator.
     */
    public ChoreManager()
    {
        this(new Random());
    }

    /**
     * Creates an empty manager that uses the given random number
     * generator. Passing a seeded Random makes schedules repeatable,
     * which is useful for testing.
     *
     * @param random the random number generator to use
     */
    public ChoreManager(Random random)
    {
        assignments = new LinkedChain<ChoreAssignment>();
        roommates = new LinkedChain<String>();
        chores = new LinkedChain<Chore>();
        this.random = random;
        week = 0;
        lastMessage = "";
    }

    /**
     * Gets the message from the most recent operation, such as
     * "Roommate already exists." or "Added roommate John."
     *
     * @return the last message
     */
    public String getLastMessage()
    {
        return lastMessage;
    }

    /**
     * Adds a roommate. Empty and duplicate names are rejected.
     *
     * @param name the roommate name
     * @return true if the roommate was added, false otherwise
     */
    public boolean addRoommate(String name)
    {
        if (isBlank(name))
        {
            lastMessage = "Roommate name cannot be empty.";
            return false;
        }
        String trimmed = name.trim();
        if (findRoommate(trimmed) != null)
        {
            lastMessage = "Roommate already exists.";
            return false;
        }
        roommates.addToEnd(trimmed);
        lastMessage = "Added roommate " + trimmed + ".";
        if (!assignments.isEmpty())
        {
            String busiest = busiestRoommate();
            if (countFor(busiest) > 1)
            {
                ChoreAssignment toMove = lastAssignmentOf(busiest);
                toMove.setRoommate(trimmed);
                lastMessage += " Gave them " + toMove.getChore() + ".";
            }
        }
        return true;
    }

    /**
     * Adds a chore with Medium priority. Duplicate chores are allowed.
     *
     * @param chore the chore name
     * @return true if the chore was added, false otherwise
     */
    public boolean addChore(String chore)
    {
        return addChore(chore, "Medium");
    }

    /**
     * Adds a chore with the given priority. Duplicate chores are allowed.
     * Priority must be High, Medium, or Low (any capitalization).
     *
     * @param chore the chore name
     * @param priority the chore priority
     * @return true if the chore was added, false otherwise
     */
    public boolean addChore(String chore, String priority)
    {
        if (isBlank(chore))
        {
            lastMessage = "Chore cannot be empty.";
            return false;
        }
        String validPriority = normalizePriority(priority);
        if (validPriority == null)
        {
            lastMessage = "Priority must be High, Medium, or Low.";
            return false;
        }
        String name = chore.trim();
        chores.add(new Chore(name, validPriority));
        lastMessage = "Added chore " + name + " (" + validPriority + ").";
        if (!assignments.isEmpty())
        {
            String roommate = leastBusyRoommate();
            assignments.addToEnd(new ChoreAssignment(roommate, name,
                validPriority));
            lastMessage += " Assigned to " + roommate + ".";
        }
        return true;
    }

    /**
     * Removes a roommate and any assignments they had. Their chores stay
     * in the chore list and are assigned again on the next schedule.
     *
     * @param name the roommate name
     * @return true if the roommate was removed, false otherwise
     */
    public boolean removeRoommate(String name)
    {
        String stored = findRoommate(name);
        if (stored == null)
        {
            lastMessage = "Roommate not found.";
            return false;
        }
        roommates.remove(stored);
        if (roommates.isEmpty())
        {
            assignments.clear();
            lastMessage = "Removed roommate " + stored
                + ". No roommates left, so the schedule was cleared.";
            return true;
        }
        int moved = 0;
        ChoreAssignment[] current = assignmentArray();
        for (int i = 0; i < current.length; i++)
        {
            if (current[i].getRoommate().equals(stored))
            {
                current[i].setRoommate(leastBusyRoommate());
                moved++;
            }
        }
        lastMessage = "Removed roommate " + stored + ".";
        if (moved > 0)
        {
            lastMessage += " Reassigned " + moved + " chore(s).";
        }
        return true;
    }

    /**
     * Finds a specific chore belonging to a roommate.
     *
     * @param roommate the roommate
     * @param chore the chore name (any capitalization)
     * @return the assignment, or null if the roommate doesn't have it
     */
    public ChoreAssignment findAssignment(String roommate, String chore)
    {
        String stored = findRoommate(roommate);
        if (stored == null || isBlank(chore))
        {
            return null;
        }
        ChoreAssignment[] current = assignmentArray();
        for (int i = 0; i < current.length; i++)
        {
            if (current[i].getRoommate().equals(stored)
                && current[i].getChore().equalsIgnoreCase(chore.trim()))
            {
                return current[i];
            }
        }
        return null;
    }

    /**
     * Removes one chore with the given name, and one assignment for that
     * chore if a schedule exists.
     *
     * @param chore the chore name
     * @return true if the chore was removed, false otherwise
     */
    public boolean removeChore(String chore)
    {
        if (isBlank(chore))
        {
            lastMessage = "Chore not found.";
            return false;
        }
        String trimmed = chore.trim();
        Chore[] allChores = choreArray();
        Chore found = null;
        for (int i = 0; i < allChores.length && found == null; i++)
        {
            if (allChores[i].getName().equals(trimmed))
            {
                found = allChores[i];
            }
        }
        if (found == null)
        {
            lastMessage = "Chore not found.";
            return false;
        }
        chores.remove(found);
        ChoreAssignment[] current = assignmentArray();
        boolean removedAssignment = false;
        for (int i = 0; i < current.length && !removedAssignment; i++)
        {
            if (current[i].getChore().equals(trimmed))
            {
                assignments.remove(current[i]);
                removedAssignment = true;
            }
        }
        lastMessage = "Removed chore " + trimmed + ".";
        return true;
    }

        /**
     * Finds the last assignment belonging to a roommate.
     */
    private ChoreAssignment lastAssignmentOf(String roommate)
    {
        ChoreAssignment[] current = assignmentArray();
        ChoreAssignment result = null;
        for (int i = 0; i < current.length; i++)
        {
            if (current[i].getRoommate().equals(roommate))
            {
                result = current[i];
            }
        }
        return result;
    }

    /**
     * Creates a new schedule. A random roommate gets the first chore, and
     * the rest of the chores go around the roommate list in order. If
     * there are more chores than roommates, some roommates get several
     * chores. If there are more roommates than chores, some get none.
     *
     * @return true if a schedule was created, false otherwise
     */
    public boolean createSchedule()
    {
        if (roommates.isEmpty())
        {
            lastMessage = "Add a roommate first.";
            return false;
        }
        if (chores.isEmpty())
        {
            lastMessage = "Add a chore first.";
            return false;
        }
        String[] names = roommateArray();
        Chore[] allChores = choreArray();
        int start = random.nextInt(names.length);
        assignments.clear();
        for (int i = 0; i < allChores.length; i++)
        {
            String roommate = names[(start + i) % names.length];
            assignments.addToEnd(new ChoreAssignment(roommate,
                allChores[i].getName(), allChores[i].getPriority()));
        }
        week++;
        lastMessage = "Schedule created for week " + week + ".";
        return true;
    }

    /**
     * Swaps the chores (and their priorities) of two roommates. If a
     * roommate has more than one chore, their first chore is swapped.
     * All other assignments stay the same.
     *
     * @param roommate1 the first roommate
     * @param roommate2 the second roommate
     * @return true if the swap happened, false otherwise
     */
    public boolean swapAssignments(String roommate1, String roommate2)
    {
        if (!canSwap(roommate1, roommate2))
        {
            return false;
        }
        return swap(findAssignment(roommate1), findAssignment(roommate2));
    }

    public boolean swapAssignments(String roommate1, String chore1,
        String roommate2, String chore2)
    {
        if (!canSwap(roommate1, roommate2))
        {
            return false;
        }
        return swap(findAssignment(roommate1, chore1),
            findAssignment(roommate2, chore2));
    }

    private boolean canSwap(String roommate1, String roommate2)
    {
        if (assignments.isEmpty())
        {
            lastMessage = "Create a schedule first.";
            return false;
        }
        String stored1 = findRoommate(roommate1);
        String stored2 = findRoommate(roommate2);
        if (stored1 == null || stored2 == null)
        {
            lastMessage = "Roommate not found.";
            return false;
        }
        if (stored1.equals(stored2))
        {
            lastMessage = "A roommate cannot swap with themselves.";
            return false;
        }
        return true;
    }

    private boolean swap(ChoreAssignment first, ChoreAssignment second)
    {
        if (first == null || second == null)
        {
            lastMessage = "Both roommates need that chore to swap.";
            return false;
        }
        String tempChore = first.getChore();
        String tempPriority = first.getPriority();
        first.setChore(second.getChore());
        first.setPriority(second.getPriority());
        second.setChore(tempChore);
        second.setPriority(tempPriority);
        lastMessage = "Swapped chores between " + first.getRoommate()
            + " and " + second.getRoommate() + ".";
        return true;
    }

    /**
     * Rotates the schedule for the next week. Each chore moves to the
     * next roommate in the roommate list, and the last roommate's chores
     * go to the first roommate.
     *
     * @return true if the schedule was rotated, false otherwise
     */
    public boolean rotateSchedule()
    {
        if (assignments.isEmpty())
        {
            lastMessage = "Create a schedule first.";
            return false;
        }
        String[] names = roommateArray();
        ChoreAssignment[] current = assignmentArray();
        assignments.clear();
        for (int i = 0; i < current.length; i++)
        {
            int index = indexOf(names, current[i].getRoommate());
            String next = names[(index + 1) % names.length];
            assignments.addToEnd(new ChoreAssignment(next,
                current[i].getChore(), current[i].getPriority()));
        }
        week++;
        lastMessage = "Schedule rotated to week " + week + ".";
        return true;
    }

    /**
     * Finds a roommate's assignment. If they have more than one chore,
     * the first one is returned.
     *
     * @param roommate the roommate to look for
     * @return the assignment, or null if the roommate has none
     */
    public ChoreAssignment findAssignment(String roommate)
    {
        String stored = findRoommate(roommate);
        if (stored == null)
        {
            return null;
        }
        ChoreAssignment[] current = assignmentArray();
        for (int i = 0; i < current.length; i++)
        {
            if (current[i].getRoommate().equals(stored))
            {
                return current[i];
            }
        }
        return null;
    }

    /**
     * Gets every assignment with the given priority.
     *
     * @param priority "High", "Medium", or "Low" (any capitalization)
     * @return the matching assignments (possibly empty), or null if the
     *         priority is not valid
     */
    public ChoreAssignment[] getAssignmentsByPriority(String priority)
    {
        String validPriority = normalizePriority(priority);
        if (validPriority == null)
        {
            lastMessage = "Priority must be High, Medium, or Low.";
            return null;
        }
        ChoreAssignment[] current = assignmentArray();
        int matches = 0;
        for (int i = 0; i < current.length; i++)
        {
            if (current[i].getPriority().equals(validPriority))
            {
                matches++;
            }
        }
        ChoreAssignment[] result = new ChoreAssignment[matches];
        int next = 0;
        for (int i = 0; i < current.length; i++)
        {
            if (current[i].getPriority().equals(validPriority))
            {
                result[next] = current[i];
                next++;
            }
        }
        return result;
    }

    /**
     * Gets the number of roommates.
     *
     * @return the roommate count
     */
    public int getRoommateCount()
    {
        return roommates.size();
    }

    /**
     * Gets the number of chores.
     *
     * @return the chore count
     */
    public int getChoreCount()
    {
        return chores.size();
    }

    /**
     * Gets the number of assignments in the current schedule.
     *
     * @return the assignment count
     */
    public int getAssignmentCount()
    {
        return assignments.size();
    }

    /**
     * Checks whether a roommate exists.
     *
     * @param name the roommate name
     * @return true if the roommate exists, false otherwise
     */
    public boolean hasRoommate(String name)
    {
        return findRoommate(name) != null;
    }

    /**
     * Copies the assignments into an array.
     *
     * @return the assignments, in schedule order
     */
    private ChoreAssignment[] assignmentArray()
    {
        return assignments.toArray(new ChoreAssignment[assignments.size()]);
    }

    /**
     * Copies the roommates into an array.
     *
     * @return the roommates, in the order they were added
     */
    private String[] roommateArray()
    {
        return roommates.toArray(new String[roommates.size()]);
    }

    /**
     * Copies the chores into an array.
     *
     * @return the chores
     */
    private Chore[] choreArray()
    {
        return chores.toArray(new Chore[chores.size()]);
    }

    /**
     * Finds the position of a name in an array.
     *
     * @param names the array to search
     * @param name the name to find
     * @return the index, or -1 if not found
     */
    private int indexOf(String[] names, String name)
    {
        for (int i = 0; i < names.length; i++)
        {
            if (names[i].equals(name))
            {
                return i;
            }
        }
        return -1;
    }

    /**
     * Turns a priority into "High", "Medium", or "Low".
     *
     * @param priority the priority typed by the user
     * @return the standard priority, or null if it is not valid
     */
    private String normalizePriority(String priority)
    {
        if (priority == null)
        {
            return null;
        }
        String lower = priority.trim().toLowerCase();
        if (lower.equals("high"))
        {
            return "High";
        }
        if (lower.equals("medium"))
        {
            return "Medium";
        }
        if (lower.equals("low"))
        {
            return "Low";
        }
        return null;
    }

    /**
     * Checks whether a String is null or only whitespace.
     *
     * @param text the text to check
     * @return true if the text is null or blank
     */
    private boolean isBlank(String text)
    {
        return text == null || text.trim().isEmpty();
    }

    private String findRoommate(String name)
{
    if (isBlank(name))
    {
        return null;
    }
    String trimmed = name.trim();
    String[] names = roommateArray();
    for (int i = 0; i < names.length; i++)
    {
        if (names[i].equalsIgnoreCase(trimmed))
        {
            return names[i];
        }
    }
    return null;
}

    /**
     * Counts how many chores a roommate has in the current schedule.
     */
    private int countFor(String roommate)
    {
        int total = 0;
        ChoreAssignment[] current = assignmentArray();
        for (int i = 0; i < current.length; i++)
        {
            if (current[i].getRoommate().equals(roommate))
            {
                total++;
            }
        }
        return total;
    }

    /**
     * Finds the roommate with the fewest chores (first one wins ties).
     * Only call this when there is at least one roommate.
     */
    private String leastBusyRoommate()
    {
        String[] names = roommateArray();
        String best = names[0];
        int bestCount = countFor(best);
        for (int i = 1; i < names.length; i++)
        {
            int count = countFor(names[i]);
            if (count < bestCount)
            {
                best = names[i];
                bestCount = count;
            }
        }
        return best;
    }

    /**
     * Finds the roommate with the most chores (first one wins ties).
     * Only call this when there is at least one roommate.
     */
    private String busiestRoommate()
    {
        String[] names = roommateArray();
        String best = names[0];
        int bestCount = countFor(best);
        for (int i = 1; i < names.length; i++)
        {
            int count = countFor(names[i]);
            if (count > bestCount)
            {
                best = names[i];
                bestCount = count;
            }
        }
        return best;
    }
}
