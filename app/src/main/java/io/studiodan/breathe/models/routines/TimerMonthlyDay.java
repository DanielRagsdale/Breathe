package io.studiodan.breathe.models.routines;

import java.util.Calendar;

/**
 * Dummy ITimer. Used for testing and to help development
 */
public class TimerMonthlyDay implements ITimer
{
    int mTime;

    public TimerMonthlyDay(int time)
    {
        mTime = time;
    }

    @Override
    public TimePeriod getPeriodOnDay(Calendar day)
    {
        if(occursOn(day))
        {
            TimePeriod period = new TimePeriod(mTime, 60);
            return period;
        }

        return null;
    }

    @Override
    public boolean occursOn(Calendar day)
    {
        return true;
    }

    @Override
    public ETimers getTimerType()
    {
        //TODO
        return ETimers.TIMER_DATE;
    }
}