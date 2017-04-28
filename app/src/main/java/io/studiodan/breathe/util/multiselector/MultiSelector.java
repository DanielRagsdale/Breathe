package io.studiodan.breathe.util.multiselector;

import android.app.Activity;
import android.util.Log;
import android.view.ActionMode;

import java.util.HashMap;

import io.studiodan.breathe.models.CABNull;
import io.studiodan.breathe.models.CABTodo;

public class MultiSelector<T>
{
    HashMap<T, Boolean> mSelectionItems = new HashMap<>();

    int mCount;
    int mCheckedCount;
    boolean mCABDisplayed = false;

    ActionMode mActionMode;

    Activity mParentActivity;
    IMultiSelectorAction mActionObj;

    public MultiSelector(Activity activity, IMultiSelectorAction actionObj)
    {
        mParentActivity = activity;
        mActionObj = actionObj;
    }

    /**
     * Register the backing object of a ViewHolder
     *
     * @param item
     */
    public void registerItem(T item)
    {
        if(!mSelectionItems.keySet().contains(item))
        {
            mSelectionItems.put(item, false);
            mCount++;
        }
    }

    /**
     * Get whether the view represented by given backing object has been selected
     *
     * @param item the backing object of desired view
     * @return the selection status
     */
    public boolean getCheckState(T item)
    {
        return mSelectionItems.get(item);
    }

    public void checkItem(T item)
    {
        mCheckedCount++;

        displayCAB();
        mSelectionItems.put(item, true);
    }

    public void uncheckItem(T item)
    {
        mCheckedCount--;
        mSelectionItems.put(item, false);

        //Log.d("Breathe", "count " + mCheckedCount);

        if(mCheckedCount <= 0)
        {
            mCheckedCount = 0;

            mActionMode.finish();
            mCABDisplayed = false;
        }
    }

    /**
     * Get the number of items that are currently checked
     *
     * @return
     */
    public int getCheckCount()
    {
        return mCheckedCount;
    }

    public void launchAction(int actionID)
    {
        for(T i : mSelectionItems.keySet())
        {
            if(mSelectionItems.get(i) == true)
            {

                mActionObj.action(actionID, i);
            }
        }
    }

    public void clearAll()
    {
        mCheckedCount = 0;
        mCABDisplayed = false;
        for(T i : mSelectionItems.keySet())
        {
            mActionObj.clear(i);
        }
    }

    private void displayCAB()
    {
        if(!mCABDisplayed)
        {
            //TODO Generalize CAB display
            mActionMode = mParentActivity.startActionMode(new CABTodo(this));
            mCABDisplayed = true;
        }
    }

}
