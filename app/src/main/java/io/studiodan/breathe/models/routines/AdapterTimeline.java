package io.studiodan.breathe.models.routines;

import android.app.Activity;
import android.support.v4.app.Fragment;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.ViewGroup;
import android.widget.Adapter;
import android.widget.TextView;

import java.util.Collections;
import java.util.List;

import io.studiodan.breathe.R;

public class AdapterTimeline extends RecyclerView.Adapter<AdapterTimeline.ViewHolder>
{
    List<RoutineInstance> mDataset;
    Activity mParentAct;

    // Provide a reference to the views for each data item
    // Complex data items may need more than one view per item, and
    // you provide access to all the views for a data item in a view holder
    public class ViewHolder extends RecyclerView.ViewHolder
    {
        RoutineInstance mInstance;

        public CardView mCardView;
        public TextView mTimeView;

        public ViewHolder(ViewGroup v)
        {
            super(v);

            mCardView = (CardView) v.findViewById(R.id.cv_routine_instance);
            mTimeView = (TextView) v.findViewById(R.id.tv_time);
        }

        public void setRoutineRepresentation(RoutineInstance instance)
        {
            mInstance = instance;
            mTimeView.setText(mInstance.getTimePeriod().getStartTimeString());

            mCardView.removeAllViews();
            mCardView.addView(instance.createTimelineView(LayoutInflater.from(mParentAct), mCardView));
        }
    }

    /**
     * Create a new AdapterTimeline.
     * The parameter "data" is sorted during the constructor
     *
     * @param data the list of RoutineInstances
     * @param parentAct The activity that holds the RecyclerView associated with this adapter
     */
    public AdapterTimeline(List<RoutineInstance> data, Activity parentAct)
    {
        Collections.sort(data, new CompartorRoutineInstance());

        mDataset = data;
        mParentAct = parentAct;
    }

    /**
     * Create new views (invoked by the layout manager)
     *
     * @param parent
     * @param viewType
     * @return
     */
    @Override
    public AdapterTimeline.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType)
    {
        // create a new view
        ViewGroup v = (ViewGroup) LayoutInflater.from(parent.getContext()).inflate(R.layout.item_routine_instance, parent, false);

        ViewHolder vh = new ViewHolder(v);

        return vh;
    }

    /**
     * Replace the contents of a view (invoked by the layout manager)
     *
     * @param holder The ViewHolder to be set
     * @param position The position of the ViewHolder
     */
    @Override
    public void onBindViewHolder(ViewHolder holder, int position)
    {
        RoutineInstance instance = mDataset.get(position);

        // - get element from your dataset at this position
        // - replace the contents of the view with that element
        holder.setRoutineRepresentation(instance);
    }

    /**
     * Return the size of the dataset (invoked by the layout manager)
     *
     * @return the number of items
     */
    @Override
    public int getItemCount()
    {
        return mDataset.size();
    }
}







