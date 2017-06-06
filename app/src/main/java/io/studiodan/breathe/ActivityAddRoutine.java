package io.studiodan.breathe;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Spinner;

import java.util.ArrayList;
import java.util.List;

import io.studiodan.breathe.fragments.FragmentRoutines;
import io.studiodan.breathe.models.routines.AdapterTimerSelect;
import io.studiodan.breathe.models.routines.ETimers;
import io.studiodan.breathe.models.routines.ITimer;
import io.studiodan.breathe.models.routines.RoutineElement;

public class ActivityAddRoutine extends AppCompatActivity
{
    private Toolbar mToolbar;

    private Spinner mRoutineSelect;
    private ArrayAdapter<String> mSpinnerAdapter;

    private RecyclerView mRVTimerSelect;
    private AdapterTimerSelect mAdapter;
    private RecyclerView.LayoutManager mLMTimerSelect;

    private LinearLayout mInputLayout;

    private List<ITimer> mTimers;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_routine);

        mToolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(mToolbar);
        getSupportActionBar().setTitle("");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        mRoutineSelect = (Spinner) findViewById(R.id.spinner_routine_select);

        String[] rTypes = getResources().getStringArray(R.array.routine_types);
        mSpinnerAdapter = new ArrayAdapter<String>(this, R.layout.item_routine_select, rTypes);
        mRoutineSelect.setAdapter(mSpinnerAdapter);

        mRVTimerSelect = (RecyclerView) findViewById(R.id.rv_timer_select);

        mAdapter = new AdapterTimerSelect();
        mRVTimerSelect.setAdapter(mAdapter);

        mLMTimerSelect = new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false);
        mRVTimerSelect.setLayoutManager(mLMTimerSelect);

        mInputLayout = (LinearLayout) findViewById(R.id.layout_input);

        mTimers = new ArrayList<>();


        mRoutineSelect.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener()
        {
            @Override
            public void onItemSelected(AdapterView<?> parentView, View selectedItemView, int position, long id)
            {
                if(position == 0)
                {
                    mInputLayout.removeAllViews();
                    View v = getLayoutInflater().inflate(R.layout.routine_creator_simple, mInputLayout);
                    //mInputLayout.addView(v);
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parentView)
            {
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu)
    {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_add_item, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item)
    {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_add_task)
        {
            createRoutine();
            finish();
        }
        else if(id == R.id.action_add_task_no_close)
        {
            createRoutine();
        }

        if(id == android.R.id.home)
        {
            finish();
        }

        return super.onOptionsItemSelected(item);
    }

    public void createTimer(View v)
    {
        int vID = v.getId();
        if(vID == ETimers.TIMER_WEEKLY.mID)
        {
            ETimers.TIMER_WEEKLY.mDialog.show(getFragmentManager(), "TAG!");
        }
        else if(vID == ETimers.TIMER_DATE.mID)
        {

        }
    }

    public void addTimer(ITimer rt)
    {
        mTimers.add(rt);
    }

    private void createRoutine()
    {
        String title = ((EditText) findViewById(R.id.title)).getText().toString();
        RoutineElement r = new RoutineElement(title, mTimers.toArray(new ITimer[0]));
        FragmentRoutines.mRoutines.add(r);
    }
}












