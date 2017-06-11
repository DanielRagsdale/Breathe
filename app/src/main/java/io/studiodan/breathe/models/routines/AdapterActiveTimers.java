package io.studiodan.breathe.models.routines;

import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Adapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.List;

import io.studiodan.breathe.R;

public class AdapterActiveTimers extends RecyclerView.Adapter<AdapterActiveTimers.ViewHolder>
{
    List<ITimer> mTimers;

    public class ViewHolder extends RecyclerView.ViewHolder
    {
        ImageView mImageView;
        TextView mTextView;

        public ViewHolder(ViewGroup itemView)
        {
            super(itemView);

            mImageView = (ImageView) itemView.findViewById(R.id.iv_timer_icon);
            mTextView = (TextView) itemView.findViewById(R.id.tv_timer_text);
        }

        void bind(ITimer timer)
        {
            mImageView.setImageResource(timer.getTimerType().mImageResource);
            mTextView.setText(timer.description());
        }
    }

    public AdapterActiveTimers(List<ITimer> timers)
    {
        mTimers = timers;
    }

    @Override
    public AdapterActiveTimers.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType)
    {
        // create a new view
        ViewGroup v = (ViewGroup) LayoutInflater.from(parent.getContext()).inflate(R.layout.item_routine_timer_added, parent, false);
        // set the view's size, margins, paddings and layout parameters
        ViewHolder vh = new ViewHolder(v);

        return vh;
    }

    @Override
    public void onBindViewHolder(AdapterActiveTimers.ViewHolder holder, int position)
    {
        holder.bind(mTimers.get(position));
    }

    @Override
    public int getItemCount()
    {
        return mTimers.size();
    }
}
