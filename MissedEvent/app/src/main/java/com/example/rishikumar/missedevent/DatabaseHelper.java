package com.example.rishikumar.missedevent;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import java.util.jar.Attributes;

/**
 * Created by rishi.kumar on 9/14/2017.
 */


public class DatabaseHelper extends SQLiteOpenHelper {
    public static  final  String DATABASE_NAME="Missed_events_SQL";
    public static  final  String TABLE_NAME="table1";
    public static  final  String COL1="ID";
    public static  final  String COL2="NAME";
    public static  final  String COL3="NUMBER";
    public static  final  String COL4="TIME";







    public DatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, 1);

    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        Log.d("TABLE ", "TABLE is being created");
        db.execSQL("CREATE TABLE "+TABLE_NAME+"(ID INTEGER PRIMARY KEY AUTOINCREMENT, NAME STRING , NUMBER STRING , TIME STRING  )   ");
        Log.d("TABLE ", "TABLE created");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {




        db.execSQL("DROP TABLE IF EXISTS "+ TABLE_NAME);
        onCreate(db);


    }
    public boolean insertData ( String NAME , String NUMBER , String time  ){

        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues cn = new ContentValues();
        cn.put(COL2, NAME);
        cn.put(COL3, NUMBER);
        cn.put(COL4, time);




        Long result =db.insert(TABLE_NAME,"ID", cn);
        if(result ==-1)
        {return false;}

        else{  Log.d("insert", "Data inserted");return true;}
    }

    public void deleteData(int id){

        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(TABLE_NAME, "ID=?", new String[]{Integer.toString(id)});
        Log.d("delete","Deleting "+id+" th row..");
        //  db.delete(TABLE_NAME,"ID=?",new String[]{Integer.toString(id)});

    }
    public void deleteallData(){

        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(TABLE_NAME, null,null);
        Log.d("delete","Deleting all");
        //  db.delete(TABLE_NAME,"ID=?",new String[]{Integer.toString(id)});

    }
    public void updateData(int id, String NAME , String  NUMBER , String time ){
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues cn = new ContentValues();
        cn.put(COL1, id);
        cn.put(COL2, NAME);
        cn.put(COL3, NUMBER);
        cn.put(COL4, time);


        db.update(TABLE_NAME,cn,"ID=?",new String[] { Integer.toString(id) }); Log.d("update", "Data updated");

    }

    public Cursor getalldata(){
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cr = db.rawQuery("SELECT * from "+TABLE_NAME,null);   // Cursor cr = db.Query(
        return cr;
    }
}

