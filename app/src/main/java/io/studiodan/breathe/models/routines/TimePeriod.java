package io.studiodan.breathe.models.routines;

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
}
