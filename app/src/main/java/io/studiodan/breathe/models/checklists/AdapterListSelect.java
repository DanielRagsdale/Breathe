package io.studiodan.breathe.models.checklists;

import android.database.DataSetObserver;
import android.util.TypedValue;
import android.view.View;
import android.view.ViewGroup;
import android.widget.SpinnerAdapter;
import android.widget.TextView;

public class AdapterListSelect implements SpinnerAdapter
{
    private ToDoList topList;

    public AdapterListSelect(ToDoList tl)
    {
        topList = tl;
    }

    //TODO make the list select menu prettier
    @Override
    public View getDropDownView(int position, View convertView, ViewGroup parent)
    {
        ToDoList list = topList.getListAtPos(position);

        TextView tv = new TextView(parent.getContext());

        float scale = parent.getResources().getDisplayMetrics().density;
        int four_dp = (int)(4*scale + 0.5f);
        tv.setPadding(four_dp, four_dp, four_dp, four_dp);

        String displayName = list.fullName;
        if(displayName.length() > 24)
        {
            displayName = "..." + displayName.substring(displayName.length() - 21);
        }

        tv.setText(displayName);
        tv.setTextSize(TypedValue.COMPLEX_UNIT_SP, 24);

        return tv;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent)
    {
        ToDoList list = topList.getListAtPos(position);

        TextView tv = new TextView(parent.getContext());

        float scale = parent.getResources().getDisplayMetrics().density;
        int four_dp = (int)(4*scale + 0.5f);
        tv.setPadding(four_dp, four_dp, four_dp, four_dp);

        String displayName = list.fullName;
        if(displayName.length() > 50)
        {
            displayName = "..." + displayName.substring(displayName.length() - 47);
        }

        tv.setText(displayName);
        tv.setTextSize(TypedValue.COMPLEX_UNIT_SP, 18);

        return tv;
    }

    @Override
    public Object getItem(int position)
    {
        return topList.getListAtPos(position);
    }

    @Override
    public int getCount()
    {
        return topList.getTotalListCount();
    }

    @Override
    public void registerDataSetObserver(DataSetObserver observer)
    {

    }

    @Override
    public void unregisterDataSetObserver(DataSetObserver observer)
    {

    }

    @Override
    public long getItemId(int position)
    {
        return 0;
    }

    @Override
    public boolean hasStableIds()
    {
        return true;
    }

    @Override
    public int getItemViewType(int position)
    {
        return 0;
    }

    @Override
    public int getViewTypeCount()
    {
        return 1;
    }

    @Override
    public boolean isEmpty()
    {
        return false;
    }
}
