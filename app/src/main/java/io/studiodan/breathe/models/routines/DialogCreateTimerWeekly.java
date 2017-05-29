package io.studiodan.breathe.models.routines;

import android.app.DialogFragment;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.SeekBar;
import android.widget.TextView;

import java.lang.reflect.Field;

import io.studiodan.breathe.ActivityAddRoutine;
import io.studiodan.breathe.R;

public class DialogCreateTimerWeekly extends DialogFragment
{
    int mDuration = 60;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
    {
        final ViewGroup mainV = (ViewGroup) inflater.inflate(R.layout.dialog_new_timer_weekly, container);
        SeekBar sb = (SeekBar) mainV.findViewById(R.id.sb_duration);
        TextView tv = (TextView) mainV.findViewById(R.id.tv_duration);

        Button b = (Button) mainV.findViewById(R.id.button_confirm_new_timer);




        b.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                boolean[] cStates = new boolean[7];
                for(int i = 0; i < cStates.length; i++)
                {
                    CheckBox cb = (CheckBox) mainV.findViewById(getActivity().getResources().getIdentifier("cb_day_" + i, "id", getActivity().getPackageName()));

                    if(cb != null)
                    {
                        cStates[i] = cb.isChecked();
                    }
                }

                TimerWeekly w = new TimerWeekly(new TimePeriod(0, 60), cStates);

                ((ActivityAddRoutine)getActivity()).addTimer(w);
                DialogCreateTimerWeekly.this.dismiss();
            }
        });

        return mainV;
    }
}
