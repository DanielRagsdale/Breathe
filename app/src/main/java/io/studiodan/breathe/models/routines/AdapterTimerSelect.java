package io.studiodan.breathe.models.routines;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.ViewGroup;
import android.widget.ImageView;

import io.studiodan.breathe.R;

public class AdapterTimerSelect extends RecyclerView.Adapter<AdapterTimerSelect.ViewHolder>
{
    public class ViewHolder extends RecyclerView.ViewHolder
    {
        ImageView mImageView;

        int mPosition;
        ETimers mCurrentTimer;

        public ViewHolder(ImageView v)
        {
            super(v);
            mImageView = v;
        }

        public void setRepresentation(int pos)
        {
            mPosition = pos;
            mCurrentTimer = ETimers.values()[pos];

            mImageView.setImageResource(mCurrentTimer.mImageResource);
            mImageView.setId(mCurrentTimer.mID);
        }
    }

    public AdapterTimerSelect()
    {

    }

    @Override
    public AdapterTimerSelect.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType)
    {
        ImageView iv = (ImageView) LayoutInflater.from(parent.getContext()).inflate(R.layout.item_routine_timer, parent, false);

        return new AdapterTimerSelect.ViewHolder(iv);
    }

    @Override
    public void onBindViewHolder(AdapterTimerSelect.ViewHolder holder, int position)
    {
        holder.setRepresentation(position);
    }

    @Override
    public int getItemCount()
    {
        return 2;
    }
}
