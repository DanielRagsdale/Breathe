package io.studiodan.breathe.models;

import android.database.DataSetObserver;
import android.graphics.Color;
import android.graphics.Rect;
import android.support.design.widget.Snackbar;
import android.util.Log;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import org.w3c.dom.Text;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.SortedSet;

import io.studiodan.breathe.R;
import io.studiodan.breathe.util.multiselector.MultiSelector;

import static android.R.id.message;

/**
 * Adapter representing checklist of tasks within a ToDoList
 */
public class AdapterChecklist implements ListAdapter
{
    private ListView mParentList;

    private List<ToDoItem> mCheckItems;
    private List<DataSetObserver> mObservers = new ArrayList<DataSetObserver>();

    private int count;

    private boolean mDisplayChecked;
    private MultiSelector<ToDoItem> mMultiSelector;

    /**
     * Create AdapterChecklist for pList with items
     *
     * @param items Items contained in checklist
     * @param pList Listview containing the items
     * @param dispChecked should checked items be displayed
     * @param multiSelector multiSelector used by this checklist
     */
    public AdapterChecklist(SortedSet<ToDoItem> items, ListView pList, boolean dispChecked, MultiSelector<ToDoItem> multiSelector)
    {
        this(items, pList, multiSelector);

        mDisplayChecked = dispChecked;
    }

    /**
     * Create AdapterChecklist for pList with items
     *
     * @param items Items contained in checklist
     * @param pList Listview containing the items
     * @param multiSelector multiSelector used by this checklist
     */
    public AdapterChecklist(SortedSet<ToDoItem> items, ListView pList, MultiSelector<ToDoItem> multiSelector)
    {
        mParentList = pList;
        mMultiSelector = multiSelector;

        mCheckItems = Arrays.asList(Arrays.copyOf(items.toArray(), items.size(), ToDoItem[].class));
        count = mCheckItems.size();

        Collections.sort(mCheckItems);
        updateCount();
    }

    //TODO make more organized
    @Override
    public View getView(final int position, View convertView, final ViewGroup parent)
    {
        RelativeLayout v = (RelativeLayout) LayoutInflater.from(parent.getContext()).inflate(R.layout.item_check, parent, false);

        CheckBox checkItem = (CheckBox) v.findViewById(R.id.cb_item_check);
        TextView bodyText = (TextView) v.findViewById(R.id.tv_body_check);
        TextView dateText = (TextView) v.findViewById(R.id.tv_date_check);

        final ToDoItem item = mCheckItems.get(position);

        checkItem.setChecked(item.isChecked);

        String dueDateString = "";
        if(item.dueDay != Integer.MAX_VALUE && item.dueMonth != Integer.MAX_VALUE && item.dueYear != Integer.MAX_VALUE)
        {
            dueDateString = (item.dueMonth + 1) + "/" + item.dueDay + "/" + item.dueYear;
        }

        bodyText.setText(item.title);
        dateText.setText(dueDateString);

        checkItem.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener()
        {
            @Override
            public void onCheckedChanged(final CompoundButton buttonView, boolean isChecked)
            {
                if(isChecked)
                {
                    String itemText = item.title;
                    String message;

                    String occurrence;
                    if(mDisplayChecked)
                    {
                        occurrence = " checked";
                    }
                    else
                    {
                        occurrence = " hidden";
                    }

                    if(itemText.length() > 25)
                    {
                        message = itemText.substring(0, 22) + "..." + occurrence;
                    }
                    else
                    {
                        message = itemText + occurrence;
                    }

                    final int newPos = updateCheckState(position, true);

                    Snackbar s = Snackbar.make(parent, message, Snackbar.LENGTH_LONG).setAction("UNDO",
                            new View.OnClickListener()
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

        mMultiSelector.registerItem(item);

        v.setOnLongClickListener(new View.OnLongClickListener()
        {
            @Override
            public boolean onLongClick(View v)
            {
                Log.d("Breathe", "Long Click Registered");

                boolean toState = !mMultiSelector.getSelectState(item);

                if(mMultiSelector.setSelectState(item, toState))
                {
                    if (toState)
                    {
                        v.setBackgroundColor(Color.parseColor("#F8BBD0"));
                    }
                    else
                    {
                        v.setBackgroundColor(Color.parseColor("#FFFFFFFF"));
                    }
                    return true;
                }

                return false;
            }
        });

        return v;
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
    public int getCount()
    {
        if(mDisplayChecked)
        {
            return mCheckItems.size();
        }

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
