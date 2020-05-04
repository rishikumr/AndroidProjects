package com.example.rishikumar.redhogg;

import android.content.Intent;
import android.graphics.Color;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import static com.example.rishikumar.redhogg.R.id.r3;
import static com.example.rishikumar.redhogg.R.id.r5;
import static com.example.rishikumar.redhogg.R.id.radio;
import static com.example.rishikumar.redhogg.R.id.radio1;

import static com.example.rishikumar.redhogg.R.id.vi1;

public class MainActivity extends AppCompatActivity implements AdapterView.OnItemSelectedListener  {

    String[] lists={"Default","20","40","60","80","100"};
    TextView v1;
    private Toast mToast;



    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        final EditText e1 = (EditText) findViewById(R.id.ed1);
        Button b1 = (Button) findViewById(R.id.bt1);

         v1 = (TextView)findViewById(R.id.vi1);
        final float f5= v1.getTextSize();
        //Getting the instance of Spinner and applying OnItemSelectedListener on it
        Spinner spin = (Spinner) findViewById(R.id.spinner1);
        spin.setOnItemSelectedListener(this);

        //Creating the ArrayAdapter instance having the bank name list

        // ArrayAdapter aa = new ArrayAdapter(this,android.R.layout.simple_spinner_dropdown_item,);
        ArrayAdapter aa = new ArrayAdapter(this,android.R.layout.simple_spinner_item,lists);
        aa.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
//Setting the ArrayAdapter data on the Spinner
        spin.setAdapter(aa);






        b1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                v1.setText(e1.getText().toString());
                v1.setTextSize((float) 50);
                v1.setBackgroundColor(Color.WHITE);
              //  v1.setTextColor(Color.BLACK);
            }
        });





    }
    public void onRadioButtonClicked(View view) {
        // Is the button now checked?
        boolean checked = ((RadioButton) view).isChecked();

        final TextView v1 = (TextView)findViewById(R.id.vi1);

        switch(view.getId()) {

            case R.id.r1:
                if (checked)

                    v1.setTextColor(Color.RED);
                showAToast ("You selected color: RED");

                    break;
            case R.id.r2:
                if (checked)

                    v1.setTextColor(Color.GREEN);
                showAToast ("You selected color: GREEN");                   break;

            case r3:
                if (checked)
                    v1.setTextColor(Color.BLUE);

                showAToast ("You selected color: BLUE");                break;
            case R.id.r4:
                if (checked)

                    v1.setTextColor(Color.BLACK);
                showAToast ("You selected color: BLACK");                break;
            case r5:
                if (checked)

                    v1.setTextColor(Color.CYAN);
                showAToast ("You selected color: CYAN");                break;
            case R.id.r6:
                if (checked)
                    v1.setTextColor(Color.YELLOW);

                showAToast ("You selected color: YELLOW");                break;
            case R.id.r7:
                if (checked)
                    v1.setTextColor(Color.MAGENTA);
                showAToast ("You selected color: MAGENTA");

                break;
            case R.id.r8:
                if (checked)
                    v1.setTextColor(Color.WHITE);
                showAToast ("You selected color: WHITE");

                break;

        }
    }


    //Performing action onItemSelected and onNothing selected
    @Override
    public void onItemSelected(AdapterView<?> arg0, View arg1, int position,long id) {


        if(position!=0) {
            showAToast ("You selected size "+lists[position]);
            float f = Float.parseFloat(lists[position]);
            v1.setTextSize(f);
        }
    }

    @Override
    public void onNothingSelected(AdapterView<?> arg0) {
        // TODO Auto-generated method stub

    }
    public void showAToast (String message){
        if (mToast != null) {
            mToast.cancel();
        }
        mToast = Toast.makeText(this, message, Toast.LENGTH_SHORT);
        mToast.show();
    }


}
