package io.studiodan.breathe.models.routines;


import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.List;

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
    List<RoutineTimer> mTimers;

    /**
     * Create a new basic routine element with given title
     *
     * @param title the title of this routine
     */
    public RoutineElement(String title, RoutineTimer... timers)
    {
        this(timers);
        mTitle = title;
    }

    protected RoutineElement(RoutineTimer... timers)
    {
        mTimers = Arrays.asList(timers);
    }

    /**
     * Get an array of all the periods of the routine on the given day
     *
     * @param day the day where the routine is examined
     * @return
     */
    public List<RoutineInstance> getInstancesForDay(Calendar day)
    {
        ArrayList<RoutineInstance> ri = new ArrayList<>();

        for(RoutineTimer t : mTimers)
        {
            for(TimePeriod p : t.getPeriodsOnDay(Calendar.getInstance()))
            {
                ri.add(new RoutineElement.Instance(p, mTitle));
            }
        }

        return ri;
    }

    /**
     * Instance for the most basic type of RoutineElement
     */
    public class Instance implements  RoutineInstance
    {
        TimePeriod mTimePeriod;
        String mName;

        public Instance(TimePeriod period, String name)
        {
            mTimePeriod = period;
            mName = name;
        }

        @Override
        public TimePeriod getTimePeriod()
        {
            return mTimePeriod;
        }

        @Override
        public String getName()
        {
            return mName;
        }

        @Override
        public View createTimelineView(LayoutInflater inflater, ViewGroup container)
        {
            TextView tv = new TextView(container.getContext());
            tv.setText(mName);

            return tv;
        }

        @Override
        public View createScheduleView(LayoutInflater inflater, ViewGroup container)
        {
            TextView tv = new TextView(container.getContext());
            tv.setText(mName);

            return tv;
        }
    }
}






