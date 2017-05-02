package io.studiodan.breathe.util.multiselector;

import io.studiodan.breathe.R;
import io.studiodan.breathe.models.AdapterToDo;
import io.studiodan.breathe.models.ToDoItem;

/**
 * Created by dan on 5/2/17.
 */

public class ActionCheckItemMulti extends ActionMultiSelector<ToDoItem>
{
    AdapterToDo mAdapterTD;

    public ActionCheckItemMulti(AdapterToDo adapterTD)
    {
        mAdapterTD = adapterTD;
    }

    @Override
    public void action(int actionID, Object obj)
    {
        if(actionID == R.id.action_delete)
        {
            mAdapterTD.mDataset.removeFromChildren((ToDoItem) obj);
            mSelector.unselectItem((ToDoItem) obj);
        }

        mAdapterTD.notifyDataSetChanged();
    }

    @Override
    public void clearOnce()
    {
        mAdapterTD.notifyDataSetChanged();
    }

    @Override
    public boolean allowUndo(int actionID)
    {
        return false;
    }
}
