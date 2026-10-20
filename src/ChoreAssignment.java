public class ChoreAssignment
{
    private String roommate;
    private String chore;
    private String priority;

    /**
     * Creates one assignment.
     *
     * @param roommate the roommate responsible for the chore
     * @param chore the chore name
     * @param priority the chore priority: "High", "Medium", or "Low"
     */
    public ChoreAssignment(String roommate, String chore, String priority)
    {
        this.roommate = roommate;
        this.chore = chore;
        this.priority = priority;
    }

    /**
     * Gets the roommate name.
     *
     * @return the roommate
     */
    public String getRoommate()
    {
        return roommate;
    }

    /**
     * Gets the assigned chore.
     *
     * @return the chore
     */
    public String getChore()
    {
        return chore;
    }

    /**
     * Gets the priority of the assigned chore.
     *
     * @return the priority
     */
    public String getPriority()
    {
        return priority;
    }

    public void setRoommate(String roommate)
    {
        this.roommate = roommate;
    }

    /**
     * Changes the chore during an approved swap.
     *
     * @param chore the new chore
     */
    public void setChore(String chore)
    {
        this.chore = chore;
    }

    /**
     * Changes the priority during an approved swap, so the priority
     * stays with its chore.
     *
     * @param priority the new priority
     */
    public void setPriority(String priority)
    {
        this.priority = priority;
    }

    /**
     * Returns a readable assignment, such as "John - Dishes - High".
     *
     * @return the assignment as a String
     */
    @Override
    public String toString()
    {
        return roommate + " - " + chore + " - " + priority;
    }

    /**
     * Checks whether two assignments have the same roommate, chore,
     * and priority.
     *
     * @param other the object to compare to
     * @return true if all three fields match, false otherwise
     */
    @Override
    public boolean equals(Object other)
    {
        if (this == other)
        {
            return true;
        }
        if (other == null || getClass() != other.getClass())
        {
            return false;
        }
        ChoreAssignment that = (ChoreAssignment)other;
        return roommate.equals(that.roommate) && chore.equals(that.chore)
            && priority.equals(that.priority);
    }

    /**
     * Returns a hash code that matches equals.
     *
     * @return the hash code
     */
    @Override
    public int hashCode()
    {
        return (roommate.hashCode() * 31 + chore.hashCode()) * 31
            + priority.hashCode();
    }
}
