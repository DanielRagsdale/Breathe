package io.studiodan.breathe.util.multiselector;

import android.app.Activity;
import android.view.ActionMode;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class MultiSelector<T>
{
    MultiSelector[] mExclusiveItems;

    HashMap<T, Boolean> mSelectionItems = new HashMap<>();

    int mCount;
    int mCheckedCount;
    boolean mCABDisplayed = false;
    int mCABMenuID;
    String mCABMenuTitle;

    ActionMode mActionMode;

    Activity mParentActivity;
    ActionMultiSelector mActionObj;

    public MultiSelector(Activity activity, ActionMultiSelector actionObj, int CABMenuID, String menuTitle)
    {
        mParentActivity = activity;
        mActionObj = actionObj;
        mCABMenuID = CABMenuID;
        mCABMenuTitle = menuTitle;
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
    public boolean getSelectState(T item)
    {
        return mSelectionItems.get(item);
    }

    public boolean setSelectState(T item, boolean state)
    {
        if(state == true)
        {
            return selectItem(item);
        }
        else
        {
            return unselectItem(item);
        }
    }

    /**
     * Select an item
     *
     * @param item
     * @return was the item successfully selected?
     */
    public boolean selectItem(T item)
    {
        if(isFree())
        {

            mCheckedCount++;

            displayCAB();
            mSelectionItems.put(item, true);
            return true;
        }

        return false;
    }

    /**
     * Unselect an item
     *
     * @param item
     * @return was the item successfully unselected?
     */
    public boolean unselectItem(T item)
    {
        if(isFree())
        {
            mCheckedCount--;
            mSelectionItems.put(item, false);

            //Log.d("Breathe", "count " + mCheckedCount);

            if (mCheckedCount <= 0)
            {
                mCheckedCount = 0;

                mActionMode.finish();
                mCABDisplayed = false;
            }

            return true;
        }

        return false;
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
        List<T> actedObjects = new ArrayList<T>();

        for(T i : mSelectionItems.keySet())
        {
            if(mSelectionItems.get(i) == true && i != null)
            {
                mActionObj.action(actionID, i);
                actedObjects.add(i);
            }
        }

        if(mActionObj.allowUndo(actionID))
        {
            for(T i : actedObjects)
            {
                mActionObj.undo(actionID, i);
            }
        }
    }

    public void clearAll()
    {
        mCheckedCount = 0;
        mCABDisplayed = false;

        mActionObj.clearOnce();
        for(T i : mSelectionItems.keySet())
        {
            unselectItem(i);
            mActionObj.clear(i);
        }
    }

    private void displayCAB()
    {
        if(!mCABDisplayed)
        {
            //TODO Generalize CAB display
            mActionMode = mParentActivity.startActionMode(new CABGeneric(this, mCABMenuID, mCABMenuTitle));
            mCABDisplayed = true;
        }
    }

    private boolean isFree()
    {
        if(mExclusiveItems == null || mExclusiveItems.length == 0)
        {
            return true;
        }

        for(MultiSelector i : mExclusiveItems)
        {
            if(i != this && i.getCheckCount() > 0)
            {
                return false;
            }
        }
        return true;
    }

    /**
     * Create link between MultiSelectors such that only one can be in use at a time
     *
     * @param selectors
     */
    public static void createExclusivity(MultiSelector ... selectors)
    {
        for(MultiSelector i : selectors)
        {
            i.mExclusiveItems = selectors;
        }
    }
}
