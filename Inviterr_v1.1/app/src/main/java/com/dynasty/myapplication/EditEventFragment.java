package com.dynasty.myapplication;


import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.view.ViewCompat;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.viewpager2.widget.ViewPager2;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;

import com.tbuonomo.viewpagerdotsindicator.WormDotsIndicator;

import java.util.ArrayList;
import java.util.concurrent.ExecutionException;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link EditEventFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class EditEventFragment extends Fragment implements Imageutils.ImageAttachmentListener, View.OnClickListener {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    public static final String TAG = Constants.LOG_TAG;
    ViewPager2 viewPage;
    EditText edit_event_name, edit_event_date, edit_event_location, edit_event_description;
    ImageButton deleteImage, save_edit_details;
    WormDotsIndicator wormDotsIndicator;
    Imageutils imageutils;
    private int event_Id;
    private MutableLiveData<ArrayList<String>> mutable_event_imageURIs = new MutableLiveData<>();
    private ArrayList<String> event_imageURIs = new ArrayList<>();
    ImageViewPager2AdaptorCommon imgAdaptor;
    Event curr_Event;
    private EventViewModel mViewModel;
    private NavController navController;

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public EditEventFragment() {
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
    // TODO: Rename and change types and number of parameters
    public static EditEventFragment newInstance(String param1, String param2) {
        EditEventFragment fragment = new EditEventFragment();
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
        edit_event_name = requireActivity().findViewById(R.id.event_name_edit_detail_page);
        edit_event_location = requireActivity().findViewById(R.id.event_location_edit_detail_page);
        edit_event_date = requireActivity().findViewById(R.id.event_date_edit_detail_page);
        edit_event_description = requireActivity().findViewById(R.id.event_description_edit_detail_page);
        deleteImage = requireActivity().findViewById(R.id.delete_image_edit_detail_page);
        save_edit_details = requireActivity().findViewById(R.id.save_edit_details_page);
        deleteImage.setOnClickListener(this);
        save_edit_details.setOnClickListener(this);
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
                        Log.d(TAG, "EditEventFrag  : onChanged= "+ changedEvent);
                        curr_Event = changedEvent;
                        edit_event_name.setText(curr_Event.getEvent_name());
                        edit_event_location.setText(curr_Event.getEvent_location());
                        edit_event_date.setText(curr_Event.getEvent_date());
                        edit_event_description.setText(curr_Event.getEvent_description());
                        event_imageURIs = (curr_Event.getEvent_imgURIs());
                        imgAdaptor.updateImageURIs(curr_Event.getEvent_imgURIs());
                    }
                }
            });
        } catch (ExecutionException | InterruptedException e) {
            e.printStackTrace();
        }

        mutable_event_imageURIs.observe(getViewLifecycleOwner(), new Observer<ArrayList<String>>() {
            @Override
            public void onChanged(ArrayList<String> strings) {
                imgAdaptor.updateImageURIs(strings);
            }
        });

        // AddImage Utils
        imageutils =new Imageutils(getActivity(), this , true);

        //Worm image indicator
        wormDotsIndicator = (WormDotsIndicator) requireActivity().findViewById(R.id.worm_dots_indicator_edit_page);
        event_imageURIs.add("android.resource://com.dynasty.myapplication/drawable/add_image_picture");

        imgAdaptor = new ImageViewPager2AdaptorCommon(event_imageURIs);
        viewPage = requireActivity().findViewById(R.id.imageViewPager_edit_page);
        viewPage.setOrientation(ViewPager2.ORIENTATION_HORIZONTAL);
        viewPage.setAdapter(imgAdaptor);
        viewPage.setOffscreenPageLimit(3);


        wormDotsIndicator.setViewPager2(viewPage);
        final float pageMargin = getResources().getDimensionPixelOffset(R.dimen.pageMargin);
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
                // Log.d(TAG, "onPageSelected : position" + position + "   "+ event_imageURIs.size());
                if(position == event_imageURIs.size()-1){
                    deleteImage.setVisibility(View.INVISIBLE);
                }else if(deleteImage.getVisibility() == View.INVISIBLE){
                    deleteImage.setVisibility(View.VISIBLE);
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
                    saveCurrentEventDetails();
                    mViewModel.insertEvent(curr_Event);
                    Toast.makeText(requireActivity(), "Event Created.", Toast.LENGTH_SHORT).show();
                }
                else {
                    saveCurrentEventDetails();
                    mViewModel.updateEvent(curr_Event);
                    Toast.makeText(requireActivity(), "Event saved.", Toast.LENGTH_SHORT).show();
                }
                navController.popBackStack();
                break;
        }

    }

    private void saveCurrentEventDetails() {
        curr_Event.setEvent_name(edit_event_name.getText().toString());
        curr_Event.setEvent_date(edit_event_date.getText().toString());
        curr_Event.setEvent_location( edit_event_location.getText().toString());
        curr_Event.setEvent_description( edit_event_description.getText().toString());
        curr_Event.setEvent_imgURIs( event_imageURIs);
    }
}