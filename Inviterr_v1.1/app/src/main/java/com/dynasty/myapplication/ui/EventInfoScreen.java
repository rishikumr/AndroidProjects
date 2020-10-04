package com.dynasty.myapplication.ui;

import android.content.DialogInterface;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.core.view.ViewCompat;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.viewpager2.widget.ViewPager2;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewParent;
import android.widget.TextView;

import com.dynasty.myapplication.utils.Constants;
import com.dynasty.myapplication.viewmodel.EventViewModel;
import com.dynasty.myapplication.adaptors.ImageViewPager2AdaptorCommon;
import com.dynasty.myapplication.R;
import com.dynasty.myapplication.entity.Event;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.tbuonomo.viewpagerdotsindicator.WormDotsIndicator;

import java.util.ArrayList;
import java.util.concurrent.ExecutionException;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link EventInfoScreen#newInstance} factory method to
 * create an instance of this fragment.
 */
public class EventInfoScreen extends Fragment implements View.OnClickListener {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    public static final String TAG = Constants.LOG_TAG;
    ViewPager2 viewPage;
    TextView event_name, event_date ,event_location ,  event_description;
    private ArrayList<String> event_imageURIs = new ArrayList<>();
    FloatingActionButton editEventButton, deleteEventButton;
    WormDotsIndicator wormDotsIndicator;
    private int event_Id = 0;
    private Event curr_Event;
    private EventViewModel mViewModel;
    private ImageViewPager2AdaptorCommon imgAdaptor;
    private NavController navController;


    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public EventInfoScreen() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment EventDetailedFragment.
     */
    // TODO: Rename and change types and number of parameters
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
        return inflater.inflate(R.layout.single_event_full_details_page, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        event_name = view.findViewById(R.id.event_name_full_detail_page);
        event_location = view.findViewById(R.id.event_location_full_detail_page);
        event_date = view.findViewById(R.id.event_date_full_detail_page);
        event_description = view.findViewById(R.id.event_description_full_detail_page);
        wormDotsIndicator = (WormDotsIndicator) view.findViewById(R.id.worm_dots_indicator_full_detail_page);
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
                public void onChanged(Event changedEvent) {
                    if(changedEvent != null){
                        Log.d(TAG, "EventDetailedFrag  : onChanged="+ changedEvent);
                        curr_Event = changedEvent;
                        event_name.setText(curr_Event.getEvent_name());
                        event_location.setText(curr_Event.getEvent_location());
                        event_date.setText(curr_Event.getEvent_date());
                        event_description.setText(curr_Event.getEvent_description());
                        event_imageURIs = (curr_Event.getEvent_imgURIs());
                        imgAdaptor.updateImageURIs(curr_Event.getEvent_imgURIs());
                    }
                }
            });
        } catch (ExecutionException | InterruptedException e) {
            e.printStackTrace();
        }


        imgAdaptor = new ImageViewPager2AdaptorCommon(event_imageURIs);
        viewPage = view.findViewById(R.id.imageViewPager_detailed_page);
        viewPage = view.findViewById(R.id.imageViewPager_detailed_page);
        viewPage.setOrientation(ViewPager2.ORIENTATION_HORIZONTAL);

        ViewParent parent = (ViewParent) viewPage;
        // or get a reference to the ViewPager and cast it to ViewParent
        parent.requestDisallowInterceptTouchEvent(false);

        viewPage.setAdapter(imgAdaptor);
        viewPage.setOffscreenPageLimit(3);
        wormDotsIndicator.setViewPager2(viewPage);
        final float pageMargin= getResources().getDimensionPixelOffset(R.dimen.pageMargin);
        final float pageOffset = getResources().getDimensionPixelOffset(R.dimen.offset);

        viewPage.setPageTransformer(new ViewPager2.PageTransformer() {
            @Override
            public void transformPage(@NonNull View page, float position) {
                float myOffset = position * -(2 * pageOffset + pageMargin);
                if (viewPage.getOrientation() == ViewPager2.ORIENTATION_HORIZONTAL) {
                    if (ViewCompat.getLayoutDirection(viewPage) == ViewCompat.LAYOUT_DIRECTION_RTL) {
                        page.setTranslationX(-myOffset);
                    } else {
                        page.setTranslationX(myOffset);
                    }
                } else {
                    page.setTranslationY(myOffset);
                }
            }
        });

        viewPage.registerOnPageChangeCallback(new ViewPager2.OnPageChangeCallback() {
            @Override
            public void onPageSelected(int position) {
                super.onPageSelected(position);
                Log.d(TAG, "onPageSelected : position" + position + "   "+ event_imageURIs.size());

            }
        });



        editEventButton.setOnClickListener(this);
        deleteEventButton.setOnClickListener(this);
        imgAdaptor.updateImageURIs(event_imageURIs);

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


}