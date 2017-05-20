package io.studiodan.breathe.util.multiselector;

import io.studiodan.breathe.R;
import io.studiodan.breathe.models.checklists.AdapterToDo;
import io.studiodan.breathe.models.checklists.ToDoList;

/**
 * Created by dan on 5/1/17.
 */

public class ActionToDo extends ActionMultiSelector<ToDoList>
{
    private AdapterToDo mAdapter;

    public ActionToDo(AdapterToDo adapterToDo)
    {
        mAdapter = adapterToDo;
    }

    @Override
    public void action(int actionID, Object obj)
    {
        //Log.d("Breathe", "Action called");
        if(actionID == R.id.action_delete)
        {
            //Log.d("Breathe", "Action delete called");
            mAdapter.mDataset.removeFromChildren((ToDoList) obj);
            mSelector.unselectItem((ToDoList) obj);
        }

        mAdapter.notifyDataSetChanged();
    }

    @Override
    public void clearOnce()
    {
        //mClickState = false;
        //setVisualClickState(mClickState);
        mAdapter.notifyDataSetChanged();
    }

    @Override
    public boolean allowUndo(int actionID)
    {
        if(actionID == R.id.action_delete)
        {
            return true;
        }
        return false;
    }

    @Override
    public void undo(int actionID, Object obj)
    {
    }
}

