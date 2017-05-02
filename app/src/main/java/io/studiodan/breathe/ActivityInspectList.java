package io.studiodan.breathe;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.CardView;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.widget.ListView;
import android.widget.TextView;

import io.studiodan.breathe.fragments.FragmentLifeList;
import io.studiodan.breathe.models.AdapterChecklist;
import io.studiodan.breathe.models.ToDoItem;
import io.studiodan.breathe.models.ToDoList;
import io.studiodan.breathe.util.multiselector.ActionCheckItemSingle;
import io.studiodan.breathe.util.multiselector.MultiSelector;

public class ActivityInspectList extends AppCompatActivity
{
    ToDoList mList;

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

        String dispName = mList.fullName;

        if(dispName.length() > 30)
        {
            dispName = "..." + dispName.substring(dispName.length() - 27);
        }

        getSupportActionBar().setTitle(dispName);

        mTitle.setText(mList.fullName);

        MultiSelector<ToDoItem> ms;


        ActionCheckItemSingle acis = new ActionCheckItemSingle(mList);
        ms = new MultiSelector<>(this, acis, R.menu.menu_edit_todo_list, "Editing Items");

        AdapterChecklist adapter = new AdapterChecklist(mList, mBody, true, ms);
        mBody.setAdapter(adapter);
        adapter.setHeightBasedOnChildren();

        acis.setAdapter(adapter);
        acis.setMultiSelector(ms);

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

        return super.onOptionsItemSelected(item);
    }

}
