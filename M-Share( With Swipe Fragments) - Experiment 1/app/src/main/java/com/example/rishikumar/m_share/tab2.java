package com.example.rishikumar.m_share;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.ToggleButton;

import static android.content.ContentValues.TAG;
import static android.content.Context.MODE_PRIVATE;


/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link tab2.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link tab2# newInstance} factory method to
 * create an instance of this fragment.
 */
public class tab2 extends Fragment implements View.OnClickListener {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    public static final String ARG_PARAM1 = "param1";
    public static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    public String mParam1;
    public String mParam2;
    public ImageButton ib1;
    public ToggleButton tb1;
    public TextView tv12, tv22, tv32;
    public Boolean isPressed = false;
    public OnFragmentInteractionListener mListener;
    public LayoutInflater thisinflater;
    public ViewGroup thiscontainer;
    public MainActivity activity;


    public tab2() {
        // Required empty public constructor
    }


    public static tab2 newInstance(String param1, String param2) {
        tab2 fragment = new tab2();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    // TODO: Rename and change types and number of parameters


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

        if (getActivity() instanceof MainActivity) {
            activity = (MainActivity) getActivity();
        }


        thiscontainer = container;
        thisinflater = inflater;
        View thview = thisinflater.inflate(R.layout.tab2, thiscontainer, false);


        return thview;
    }

    // TODO: Rename method, update argument and hook method into UI event
    public void onButtonPressed(Uri uri) {
        if (mListener != null) {
            mListener.onFragmentInteraction(uri);
        }
    }


    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {

        tb1 = (ToggleButton) view.findViewById(R.id.tb1);
        tb1.setOnClickListener(this);
        ib1 = (ImageButton) view.findViewById(R.id.ib1);
        ib1.setOnClickListener(this);
        tv12 = (TextView) view.findViewById(R.id.tv1);
        tv22 = (TextView) view.findViewById(R.id.tv2);
        tv32 = (TextView) view.findViewById(R.id.tv3);

        SharedPreferences sh1 = this.getActivity().getSharedPreferences("MyPref22", Context.MODE_PRIVATE);
        isPressed = sh1.getBoolean("ispressed", true);
        infopressed();
        tv12.setText(sh1.getString("title", "Title"));
        tv22.setText(sh1.getString("album", "Album"));
        tv32.setText(sh1.getString("artist", "Artist"));
        tv12.setTextColor(Color.WHITE);
        tv22.setTextColor(Color.WHITE);
        tv32.setTextColor(Color.WHITE);

        if (sh1.getBoolean("ValidURI", false)==true) {
            Uri imgageuri = Uri.parse(sh1.getString("albummart", "null"));
            ib1.setImageURI(imgageuri);

        } else {
            ib1.setImageResource(R.drawable.noalbum1);
        }

    }

    @Override
    public void onStop() {
        super.onStop();
        SharedPreferences sh1 = this.getActivity().getSharedPreferences("MyPref22", Context.MODE_PRIVATE);
        SharedPreferences.Editor ed1 = sh1.edit();
        ed1.putBoolean("ispressed", !isPressed);

        ed1.commit();

    }

    @Override
    public void onClick(View view) {

        switch (view.getId()) {

            case R.id.tb1:

                infopressed();

                break;

        }
    }

    private void infopressed() {

       // Log.d("taggg", "You pressed info button");
        if (isPressed == false) {
            tv12.setVisibility(View.VISIBLE);
            tv22.setVisibility(View.VISIBLE);
            tv32.setVisibility(View.VISIBLE);


            isPressed = true;
            tb1.setChecked(true);
        } else {
            tv12.setVisibility(View.GONE);
            tv22.setVisibility(View.GONE);
            tv32.setVisibility(View.GONE);
            isPressed = false;
        }


    }


    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        void onFragmentInteraction(Uri uri);
    }


}
