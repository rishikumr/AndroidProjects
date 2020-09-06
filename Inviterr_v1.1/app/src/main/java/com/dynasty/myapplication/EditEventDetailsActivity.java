package com.dynasty.myapplication;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.ViewCompat;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.Observer;
import androidx.viewpager2.widget.ViewPager2;

import com.tbuonomo.viewpagerdotsindicator.WormDotsIndicator;

import java.util.ArrayList;

public class EditEventDetailsActivity extends AppCompatActivity implements Imageutils.ImageAttachmentListener, View.OnClickListener {
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



    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // Log.d(TAG, "onCreate: EditEventDetailsActivity");
        setContentView(R.layout.edit_single_event_details_page);
        edit_event_name = findViewById(R.id.event_name_edit_detail_page);
        edit_event_location = findViewById(R.id.event_location_edit_detail_page);
        edit_event_description = findViewById(R.id.event_description_edit_detail_page);
        deleteImage = findViewById(R.id.delete_image_edit_detail_page);
        save_edit_details = findViewById(R.id.save_edit_details_page);
        deleteImage.setOnClickListener(this);
        save_edit_details.setOnClickListener(this);

        Intent inputIntent = getIntent();
        if(inputIntent.hasExtra(Constants.EXTRA_ID)){
            event_Id = inputIntent.getIntExtra(Constants.EXTRA_ID, 0);
            edit_event_name.setText(inputIntent.getStringExtra(Constants.EXTRA_NAME ));
            edit_event_location.setText(inputIntent.getStringExtra(Constants.EXTRA_LOCATION ));
            edit_event_date.setText(inputIntent.getStringExtra(Constants.EXTRA_DATE ));
            edit_event_description.setText(inputIntent.getStringExtra(Constants.EXTRA_DESC ));
            event_imageURIs = (inputIntent.getStringArrayListExtra(Constants.EXTRA_IMAGE_URIS ));
            mutable_event_imageURIs.setValue(event_imageURIs);

        }
        mutable_event_imageURIs.observe(this, new Observer<ArrayList<String>>() {
            @Override
            public void onChanged(ArrayList<String> strings) {
                imgAdaptor.updateImageURIs(strings);
            }
        });

        // AddImage Utils
        imageutils =new Imageutils(EditEventDetailsActivity.this);

        //Worm image indicator
        wormDotsIndicator = (WormDotsIndicator) findViewById(R.id.worm_dots_indicator_edit_page);
        event_imageURIs.add("android.resource://com.dynasty.myapplication/drawable/add_image_picture");

        imgAdaptor = new ImageViewPager2AdaptorCommon(event_imageURIs);
        viewPage = findViewById(R.id.imageViewPager_edit_page);
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
    protected void onPostResume() {
        super.onPostResume();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data)
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
                Intent returnIntent = new Intent();
                if(!edit_event_name.getText().toString().isEmpty()){
                    event_imageURIs.remove(event_imageURIs.size()-1);

                    returnIntent.putExtra(Constants.EXTRA_ID, event_Id);
                    returnIntent.putExtra(Constants.EXTRA_NAME, edit_event_name.getText().toString());
                    returnIntent.putExtra(Constants.EXTRA_LOCATION, edit_event_location.getText().toString());
                    returnIntent.putExtra(Constants.EXTRA_DATE, edit_event_date.getText().toString());
                    returnIntent.putExtra(Constants.EXTRA_DESC, edit_event_description.getText().toString());
                    returnIntent.putStringArrayListExtra(Constants.EXTRA_IMAGE_URIS, event_imageURIs);
                    setResult(Activity.RESULT_OK,returnIntent);
                }else{
                    setResult(Activity.RESULT_CANCELED,returnIntent);
                }
                finish();
                break;
        }

    }
}
