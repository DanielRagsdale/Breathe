package io.studiodan.breathe;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.CardView;
import android.support.v7.widget.Toolbar;
import android.widget.ListView;
import android.widget.TextView;

import io.studiodan.breathe.fragments.FragmentLifeList;
import io.studiodan.breathe.models.ToDoList;

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
    }
}
