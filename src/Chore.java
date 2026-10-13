public class Chore
{
    private String name;
    private String priority;

    /**
     * Creates a chore with a name and priority.
     *
     * @param name the chore name, such as "Dishes"
     * @param priority the priority: "High", "Medium", or "Low"
     */
    public Chore(String name, String priority)
    {
        this.name = name;
        this.priority = priority;
    }

    /**
     * Gets the chore name.
     *
     * @return the chore name
     */
    public String getName()
    {
        return name;
    }

    /**
     * Gets the chore priority.
     *
     * @return the priority
     */
    public String getPriority()
    {
        return priority;
    }

    /**
     * Returns the chore as readable text, such as "Dishes (High)".
     *
     * @return the chore as a String
     */
    @Override
    public String toString()
    {
        return name + " (" + priority + ")";
    }

    /**
     * Checks whether two chores have the same name and priority.
     *
     * @param other the object to compare to
     * @return true if both chores match, false otherwise
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
        Chore that = (Chore)other;
        return name.equals(that.name) && priority.equals(that.priority);
    }

    /**
     * Returns a hash code that matches equals.
     *
     * @return the hash code
     */
    @Override
    public int hashCode()
    {
        return name.hashCode() * 31 + priority.hashCode();
    }
}
