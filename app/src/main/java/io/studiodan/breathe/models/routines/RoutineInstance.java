package io.studiodan.breathe.models.routines;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

/**
 * Single instance of a routine
 */
public interface RoutineInstance
{
    /**
     * Return the TimePeriod that this routine spans
     *
     * @return the TimePeriod of this routine
     */
    TimePeriod getTimePeriod();

    /**
     * Return the name of this routine
     *
     * @return the name of this routine
     */
    String getName();

    /**
     * View representation when seen in timeline (list) view
     *
     * @param inflater Layout inflater used to create view representation
     * @param container ViewGroup that will contain this view
     * @return View representatin of this element
     */
    View createTimelineView(LayoutInflater inflater, ViewGroup container);

    /**
     * View representation when seen in schedule view
     *
     * @param inflater Layout inflater used to create view representation
     * @param container ViewGroup that will contain this view
     * @return View representation of this element
     */
    View createScheduleView(LayoutInflater inflater, ViewGroup container);
}
