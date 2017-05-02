package io.studiodan.breathe.models;

import android.content.Intent;
import android.graphics.Color;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.TextView;

import io.studiodan.breathe.ActivityInspectList;
import io.studiodan.breathe.R;
import io.studiodan.breathe.fragments.FragmentLifeList;
import io.studiodan.breathe.util.multiselector.ActionToDo;
import io.studiodan.breathe.util.multiselector.MultiSelector;
import io.studiodan.breathe.util.multiselector.MultiSelectorAction;

public class AdapterToDo extends RecyclerView.Adapter<AdapterToDo.ViewHolder>
{
    public ToDoList mDataset;
    private FragmentLifeList mParentFrag;

    MultiSelector<ToDoList> mListSelector;
    MultiSelector<ToDoItem> mItemSelector;

    // Provide a reference to the views for each data item
    // Complex data items may need more than one view per item, and
    // you provide access to all the views for a data item in a view holder
    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnLongClickListener, View.OnClickListener
    {
        ToDoList mList;

        // each data item is just a string in this case
        public CardView mCardView;
        public TextView mTitleTextView;
        public ListView mBodyListView;
        public ViewHolder(CardView v)
        {
            super(v);
            mCardView = v;
            mTitleTextView = (TextView) mCardView.findViewById(R.id.tv_todo_title);
            mBodyListView = (ListView) mCardView.findViewById(R.id.lv_todo_list);

            v.setLongClickable(true);
            v.setOnLongClickListener(this);
            v.setOnClickListener(this);
        }

        public void setListRepresentation(ToDoList list)
        {
            if(list == null)
            {
                mCardView.setVisibility(View.INVISIBLE);
                mCardView.setMinimumHeight(mCardView.getHeight() + 130);
                return;
            }

            mCardView.setMinimumHeight(0);

            mCardView.setVisibility(View.VISIBLE);

            mList = list;
            mListSelector.registerItem(mList);

            mTitleTextView.setText(mList.fullName);
            mBodyListView.setAdapter(new AdapterChecklist(mList.getItems(), mBodyListView, mItemSelector));
            ((AdapterChecklist) mBodyListView.getAdapter()).setHeightBasedOnChildren();

            setVisualClickState(mListSelector.getSelectState(mList));
        }

        @Override
        public void onClick(View v)
        {
            int pos = mDataset.getPositionOfList(mList);

            Intent intent = new Intent(mParentFrag.getActivity(), ActivityInspectList.class);
            intent.putExtra(mParentFrag.getString(R.string.single_ToDo_id), pos);
            mParentFrag.getActivity().startActivity(intent);
        }

        @Override
        public boolean onLongClick(View v)
        {
            boolean toState = !mListSelector.getSelectState(mList);

            if(mListSelector.setSelectState(mList, toState))
            {
                setVisualClickState(toState);
            }

            return true;
        }

        private void setVisualClickState(boolean state)
        {
            if(state)
            {
                mCardView.setCardBackgroundColor(Color.parseColor("#F8BBD0"));
            }
            else
            {
                mCardView.setCardBackgroundColor(Color.parseColor("#FFFFFFFF"));
            }
        }
    }

    public class ActionCheckItem extends MultiSelectorAction
    {
        @Override
        public void action(int actionID, Object obj)
        {

        }

        @Override
        public void clear(Object obj)
        {
            Log.d("Breathe", "Clear called");
            notifyDataSetChanged();
        }

        @Override
        public boolean allowUndo(int actionID)
        {
            return false;
        }
    }

    // Provide a suitable constructor (depends on the kind of dataset)
    public AdapterToDo(ToDoList lists, FragmentLifeList parentFrag)
    {
        mDataset = lists;
        mParentFrag = parentFrag;

        mListSelector = new MultiSelector<>(mParentFrag.getActivity(), new ActionToDo(mListSelector, this), R.menu.menu_edit_todo_item);
        mItemSelector = new MultiSelector<>(mParentFrag.getActivity(), new ActionCheckItem(), R.menu.menu_edit_todo_item);

        MultiSelector.createExclusivity(mListSelector, mItemSelector);
    }

    /**
     * If a single list is selected, return the id of that list.
     * Otherwise, return -1.
     *
     * @return id of selected list
     */
    public int getSingleSelected()
    {
        if(mListSelector.getCheckCount() == 1)
        {
            for (int i = 0; i < mDataset.getTotalListCount(); i++)
            {
                if (mListSelector.getSelectState(mDataset.getListAtPos(i)))
                {
                    return i;
                }
            }
        }

        return -1;
    }

    // Create new views (invoked by the layout manager)
    @Override
    public AdapterToDo.ViewHolder onCreateViewHolder(final ViewGroup parent, int viewType)
    {
        // create a new view
        CardView v = (CardView) LayoutInflater.from(parent.getContext()).inflate(R.layout.item_to_do_list, parent, false);

        // set the view's size, margins, paddings and layout parameters
        ViewHolder vh = new ViewHolder(v);

        return vh;
    }

    // Replace the contents of a view (invoked by the layout manager)
    @Override
    public void onBindViewHolder(ViewHolder holder, int position)
    {
        if(position == getItemCount() - 1)
        {
            holder.setListRepresentation(null);
            return;
        }

        ToDoList nList = mDataset.getListAtPos(position);

        // - get element from your dataset at this position
        // - replace the contents of the view with that element
        holder.setListRepresentation(nList);
    }

    // Return the size of your dataset (invoked by the layout manager)
    @Override
    public int getItemCount()
    {
        return mDataset.getTotalListCount() + 1;
    }
}
