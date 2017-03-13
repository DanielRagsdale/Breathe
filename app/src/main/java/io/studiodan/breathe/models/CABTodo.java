package io.studiodan.breathe.models;

import android.util.Log;
import android.view.ActionMode;
import android.view.Menu;
import android.view.MenuItem;

import io.studiodan.breathe.R;
import io.studiodan.breathe.util.multiselector.MultiSelector;

public class CABTodo implements ActionMode.Callback
{
    MultiSelector mMultiSelector;

    public CABTodo(MultiSelector selector)
    {
        mMultiSelector = selector;
    }

    @Override
    public boolean onActionItemClicked(ActionMode mode, MenuItem item)
    {
        Log.d("Breathe", mode.toString());
        Log.d("Breathe", item.toString());

        mMultiSelector.launchAction(item.getItemId());

        // TODO Auto-generated method stub
        return false;
    }

    @Override
    public boolean onCreateActionMode(ActionMode mode, Menu menu)
    {
        // TODO Auto-generated method stub
        mode.getMenuInflater().inflate(R.menu.menu_edit_todo_item, menu);
        return true;
    }

    @Override
    public void onDestroyActionMode(ActionMode mode)
    {
        mMultiSelector.clearAll();
    }

    @Override
    public boolean onPrepareActionMode(ActionMode mode, Menu menu)
    {
        // TODO Auto-generated method stub
        mode.setTitle("CheckBox is Checked");
        return false;
    }
}
