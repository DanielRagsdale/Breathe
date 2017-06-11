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
import android.widget.EditText;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.TimePicker;

import org.w3c.dom.Text;

import java.lang.reflect.Field;

import io.studiodan.breathe.ActivityAddRoutine;
import io.studiodan.breathe.R;

public class DialogCreateTimerWeekly extends DialogFragment
{
    int mDuration = 60;

    TimePicker mTimePicker;

    //Max of 80
    //2*6 + 12*4 + 10*2
    SeekBar mDurationBar;
    TextView mDurationText;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
    {
        final ViewGroup mainV = (ViewGroup) inflater.inflate(R.layout.dialog_new_timer_weekly, container);

        mTimePicker = (TimePicker) mainV.findViewById(R.id.tp_start_time);

        mDurationBar = (SeekBar) mainV.findViewById(R.id.sb_duration);
        mDurationText = (TextView) mainV.findViewById(R.id.tv_duration);

        Button b = (Button) mainV.findViewById(R.id.button_confirm_new_timer);

        mDurationText.setText(String.format("%02d:%02d", mDuration / 60, mDuration % 60));
        mDurationBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener()
        {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser)
            {
                if(fromUser)
                {
                    mDuration = barToTime(progress);
                    mDurationText.setText(String.format("%02d:%02d", mDuration / 60, mDuration % 60));
                }
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {}

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {}
        });

        b.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                boolean[] cStates = new boolean[7];
                for(int i = 0; i < cStates.length; i++)
                {
                    CheckBox cb = (CheckBox) mainV.findViewById(getActivity().getResources().getIdentifier("cb_day_" + i, "id", getActivity().getPackageName()));
                    mDurationText.setText(String.format("%02d:%02d", mDuration / 60, mDuration % 60));
                    if(cb != null)
                    {
                        cStates[i] = cb.isChecked();
                    }
                }

                int sTime = mTimePicker.getHour() * 60 + mTimePicker.getMinute();
                TimerWeekly w = new TimerWeekly(new TimePeriod(sTime, mDuration), cStates);

                ((ActivityAddRoutine)getActivity()).addTimer(w);
                DialogCreateTimerWeekly.this.dismiss();
            }
        });

        return mainV;
    }

    //2*6 + 12*4 + 10*2
    private int barToTime(int barVal)
    {
        int minutes = 0;
        minutes += 10 * Math.min(12, barVal);
        minutes += 15 * Math.min(48, Math.max(0, barVal - 12));
        minutes += 30 * Math.min(20, Math.max(0, barVal - 60));

        return minutes;
    }
}





