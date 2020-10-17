package com.dynasty.myapplication.ui;


import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.ViewCompat;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.viewpager2.widget.ViewPager2;

import com.dynasty.myapplication.GoogleMapsActivity;
import com.dynasty.myapplication.utils.Constants;
import com.dynasty.myapplication.viewmodel.EventViewModel;
import com.dynasty.myapplication.adaptors.ImageViewPager2AdaptorCommon;
import com.dynasty.myapplication.utils.Imageutils;
import com.dynasty.myapplication.R;
import com.dynasty.myapplication.entity.Event;
import com.tbuonomo.viewpagerdotsindicator.WormDotsIndicator;

import java.util.ArrayList;
import java.util.concurrent.ExecutionException;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link EditEventDetails#newInstance} factory method to
 * create an instance of this fragment.
 */
public class EditEventDetails extends Fragment implements Imageutils.ImageAttachmentListener, View.OnClickListener {


    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    public static final String TAG = Constants.LOG_TAG;
    private static final int MAP_PIN_LOCATION_REQUEST_CODE = 1001;
    ViewPager2 viewPage;
    EditText edit_event_name, edit_event_date, edit_event_location, edit_event_description;
    ImageButton deleteImage, save_edit_details;
    WormDotsIndicator wormDotsIndicator;
    Imageutils imageutils;
    private int event_Id = 0;
    private MutableLiveData<ArrayList<String>> mutable_event_imageURIs = new MutableLiveData<>();
    private ArrayList<String> event_imageURIs = new ArrayList<>();
    ImageViewPager2AdaptorCommon imgAdaptor;
    TextView button_map;
    Event curr_Event;
    private EventViewModel mViewModel;
    private NavController navController;

    private String mParam1;
    private String mParam2;

    public EditEventDetails() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment EditEventFragment.
     */

    public static EditEventDetails newInstance(String param1, String param2) {
        EditEventDetails fragment = new EditEventDetails();
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
        return inflater.inflate(R.layout.fragment_edit_event, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        ActionBar ab =((AppCompatActivity)requireActivity()).getSupportActionBar();
        ab.setTitle("Edit event details");
        edit_event_name = view.findViewById(R.id.event_name_edit_detail_page);
        edit_event_location = view.findViewById(R.id.event_location_edit_detail_page);
        edit_event_date = view.findViewById(R.id.event_date_edit_detail_page);
        edit_event_description = view.findViewById(R.id.event_description_edit_detail_page);
        deleteImage = view.findViewById(R.id.delete_image_edit_detail_page);
        save_edit_details = view.findViewById(R.id.save_edit_details_page);
        //button_map = view.findViewById(R.id.eventLocation_button_map);
       // button_map.setOnClickListener(this);
        deleteImage.setOnClickListener(this);
        save_edit_details.setOnClickListener(this);
        mViewModel = new ViewModelProvider(requireActivity()).get(EventViewModel.class);
        navController = Navigation.findNavController(requireActivity(), R.id.nav_host_fragment);

        if (getArguments() != null) {
            event_Id = getArguments().getInt(Constants.EXTRA_ID);
        }
        try {
            mViewModel.getEvent(event_Id).observe(getViewLifecycleOwner(), new Observer<Event>() {
                @Override
                public void onChanged(Event changedEvent) {
                    if(changedEvent != null){
                        Log.d(TAG, "EditEventFrag  : onChanged= "+ changedEvent);
                        curr_Event = changedEvent;
                        edit_event_name.setText(curr_Event.getEvent_name());
                        edit_event_location.setText(curr_Event.getEvent_location());
                        edit_event_date.setText(curr_Event.getEvent_date());
                        edit_event_description.setText(curr_Event.getEvent_description());
                        event_imageURIs = (curr_Event.getEvent_imgURIs());
                        if(!event_imageURIs.isEmpty()){event_imageURIs.add(Uri.parse("android.resource://com.dynasty.myapplication/drawable/add_picture_image").toString());}
                        mutable_event_imageURIs.setValue(event_imageURIs);

                    }
                }
            });
        } catch (ExecutionException | InterruptedException e) {
            e.printStackTrace();
        }

        mutable_event_imageURIs.observe(getViewLifecycleOwner(), new Observer<ArrayList<String>>() {
            @Override
            public void onChanged(ArrayList<String> strings) {
                Log.d(TAG, "updateImageURIs: "  + strings.size());
                imgAdaptor.updateImageURIs(strings);
            }
        });

        // AddImage Utils
        imageutils =new Imageutils(getActivity(), this , true);

        //Worm image indicator
        wormDotsIndicator = (WormDotsIndicator) view.findViewById(R.id.worm_dots_indicator_edit_page);
        Log.d(TAG, "updateImageURIs:  event_imageURIs = "  + event_imageURIs.size());

        imgAdaptor = new ImageViewPager2AdaptorCommon(event_imageURIs);
        viewPage = view.findViewById(R.id.imageViewPager_edit_page);
        viewPage.setOrientation(ViewPager2.ORIENTATION_HORIZONTAL);
        viewPage.setAdapter(imgAdaptor);
        viewPage.setOffscreenPageLimit(3);


        wormDotsIndicator.setViewPager2(viewPage);
        final float pageMargin = getResources().getDimensionPixelOffset(R.dimen.pageMargin);
        final float pageOffset = getResources().getDimensionPixelOffset(R.dimen.offset);


        viewPage.registerOnPageChangeCallback(new ViewPager2.OnPageChangeCallback() {
            @Override
            public void onPageSelected(int position) {
                super.onPageSelected(position);
                // Log.d(TAG, "onPageSelected : position" + position + "   "+ event_imageURIs.size());
                if(position == event_imageURIs.size()-1){
                    deleteImage.setVisibility(View.INVISIBLE);
                }else if(deleteImage.getVisibility() == View.INVISIBLE){
                    deleteImage.setVisibility(View.VISIBLE);
                }
            }
        });

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

        imgAdaptor.setOnEventClickListener(new ImageViewPager2AdaptorCommon.OnEventClickListener() {
            @Override
            public void onItemClick(int position) {
                if(position == event_imageURIs.size()-1){
                    // launch image picking activity
                    //CaptureImage.askForImageSource();
                    imageutils.imagepicker(1);
                }
            }
        });


    }


    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data)
    {
        super.onActivityResult(requestCode, resultCode, data);
        imageutils.onActivityResult(requestCode, resultCode, data);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        imageutils.request_permission_result(requestCode, permissions, grantResults);
    }



    @Override
    public void image_attachment(int from, String filename, Bitmap file, Uri uri) {
        Log.d(TAG, "NewImage/Picked Image URI :" + uri);
        event_imageURIs.add(event_imageURIs.size()-1,String.valueOf(uri));
        mutable_event_imageURIs.setValue(event_imageURIs);


    }

    @Override
    public void onClick(View view) {
        switch(view.getId()) {
            case R.id.delete_image_edit_detail_page:
                event_imageURIs.remove(viewPage.getCurrentItem());
                mutable_event_imageURIs.setValue(event_imageURIs);
                break;
            case R.id.save_edit_details_page:
                // Save Item details
                if(curr_Event == null) {
                    curr_Event = new Event();
                    event_imageURIs.remove(Uri.parse("android.resource://com.dynasty.myapplication/drawable/add_picture_image").toString());
                    saveCurrentEventDetails();
                    mViewModel.insertEvent(curr_Event);
                    Toast.makeText(requireActivity(), "Event Created.", Toast.LENGTH_SHORT).show();
                }
                else {
                    //event_imageURIs.remove("android.resource://com.dynasty.myapplication/drawable/add_picture_image");
                    event_imageURIs.remove(Uri.parse("android.resource://com.dynasty.myapplication/drawable/add_picture_image").toString());
                    saveCurrentEventDetails();
                    mViewModel.updateEvent(curr_Event);
                    Toast.makeText(requireActivity(), "Event saved.", Toast.LENGTH_SHORT).show();
                }
                navController.popBackStack();
                break;

           /* case R.id.eventLocation_button_map :
                Log.d(TAG, "Map button clicked:" );
                getMapActivity();
                FragmentManager fragmentManager = requireActivity().getSupportFragmentManager();
                FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                break;*/
        }

    }

    private void saveCurrentEventDetails() {
        curr_Event.setEvent_name(edit_event_name.getText().toString());
        curr_Event.setEvent_date(edit_event_date.getText().toString());
        curr_Event.setEvent_location( edit_event_location.getText().toString());
        curr_Event.setEvent_description( edit_event_description.getText().toString());
        curr_Event.setEvent_imgURIs( event_imageURIs);
    }

    private void getMapActivity(){

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (requireActivity().checkSelfPermission(Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && requireActivity().checkSelfPermission(Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                requestPermissions(new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, MAP_PIN_LOCATION_REQUEST_CODE);
                Log.d(TAG, "Requesting permission");
                return;
            }
        }
        Log.d(TAG, "permission present ");
        Intent intent = new Intent(getActivity(), GoogleMapsActivity.class);
        startActivity(intent);

    }
}