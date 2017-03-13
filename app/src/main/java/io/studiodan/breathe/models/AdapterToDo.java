package io.studiodan.breathe.models;

import android.graphics.Color;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.TextView;

import io.studiodan.breathe.R;
import io.studiodan.breathe.fragments.FragmentToDo;
import io.studiodan.breathe.util.multiselector.MultiSelector;
import io.studiodan.breathe.util.multiselector.IMultiSelectorAction;

public class AdapterToDo extends RecyclerView.Adapter<AdapterToDo.ViewHolder>
{
    private ToDoList mDataset;
    private FragmentToDo mParentFrag;

    MultiSelector<ToDoList> mSelector;

    // Provide a reference to the views for each data item
    // Complex data items may need more than one view per item, and
    // you provide access to all the views for a data item in a view holder
    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnLongClickListener
    {
        boolean mClickState;
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
        }

        public void setListRepresentation(ToDoList list)
        {
            mList = list;
            mSelector.registerItem(mList);

            mTitleTextView.setText(mList.fullName);
            mBodyListView.setAdapter(new AdapterChecklist(mList.getItems(), mBodyListView));
            ((AdapterChecklist) mBodyListView.getAdapter()).setHeightBasedOnChildren();

            mClickState = mSelector.getCheckState(mList);
            setVisualClickState(mClickState);
        }

        @Override
        public boolean onLongClick(View v)
        {
            mClickState = !mClickState;
            setVisualClickState(mClickState);

            if(mClickState)
            {
                mSelector.checkItem(mList);
            }
            else
            {
                mSelector.uncheckItem(mList);
            }

            return false;
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

    public class ViewHolderAction implements IMultiSelectorAction
    {
        @Override
        public void action(int actionID, Object obj)
        {
            Log.d("Breathe", "Action called");
            if(actionID == R.id.action_delete)
            {
                Log.d("Breathe", "Action delete called");
                mDataset.removeFromChildren((ToDoList) obj);
                notifyDataSetChanged();
            }
        }

        @Override
        public void clear(Object obj)
        {
            //mClickState = false;
            //setVisualClickState(mClickState);

            mSelector.uncheckItem((ToDoList) obj);
            notifyDataSetChanged();
        }
    }

    // Provide a suitable constructor (depends on the kind of dataset)
    public AdapterToDo(ToDoList lists, FragmentToDo parentFrag)
    {
        mDataset = lists;
        mParentFrag = parentFrag;

        mSelector = new MultiSelector<>(mParentFrag.getActivity(), new ViewHolderAction());
    }

    // Create new views (invoked by the layout manager)
    @Override
    public AdapterToDo.ViewHolder onCreateViewHolder(final ViewGroup parent, int viewType)
    {
        // create a new view
        CardView v = (CardView) LayoutInflater.from(parent.getContext()).inflate(R.layout.to_do_item, parent, false);

        // set the view's size, margins, paddings and layout parameters
        ViewHolder vh = new ViewHolder(v);

        return vh;
    }

    // Replace the contents of a view (invoked by the layout manager)
    @Override
    public void onBindViewHolder(ViewHolder holder, int position)
    {
        ToDoList nList = mDataset.getListAtPos(position);

        // - get element from your dataset at this position
        // - replace the contents of the view with that element
        holder.setListRepresentation(nList);
    }

    // Return the size of your dataset (invoked by the layout manager)
    @Override
    public int getItemCount()
    {
        return mDataset.getTotalListCount();
    }
}
