package com.dynasty.myapplication.ui.SecondTabEvent;

import android.os.Bundle;
import android.widget.EditText;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.StaggeredGridLayoutManager;
import com.dynasty.myapplication.R;
import com.dynasty.myapplication.adaptors.GiftListAdaptor;
import com.dynasty.myapplication.utils.Constants;
import com.dynasty.myapplication.viewmodel.EventViewModel;

import java.util.ArrayList;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link GiftManagementScreen#newInstance} factory method to
 * create an instance of this fragment.
 */
public class GiftManagementScreen extends Fragment {


    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    private StaggeredGridLayoutManager gridLayoutManager1,  gridLayoutManager2 ,  gridLayoutManager3 ;
    EventViewModel viewModel;
    ArrayList<String> giveGifts;
    ArrayList<String> doNotGiveGifts;
    ArrayList<String> myGifts;
    int currEventID;


    private String mParam1;
    private String mParam2;

    public GiftManagementScreen() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment GiftManagmentScreen.
     */

    public static GiftManagementScreen newInstance(String param1, String param2) {
        GiftManagementScreen fragment = new GiftManagementScreen();
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
        viewModel =  new ViewModelProvider(this).get(EventViewModel.class);
        currEventID = getArguments().getInt(Constants.EXTRA_ID);
        giveGifts = viewModel.getEventGiveGifts(currEventID);
        doNotGiveGifts = viewModel.getEventDoNotGiveGifts(currEventID);
        myGifts = viewModel.getMyGivenGifts(currEventID);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment

        return inflater.inflate(R.layout.fragment_gift_managment_screen, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view,  Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        RecyclerView recyclerViewGiftGive = (RecyclerView) view.findViewById(R.id.recylView_Gift_Give);
        recyclerViewGiftGive.setHasFixedSize(true);
        gridLayoutManager1 = new StaggeredGridLayoutManager(Math.min(giveGifts.size(), 3), StaggeredGridLayoutManager.VERTICAL);
        recyclerViewGiftGive.setLayoutManager(gridLayoutManager1);
        GiftListAdaptor giveGiftAdaptor = new GiftListAdaptor(requireActivity(), giveGifts , false);
        recyclerViewGiftGive.setAdapter(giveGiftAdaptor);


        RecyclerView recyclerViewGiftDoNotGive = (RecyclerView) view.findViewById(R.id.recylView_Gift_Donot_Give);
        recyclerViewGiftDoNotGive.setHasFixedSize(true);
        gridLayoutManager2 = new StaggeredGridLayoutManager(Math.min(doNotGiveGifts.size(), 3), StaggeredGridLayoutManager.VERTICAL);
        recyclerViewGiftDoNotGive.setLayoutManager(gridLayoutManager2);
        GiftListAdaptor doNotGiftAdaptor = new GiftListAdaptor(requireActivity(), doNotGiveGifts , false);
        recyclerViewGiftDoNotGive.setAdapter(doNotGiftAdaptor);



        RecyclerView recyclerViewMyGifts = (RecyclerView) view.findViewById(R.id.recyclVew_my_gifts);
        recyclerViewMyGifts.setHasFixedSize(true);
        gridLayoutManager3= new StaggeredGridLayoutManager(Math.min(myGifts.size(), 2), StaggeredGridLayoutManager.VERTICAL);
        recyclerViewMyGifts.setLayoutManager(gridLayoutManager3);
        GiftListAdaptor myGiftAdaptor = new GiftListAdaptor(requireActivity(), myGifts , true);
        recyclerViewMyGifts.setAdapter(myGiftAdaptor);

        myGiftAdaptor.setOnEventClickListener(new GiftListAdaptor.OnEventClickListener() {
            @Override
            public void onItemClick(int position) {
                myGifts.remove(position);
                myGiftAdaptor.notifyDataSetChanged();

            }
        });

        EditText ed = view.findViewById(R.id.et_enter_my_gift);
        view.findViewById(R.id.ib_add_my_gift_to_list).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                myGifts.add(ed.getText().toString());
                myGiftAdaptor.notifyDataSetChanged();
            }
        });




    }
}