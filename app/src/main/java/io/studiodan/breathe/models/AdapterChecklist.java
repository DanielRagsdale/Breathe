package io.studiodan.breathe.models;

import android.database.DataSetObserver;
import android.graphics.Rect;
import android.support.design.widget.Snackbar;
import android.util.TypedValue;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ListAdapter;
import android.widget.ListView;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.SortedSet;

/**
 * Adapter representing checklist of tasks within a ToDoList
 */
public class AdapterChecklist implements ListAdapter
{
    private ListView mParentList;

    private List<ToDoItem> mCheckItems;
    private List<DataSetObserver> mObservers = new ArrayList<DataSetObserver>();

    int count;

    /**
     * Create AdapterChecklist for pList with items
     *
     * @param items Items contained in checklist
     * @param pList Listview containing the items
     */
    public AdapterChecklist(SortedSet<ToDoItem> items, ListView pList)
    {
        mParentList = pList;

        mCheckItems = Arrays.asList(Arrays.copyOf(items.toArray(), items.size(), ToDoItem[].class));
        count = mCheckItems.size();

        Collections.sort(mCheckItems);
        updateCount();
    }

    //TODO
    @Override
    public View getView(final int position, View convertView, final ViewGroup parent)
    {
        CheckBox checkItem = new CheckBox(parent.getContext());

        ToDoItem item = mCheckItems.get(position);

        checkItem.setTextSize(TypedValue.COMPLEX_UNIT_SP, 16.0f);
        checkItem.setChecked(item.isChecked);


        final String tdlName = item.title;
        String dueDateString = "";

        Rect bounds = new Rect();
        checkItem.getPaint().getTextBounds(tdlName, 0, tdlName.length(), bounds);

        int multiplier = Math.max(0, (750 - bounds.width()) / 10);
        String padding = new String(new char[multiplier]).replace("\0", " ");

        if(item.dueDay != Integer.MAX_VALUE && item.dueMonth != Integer.MAX_VALUE && item.dueYear != Integer.MAX_VALUE)
        {
            dueDateString = (item.dueMonth + 1) + "/" + item.dueDay + "/" + item.dueYear;
        }

        checkItem.setText(tdlName + padding + dueDateString);

        checkItem.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener()
        {
            @Override
            public void onCheckedChanged(final CompoundButton buttonView, boolean isChecked)
            {
                if(isChecked)
                {
//                    Toast t = Toast.makeText(parent.getContext(), "Congrats on getting work done!", Toast.LENGTH_SHORT);
//                    t.show();
                    String itemText = tdlName;
                    String message;

                    if(itemText.length() > 25)
                    {
                        message = itemText.substring(0, 22) + "... hidden";
                    }
                    else
                    {
                        message = itemText + " hidden";
                    }

                    final int newPos = updateCheckState(position, true);

                    Snackbar s = Snackbar
                            .make(parent, message, Snackbar.LENGTH_LONG)
                            .setAction("UNDO", new View.OnClickListener()
                            {
                                @Override
                                public void onClick(View v)
                                {
                                    buttonView.setChecked(false);
                                    updateCheckState(newPos, false);
                                }
                            });

                    s.show();
                }
                else
                {
                    updateCheckState(position, false);
                }
            }
        });

        return checkItem;
    }

    /**
     * Changes whether one of the Check Items is checked.
     * Will then sort the list again
     *
     * @param position position of item to be updated
     * @param checkState state of item after it is updated
     * @return position of item after list is checked and then sorted
     */
    public int updateCheckState(int position, boolean checkState)
    {
        ToDoItem item = mCheckItems.get(position);
        item.setCheckedState(checkState);

        Collections.sort(mCheckItems);

        updateCount();
        setHeightBasedOnChildren();

        callObservers();
        return mCheckItems.indexOf(item);
    }

    /**
     * Update the count of unchecked items
     */
    protected void updateCount()
    {
        int i;

        for(i = 0; i < mCheckItems.size(); i++)
        {
            if(mCheckItems.get(i).isChecked)
            {
                break;
            }
        }

        count = i;
    }

    @Override
    public int getItemViewType(int position) {
        return 0;
    }

    @Override
    public boolean areAllItemsEnabled()
    {
        return true;
    }

    @Override
    public boolean isEnabled(int pos)
    {
        return pos < mCheckItems.size();
    }


    //region trivial methods
    //MVP
    @Override
    public void registerDataSetObserver(DataSetObserver observer) {
        mObservers.add(observer);
    }

    //MVP
    @Override
    public void unregisterDataSetObserver(DataSetObserver observer) {
        mObservers.remove(observer);
    }

    //MVP
    @Override
    public int getCount() {
        return count;
    }

    //MVP
    @Override
    public Object getItem(int position) {
        return mCheckItems.get(position);
    }

    //MVP
    @Override
    public long getItemId(int position) {
        return position;
    }

    //MVP
    @Override
    public boolean hasStableIds() {
        return false;
    }
    //MVP
    @Override
    public int getViewTypeCount() {
        return 1;
    }

    //MVP
    @Override
    public boolean isEmpty() {
        return mCheckItems.size() == 0;
    }
    //endregion

    private void callObservers()
    {
        for (DataSetObserver observer : mObservers)
        {
            observer.onChanged();
        }
    }

    public void setHeightBasedOnChildren()
    {
        int totalHeight = 0;

        heightLoop:
        for (int i = 0; i < getCount(); i++)
        {
            View listItem = getView(i, null, mParentList);
            listItem.measure(0, 0);
            totalHeight += listItem.getMeasuredHeight();
        }

        ViewGroup.LayoutParams params = mParentList.getLayoutParams();
        params.height = totalHeight + (mParentList.getDividerHeight() * (getCount() - 1));
        mParentList.setLayoutParams(params);
        mParentList.requestLayout();
    }
}
