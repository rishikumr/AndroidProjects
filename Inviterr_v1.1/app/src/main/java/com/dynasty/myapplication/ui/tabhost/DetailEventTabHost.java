package com.dynasty.myapplication.ui.tabhost;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.ShareActionProvider;
import androidx.core.view.MenuItemCompat;
import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.viewpager2.widget.ViewPager2;
import com.dynasty.myapplication.R;
import com.dynasty.myapplication.adaptors.DetailEvent_TabLayoutVP2Adaptor;
import com.dynasty.myapplication.utils.Constants;
import com.google.android.material.tabs.TabLayout;
import com.google.android.material.tabs.TabLayoutMediator;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link DetailEventTabHost#newInstance} factory method to
 * create an instance of this fragment.
 */
public class DetailEventTabHost extends Fragment {

    //  Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    DetailEvent_TabLayoutVP2Adaptor tabLayoutVP2Adaptor;
    ViewPager2 viewPager;
    boolean isCreator;
    int event_ID; String event_name;
    NavController navController;
    ShareActionProvider mShareActionProvider;


    //  Rename and change types of parameters
    private String mParam1;
    private String mParam2;
    public static final String TAG = Constants.LOG_TAG;

    public DetailEventTabHost() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment DetailEvent.
     */
    //  Rename and change types and number of parameters
    public static DetailEventTabHost newInstance(String param1, String param2) {
        DetailEventTabHost fragment = new DetailEventTabHost();
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
        setHasOptionsMenu(true);
        return inflater.inflate(R.layout.fragment_detail_event_tabhost, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        navController = Navigation.findNavController(requireActivity(), R.id.nav_host_fragment);
        if (getArguments() != null) {
            event_ID= getArguments().getInt(Constants.EXTRA_ID);
            isCreator = getArguments().getBoolean(Constants.IS_CREATOR);
            event_name = getArguments().getString(Constants.EXTRA_NAME);
        }
        DetailEvent_TabLayoutVP2Adaptor tabLayoutVP2Adaptor  = new DetailEvent_TabLayoutVP2Adaptor(requireActivity() , isCreator , event_ID);
        viewPager = view.findViewById(R.id.viewPager_EventDetailScreen);
        viewPager.setAdapter(tabLayoutVP2Adaptor);
        TabLayout tabLayout = view.findViewById(R.id.tabLayout_EventDetailScreen);
        new TabLayoutMediator(tabLayout, viewPager, new TabLayoutMediator.TabConfigurationStrategy() {
            @Override
            public void onConfigureTab(@NonNull TabLayout.Tab tab, int position) {
                if(position == 0){  tab.setText(Constants.EventDetailScreen_Tab1_Name); }
                if(position == 1){  tab.setText(isCreator == false ? Constants.EventDetailScreen_Tab2_1_Name : Constants.EventDetailScreen_Tab2_2_Name );}
                if(position == 2){   tab.setText(isCreator == false ? Constants.EventDetailScreen_Tab3_1Name : Constants.EventDetailScreen_Tab3_2Name );}
            }
        }).attach();

        ActionBar ab =((AppCompatActivity)getActivity()).getSupportActionBar();
        ab.setDisplayHomeAsUpEnabled(true);
        ab.setTitle(event_name);
    }




    @Override
    public void onCreateOptionsMenu(@NonNull Menu menu , @NonNull MenuInflater inflater) {
        requireActivity().getMenuInflater().inflate(R.menu.menu_event_info_screen, menu);
        // Set file with share history to the provider and set the share intent.
        MenuItem mShare = menu.findItem(R.id.menu_event_share);
        Log.d(TAG, "onCreateOptionsMenu: EventInfoScreen " + menu.size() + "  mShare"+mShare);
        mShareActionProvider = (ShareActionProvider) MenuItemCompat.getActionProvider(mShare);
        mShareActionProvider.setShareHistoryFileName(null   );
        // Note that you can set/change the intent any time,say when the user has selected an image.
        Intent shareIntent = new Intent(Intent.ACTION_SEND);
        shareIntent.setType("text/plain");
        shareIntent.putExtra(Intent.EXTRA_TEXT, "http://google.com");
        mShareActionProvider.setShareIntent(shareIntent);

    }

    @Override
    public void onPrepareOptionsMenu(@NonNull Menu menu) {
        //super.onPrepareOptionsMenu(menu);
        Log.d(TAG, "onPrepareOptionsMenu: EventInfoScreen " + menu.size());
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {

        switch (item.getItemId()) {
            case android.R.id.home:
                navController.navigateUp();
                return true;

            case R.id.menu_refresh:
                Toast.makeText(requireActivity(), "This feature will be available soon.", Toast.LENGTH_SHORT).show();
                return true;

            default:
                // If we got here, the user's action was not recognized.Invoke the superclass to handle it.
                return super.onOptionsItemSelected(item);

        }

    }




}