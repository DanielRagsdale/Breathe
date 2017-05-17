package io.studiodan.breathe.models.routines;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.Calendar;

/**
 * Dummy Timer. Used for testing and to help development
 */
public class DummyRoutineElement extends RoutineTimer
{
    @Override
    public TimePeriod[] getPeriodsOnDay(Calendar day)
    {
        if(occursOn(day))
        {
            TimePeriod[] periods = {new TimePeriod(12, 0, 60)};
            return periods;
        }

        return null;
    }

    @Override
    public boolean occursOn(Calendar day)
    {
        return true;
    }

    public class DummyRoutineInstance implements RoutineInstance
    {

        @Override
        public View createTimelineView(LayoutInflater inflater, ViewGroup container)
        {
            return null;
        }

        @Override
        public View createScheduleView(LayoutInflater inflater, ViewGroup container)
        {
            return null;
        }
    }
}
