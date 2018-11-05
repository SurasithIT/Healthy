package com.example.surasith.healthy.sleep;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.surasith.healthy.R;

import java.util.List;

public class SleepTimeAdapter extends RecyclerView.Adapter<SleepTimeAdapter.ViewHolder>{
    private List<SleepTimeItem> sleepTimes;
    private Context mContext;
    private onItemClickListener onItemClickListener;

    // my on item click
    public interface onItemClickListener {
        void onItemClick(int position);
    }

    // create ViewHolder class
    public static class ViewHolder extends RecyclerView.ViewHolder {
        public TextView duration;
        public TextView date;
        public TextView startTime;

        public ViewHolder(View view, final onItemClickListener listener){
            super(view);
            date = view.findViewById(R.id.sleep_date);
            duration = view.findViewById(R.id.sleep_duration);
            startTime = view.findViewById(R.id.sleep_start);

            view.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if(listener != null) {
                        int position = getAdapterPosition();
                        if(position != RecyclerView.NO_POSITION){
                            listener.onItemClick(position);
                        }
                    }
                }
            });
        }

    }

    // constructor
    public SleepTimeAdapter(Context context, List<SleepTimeItem> dataset){
        mContext = context;
        sleepTimes = dataset;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.fragment_sleep_item, viewGroup, false);
        ViewHolder viewHolder = new ViewHolder(view, onItemClickListener);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder viewHolder, int i) {
        // set data for display
        SleepTimeItem sleepTime = sleepTimes.get(i);
        viewHolder.date.setText(sleepTime.getDateString());
        viewHolder.startTime.setText(sleepTime.getStartTime()+" - "+sleepTime.getEndTime());
        viewHolder.duration.setText(sleepTime.getDuration());
    }

    @Override
    public int getItemCount() {
        return sleepTimes.size();
    }

    public void setOnItemClickListener(onItemClickListener listener){
        this.onItemClickListener = listener;
    }
}
