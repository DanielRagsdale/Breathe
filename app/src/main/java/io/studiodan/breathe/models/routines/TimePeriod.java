package io.studiodan.breathe.models.routines;

import java.util.Formatter;
import java.util.Locale;

/**
 * Class representing a specific time period throughout the day
 */
public class TimePeriod
{
    /**
     * The minute of the day that this TimePeriod begins
     */
    public int mStartTime;

    /**
     * The duration of this TimePeriod in minutes
     */
    public int mDuration;

    /**
     * Create a new TimePeriod with specified parameters
     *
     * @param startTime the minute of the hour that this TimePeriod begins
     * @param duration the duration of this TimePeriod in minutes
     */
    public TimePeriod(int startTime, int duration)
    {
        mStartTime = startTime;
        mDuration = duration;
    }

    /**
     * Get a string representing the start time of this period
     *
     * @return the string reprsenting this TimePeriod
     */
    public String getStartTimeString()
    {
        int minutes = mStartTime % 60;

        int hours = mStartTime / 60;

        if(hours == 0)
        {
            return String.format("12:%02d AM", minutes);
        }
        else if(hours == 12)
        {
            return String.format("12:%02d PM", minutes);
        }
        else if(hours < 12)
        {
            return String.format("%02d:%02d AM", hours, minutes);
        }
        else
        {
            return String.format("%02d:%02d PM", hours - 12, minutes);
        }
    }
}






