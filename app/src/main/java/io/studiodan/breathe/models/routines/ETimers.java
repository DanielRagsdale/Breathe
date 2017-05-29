package io.studiodan.breathe.models.routines;

import android.app.DialogFragment;

import io.studiodan.breathe.R;

public enum ETimers
{
    TIMER_WEEKLY(R.mipmap.ic_timer_week, 11900, new DialogCreateTimerWeekly()), TIMER_DATE(R.mipmap.ic_timer_date, 11901, null);

    public int mImageResource;
    public int mID;
    public DialogFragment mDialog;

    ETimers(int imRes, int id, DialogFragment dialog)
    {
        mImageResource = imRes;
        mID = id;
        mDialog = dialog;
    }
}

