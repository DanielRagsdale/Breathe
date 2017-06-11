package io.studiodan.breathe.models.routines;

import java.util.Calendar;

/**
 * Dummy ITimer. Used for testing and to help development
 */
public class TimerWeekly implements ITimer
{
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

        d -= 2;
        if(d == -1) // Reorder so Monday is first day of week
        {
            d = 6;
        }

        return mDays[d];
    }

    @Override
    public String description()
    {
        String desc = "Every ";

        if(mDays[0]) desc += "M, ";
        if(mDays[1]) desc += "Tu, ";
        if(mDays[2]) desc += "W, ";
        if(mDays[3]) desc += "Th, ";
        if(mDays[4]) desc += "F, ";
        if(mDays[5]) desc += "Sa, ";
        if(mDays[6]) desc += "Su, ";

        desc = desc.substring(0, desc.length() - 2);
        desc += " at " + mPeriod.getStartTimeString();
        desc += " for " + mPeriod.mDuration + " minutes.";

        return desc;
    }

    @Override
    public ETimers getTimerType()
    {
        return ETimers.TIMER_WEEKLY;
    }
}




