package io.studiodan.breathe.models.routines;

import java.util.Calendar;

/**
 * Dummy Timer. Used for testing and to help development
 */
public class TimerWeekly extends Timer
{
    static
    {
        mType = ETimers.TIMER_WEEKLY;
    }

    TimePeriod mPeriod;
    boolean[] mDays;

    public TimerWeekly(TimePeriod period, boolean[] days)
    {
        mPeriod = period;
        mDays = days;
    }

    public TimerWeekly(int startTime, int duration, boolean[] days)
    {
        this(new TimePeriod(startTime, duration), days);
        mDays = days;
    }

    @Override
    public TimePeriod getPeriodOnDay(Calendar day)
    {
        if(occursOn(day))
        {
            return mPeriod;
        }

        return null;
    }

    @Override
    public boolean occursOn(Calendar checkDay)
    {
        int d = checkDay.get(Calendar.DAY_OF_WEEK);

        return mDays[d];
    }
}
