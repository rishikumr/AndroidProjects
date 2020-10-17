package com.dynasty.myapplication.ui.homescreen;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;
import androidx.appcompat.widget.SwitchCompat;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RadioButton;
import android.widget.Toast;

import com.dynasty.myapplication.utils.Constants;
import com.dynasty.myapplication.adaptors.EventScrollViewAdaptor;
import com.dynasty.myapplication.viewmodel.EventViewModel;
import com.dynasty.myapplication.R;
import com.dynasty.myapplication.entity.Event;
import com.dynasty.myapplication.entity.People;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link AllInvitationHomeScreen#newInstance} factory method to
 * create an instance of this fragment.
 */
public class AllInvitationHomeScreen extends Fragment implements  View.OnClickListener{

    //  Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    public static final String TAG = Constants.LOG_TAG;
    private EventViewModel mViewModel;
    SwitchCompat pastEventsFilter, futureEventsFilter;
    private List<Event> allEvents = new ArrayList<>();
    private List<Event> dummyEventsList = new ArrayList<>();
    private List<Event> invitationList = new ArrayList<>();
    FloatingActionButton addButton;
    EventScrollViewAdaptor adaptor;
    int i =0 , updatePosition =0;
    RecyclerView recyclerView;
    NavController navController;



    //  Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public AllInvitationHomeScreen() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment EventHomeScreenFragment.
     */
    //  Rename and change types and number of parameters
    public static AllInvitationHomeScreen newInstance(String param1, String param2) {
        AllInvitationHomeScreen fragment = new AllInvitationHomeScreen();
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

        mViewModel = new ViewModelProvider(this).get(EventViewModel.class);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        Log.d(TAG, "onCreateView: AllEventHomeScreen");
        //setHasOptionsMenu(true);
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
        //addButton.setVisibility(View.GONE);
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



        mViewModel.getAllInvitations().observe(getViewLifecycleOwner(), new Observer<List<Event>>() {
            @Override
            public void onChanged(List<Event> events) {
                Log.d(TAG, "onChanged: getAllEvents()" + events.size() + "   updatePosition"+ updatePosition);
                invitationList = events;
                adaptor.submitFilteredList(events , pastEventsFilter.isChecked() , futureEventsFilter.isChecked());
                recyclerView.smoothScrollToPosition(updatePosition);
            }
        });
        adaptor.setOnEventClickListener(new EventScrollViewAdaptor.OnEventClickListener() {
            @Override
            public void onItemClick(Event ev , int position) {
               Log.d(TAG, "on Event Clicked  ev.getID()=" + ev.getId()  + "position  ="+ position);
                Bundle mBundle  = new Bundle();
                mBundle.putInt(Constants.EXTRA_ID,ev.getId());
                mBundle.putBoolean(Constants.IS_CREATOR, false);
                navController.navigate(R.id.action_homeScreenTabHost_to_detailEventTabHost, mBundle);
                updatePosition = position;
                recyclerView.smoothScrollToPosition(updatePosition);

            }
        });



    }

    private void setDummyData() {

        dummyEventsList.add(new Event("Conference Meeting","Ram",  "10-05-2022",  "Delhi", "Please don't forget to attend this high level meting you have to be in.\n Please mark your availability \n Please don't forget to attend this high level meting you have to be in.\n Please mark your availability \n \n \n\n \n\n\n\n\n Thank you.", Constants.getDummyEventImages1(), new People().getDummyPeopleList()));
        dummyEventsList.add(new Event("Family dinner","Tam",  "13-05-2019",  "Hydrabad", "Please join us to have a dinner with all of your loved ones.", Constants.getDummyEventImages1(), new People().getDummyPeopleList()));
        dummyEventsList.add(new Event("Birthday Celebration", "Cam", "12-05-2022", "Bangalore", "Hey ! It is my birthday celebration party. \n Come join us and celebrate together", Constants.getDummyEventImages3(),new People().getDummyPeopleList()));
        dummyEventsList.add(new Event("Celebration Meeting", "Sam", "11-05-2020",  "Mumbai", "Come together on this joyous occasion ", Constants.getDummyEventImages2() ,  new People().getDummyPeopleList()));
        dummyEventsList.add(new Event("Family dinner", "Pam", "13-05-2022",  "Hydrabad", "Please join us to have a dinner with all of your loved ones.", Constants.getDummyEventImages3(), new People().getDummyPeopleList()));


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
                adaptor.submitFilteredList(invitationList , pastEventsFilter.isChecked() , futureEventsFilter.isChecked());
                recyclerView.smoothScrollToPosition(updatePosition);
                break;
        }
    }

   /* @Override
    public void onCreateOptionsMenu(@NonNull Menu menu , @NonNull MenuInflater inflater) {

        requireActivity().getMenuInflater().inflate(R.menu.menu_home_screen_rcv, menu);
        MenuItem mSearch = menu.findItem(R.id.menu_event_search);
        Log.d(TAG, "onCreateOptionsMenu: AllEvent " + menu.size() + "  mSearch"+mSearch);
        SearchView searchView = (SearchView) mSearch.getActionView();
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                Log.d(TAG, "onQueryTextSubmit: ");
                // adapter.getFilter().filter(newText);
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                Log.d(TAG, "onQueryTextChange: ");
                return false;
            }
        });

    }
*/
    @Override
    public void onPause() {
        super.onPause();
        setHasOptionsMenu(false);
        Log.d(TAG, "onPause: AllInvi");
    }
    @Override
    public void onPrepareOptionsMenu(@NonNull Menu menu) {
        Log.d(TAG, "onPrepareOptionsMenu: AllInvi " + menu.size());
    }


    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {

        switch (item.getItemId()) {

            case android.R.id.home:
                navController.navigateUp();
                return true;
           /* case R.id.menu_exit:

                System.exit(0);
                return true;*/
            default:
                // If we got here, the user's action was not recognized.
                // Invoke the superclass to handle it.
                return super.onOptionsItemSelected(item);

        }

    }
}