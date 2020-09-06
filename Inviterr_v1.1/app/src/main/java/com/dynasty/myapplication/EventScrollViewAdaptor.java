package com.dynasty.myapplication;


import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.DiffUtil;
import androidx.recyclerview.widget.ListAdapter;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;


public class EventScrollViewAdaptor extends ListAdapter<Event, EventScrollViewAdaptor.EventScrollViewHolder> {

    private OnEventClickListener listener;
    public EventScrollViewAdaptor() {
        super(DIFF_CALLBACK);
    }

    private static final DiffUtil.ItemCallback<Event> DIFF_CALLBACK = new DiffUtil.ItemCallback<Event>() {
        @Override
        public boolean areItemsTheSame(@NonNull Event oldItem, @NonNull Event newItem) {
            return oldItem.getId() == newItem.getId();
        }

        @Override
        public boolean areContentsTheSame(@NonNull Event oldItem, @NonNull Event newItem) {
            return (oldItem.getEvent_name().equals(newItem.getEvent_name())
                    && oldItem.getEvent_date().equals(newItem.getEvent_date())
                    && oldItem.getEvent_location().equals(newItem.getEvent_location())
                    && oldItem.getEvent_description().equals(newItem.getEvent_description())
                    && oldItem.getEvent_imgURIs().equals(newItem.getEvent_imgURIs())
            );
        }
    };

    @NonNull
    @Override
    public EventScrollViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        //Log.d(Constants.LOG_TAG, "onCreateViewHolder");
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.single_event_minimal_details_card, parent, false);
        return new EventScrollViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull EventScrollViewHolder holder, int position) {

        Event currEvent = getItem(position);
        //Log.d(Constants.LOG_TAG, "onBindViewHolder + "+ currEvent.getEvent_imgURIs()  );
        holder.eventName.setText(currEvent.getEvent_name());
        holder.eventDate.setText(currEvent.getEvent_date());
        holder.eventLocation.setText(currEvent.getEvent_location());
        holder.eventDescription.setText(currEvent.getEvent_description());
        if(currEvent.getEvent_imgURIs().size() != 0){
            holder.eventImage.setImageURI(Uri.parse(currEvent.getEvent_imgURIs().get(0)));
        }else{
            holder.eventImage.setImageResource(R.drawable.zero_picture_image);
        }


    }

    @Override
    public void submitList(@Nullable List<Event> list) {
        super.submitList(list);

    }

    class EventScrollViewHolder extends RecyclerView.ViewHolder {
        private TextView eventName, eventLocation, eventDate, eventDescription;
        private ImageView eventImage;

        public EventScrollViewHolder(@NonNull View itemView) {
            super(itemView);
            eventDate = itemView.findViewById(R.id.eventDate);
            eventLocation = itemView.findViewById(R.id.eventLocation);
            eventDescription = itemView.findViewById(R.id.eventDescription);
            eventImage = itemView.findViewById(R.id.single_event_imageView);
            eventName = itemView.findViewById(R.id.eventName);
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    int position = getAdapterPosition();
                    if (listener != null && position != RecyclerView.NO_POSITION) {
                        listener.onItemClick(getItem(position) , position);
                    }
                }
            });
        }
    }

    public interface OnEventClickListener {
        void onItemClick(Event ev , int position);
    }

    public void setOnEventClickListener(OnEventClickListener listener) {
        this.listener = listener;

    }
}
