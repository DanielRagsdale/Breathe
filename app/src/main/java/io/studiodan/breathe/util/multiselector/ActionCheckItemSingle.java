package io.studiodan.breathe.util.multiselector;

import io.studiodan.breathe.R;
import io.studiodan.breathe.models.AdapterChecklist;
import io.studiodan.breathe.models.AdapterToDo;
import io.studiodan.breathe.models.ToDoItem;
import io.studiodan.breathe.models.ToDoList;

/**
 * Created by dan on 5/2/17.
 */

public class ActionCheckItemSingle extends ActionMultiSelector<ToDoItem>
{
    ToDoList mList;
    AdapterChecklist mAdapterCL;

    public ActionCheckItemSingle(ToDoList list)
    {
        mList = list;
    }

    public void setAdapter(AdapterChecklist adapterCL)
    {
        mAdapterCL = adapterCL;
    }

    @Override
    public void action(int actionID, Object obj)
    {
        if(actionID == R.id.action_delete)
        {

            mList.removeItem((ToDoItem) obj);
            mSelector.unselectItem((ToDoItem) obj);
        }

        mAdapterCL.updateList();
    }

    @Override
    public void clearOnce()
    {
        mAdapterCL.updateList();
    }

    @Override
    public boolean allowUndo(int actionID)
    {
        return false;
    }
}
