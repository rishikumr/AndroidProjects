package com.dynasty.myapplication;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.ViewCompat;

import androidx.lifecycle.Lifecycle;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.viewpager2.widget.ViewPager2;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.tbuonomo.viewpagerdotsindicator.WormDotsIndicator;

import java.util.ArrayList;
import java.util.concurrent.ExecutionException;


public class FullDetailEventActivity extends AppCompatActivity implements View.OnClickListener{
    public static final String TAG = Constants.LOG_TAG;
    ViewPager2 viewPage;
    TextView event_name, event_date ,event_location ,  event_description;
    private ArrayList<String> event_imageURIs = new ArrayList<>();
    FloatingActionButton editEventButton, deleteEventButton;
    WormDotsIndicator wormDotsIndicator;
    private int event_Id;
    private Event curr_Event;
    private EventViewModel mViewModel;
    private ImageViewPager2AdaptorCommon imgAdaptor;

    public Lifecycle getContext() {
        return  getLifecycle();
    }


    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.single_event_full_details_page);
        event_name = findViewById(R.id.event_name_full_detail_page);
        event_location = findViewById(R.id.event_location_full_detail_page);
        event_date = findViewById(R.id.event_date_full_detail_page);
        event_description = findViewById(R.id.event_description_full_detail_page);
        wormDotsIndicator = (WormDotsIndicator) findViewById(R.id.worm_dots_indicator_full_detail_page);
        editEventButton = findViewById(R.id.edit_mode_full_detailed_page);
        deleteEventButton = findViewById(R.id.delete_event_full_detailed_page);

        mViewModel = new ViewModelProvider(this).get(EventViewModel.class);


        Intent in = getIntent();

        if(in.hasExtra(Constants.EXTRA_ID)){
           event_Id = in.getIntExtra(Constants.EXTRA_ID, 0);
            try {
                mViewModel.getEvent(event_Id).observe(this, new Observer<Event>() {
                    @Override
                    public void onChanged(Event changedEvent) {

                       if(changedEvent != null){
                           Log.d(TAG, "FullDetailEventActivity  : onChanged="+ changedEvent);
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
            } catch (ExecutionException e) {
                e.printStackTrace();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }

        }

        imgAdaptor = new ImageViewPager2AdaptorCommon(event_imageURIs);
        viewPage = findViewById(R.id.imageViewPager_detailed_page);
        viewPage.setOrientation(ViewPager2.ORIENTATION_HORIZONTAL);
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

        editEventButton.setOnClickListener(this);
        deleteEventButton.setOnClickListener(this);



    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == Constants.EDIT_EVENT_DETAILS && resultCode == RESULT_OK) {
            //int temp_event_id = data.getIntExtra(Constants.EXTRA_ID , event_Id);
            curr_Event.setEvent_name(data.getStringExtra(Constants.EXTRA_NAME));
            curr_Event.setEvent_date(data.getStringExtra(Constants.EXTRA_DATE));
            curr_Event.setEvent_location( data.getStringExtra(Constants.EXTRA_LOCATION));
            curr_Event.setEvent_description( data.getStringExtra(Constants.EXTRA_DESC));
            curr_Event.setEvent_imgURIs( data.getStringArrayListExtra(Constants.EXTRA_IMAGE_URIS));
            mViewModel.updateEvent(curr_Event);

            Toast.makeText(this, "Event saved", Toast.LENGTH_SHORT).show();

        } else {
           // Toast.makeText(this, "Event not saved", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        imgAdaptor.updateImageURIs(event_imageURIs);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.edit_mode_full_detailed_page :
                Intent intent1 = new Intent(FullDetailEventActivity.this, EditEventDetailsActivity.class);
                intent1.putExtra(Constants.EXTRA_ID, event_Id);
                intent1.putExtra(Constants.EXTRA_NAME, event_name.getText().toString());
                intent1.putExtra(Constants.EXTRA_LOCATION, event_location.getText().toString());
                intent1.putExtra(Constants.EXTRA_DATE, event_date.getText().toString());
                intent1.putExtra(Constants.EXTRA_DESC, event_description.getText().toString());
                intent1.putStringArrayListExtra(Constants.EXTRA_IMAGE_URIS, event_imageURIs);
                startActivityForResult(intent1 , Constants.EDIT_EVENT_DETAILS);
                break;
            case R.id.delete_event_full_detailed_page :
                AlertDialog.Builder builder1 = new AlertDialog.Builder(this);
                builder1.setMessage("Do you really want to delete this event ?");
                builder1.setCancelable(true);

                builder1.setPositiveButton(
                        "Yes",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                dialog.cancel();
                                mViewModel.deleteEvent(curr_Event);
                                finish();
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
