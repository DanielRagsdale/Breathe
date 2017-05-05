package io.studiodan.breathe.models;

/**
 * Class representing a single task on a To Do list
 */
public class ToDoItem implements Comparable<ToDoItem>
{
    public String title;
    public String description;
    public boolean isChecked;

    public int dueYear = Integer.MAX_VALUE;
    public int dueMonth = Integer.MAX_VALUE;
    public int dueDay = Integer.MAX_VALUE;

    /**
     * Create ToDoItem with given title
     *
     * @param title title of ToDoItem
     */
    public ToDoItem(String title)
    {
        this.title = title;
    }

    /**
     * Create ToDoItem with given title and due date
     *
     * @param title title of ToDoItem
     * @param dY due year of ToDoItem
     * @param dM due month of ToDoItem
     * @param dD due day of ToDoItem
     */
    public ToDoItem(String title, int dY, int dM, int dD)
    {
       this(title);

        dueYear = dY;
        dueMonth = dM;
        dueDay = dD;
    }

    /**
     * Create ToDoItem with given title and due date
     *
     * @param title title of ToDoItem
     * @param dY due year of ToDoItem
     * @param dM due month of ToDoItem
     * @param dD due day of ToDoItem
     * @param desc description of ToDoItem
     */
    public ToDoItem(String title, int dY, int dM, int dD, String desc)
    {
        this(title, dY, dM, dD);

        description = desc;
    }

    public void setCheckedState(boolean checkedState)
    {
        isChecked = checkedState;
    }

    /**
     * Order ToDoItem
     * Sorts by check state, due date, then name
     *
     * @param o ToDoItem to be compared against
     * @return comparison result
     */
    @Override
    public int compareTo(ToDoItem o)
    {
        if(isChecked == o.isChecked)
        {
            int yearDif = dueYear - o.dueYear;
            if(yearDif == 0)
            {
                int monthDif = dueMonth - o.dueMonth;
                if(monthDif == 0)
                {
                    int dayDiff = dueDay - o.dueDay;
                    if(dayDiff == 0)
                    {
                        return title.compareTo(o.title);
                    }
                    return dayDiff;
                }
                return monthDif;
            }
            return yearDif;
        }
        else
        {
            return isChecked ? 1 : -1;
        }
    }

    @Override
    public String toString()
    {
        return title + " " + isChecked;
    }
}
