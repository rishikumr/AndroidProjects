package com.example.rishikumar.expensediary;



import android.content.Context;
import android.database.Cursor;
import android.graphics.Color;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.TextView;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {

    String buds;
    String exps;
    String bals;

    long no1=0;
    long no2=0;
    long no3=0;
    long no4=0;
    long bal=0;
    long sum=0;
    long bud;
    long exp=0;

    long no1o=0;
    long no2o=0;
    long no3o=0;
    long no4o=0;
    long balo=0;
    long sumo=0;
    long budo;
    long expo=0;

    Boolean flag=false;
    Boolean r11=false;
    Boolean r12=false;
    Boolean r13=false;
    Boolean r14=false;
    DatabaseHelper myDb;
    Context context ;




    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        context = getApplicationContext();
        final TextView tv4 = (TextView) findViewById(R.id.tv4);
        final TextView tv3 = (TextView) findViewById(R.id.tv3);
        final EditText ed1 = (EditText) findViewById(R.id.ed1);
        final EditText ed2 = (EditText) findViewById(R.id.ed2);
        Button rset = (Button) findViewById(R.id.clear);

        final TextView p1 = (TextView) findViewById(R.id.p1);
        final TextView p2 = (TextView) findViewById(R.id.p2);
        final TextView p3 = (TextView) findViewById(R.id.p3);
        final TextView p4 = (TextView) findViewById(R.id.p4);
        final Button bt1 = (Button) findViewById(R.id.bt1);
        Button undo = (Button) findViewById(R.id.undo);

        myDb = new DatabaseHelper(this);
        Log.d("ERROR", "No table");



      //  ::::::::::::::::::::::::::::::::::::::::::::


        Cursor cu1 = myDb.getalldata();
        int v= cu1.getCount();
        Log.d("v vaule is ::::::: ", Integer.toString(v));










       // ::::::::::::::::::::::::::::::::::::::::::::::::::::
       // myDb.deleteData(0);
       // myDb.deleteData(1);
        myDb.insertData(no1, no2, no3, no4, bud, bal,no1o, no2o, no3o, no4o, budo, balo);
        myDb.deleteData(2);


        Log.d("ERROR", "Aded first row");


        Cursor cu = myDb.getalldata();
        if (cu.getColumnCount() == 0) {
            Log.d("ERROR", "EROOR");
        } else {
            Log.d("onstop", "retriving Data: nuumber of rows  "+cu.getCount());

            cu.moveToFirst();
            {
                Log.d("no1", cu.getString(1));
               no1= Long.parseLong(cu.getString(1)) ;
                Log.d("no2", cu.getString(2));
               no2= Long.parseLong(cu.getString(2)) ;
                Log.d("no3", cu.getString(3));
                no3= Long.parseLong(cu.getString(3)) ;
                Log.d("no4", cu.getString(4));
                no4= Long.parseLong(cu.getString(4)) ;
                Log.d("bud", cu.getString(5));
                bud= Long.parseLong(cu.getString(5)) ;
                Log.d("bal", cu.getString(6));
               bal= Long.parseLong(cu.getString(6)) ;
                Log.d("no1o", cu.getString(7));
                no1o= Long.parseLong(cu.getString(7)) ;
                Log.d("no2o", cu.getString(8));
                no2o= Long.parseLong(cu.getString(8)) ;
                Log.d("no3o", cu.getString(9));
                no3o= Long.parseLong(cu.getString(9)) ;
                Log.d("no4o", cu.getString(10));
                no4o= Long.parseLong(cu.getString(10)) ;
                Log.d("budo", cu.getString(11));
                budo= Long.parseLong(cu.getString(11)) ;
             //  Log.d("balo", cu.getString(12));
           // balo= Long.parseLong(cu.getString(12)) ;
              //  balo= Long.parseLong(cu.getString(13)) ;



            }
        }
        Log.d("ia am here", "I am here..");

        {

            sum = no1 + no2 + no3 + no4;
            tv3.setText(Long.toString(sum));
            p1.setText(cu.getString(1));
            p2.setText(cu.getString(2));
            p3.setText(cu.getString(3));
            p4.setText(cu.getString(4));
            ed1.setText(cu.getString(5));
            tv4.setText(cu.getString(6));


            if ((bal) < 0) {
                tv4.setTextColor(Color.rgb(205, 62, 45));
            } else {
                tv4.setTextColor(Color.rgb(34, 139, 34));
            }
        }
        if(no1==0){p1.setText("--");}
        if(no2==0){p2.setText("--");}
        if(no3==0){p3.setText("--");}
        if(no4==0){p4.setText("--");}



        undo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Cursor  cu = myDb.getalldata();
                if (cu.getColumnCount() == 0) {
                    Log.d("ERROR", "EROOR undo");
                } else {
                    Log.d("onstop", "retriving Data when undo is pressed:");

                    cu.moveToFirst();
                    {       sumo = no1o+no2o+no3o+no4o;
                            //Long.parseLong(cu.getString(1))+Long.parseLong(cu.getString(2))+Long.parseLong(cu.getString(3)+Long.parseLong(cu.getString(4));
                       // Log.d("no1", cu.getString(1));
                        no1= Long.parseLong(cu.getString(7)) ;
                      //  Log.d("no2", cu.getString(2));
                        no2= Long.parseLong(cu.getString(8)) ;
                      //  Log.d("no3", cu.getString(3));
                        no3= Long.parseLong(cu.getString(9)) ;
                      //  Log.d("no4", cu.getString(4));
                        no4= Long.parseLong(cu.getString(10)) ;
                      //  Log.d("bud", cu.getString(5));
                        bud= Long.parseLong(cu.getString(11)) ;
                      //  Log.d("bal", cu.getString(6));
                      //  bal= (bud-no1+no2+no3+no4);
                        Log.d("no1o", cu.getString(7));
                     //   no1o= Long.parseLong(cu.getString(7)) ;
                        Log.d("no2o", cu.getString(8));
                       // no2o= Long.parseLong(cu.getString(8)) ;
                        Log.d("no3o", cu.getString(9));
                      //  no3o= Long.parseLong(cu.getString(9)) ;
                        Log.d("no4o", cu.getString(10));
                       // no4o= Long.parseLong(cu.getString(10)) ;
                        Log.d("budo", cu.getString(11));
                        //budo= Long.parseLong(cu.getString(11)) ;
                         Log.d("balo", cu.getString(12));
                       // balo= Long.parseLong(cu.getString(12)) ;


                    }
                }

              {
                    p1.setText(Long.toString(no1));
                    p2.setText(Long.toString(no2));
                    p3.setText(Long.toString(no3));
                    p4.setText(Long.toString(no4));


                    sum = no1 + no2 + no3 + no4;
                    bal = bud - sum;

                    tv3.setText(Long.toString(sum));
                    ed1.setText(Long.toString(bud));
                    tv4.setText(Long.toString(bal));


                }


                if(no1==0){p1.setText("--");}
                if(no2==0){p2.setText("--");}
                if(no3==0){p3.setText("--");}
                if(no4==0){p4.setText("--");}

                myDb.updateData(1,no1, no2, no3, no4, bud, bal,no1o, no2o, no3o, no4o, budo, balo);




            }
        });






        rset.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                no1=0;
                no2=0;
                no3=0;
                no4=0;
                bud=0;
                bal=0;
                buds="";
                exps="";
                bals="";
                no1o=0;
                no2o=0;
                no3o=0;
                no4o=0;
                budo=0;
                balo=0;
                expo=0;

                sum=0;
                ed1.setText("");
                ed2.setText("");
                tv4.setText("Balance");
                p1.setText("--");
                p2.setText("--");
                p3.setText("--");
                p4.setText("--");

                tv3.setText("--");
               myDb.updateData(1,no1, no2, no3, no4, bud, bal,no1o, no2o, no3o, no4o, budo, balo);
                if((bal)<0)
                {
                    tv4.setTextColor(Color.rgb( 205, 62, 45));
                }
                else{tv4.setTextColor(Color.rgb(34,139,34));}





            }
        });




        ed1.setOnKeyListener(new View.OnKeyListener() {
            public boolean onKey(View v, int keyCode, KeyEvent event) {


                // If the event is a key-down event on the "enter" button
                if ((event.getAction() == KeyEvent.ACTION_DOWN) && (keyCode == KeyEvent.KEYCODE_ENTER))
                {
                    buds =ed1.getText().toString();
                    sum=no1+no2+no3+no4;
                    if(buds.length()>0)
                    {

                        bud =  Long.parseLong( buds);




                    }
                  //  else{bud=0;}
                    bal = bud - sum;
                    bals = Long.toString(bal);

                    tv3.setText(Long.toString(sum));
                    tv4.setText(bals);
                    ed1.setText(buds);
                    findViewById(R.id.mainLayout).requestFocus();
                    InputMethodManager imm = (InputMethodManager)getSystemService(INPUT_METHOD_SERVICE);
                    //imm.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(), 0);
                    imm.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(),InputMethodManager.HIDE_NOT_ALWAYS);
                    myDb.updateData(1,no1, no2, no3, no4, bud, bal,no1o, no2o, no3o, no4o, budo, balo);


                    if((bud-sum)<0)
                    {
                        tv4.setTextColor(Color.rgb( 205, 62, 45));
                    }
                    else{tv4.setTextColor(Color.rgb(34,139,34));}

                    return true;
                }

                return false;
            }
        });

        bt1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                findViewById(R.id.mainLayout).requestFocus();
                InputMethodManager imm = (InputMethodManager)getSystemService(INPUT_METHOD_SERVICE);
                //imm.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(), 0);
                imm.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(),InputMethodManager.HIDE_NOT_ALWAYS);

                Cursor cu = myDb.getalldata();
                cu.moveToFirst();
                {
                    Log.d("no1", cu.getString(1));
                    no1= Long.parseLong(cu.getString(1)) ;
                    Log.d("no2", cu.getString(2));
                    no2= Long.parseLong(cu.getString(2)) ;
                    Log.d("no3", cu.getString(3));
                    no3= Long.parseLong(cu.getString(3)) ;
                    Log.d("no4", cu.getString(4));
                    no4= Long.parseLong(cu.getString(4)) ;
                    Log.d("bud", cu.getString(5));
                    bud= Long.parseLong(cu.getString(5)) ;
                    Log.d("bal", cu.getString(6));
                    bal= Long.parseLong(cu.getString(6)) ;
                    Log.d("no1o", cu.getString(7));
                    no1o= Long.parseLong(cu.getString(7)) ;
                    Log.d("no2o", cu.getString(8));
                    no2o= Long.parseLong(cu.getString(8)) ;
                    Log.d("no3o", cu.getString(9));
                    no3o= Long.parseLong(cu.getString(9)) ;
                    Log.d("no4o", cu.getString(10));
                    no4o= Long.parseLong(cu.getString(10)) ;
                    Log.d("budo", cu.getString(11));
                    budo= Long.parseLong(cu.getString(11)) ;
                    //  Log.d("balo", cu.getString(12));
                    // balo= Long.parseLong(cu.getString(12)) ;
                    //  balo= Long.parseLong(cu.getString(13)) ;



                }

                no1o=no1;
                no2o=no2;
                no3o=no3;
                no4o=no4;
                budo=bud;
                balo=bal;
                expo=exp;


                buds =ed1.getText().toString();

                sum=no1+no2+no3+no4;
                if(buds.length()>0 ) {

                    bud = Long.parseLong(buds);
                    bal = bud - sum;
                    bals = Long.toString(bal);


                    tv4.setText(bals);

                    tv3.setText(Long.toString(sum));
                }

                exps = ed2.getText().toString();
                if(exps.length()>0 &&(Long.parseLong(exps)!=0)){
                    exp = Long.parseLong(exps);
                    if(flag==true) {
                        if(r11==true){   no1 =no1 +exp; sum=no1+no2+no3+no4; p1.setText(Long.toString(no1));bal=bud-sum;tv4.setText(Long.toString(bal)); tv3.setText(Long.toString(sum));ed2.setText("");
                            Toast toast = Toast.makeText(context,"Added "+exp+" Rupees to Food " , Toast.LENGTH_SHORT);
                            toast.show();}
                        else if(r12==true){no2 =no2 +exp; sum=no1+no2+no3+no4; p2.setText(Long.toString(no2));bal=bud-sum;tv4.setText(Long.toString(bal)); tv3.setText(Long.toString(sum));ed2.setText("");
                            Toast toast = Toast.makeText(context,"Added "+exp+" Rupees to Study " , Toast.LENGTH_SHORT);
                            toast.show();}
                        else if(r13==true){ no3 =no3 +exp ; sum=no1+no2+no3+no4; p3.setText(Long.toString(no3));bal=bud-sum;tv4.setText(Long.toString(bal)); tv3.setText(Long.toString(sum));ed2.setText("");
                            Toast toast = Toast.makeText(context,"Added "+exp+" Rupees to Entertainment " , Toast.LENGTH_SHORT);
                            toast.show();}
                        else if(r14==true){ no4 =no4 +exp; sum=no1+no2+no3+no4; p4.setText(Long.toString(no4));bal=bud-sum;tv4.setText(Long.toString(bal)); tv3.setText(Long.toString(sum));ed2.setText("");
                            Toast toast = Toast.makeText(context,"Added "+exp+" Rupees to Others " , Toast.LENGTH_SHORT);
                            toast.show();}


                    }
                    else{
                        Context context = getApplicationContext();
                        Toast toast = Toast.makeText(context, "Please , Select a Category", Toast.LENGTH_SHORT);
                        toast.show();
                    }

                    if((bud-sum)<0)
                    {
                        tv4.setTextColor(Color.rgb( 205, 62, 45));
                    }
                    else{tv4.setTextColor(Color.rgb( 34,139,34));}





                }
                else{
                    ed2.setText("");
                    Context context = getApplicationContext();
                    Toast toast = Toast.makeText(context, "Enter a valid number ", Toast.LENGTH_SHORT);
                    toast.show();
                }

                myDb.updateData(1, no1, no2, no3, no4, bud, bal,no1o, no2o, no3o, no4o, budo, balo);

                cu = myDb.getalldata();
                if (cu.getColumnCount() == 0) {
                    Log.d("onADDing", "EROOR");
                } else {
                    Log.d("onADDing", "on clickking on Add");

                    cu.moveToFirst();
                    {
                        Log.d("no1", cu.getString(1));
                        Log.d("no2", cu.getString(2));
                        Log.d("no3", cu.getString(3));
                        Log.d("no4", cu.getString(4));
                        Log.d("bud", cu.getString(5));
                        Log.d("bal", cu.getString(6));
                        Log.d("no1o", cu.getString(7));
                        Log.d("no2o", cu.getString(8));
                        Log.d("no3o", cu.getString(9));
                        Log.d("no4o", cu.getString(10));
                        Log.d("budo", cu.getString(11));
                       Log.d("balo", cu.getString(12));

                    }
                }




            }
        });





    }







    public void onRadioButtonClicked(View view) {

        // Is the button now checked?
        boolean checked = ((RadioButton) view).isChecked();

        // Check which radio button was clicked
        switch(view.getId()) {
            case R.id.rb1:
                if (checked)

                    r12=false;
                r13=false;
                r14=false;

                flag=true;
                r11=true;

                break;
            case R.id.rb2:
                if (checked)
                    r11=false;

                r13=false;
                r14=false;
                flag=true;
                r12=true;
                break;
            case R.id.rb3:
                if (checked)
                    r11=false;
                r12=false;

                r14=false;
                flag=true;
                r13=true;
                break;
            case R.id.rb4:
                if (checked)
                    r11=false;
                r12=false;
                r13=false;

                flag=true;
                r14=true;
                break;
        }
    }







    @Override
    protected void onStop() {
        super.onStop();
        myDb.updateData(1, no1, no2, no3, no4, bud, bal,no1o, no2o, no3o, no4o, budo, balo);

        Cursor cu = myDb.getalldata();
        if (cu.getColumnCount() == 0) {
            Log.d("ERROR", "EROOR");
        } else {
            Log.d("onstop", "onstop");

            cu.moveToFirst();
            {
                Log.d("no1", cu.getString(1));
                Log.d("no2", cu.getString(2));
                Log.d("no3", cu.getString(3));
                Log.d("no4", cu.getString(4));
                Log.d("bud", cu.getString(5));
                Log.d("bal", cu.getString(6));
                Log.d("no1o", cu.getString(7));
                Log.d("no2o", cu.getString(8));
                Log.d("no3o", cu.getString(9));
                Log.d("no4o", cu.getString(10));
                Log.d("budo", cu.getString(11));
             Log.d("balo", cu.getString(12));


            }
        }
    }










}



