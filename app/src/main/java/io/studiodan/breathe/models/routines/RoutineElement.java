package io.studiodan.breathe.models.routines;


import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;
import java.util.Calendar;

/**
 * An item that repeats at specified intervals
 *
 * Attributes:
 *      Name -- The name of this routine
 *      Description -- The description of this routine
 *      Color -- The color that represents this routine
 *      Icon -- The icon that represents this routine
 *
 * Routine Types:
 *      Basic -- The routine only has a title
 *      Description -- The routine only has a simple title and description
 *      Checklist -- Routine has a checklist format
 */
public class RoutineElement
{
    String mTitle;

    ArrayList<RoutineTimer> mTimers;

    /**
     * Create a new basic routine element with given title
     *
     * @param title the title of this routine
     */
    public RoutineElement(String title)
    {
        this();
        mTitle = title;
    }

    protected RoutineElement()
    {
        mTimers = new ArrayList<>();

        //TODO make this make sense
        mTimers.add(new DummyRoutineElement());
    }

    /**
     * Get an array of all the periods of the routine on the given day
     *
     * @param day the day where the routine is examined
     * @return
     */
    public RoutineInstance[] getInstancesForDay(Calendar day)
    {
        return null;
    }
}






