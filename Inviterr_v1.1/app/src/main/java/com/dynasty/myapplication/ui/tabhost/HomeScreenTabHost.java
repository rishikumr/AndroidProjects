package com.dynasty.myapplication.ui.tabhost;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.ShareActionProvider;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.viewpager2.widget.ViewPager2;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.dynasty.myapplication.R;
import com.dynasty.myapplication.adaptors.HomeScreenTabLayoutVP2Adaptor;
import com.dynasty.myapplication.utils.Constants;
import com.google.android.material.tabs.TabLayout;
import com.google.android.material.tabs.TabLayoutMediator;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link HomeScreenTabHost#newInstance} factory method to
 * create an instance of this fragment.
 */
public class HomeScreenTabHost extends Fragment {


    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    HomeScreenTabLayoutVP2Adaptor tabLayoutVP2Adaptor;
    ViewPager2 viewPager;
    NavController navController;
    ActionBar ab;
    ShareActionProvider mShareActionProvider;


    private String mParam1;
    private String mParam2;

    public HomeScreenTabHost() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment HomeScreen.
     */

    public static HomeScreenTabHost newInstance(String param1, String param2) {
        HomeScreenTabHost fragment = new HomeScreenTabHost();
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
        return inflater.inflate(R.layout.fragment_home_screen_tabhost, container, false);
    }
    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {


        HomeScreenTabLayoutVP2Adaptor tabLayoutVP2Adaptor  = new HomeScreenTabLayoutVP2Adaptor(requireActivity());
        viewPager = view.findViewById(R.id.viewPager_HomeScreen);
        viewPager.setAdapter(tabLayoutVP2Adaptor);
        TabLayout tabLayout = view.findViewById(R.id.tabLayout_HomeScreen);
        new TabLayoutMediator(tabLayout, viewPager, new TabLayoutMediator.TabConfigurationStrategy() {
            @Override
            public void onConfigureTab(@NonNull TabLayout.Tab tab, int position) {
                if(position == 0){  tab.setText(Constants.HomeScreen_Tab1_Name);tab.setIcon(R.drawable.profile);}
                if(position == 1){  tab.setText(Constants.HomeScreen_Tab2_Name);tab.setIcon(R.drawable.invitations);  }
            }
        }).attach();

        navController = Navigation.findNavController(requireActivity(), R.id.nav_host_fragment);

        ActionBar ab =((AppCompatActivity)requireActivity()).getSupportActionBar();
        ab.setDisplayHomeAsUpEnabled(false);
        //requireActivity().invalidateOptionsMenu();
        ab.setTitle(R.string.app_name);
    }


    @Override
    public void onCreateOptionsMenu(@NonNull Menu menu, @NonNull MenuInflater inflater) {
        Log.d(Constants.LOG_TAG, "onCreateOptionsMenu:  MainTabHost " + menu.size());
        requireActivity().getMenuInflater().inflate(R.menu.menu_main_activity_items, menu);
        requireActivity().getMenuInflater().inflate(R.menu.menu_home_screen_rcv, menu);
        }

    @Override
    public void onPrepareOptionsMenu(@NonNull Menu menu) {
        Log.d(Constants.LOG_TAG, "onPrepareOptionsMenu: MainTabHost " + menu.size());
    }
    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case R.id.menu_exit:
                requireActivity().finish();
                System.exit(0);
                return true;

            case R.id.menu_help:
                Toast.makeText(requireActivity(), "Lol! There is no help.", Toast.LENGTH_SHORT).show();
                return true;

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