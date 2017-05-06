package io.studiodan.breathe.models;

import android.app.Activity;
import android.app.DialogFragment;
import android.database.DataSetObserver;
import android.graphics.Color;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v4.app.FragmentActivity;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.SortedSet;

import io.studiodan.breathe.ActivityInspectList;
import io.studiodan.breathe.R;
import io.studiodan.breathe.util.multiselector.MultiSelector;

/**
 * Adapter representing checklist of tasks within a ToDoList
 */
public class AdapterChecklist implements ListAdapter
{
    private FragmentActivity mParentAct;

    private ListView mParentList;
    private ToDoList mToDoList;

    private List<ToDoItem> mCheckItems;
    private List<DataSetObserver> mObservers = new ArrayList<DataSetObserver>();

    private int count;

    private boolean mDisplayChecked;
    private MultiSelector<ToDoItem> mMultiSelector;

    /**
     * Create AdapterChecklist for pList with items
     *
     * @param list Items contained in checklist
     * @param pList Listview containing the items
     * @param dispChecked should checked items be displayed
     * @param multiSelector multiSelector used by this checklist
     */
    public AdapterChecklist(FragmentActivity parentAct, ToDoList list, ListView pList, boolean dispChecked, MultiSelector<ToDoItem> multiSelector)
    {
        this(parentAct, list, pList, multiSelector);

        mDisplayChecked = dispChecked;
    }

    /**
     * Create AdapterChecklist for pList with items
     *
     * @param list Items contained in checklist
     * @param pList Listview containing the items
     * @param multiSelector multiSelector used by this checklist
     */
    public AdapterChecklist(FragmentActivity parentAct, ToDoList list, ListView pList, MultiSelector<ToDoItem> multiSelector)
    {
        mParentAct = parentAct;

        mParentList = pList;
        mToDoList = list;
        mMultiSelector = multiSelector;

        buildItems();
    }

    private void buildItems()
    {
        SortedSet<ToDoItem> items = mToDoList.getItems();

        mCheckItems = Arrays.asList(Arrays.copyOf(items.toArray(), items.size(), ToDoItem[].class));
        count = mCheckItems.size();

        Collections.sort(mCheckItems);
        updateCount();
    }

    public void updateList()
    {
        buildItems();
        setHeightBasedOnChildren();
        callObservers();
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

        bodyText.setText(item.title);
        dateText.setText(item.getDueDateString());

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

        v.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                DialogFragment newList = InspectFragment.newInstance(1, item);
                newList.show(mParentAct.getFragmentManager(), "test");
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

    public void callObservers()
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

    //region trivial methods
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


    public static class InspectFragment extends DialogFragment
    {
        int mNum;
        String mTitle;
        String mDueDate;
        String mDescription;

        static InspectFragment newInstance(int num, ToDoItem task)
        {
            InspectFragment f = new InspectFragment();

            // Supply num input as an argument.

            Bundle args = new Bundle();
            args.putInt("num", num);
            args.putString("title", task.title);
            args.putString("date", task.getDueDateString());
            args.putString("desc", task.description);
            f.setArguments(args);

            return f;
        }

        @Override
        public void onCreate(Bundle savedInstanceState)
        {
            super.onCreate(savedInstanceState);
            Bundle args = getArguments();

            mNum = args.getInt("num");
            mTitle = args.getString("title");
            mDescription = args.getString("desc");
            mDueDate = args.getString("date");

            // Pick a style based on the num.
            int style = DialogFragment.STYLE_NORMAL, theme = 0;
            switch ((mNum-1)%6)
            {
                case 1: style = DialogFragment.STYLE_NO_TITLE; break;
                case 2: style = DialogFragment.STYLE_NO_FRAME; break;
                case 3: style = DialogFragment.STYLE_NO_INPUT; break;
                case 4: style = DialogFragment.STYLE_NORMAL; break;
                case 5: style = DialogFragment.STYLE_NORMAL; break;
                case 6: style = DialogFragment.STYLE_NO_TITLE; break;
                case 7: style = DialogFragment.STYLE_NO_FRAME; break;
                case 8: style = DialogFragment.STYLE_NORMAL; break;
            }
            switch ((mNum-1)%6)
            {
                case 4: theme = android.R.style.Theme_Holo; break;
                case 5: theme = android.R.style.Theme_Holo_Light_Dialog; break;
                case 6: theme = android.R.style.Theme_Holo_Light; break;
                case 7: theme = android.R.style.Theme_Holo_Light_Panel; break;
                case 8: theme = android.R.style.Theme_Holo_Light; break;
            }
            setStyle(style, theme);
        }

        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
        {
            View v = inflater.inflate(R.layout.dialog_inspect_task, container, false);

            TextView title = (TextView) v.findViewById(R.id.tv_title);
            title.setText(mTitle);

            TextView description = (TextView) v.findViewById(R.id.tv_task_desc);
            description.setText(mDescription);

            TextView dueDate = (TextView) v.findViewById(R.id.tv_date);
            dueDate.setText(mDueDate);

            return v;
        }
    }

}

















