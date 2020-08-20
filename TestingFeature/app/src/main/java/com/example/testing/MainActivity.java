package com.example.testing;

import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.constraintlayout.widget.ConstraintSet;

import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    Button add;
    int curr_id = 0;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mainqq);
        add = findViewById(R.id.addbutton);
        Button addbutton = findViewById(R.id.addbutton);
        addbutton.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
    if(v.getId() == R.id.addbutton){
        Log.d("Testing", "Preseed add");
        ConstraintLayout constraintLayout = findViewById(R.id.mainlayoutqq);
        setContentView(constraintLayout);
        TextView contactUs = new TextView(this);
        contactUs.setText("Demoooooooo Id");
        contactUs.setTextSize(25);
        contactUs.
        int new_curr_id= View.generateViewId();
        contactUs.setId(new_curr_id);
        ConstraintLayout.LayoutParams clpcontactUs = new ConstraintLayout.LayoutParams(
                ConstraintLayout.LayoutParams.WRAP_CONTENT, ConstraintLayout.LayoutParams.WRAP_CONTENT);
        contactUs.setLayoutParams(clpcontactUs);
        constraintLayout.addView(contactUs);

        ConstraintSet constraintSet = new ConstraintSet();
        constraintSet.clone(constraintLayout);
        constraintSet.connect(contactUs.getId(), ConstraintSet.TOP, constraintLayout.getId(), ConstraintSet.TOP, 30);
        constraintSet.connect(contactUs.getId(), ConstraintSet.LEFT, constraintLayout.getId(), ConstraintSet.LEFT, 18);
        constraintSet.connect(contactUs.getId(), ConstraintSet.TOP, curr_id, ConstraintSet.BOTTOM, 30);
        constraintSet.applyTo(constraintLayout);
        curr_id =new_curr_id;
    }
    }
}
