package io.studiodan.breathe;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.icu.util.Calendar;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import io.studiodan.breathe.models.AdapterListSelect;
import io.studiodan.breathe.models.ToDoItem;
import io.studiodan.breathe.models.ToDoList;
import io.studiodan.breathe.fragments.FragmentToDo;

/**
 * Activity representing pain to add tasks and sublists to a ToDoList
 */
public class ActivityAddTask extends AppCompatActivity implements DatePickerDialog.OnDateSetListener
{
    protected static Spinner mListSelect;

    private Toolbar mToolbar;
    private EditText mInputName;
    private EditText mInputDate;

    private int dueYear = Integer.MAX_VALUE;
    private int dueMonth = Integer.MAX_VALUE;
    private int dueDay = Integer.MAX_VALUE;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_task);

        mToolbar = (Toolbar) findViewById(R.id.toolbar);
        mListSelect = (Spinner) findViewById(R.id.spinner_list_select);
        mInputName = (EditText) findViewById(R.id.input_task_add);
        mInputDate = (EditText) findViewById(R.id.input_task_due_date);

        mInputDate.setKeyListener(null);

        AdapterListSelect adapter = new AdapterListSelect(FragmentToDo.topList);
        mListSelect.setAdapter(adapter);

        setSupportActionBar(mToolbar);
        getSupportActionBar().setTitle("");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu)
    {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_add_task, menu);
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
            //Add the task
            String itemName = mInputName.getText().toString();
            if(!itemName.equals(""))
            {
                Toast.makeText(this, "Task Added", Toast.LENGTH_SHORT).show();

                ToDoList list = (ToDoList) mListSelect.getSelectedItem();
                list.add(new ToDoItem(itemName, dueYear, dueMonth, dueDay));
            }

            finish();
        }

        if(id == android.R.id.home)
        {
            finish();
        }

        return super.onOptionsItemSelected(item);
    }

    /**
     * Show a Dialog allowing user to create a new ToDoList. Invoked from layout file
     *
     * @param v
     */
    public void showNewListDialog(View v)
    {
        int id = mListSelect.getSelectedItemPosition();

        DialogFragment newList = NewListFragment.newInstance(1, id);

        newList.show(getSupportFragmentManager(), "newList");
    }

    /**
     * Show a Date Picker Dialog allowing user to specify task's due date. Invoked from layout file
     *
     * @param v
     */
    public void showDatePickerDialog(View v)
    {
        final Calendar c = Calendar.getInstance();

        DialogFragment datePicker = new DatePickerFragment();
        Bundle fragArgs = new Bundle();

        if(dueDay == Integer.MAX_VALUE || dueMonth == Integer.MAX_VALUE || dueYear == Integer.MAX_VALUE)
        {
            fragArgs.putInt("year", c.get(Calendar.YEAR));
            fragArgs.putInt("month", c.get(Calendar.MONTH));
            fragArgs.putInt("day", c.get(Calendar.DAY_OF_MONTH));
        }
        else
        {
            fragArgs.putInt("year", dueYear);
            fragArgs.putInt("month", dueMonth);
            fragArgs.putInt("day", dueDay);
        }

        datePicker.setArguments(fragArgs);
        datePicker.show(getSupportFragmentManager(), "datePicker");
    }

    public static class NewListFragment extends DialogFragment
    {
        int mNum;
        int mListVal;

        static NewListFragment newInstance(int num, int listVal)
        {
            NewListFragment f = new NewListFragment();

            // Supply num input as an argument.
            Bundle args = new Bundle();
            args.putInt("num", num);
            args.putInt("listVal", listVal);
            f.setArguments(args);

            return f;
        }

        @Override
        public void onCreate(Bundle savedInstanceState)
        {
            super.onCreate(savedInstanceState);
            mNum = getArguments().getInt("num");
            mListVal = getArguments().getInt("listVal");

            // Pick a style based on the num.
            int style = DialogFragment.STYLE_NORMAL, theme = 0;
            switch ((mNum-1)%6)
            {
                case 1: style = DialogFragment.STYLE_NO_TITLE; break;
                case 2: style = DialogFragment.STYLE_NO_FRAME; break;
                case 3: style = DialogFragment.STYLE_NO_INPUT; break;
                case 4: style = DialogFragment.STYLE_NORMAL; break;
                case 5: style = DialogFragment.STYLE_NORMAL; break;
                case 6: style = DialogFragment.STYLE_NO_TITLE; break;
                case 7: style = DialogFragment.STYLE_NO_FRAME; break;
                case 8: style = DialogFragment.STYLE_NORMAL; break;
            }
            switch ((mNum-1)%6)
            {
                case 4: theme = android.R.style.Theme_Holo; break;
                case 5: theme = android.R.style.Theme_Holo_Light_Dialog; break;
                case 6: theme = android.R.style.Theme_Holo_Light; break;
                case 7: theme = android.R.style.Theme_Holo_Light_Panel; break;
                case 8: theme = android.R.style.Theme_Holo_Light; break;
            }
            setStyle(style, theme);
        }

        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
        {
            View v = inflater.inflate(R.layout.dialog_new_list, container, false);

            final EditText listName = (EditText) v.findViewById(R.id.input_new_list_name);

            Button confirm = (Button) v.findViewById(R.id.button_confirm_new_list);
            confirm.setOnClickListener(new View.OnClickListener() { @Override
                public void onClick(View v)
                {
                    String name = listName.getText().toString();
                    if(!name.equals(""))
                    {
                        ToDoList list = FragmentToDo.topList;
                        ToDoList activeList = list.getListAtPos(mListVal);

                        ToDoList newList = new ToDoList(name);
                        activeList.add(newList);

                        //mListSelect.getAdapter().g
                        int pos = list.getPositionOfList(newList);
                        mListSelect.setSelection(pos);

                        NewListFragment.this.dismiss();
                    }
                }
            });

            return v;
        }
    }

    @Override
    public void onDateSet(DatePicker view, int year, int month, int day)
    {
        dueYear = year;
        dueMonth = month;
        dueDay = day;

        mInputDate.setText((month + 1) + "/" + day + "/" + year);
    }

    public static class DatePickerFragment extends DialogFragment implements DatePickerDialog.OnDateSetListener
    {

        @Override
        public Dialog onCreateDialog(Bundle savedInstanceState)
        {
            //Log.d("Breathe", "Test " + savedInstanceState);

            Bundle args = getArguments();

            // Use the current date as the default date in the picker
            int year = args.getInt("year");
            int month = args.getInt("month");
            int day = args.getInt("day");

            // Create a new instance of DatePickerDialog and return it
            return new DatePickerDialog(getActivity(), (ActivityAddTask)getActivity(), year, month, day);
        }

        public void onDateSet(DatePicker view, int year, int month, int day)
        {
            // Do something with the date chosen by the user
        }
    }
}
