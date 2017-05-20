package io.studiodan.breathe;

import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.CardView;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;

import io.studiodan.breathe.fragments.FragmentLifeList;
import io.studiodan.breathe.models.checklists.AdapterChecklist;
import io.studiodan.breathe.models.checklists.ToDoItem;
import io.studiodan.breathe.models.checklists.ToDoList;
import io.studiodan.breathe.util.multiselector.ActionCheckItemSingle;
import io.studiodan.breathe.util.multiselector.MultiSelector;

public class ActivityInspectList extends AppCompatActivity
{
    protected ToDoList mList;

    private Toolbar mToolbar;
    private CardView mCardView;
    private TextView mTitle;
    private ListView mBody;

    @Override
    public void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_inspect_list);
        mToolbar = (Toolbar) findViewById(R.id.toolbar);
        mCardView = (CardView) findViewById(R.id.cv_list);
        mTitle = (TextView) mCardView.findViewById(R.id.tv_todo_title);
        mBody = (ListView) mCardView.findViewById(R.id.lv_todo_list);

        int activeList = getIntent().getExtras().getInt(getString(R.string.single_ToDo_id));
        mList = FragmentLifeList.topList.getListAtPos(activeList);

        setSupportActionBar(mToolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        MultiSelector<ToDoItem> ms;
        ActionCheckItemSingle acis = new ActionCheckItemSingle(mList);
        ms = new MultiSelector<>(this, acis, R.menu.menu_edit_todo_list, "Editing Items");

        AdapterChecklist adapter = new AdapterChecklist(this, mList, mBody, true, ms);
        mBody.setAdapter(adapter);
        adapter.setHeightBasedOnChildren();

        acis.setAdapter(adapter);
        acis.setMultiSelector(ms);


        updateName();
    }

    public void updateName()
    {
        String dispName = mList.localName;

        if(dispName.length() > 30)
        {
            dispName = "..." + dispName.substring(dispName.length() - 27);
        }

        getSupportActionBar().setTitle(dispName);

        mTitle.setText(mList.localName);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu)
    {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_inspect_list, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item)
    {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        if(id == android.R.id.home)
        {
            finish();
        }
        if (id == R.id.action_rename)
        {
            DialogFragment newList = RenameFragment.newInstance(1);
            newList.show(getSupportFragmentManager(), "renameList");

            return true;
        }


        return super.onOptionsItemSelected(item);
    }

    public static class RenameFragment extends DialogFragment
    {
        int mNum;

        static RenameFragment newInstance(int num)
        {
            RenameFragment f = new RenameFragment();

            // Supply num input as an argument.

            Bundle args = new Bundle();
            args.putInt("num", num);
            f.setArguments(args);

            return f;
        }

        @Override
        public void onCreate(Bundle savedInstanceState)
        {
            super.onCreate(savedInstanceState);
            mNum = getArguments().getInt("num");

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
            final ActivityInspectList activity = (ActivityInspectList) getActivity();
            View v = inflater.inflate(R.layout.dialog_rename, container, false);

            final EditText listName = (EditText) v.findViewById(R.id.input_new_list_name);

            listName.setText(activity.mList.localName);

            Button confirm = (Button) v.findViewById(R.id.button_confirm_rename);
            confirm.setOnClickListener(new View.OnClickListener()
            {
                @Override
                public void onClick(View v)
                {
                    String name = listName.getText().toString();
                    if(!name.equals(""))
                    {

                        activity.mList.localName = name;
                        activity.updateName();

                        RenameFragment.this.dismiss();
                    }
                }
            });

            return v;
        }
    }

}






