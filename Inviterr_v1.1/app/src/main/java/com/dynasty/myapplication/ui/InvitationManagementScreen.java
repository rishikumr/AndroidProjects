package com.dynasty.myapplication.ui;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.PersistableBundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ExpandableListView;

import com.dynasty.myapplication.R;
import com.dynasty.myapplication.adaptors.ExpandableListAdapter;
import com.dynasty.myapplication.adaptors.InvitationListAdaptor;
import com.dynasty.myapplication.entity.People;
import com.dynasty.myapplication.viewmodel.EventViewModel;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link InvitationManagementScreen#newInstance} factory method to
 * create an instance of this fragment.
 */
public class InvitationManagementScreen extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    ExpandableListView expandableListView;
    ExpandableListAdapter expandableListAdapter;
    ArrayList<String> expandableListTitle;
    ArrayList<People> guestInvitationList;
    HashMap<String, List<String>> expandableListDetail;
    int totalAcceptedPeople =7;
    int totalDeclinedPeople =1;
    int totalNotResponded = 2;
    int totalPeopleSentTo =10;

    EventViewModel mViewModel;
    RecyclerView recyclerView;


    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    ExpandableListView exList;

    public InvitationManagementScreen() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment InvitationManagementScreen.
     */
    // TODO: Rename and change types and number of parameters
    public static InvitationManagementScreen newInstance(String param1, String param2) {
        InvitationManagementScreen fragment = new InvitationManagementScreen();
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
        mViewModel = new ViewModelProvider(this).get(EventViewModel.class);
        guestInvitationList = mViewModel.getCurrentGuestInvitationList();

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_invitation_management_screen, null, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        setupExpandableList(view);
        InvitationListAdaptor invitationListAdaptor = new InvitationListAdaptor(guestInvitationList);
        recyclerView = view.findViewById(R.id.invitation_list_rcv);
        recyclerView.setHasFixedSize(true);
        recyclerView.setAdapter(invitationListAdaptor);
        LinearLayoutManager layoutManager = new LinearLayoutManager(requireActivity(), LinearLayoutManager.VERTICAL, false);
        recyclerView.setLayoutManager(layoutManager);

    }

    private void setupInvitationStatusList( View view) {



    }

    private void setupExpandableList(View view) {
        expandableListView = (ExpandableListView) view.findViewById(R.id.expand_invitation_info);
        expandableListDetail = getExpandableListData();
        expandableListTitle = new ArrayList<String>(expandableListDetail.keySet());
        expandableListAdapter = new ExpandableListAdapter(requireActivity(), expandableListTitle, expandableListDetail);
        expandableListView.setAdapter(expandableListAdapter);
    }


    public  HashMap<String, List<String>> getExpandableListData() {
        HashMap<String, List<String>> expandableListDetail = new HashMap<String, List<String>>();

        List<String> dashboard = new ArrayList<String>();
        dashboard.add(" Accepted = "+ totalAcceptedPeople);
        dashboard.add(" Declined = " + totalDeclinedPeople);
        dashboard.add(" No Response = "+ totalNotResponded);
        dashboard.add(" Sent to = "+ totalPeopleSentTo);


        expandableListDetail.put("Dashboard", dashboard);

        return expandableListDetail;
    }
}

