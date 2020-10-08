package com.dynasty.myapplication.adaptors;


import android.net.Uri;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.DiffUtil;
import androidx.recyclerview.widget.ListAdapter;
import androidx.recyclerview.widget.RecyclerView;

import com.dynasty.myapplication.R;
import com.dynasty.myapplication.entity.Event;
import com.dynasty.myapplication.utils.Constants;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.TimeZone;


public class EventScrollViewAdaptor extends ListAdapter<Event, EventScrollViewAdaptor.EventScrollViewHolder>  implements Filterable {

    private OnEventClickListener listener;
    Date todayDate;
    List<Event> submitted_list = new ArrayList<>();
     boolean pastEventsFilterEnabled, futureEventsFilterEnabled;

    public EventScrollViewAdaptor() {
        super(DIFF_CALLBACK);
        todayDate = Calendar.getInstance().getTime();
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
        holder.eventInviter.setText("By, " + currEvent.getEvent_creator());
        if (currEvent.getEvent_imgURIs().size() != 0) {
            holder.eventImage.setImageURI(Uri.parse(currEvent.getEvent_imgURIs().get(0)));
        } else {
            holder.eventImage.setImageResource(R.drawable.zero_picture_image);
        }


    }

    public void submitFilteredList(@Nullable List<Event> list, boolean pastEventsFilterEnabled, boolean futureEventsFilterEnabled) {
       // Log.d(Constants.LOG_TAG, "new submitted_list "+ list);
        submitted_list.clear();
        submitted_list.addAll(list);
        this.pastEventsFilterEnabled = pastEventsFilterEnabled;
        this.futureEventsFilterEnabled = futureEventsFilterEnabled;
        getFilter().filter("");
    }

    private boolean isPastEvent(String event_date) {
        Date mEventDate = new Date();
        try {
            SimpleDateFormat sdf = new SimpleDateFormat("dd-mm-yyyy", Locale.getDefault());
            mEventDate = sdf.parse(event_date);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        if (todayDate.compareTo(mEventDate) < 0) {
            return false;
        }
        return true;
    }


    @Override
    public Filter getFilter() {
        return filter;
    }

    Filter filter = new Filter() {
        @Override
        protected FilterResults performFiltering(CharSequence constraint) {
            List<Event> temp_list = new ArrayList<>();
            for (Event mEvent : submitted_list) {
               // Log.d(Constants.LOG_TAG, "looping  "+ mEvent.getEvent_date());
                if (isPastEvent(mEvent.getEvent_date())) {
                    if (pastEventsFilterEnabled) {
                        temp_list.add(mEvent);
                    }
                } else if (futureEventsFilterEnabled) {
                    temp_list.add(mEvent);
                }
            }
            FilterResults fr = new FilterResults();
            //Log.d(Constants.LOG_TAG, "temp_list "+ temp_list);
            fr.values = temp_list;
            return  fr;
        }

        @Override
        protected void publishResults(CharSequence constraint, FilterResults results) {
           // Log.d(Constants.LOG_TAG, "publish results "+ String.valueOf(results.values));
            submitList((List<Event>) results.values);
        }
    };


    class EventScrollViewHolder extends RecyclerView.ViewHolder {
        private TextView eventName, eventLocation, eventDate, eventInviter;
        private ImageView eventImage;

        public EventScrollViewHolder(@NonNull View itemView) {
            super(itemView);
            eventDate = itemView.findViewById(R.id.eventDate);
            eventLocation = itemView.findViewById(R.id.eventLocation);
            eventInviter = itemView.findViewById(R.id.eventInviter);
            eventImage = itemView.findViewById(R.id.single_event_imageView);
            eventName = itemView.findViewById(R.id.eventName);
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    int position = getAdapterPosition();
                    if (listener != null && position != RecyclerView.NO_POSITION) {
                        listener.onItemClick(getItem(position), position);
                    }
                }
            });
        }
    }

    public interface OnEventClickListener {
        void onItemClick(Event ev, int position);
    }

    public void setOnEventClickListener(OnEventClickListener listener) {
        this.listener = listener;

    }
}
