package com.example.rishikumar.m_share;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ToggleButton;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.HashMap;

import static android.app.Activity.RESULT_OK;


public class tab3 extends Fragment implements View.OnClickListener {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;
    private Switch fire;
    private Button Signout;
    private OnFragmentInteractionListener mListener;
    private FirebaseUser mUser;
    private FirebaseAuth mAuth;
    private TextView UserInfo;
    private DatabaseReference mFireData;
    private static Boolean isLoggedIn = false;
    private ListView listView;
    private ArrayList<UserMangaer> UserList;
    static UserListAdaptor userListAdaptor;
    private static Boolean isShared = false;


    public tab3() {
        // Required empty public constructor
    }


    // TODO: Rename and change types and number of parameters
    public static tab3 newInstance(String param1, String param2) {
        tab3 fragment = new tab3();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onStart() {

        super.onStart();
        try {
            Thread.sleep(50);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        SetLoggedInUserName();
        Log.d("Tagg", "I am on onStart()");

    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
        // Log.d("Tagg", "I am on onCreate()");
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);


        fire = (Switch) view.findViewById(R.id.firebasebtn);
        fire.setOnClickListener(this);
        Signout = (Button) view.findViewById(R.id.sign_out);
        Signout.setVisibility(View.GONE);
        Signout.setOnClickListener(this);
        UserInfo = (TextView) view.findViewById(R.id.UserInfo);
        mAuth = FirebaseAuth.getInstance();
        listView = (ListView) view.findViewById(R.id.shareList);


        UserList = new ArrayList<UserMangaer>() {        };

        userListAdaptor = new UserListAdaptor(getContext(), UserList);
        listView.setAdapter(userListAdaptor);
        UserList.clear();
        userListAdaptor.notifyDataSetChanged();
        SetLoggedInUserName();



      //Log.d("Tagg", "I am on onViewCreated()");
    }

    private void UpdateList() {

        FirebaseAuth mAuth = FirebaseAuth.getInstance();

        FirebaseUser currentUser = mAuth.getCurrentUser();

        if (currentUser != null) {

            DatabaseReference rootRef = FirebaseDatabase.getInstance().getReference();

            DatabaseReference userRef = rootRef.child("User_Accounts");


            userRef.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {

                    UserList.clear();

                    for (DataSnapshot ds : dataSnapshot.getChildren()) {
                        String name = ds.child("Name").getValue(String.class);
                        String current_Song = ds.child("Current_Song").getValue(String.class);



                        UserMangaer person = new UserMangaer(name, current_Song);

                        if (/*(current_Song.equalsIgnoreCase("Online (No Song Playing)")) ||*/ (current_Song.equals("Offline"))) {
                         //   Log.d("Tagg", "Skkipping bcoz for :"+ name + "Current song is :" + current_Song);
                        } else {
                            Log.d("Tagg", "i added  " + name+ " with " + current_Song);
                            UserList.add(person);
                        }


                    }

                    userListAdaptor.notifyDataSetChanged();
                }


                @Override
                public void onCancelled(DatabaseError databaseError) {

                }
            });


        }
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
       // Log.d("Tagg", "I am on onViewCreating()");
        {

        }
        return inflater.inflate(R.layout.tab3, container, false);

    }

    // TODO: Rename method, update argument and hook method into UI event
    public void onButtonPressed(Uri uri) {
        if (mListener != null) {
            mListener.onFragmentInteraction(uri);
        }
    }

    @Override
    public void onClick(View view) {

        switch (view.getId()) {

            case R.id.firebasebtn:

                ShareNowPressd();

                break;


            case R.id.sign_out:

                fire.setChecked(false);
                isLoggedIn = false;
                UserInfo.setText("To use this feature you must be signed in . \n Tap on \"SHARE button\" to proceed.");
                Signout.setVisibility(View.GONE);
                UpdateCurrentPlaying("Offline", true);
                UserList.clear();
                userListAdaptor.notifyDataSetChanged();
                FirebaseAuth.getInstance().signOut();
                Log.d("Tagg", "You have been logged out ");

                break;


        }

    }

    private void ShareNowPressd() {

        if (MainActivity.isInternetConnected(getContext()) == true) {
            FirebaseUser currentUser = mAuth.getCurrentUser();

            if (currentUser == null) {
                isShared = false;
                fire.setChecked(false);

                Log.d("Tagg", "You are not logged In and isShared is :" + isShared);
                Toast.makeText(getContext(), "\tYou are not Logged In.\n\t Do Log In or Sign Up", Toast.LENGTH_SHORT).show();
                Intent startLoginorSignup = new Intent(getContext(), SignUporLogInActivity.class);
                startActivityForResult(startLoginorSignup, 99);


            } else if (isShared == true) {

                isShared = false;
                fire.setChecked(false);
                UserList.clear();
                userListAdaptor.notifyDataSetChanged();
                UpdateCurrentPlaying("Offline", true);


            } else {



                Log.d("Tagg", "You have logged In as :" + currentUser.getUid().toString());
                isLoggedIn = true;
                isShared = true;
                String curr = MainActivity.getCurrentsong();
                UpdateCurrentPlaying(curr, false);

                UpdateList();


            }
        }
        else{
            Toast.makeText(getContext(), "You are not connected to Internet", Toast.LENGTH_SHORT).show();
            try {
                Thread.sleep(200);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            isShared = false;
            fire.setChecked(false);
        }
    }



    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        // Check which request we're responding to
        if (requestCode == 99) {
            // Make sure the request was successful
            if (resultCode == RESULT_OK) {
                Log.d("Tagg ", " SignIn or LogIn activity finished successfully.");
                Log.d("Firebsae Tagg ", "You have logged In  ");
                isLoggedIn = true;
                String curr = MainActivity.getCurrentsong();
                UpdateCurrentPlaying(curr, false);
                // SetLoggedInUserName();

            }
        }
    }


    private void SetLoggedInUserName() {
        AsyncTaskRunner runner = new AsyncTaskRunner();
        runner.execute();


    }

    @Override
    public void onStop() {
        super.onStop();

    }

    public static void UpdateCurrentPlaying(String title, boolean ShareOff) {
        //   Log.d("TAG", "I am updating Playing List");


        if ((isLoggedIn == true && title != null && isShared == true) || (ShareOff == true)) {
            final String CurrentPlaying = title;

            FirebaseUser curr_user = FirebaseAuth.getInstance().getCurrentUser();
            String curr_Uid = curr_user.getUid();

            HashMap<String, Object> result = new HashMap<>();
            result.put("Current_Song", CurrentPlaying);

            DatabaseReference mFireData = FirebaseDatabase.getInstance().getReference().child("User_Accounts").child(curr_Uid);


            mFireData.updateChildren(result).addOnCompleteListener(new OnCompleteListener<Void>() {
                @Override
                public void onComplete(@NonNull com.google.android.gms.tasks.Task<Void> task) {


                    Log.d("TAG", "Updated current playing song = " + CurrentPlaying);
                }
            });


        }


    }

    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        void onFragmentInteraction(Uri uri);
    }


    private class AsyncTaskRunner extends AsyncTask<String, String, String> {
        private String resp;
        ProgressDialog progressDialog;


        @Override
        protected String doInBackground(String... params) {
            mAuth = FirebaseAuth.getInstance();
            FirebaseUser currentUser = mAuth.getCurrentUser();
            if (currentUser != null) {
                try {



                    mUser = FirebaseAuth.getInstance().getCurrentUser();


                    String curr_uid = currentUser.getUid();
                    mFireData = FirebaseDatabase.getInstance().getReference().child("User_Accounts").child(curr_uid);


                    mFireData.addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(DataSnapshot dataSnapshot) {
                        //   Log.d("Tagg", dataSnapshot.toString());
                            if (dataSnapshot != null) {
                                if ((dataSnapshot.child("Name").getValue().toString() != null) && (
                                        dataSnapshot.child("Current_Song").getValue().toString() != null)) {
                                    String Name = dataSnapshot.child("Name").getValue().toString();
                                    //String Current_Song = dataSnapshot.child("Current_Song").getValue().toString();
                                  //  Log.d("Tagg", " SetLoggedInUser : You are logged in as  :" + Name /*+ " and playing : " + Current_Song*/);

                                    UserInfo.setText("Welcome ,  " + Name + " !");
                                    Signout.setVisibility(View.VISIBLE);


                                }
                            }

                        }

                        @Override
                        public void onCancelled(DatabaseError databaseError) {

                        }
                    });


                } catch (Exception e) {
                    e.printStackTrace();
                }


            }
            return null;
        }


        @Override
        protected void onPostExecute(String result) {


        }


        @Override
        protected void onPreExecute() {

        }


        @Override
        protected void onProgressUpdate(String... text) {


        }
    }
}
