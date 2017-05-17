package io.studiodan.breathe.models.routines;


import java.util.Calendar;

/**
 * Parent Class determining when a routine item will be displayed
 *
 * Types:
 *      Weekly on specific days (e.g. m, w, f)
 *      Monthly on specific dates (e.g. 1st, 15th)
 *      Monthly on specific day (e.g. first two saturdays of the month)
 *
 *      Multiple timer types can be added to a single routine element
 *
 * Times:
 *      Any time range can be specified for a routine
 *      Different time ranges can be specified for the different days
 *          that an event occurs (e.g. Monday at 1:00PM, Friday at 3:00PM)
 *
 */
public abstract class RoutineTimer
{
    /**
     * Get an array of TimePeriods representing when this routine is scheduled
     *
     * @param day what day to get TimePeriods for
     * @return the array of TimePeriods
     */
    public abstract TimePeriod[] getPeriodsOnDay(Calendar day);

    /**
     * Does the routine element occur on the given day
     *
     * @param day the day that is checked for occurance
     * @return whether or not the routine element occurs on that day
     */
    public abstract boolean occursOn(Calendar day);

}

