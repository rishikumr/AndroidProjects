package com.dynasty.myapplication.ui.FirstTabEvent;

import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;
import androidx.appcompat.widget.ShareActionProvider;
import androidx.core.view.MenuItemCompat;
import androidx.core.view.ViewCompat;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager2.widget.ViewPager2;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewParent;
import android.widget.ImageView;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import com.dynasty.myapplication.utils.Constants;
import com.dynasty.myapplication.viewmodel.EventViewModel;
import com.dynasty.myapplication.adaptors.ImageViewPager2AdaptorCommon;
import com.dynasty.myapplication.R;
import com.dynasty.myapplication.entity.Event;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;
import java.util.concurrent.ExecutionException;

public class EventInfoScreen extends Fragment implements View.OnClickListener {

    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    public static final String TAG = Constants.LOG_TAG;
    TextView event_name, event_date ,event_location ,  event_description, event_inviter;
    private ArrayList<String> event_imageURIs = new ArrayList<>();
    FloatingActionButton editEventButton, deleteEventButton;
    private int event_Id = 0;
    private Event curr_Event;
    private EventViewModel mViewModel;
    private ImageView evImage;
    private NavController navController;
    private ShareActionProvider mShareActionProvider;




    private String mParam1;
    private String mParam2;

    public EventInfoScreen() {
        // Required empty public constructor
    }

    public static EventInfoScreen newInstance(String param1, String param2) {
        EventInfoScreen fragment = new EventInfoScreen();
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
        //setHasOptionsMenu(true);
        return inflater.inflate(R.layout.single_event_full_details_page, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        Log.d(TAG, "onViewCreated: EventInfo");
        ActionBar ab =((AppCompatActivity)requireActivity()).getSupportActionBar();


        event_name = view.findViewById(R.id.event_name_full_detail_page);
        event_location = view.findViewById(R.id.event_location_full_detail_page);
        event_date = view.findViewById(R.id.event_date_full_detail_page);
        event_description = view.findViewById(R.id.event_description_full_detail_page);
        evImage = view.findViewById(R.id.imageScrollView_detailed_page);
        event_inviter = view.findViewById(R.id.event_inviter_full_detail_page);

        editEventButton = view.findViewById(R.id.edit_mode_full_detailed_page);
        deleteEventButton = view.findViewById(R.id.delete_event_full_detailed_page);
        mViewModel = new ViewModelProvider(this).get(EventViewModel.class);
        navController = Navigation.findNavController(requireActivity(), R.id.nav_host_fragment);

        if (getArguments() != null) {
           event_Id = getArguments().getInt(Constants.EXTRA_ID);
        }
        try {
            mViewModel.getEvent(event_Id).observe(getViewLifecycleOwner(), new Observer<Event>() {
                @Override
                public void onChanged(Event updatedEvent) {
                    if(updatedEvent != null){
                        Log.d(TAG, "EventDetailedFrag  : onChanged="+ updatedEvent);
                        curr_Event = updatedEvent;
                        event_name.setText(curr_Event.getEvent_name());
                        event_inviter.setText(": "+curr_Event.getEvent_creator());
                        event_location.setText(": "+curr_Event.getEvent_location());
                        event_date.setText(": "+curr_Event.getEvent_date());
                        event_description.setText(curr_Event.getEvent_description());
                        event_imageURIs = curr_Event.getEvent_imgURIs();
                        evImage.setImageURI(Uri.parse(event_imageURIs.get(0)));
                        mViewModel.setCurrent_Selected_Event(updatedEvent);
                        ab.setTitle(curr_Event.getEvent_name());

                    }
                }
            });
        } catch (ExecutionException | InterruptedException e) {
            e.printStackTrace();
        }
        editEventButton.setOnClickListener(this);
        deleteEventButton.setOnClickListener(this);





    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.edit_mode_full_detailed_page:
                Bundle mBundle = new Bundle();
                mBundle.putInt(Constants.EXTRA_ID, event_Id);
                navController.navigate(R.id.action_detailEventTabHost_to_editEventDetails, mBundle);
                break;
            case R.id.delete_event_full_detailed_page:
                AlertDialog.Builder builder1 = new AlertDialog.Builder(requireActivity());
                builder1.setMessage("Do you really want to delete this event ?");
                builder1.setCancelable(true);

                builder1.setPositiveButton(
                        "Yes",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                dialog.cancel();
                                mViewModel.deleteEvent(curr_Event);
                                navController.navigateUp();
                            }
                        });

                builder1.setNegativeButton(
                        "No",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                dialog.cancel();
                            }
                        });

                AlertDialog alert11 = builder1.create();
                alert11.show();

        }

    }
    @Override
    public void onPause() {
        super.onPause();
        setHasOptionsMenu(false);
        Log.d(TAG, "onPause: EventInfo");
    }



    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {

        switch (item.getItemId()) {
            case android.R.id.home:
                navController.navigateUp();
                return true;


            default:
                // If we got here, the user's action was not recognized.Invoke the superclass to handle it.
                return super.onOptionsItemSelected(item);

        }

    }



}