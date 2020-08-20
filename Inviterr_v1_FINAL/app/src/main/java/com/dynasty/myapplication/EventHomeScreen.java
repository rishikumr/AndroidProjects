package com.dynasty.myapplication;

import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class EventHomeScreen extends AppCompatActivity {

    public static final String TAG = Constants.LOG_TAG;
    private EventViewModel mViewModel;
    private List<Event> allEvents = new ArrayList<>();
    private List<Event> dummyEventsList = new ArrayList<>();
    FloatingActionButton addButton;
    EventScrollViewAdaptor adaptor;
    int i =0 , updatePosition =0;
    RecyclerView recyclerView;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.d(TAG, "onCreate: EventHomeScreen");
        setContentView(R.layout.all_events_home_page);
        addButton = findViewById(R.id.floatingAddButton);
        addButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent1 = new Intent(EventHomeScreen.this, EditEventDetailsActivity.class);
                startActivityForResult(intent1 , Constants.ADD_EVENT_DETAILS);
            }
        });
        addButton.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                Log.d(TAG, "Add Event "+ i);
                if(i ==4){mViewModel.deleteAllEvents(); i =0;}
                updatePosition= 0;
                mViewModel.insertEvent(dummyEventsList.get(i++));
                return true;
            }
        });
        recyclerView = findViewById(R.id.eventsScrollView);
        adaptor = new EventScrollViewAdaptor();
        recyclerView.setAdapter(adaptor);
        LinearLayoutManager layoutManager = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
        recyclerView.setLayoutManager(layoutManager);
        setDummyData();



        mViewModel = new ViewModelProvider(this).get(EventViewModel.class);
        mViewModel.getAllEvents().observe(this, new Observer<List<Event>>() {
            @Override
            public void onChanged(List<Event> events) {
                Log.d(TAG, "onChanged: getAllEvents()" + events.size() + "   updatePosition"+ updatePosition);
                adaptor.submitList(events);
                recyclerView.smoothScrollToPosition(updatePosition);

            }
        });
        adaptor.setOnEventClickListener(new EventScrollViewAdaptor.OnEventClickListener() {
            @Override
            public void onItemClick(Event ev , int position) {
                Log.d(TAG, "on Event Clicked  ev.getID()=" + ev.getId());
                updatePosition = position;
                Intent in = new Intent(EventHomeScreen.this,FullDetailEventActivity.class);
                in.putExtra(Constants.EXTRA_ID, ev.getId());
                startActivity(in);
            }
        });

    }

    @Override
    protected void onResume() {
        Log.d(TAG, "EVENT HOME SCREEN onResume" );
        super.onResume();
        adaptor.notifyDataSetChanged();
    }

    private void setDummyData() {
        dummyEventsList.add(new Event("Conference Meeting", "10-05-2020",  "Delhi", "Please don't forget to attend this high level meting you have to be in.\n Please mark your availability \n Please don't forget to attend this high level meting you have to be in.\n Please mark your availability \n \n \n\n \n\n\n\n\n Thank you.", Constants.getDummyEventImages3(), new People().getDummyPeopleList()));
        dummyEventsList.add(new Event("Celebration Meeting", "11-05-2020",  "Mumbai", "Come together on this joyous occasion ", Constants.getDummyEventImages1() ,  new People().getDummyPeopleList()));
        dummyEventsList.add(new Event("Birthday Celebration", "12-05-2020", "Bangalore", "Hey ! It is my birthday celebration party. \n Come join us and celevrate together", Constants.getDummyEventImages2(),new People().getDummyPeopleList()));
        dummyEventsList.add(new Event("Family dinner", "13-05-2020",  "Hydrabad", "Please join us to have a dinner with all of your loved ones.", Constants.getDummyEventImages3(), new People().getDummyPeopleList()));

    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == Constants.ADD_EVENT_DETAILS && resultCode == RESULT_OK) {
            Event curr_Event = new Event(
            data.getStringExtra(Constants.EXTRA_NAME),
            data.getStringExtra(Constants.EXTRA_DATE),
            data.getStringExtra(Constants.EXTRA_LOCATION),
            data.getStringExtra(Constants.EXTRA_DESC),
            data.getStringArrayListExtra(Constants.EXTRA_IMAGE_URIS),
            new People().getDummyPeopleList());
            mViewModel.insertEvent(curr_Event);
            Toast.makeText(this, "Event created.", Toast.LENGTH_SHORT).show();
            updatePosition= 0;

        } else if (requestCode == Constants.ADD_EVENT_DETAILS && resultCode == RESULT_CANCELED){
             Toast.makeText(this, "Event not created.", Toast.LENGTH_SHORT).show();
        }else if(requestCode == Constants.EDIT_EVENT_DETAILS && resultCode == RESULT_CANCELED){
            Toast.makeText(this, "Event not saved.", Toast.LENGTH_SHORT).show();
        }
    }

}
