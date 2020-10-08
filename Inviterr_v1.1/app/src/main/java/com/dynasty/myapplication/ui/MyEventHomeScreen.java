package com.dynasty.myapplication.ui;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.SwitchCompat;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.dynasty.myapplication.R;
import com.dynasty.myapplication.adaptors.EventScrollViewAdaptor;
import com.dynasty.myapplication.entity.Event;
import com.dynasty.myapplication.entity.People;
import com.dynasty.myapplication.utils.Constants;
import com.dynasty.myapplication.viewmodel.EventViewModel;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link MyEventHomeScreen#newInstance} factory method to
 * create an instance of this fragment.
 */
public class MyEventHomeScreen extends Fragment implements  View.OnClickListener{


    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    public static final String TAG = Constants.LOG_TAG;
    private EventViewModel mViewModel;
    private List<Event> allEvents = new ArrayList<>();
    private List<Event> myAllEventsList = new ArrayList<>();
    private List<Event> dummyEventsList = new ArrayList<>();
    SwitchCompat pastEventsFilter, futureEventsFilter;
    FloatingActionButton addButton;
    EventScrollViewAdaptor adaptor;
    int i =0 , updatePosition =0;
    RecyclerView recyclerView;
    NavController navController;




    private String mParam1;
    private String mParam2;

    public MyEventHomeScreen() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment MyEventHomeScreen.
     */

    public static MyEventHomeScreen newInstance(String param1, String param2) {
        MyEventHomeScreen fragment = new MyEventHomeScreen();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        //Log.d(TAG, "onCreateView: EventHomeScreen");
        return inflater.inflate(R.layout.fragment_event_home_screen, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        navController = Navigation.findNavController(requireActivity(), R.id.nav_host_fragment);
        pastEventsFilter = view.findViewById(R.id.pastEventsFilter);
        futureEventsFilter = view.findViewById(R.id.futureEventsFilter);
        pastEventsFilter.setOnClickListener(this);
        futureEventsFilter.setOnClickListener(this);

        addButton = view.findViewById(R.id.floatingAddButton);
        addButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                navController.navigate(R.id.action_homeScreenTabHost_to_editEventDetails);
            }
        });
        addButton.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                if(i ==5){mViewModel.deleteAllEvents(); i =0;}
                updatePosition= 0;
                mViewModel.insertEvent(dummyEventsList.get(i++));
                return true;
            }
        });
        recyclerView = view.findViewById(R.id.eventsScrollView);
        adaptor = new EventScrollViewAdaptor();
        recyclerView.setAdapter(adaptor);
        LinearLayoutManager layoutManager = new LinearLayoutManager(requireActivity(), LinearLayoutManager.VERTICAL, false);
        recyclerView.setLayoutManager(layoutManager);
        setDummyData();


        mViewModel = new ViewModelProvider(this).get(EventViewModel.class);
        mViewModel.getAllMyEvents().observe(getViewLifecycleOwner(), new Observer<List<Event>>() {
            @Override
            public void onChanged(List<Event> events) {
                Log.d(TAG, "onChanged: getAllEvents()" + events.size() + "   updatePosition"+ updatePosition);
                myAllEventsList = events;
                adaptor.submitFilteredList(events , pastEventsFilter.isChecked() , futureEventsFilter.isChecked());
                recyclerView.smoothScrollToPosition(updatePosition);
            }
        });
        adaptor.setOnEventClickListener(new EventScrollViewAdaptor.OnEventClickListener() {
            @Override
            public void onItemClick(Event ev , int position) {
                //  Log.d(TAG, "on Event Clicked  ev.getID()=" + ev.getId()  + "position  ="+ position);
                Bundle mBundle  = new Bundle();
                mBundle.putInt(Constants.EXTRA_ID,ev.getId());
                mBundle.putBoolean(Constants.IS_CREATOR, true);
                navController.navigate(R.id.action_homeScreenTabHost_to_detailEventTabHost, mBundle);
                updatePosition = position;
                recyclerView.smoothScrollToPosition(updatePosition);

            }
        });

    }

    private void setDummyData() {
        dummyEventsList.add(new Event("Family dinner","SELF",  "13-05-2022",  "Hydrabad", "Please join us to have a dinner with all of your loved ones.", Constants.getDummyEventImages1(), new People().getDummyPeopleList()));
        dummyEventsList.add(new Event("Celebration Meeting", "SELF", "11-05-2020",  "Mumbai", "Come together on this joyous occasion ", Constants.getDummyEventImages2() ,  new People().getDummyPeopleList()));
        dummyEventsList.add(new Event("Conference Meeting","SELF",  "10-05-2022",  "Delhi", "Please don't forget to attend this high level meting you have to be in.\n Please mark your availability \n Please don't forget to attend this high level meting you have to be in.\n Please mark your availability \n \n \n\n \n\n\n\n\n Thank you.", Constants.getDummyEventImages1(), new People().getDummyPeopleList()));
        dummyEventsList.add(new Event("Family dinner","SELF",  "13-05-2020",  "Hydrabad", "Please join us to have a dinner with all of your loved ones.", Constants.getDummyEventImages2(), new People().getDummyPeopleList()));
        dummyEventsList.add(new Event("Birthday Celebration", "SELF", "12-05-2022", "Bangalore", "Hey ! It is my birthday celebration party. \n Come join us and celebrate together", Constants.getDummyEventImages3(),new People().getDummyPeopleList()));


    }

    @Override
    public void onResume() {
        super.onResume();
        recyclerView.smoothScrollToPosition(updatePosition);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.pastEventsFilter :
            case R.id.futureEventsFilter :
                adaptor.submitFilteredList(myAllEventsList , pastEventsFilter.isChecked() , futureEventsFilter.isChecked());
                recyclerView.smoothScrollToPosition(updatePosition);
                break;
        }
    }
}