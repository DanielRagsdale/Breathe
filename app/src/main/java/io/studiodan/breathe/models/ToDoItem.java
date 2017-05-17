package io.studiodan.breathe.models;

import java.util.Calendar;

/**
 * Class representing a single task on a To Do list
 */
public class ToDoItem implements Comparable<ToDoItem>
{
    public String title;
    public String description;
    public boolean isChecked;

    Calendar dueDate;

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

        dueDate = Calendar.getInstance();
        dueDate.set(dY, dM, dD);
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
            int dateComp = dueDate.compareTo(o.dueDate);
            if(dateComp == 0)
            {
                return title.compareTo(o.title);
            }

            return dateComp;
        }
        else
        {
            return isChecked ? 1 : -1;
        }
    }

    //TODO generalize date display format
    /**
     * Get a string representation of the Due Date. mm/dd/yyyy format
     *
     * @return string representing the due date
     */
    public String getDueDateString()
    {
        String dueDateString = "";
        if(dueDate != null)
        {
            dueDateString = (dueDate.get(dueDate.MONTH) + 1) + "/" + dueDate.get(dueDate.DAY_OF_MONTH) + "/" + dueDate.get(dueDate.YEAR);
        }

        return dueDateString;
    }

    @Override
    public String toString()
    {
        return title + " " + isChecked;
    }
}
