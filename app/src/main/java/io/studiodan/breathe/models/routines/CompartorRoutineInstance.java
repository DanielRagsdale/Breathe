package io.studiodan.breathe.models.routines;

import java.util.Comparator;

public class CompartorRoutineInstance implements Comparator<RoutineInstance>
{
    @Override
    public int compare(RoutineInstance o1, RoutineInstance o2)
    {
        return o1.getTimePeriod().mStartTime - o2.getTimePeriod().mStartTime;
    }
}
