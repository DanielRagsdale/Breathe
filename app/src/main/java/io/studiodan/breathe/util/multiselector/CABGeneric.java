package io.studiodan.breathe.util.multiselector;

import android.util.Log;
import android.view.ActionMode;
import android.view.Menu;
import android.view.MenuItem;

import io.studiodan.breathe.R;
import io.studiodan.breathe.util.multiselector.CABGeneric;
import io.studiodan.breathe.util.multiselector.MultiSelector;

public class CABGeneric implements ActionMode.Callback
{
    MultiSelector mMultiSelector;
    int mMenuID;
    String mTitle;

    public CABGeneric(MultiSelector selector, int menuID, String title)
    {
        mMultiSelector = selector;
        mMenuID = menuID;
        mTitle = title;
    }

    @Override
    public boolean onActionItemClicked(ActionMode mode, MenuItem item)
    {
        mMultiSelector.launchAction(item.getItemId());
        return false;
    }

    @Override
    public boolean onCreateActionMode(ActionMode mode, Menu menu)
    {
        mode.getMenuInflater().inflate(mMenuID, menu);
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
        mode.setTitle(mTitle);
        return false;
    }
}
