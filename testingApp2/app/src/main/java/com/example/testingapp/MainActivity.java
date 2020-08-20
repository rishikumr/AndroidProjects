package com.example.testingapp;

import android.os.Bundle;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.View;

import android.view.Menu;
import android.view.MenuItem;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {
    private List<DataModel> dataModelList;
    RecyclerView recyclerView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

// Instantiate the recyclerView
        recyclerView = findViewById(R.id.recyclerView);

        recyclerView.setHasFixedSize(true);
        LinearLayoutManager layoutManager = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setFocusable(false);
        setData();
        setDataToRecyclerView();
    }

    private void setDataToRecyclerView() {
        RecyclerViewAdapter adapter = new RecyclerViewAdapter(dataModelList);
        recyclerView.setAdapter(adapter);

        recyclerView.addOnItemTouchListener(new RecyclerTouchListener(this, recyclerView, new RecyclerViewClickListener() {
            @Override
            public void onClick(View view, int position) {
                String firstName = dataModelList.get(position).getFirstName();
                String lastName = dataModelList.get(position).getLastName();
                String contactNo = dataModelList.get(position).getContactNo();
                int id = dataModelList.get(position).getId();
                Log.d("TestingApp", "onClick: " + position);
            }
        }));
    }

    private void setData() {
        dataModelList = new ArrayList<>();
        dataModelList.add(new DataModel(1, "Supun", "Ishara", "+94711234567"));
        dataModelList.add(new DataModel(2, "Jhon", "Smith", "+11711234567"));
        dataModelList.add(new DataModel(3, "Alexa", "Mori", "+23711234567"));
        dataModelList.add(new DataModel(4, "Kasun", "Weerasekara", "+61711234567"));
        dataModelList.add(new DataModel(5, "Ishara", "De Silva", "+91711234567"));
        dataModelList.add(new DataModel(6, "Sanath", "Weerawarana", "+67711234567"));
        dataModelList.add(new DataModel(7, "Jhone", "English", "+41711234567"));
        dataModelList.add(new DataModel(8, "April", "Beauty", "+32711234567"));
    }


}