package com.dynasty.myapplication.adaptors;

import android.net.Uri;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.dynasty.myapplication.R;
import com.dynasty.myapplication.utils.Constants;

import java.util.ArrayList;

public class ImageViewPager2AdaptorCommon extends RecyclerView.Adapter<ImageViewPager2AdaptorCommon.EventImagesPageViewHolder> {


    private ArrayList<String> eventImageURIs = new ArrayList<>();
    public static final String TAG = Constants.LOG_TAG;
    private ImageViewPager2AdaptorCommon.OnEventClickListener listener;

    public ImageViewPager2AdaptorCommon(ArrayList<String> imageURIs){
        eventImageURIs.clear();
        if(imageURIs == null || imageURIs.isEmpty() ){ eventImageURIs.add(Uri.parse("android.resource://com.dynasty.myapplication/drawable/zero_picture_image").toString());}
        eventImageURIs.addAll(imageURIs);
    }

    public void updateImageURIs(ArrayList<String> imageURIs){
        eventImageURIs.clear();
        eventImageURIs.addAll(imageURIs);
        if(imageURIs == null || imageURIs.isEmpty() ){ eventImageURIs.add(Uri.parse("android.resource://com.dynasty.myapplication/drawable/zero_picture_image").toString());}
        //Log.d(TAG, "updateImageURIs: adaptor "  + "  size "+ eventImageURIs.size());
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public EventImagesPageViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.single_image_detailed_event, parent , false);
        return new EventImagesPageViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull EventImagesPageViewHolder holder, int position) {
        //Log.d(TAG, "onBindViewHolder: "+ position  + "  size "+ eventImageURIs.size());
            holder.imgView.setImageURI(Uri.parse(eventImageURIs.get(position)));

    }

    @Override
    public int getItemCount() {
        return eventImageURIs.size();
    }

    public class EventImagesPageViewHolder extends RecyclerView.ViewHolder{
        ImageView imgView;

        public EventImagesPageViewHolder(@NonNull final View itemView) {
            super(itemView);
            imgView = itemView.findViewById(R.id.event_image_full_detailed_page);
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    int position = getAdapterPosition();
                    if (listener != null && position != RecyclerView.NO_POSITION) {
                        listener.onItemClick(position);
                    }
                }
            });
        }
    }

    public interface OnEventClickListener {
        void onItemClick(int position);
    }

    public void setOnEventClickListener(ImageViewPager2AdaptorCommon.OnEventClickListener listener) {
        this.listener = listener;

    }



}
