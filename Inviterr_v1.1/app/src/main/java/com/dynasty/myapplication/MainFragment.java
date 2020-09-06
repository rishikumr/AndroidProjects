package com.dynasty.myapplication;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link MainFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class MainFragment extends Fragment  implements View.OnClickListener  {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    public static final String TAG = Constants.LOG_TAG;
    Button testing_button;
    Button login_button;
    TextView register_button;
    NavController navController;
    String currToken ;

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public MainFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment MainFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static MainFragment newInstance(String param1, String param2) {
        MainFragment fragment = new MainFragment();
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

        SharedPreferences pref = requireActivity().getSharedPreferences(Constants.MyPref, 0); // 0 - for private mode
        currToken = pref.getString("mUserToken", null);
        //Log.i(TAG, " MainActivity : currToken="+ currToken);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_main, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        navController = Navigation.findNavController(requireActivity(), R.id.nav_host_fragment);
        if(currToken != null){
            navController.navigate(R.id.action_mainFragment_to_eventHomeScreenFragment);
        }else{
            testing_button = requireActivity().findViewById(R.id.testing_button);
            login_button = requireActivity().findViewById(R.id.login_button);
            register_button = requireActivity().findViewById(R.id.register_button);

            testing_button.setOnClickListener( this);
            login_button.setOnClickListener( this);
            register_button.setOnClickListener( this);
        }

    }


    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.testing_button:
                navController.navigate(R.id.action_mainFragment_to_testingFragment);
                break;
            case R.id.login_button:
                navController.navigate(R.id.action_mainFragment_to_login_fragment);
                break;
            case R.id.register_button:
                navController.navigate(R.id.action_mainFragment_to_requestOTPFragment);
                break;

        }
    }
}