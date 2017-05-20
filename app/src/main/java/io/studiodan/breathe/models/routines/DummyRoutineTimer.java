package io.studiodan.breathe.models.routines;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.Calendar;

/**
 * Dummy Timer. Used for testing and to help development
 */
public class DummyRoutineTimer extends RoutineTimer
{
    int mTime;

    public DummyRoutineTimer(int time)
    {
        mTime = time;
    }

    @Override
    public TimePeriod[] getPeriodsOnDay(Calendar day)
    {
        if(occursOn(day))
        {
            TimePeriod[] periods = {new TimePeriod(mTime, 60)};
            return periods;
        }

        return null;
    }

    @Override
    public boolean occursOn(Calendar day)
    {
        return true;
    }
}
