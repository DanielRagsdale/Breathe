package io.studiodan.breathe.models.routines;

import java.util.Calendar;

/**
 * Dummy Timer. Used for testing and to help development
 */
public class TimerMonthlyDate extends Timer
{
    static
    {
        mType = ETimers.TIMER_DATE;
    }

    int mTime;

    public TimerMonthlyDate(int time)
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
}
