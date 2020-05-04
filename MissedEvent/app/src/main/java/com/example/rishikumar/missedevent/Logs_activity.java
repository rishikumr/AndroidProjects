package com.example.rishikumar.missedevent;


import android.app.Activity;
import android.app.ListActivity;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.ToggleButton;

import java.util.ArrayList;


/**
 * Created by rishi.kumar on 9/15/2017.
 */

public class Logs_activity   extends Activity {

    TextView tb;
    Intent i;
    DatabaseHelper db;
    String str1, str2, str3,str4;
    TextView tv;
    String data="";
    String str[];
    int j=0;
    ListView list;
    Button clear;
    ListView lv;
    ArrayList<String> strarr;
    ArrayAdapter<String> adaptor;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate( savedInstanceState );
        setContentView( R.layout.calllogs );

        db = new DatabaseHelper( this );


        Log.d( "Tagg", "The CallLogs create  event" );

        lv = (ListView) findViewById( R.id.lv );
        tv = (TextView) findViewById( R.id.tv );
        strarr = new ArrayList<String>(  );
        adaptor = new ArrayAdapter<String>( getApplicationContext(),android.R.layout.simple_expandable_list_item_1,strarr );
        lv.setAdapter( adaptor );

try {

    Cursor cursor = db.getalldata();
    if (cursor != null)
        cursor.moveToLast();

    do {
        {
            //data = data + " \n" + /*(cursor.getString( 0 )) +*/"   " +
              //      cursor.getString( 1 ) + "\n\t " + "+" + cursor.getString( 2 ) + "\n\t\t" + cursor.getString( 3 ) + "     " + " \n" + "-----------------------------------------------------------------";
            //  Log.d( "Tagg", "Data retrieve from database is : " + data );

            strarr.add(  cursor.getString( 1 ) + "\t\t\t\t\t\t\t\t" + "+" + cursor.getString( 2 ) + "\n" + cursor.getString( 3 ) ) ;
            adaptor.notifyDataSetChanged();
             Log.d( "Tagg", "Data retrieve from database is : " + cursor.getString( 1 ) + "\t\t\t\t\t\t\t\t" + "+" + cursor.getString( 2 ) + "\n" + cursor.getString( 3 ) );
        }
    } while (cursor.moveToPrevious());



    tv.setText( data );
}
catch(Exception e){e.getStackTrace();}
        clear = (Button) findViewById(R.id.clear);
        clear.setOnClickListener( new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                db.deleteallData();
                tv.setText( "" );
            }
        } );



    }




    @Override
    protected void onStop () {
        super.onStop();
        Log.d( "Tagg", "The CallLogs activity is destroyed" );

    }

}
