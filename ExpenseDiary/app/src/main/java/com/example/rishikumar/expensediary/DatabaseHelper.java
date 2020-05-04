package com.example.rishikumar.expensediary;


import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

/**
 * Created by rishi on 9/5/2017.
 */

public class DatabaseHelper extends SQLiteOpenHelper{
    public static  final  String DATABASE_NAME="Expence_Diary_SQL";
    public static  final  String TABLE_NAME="Sqldata_table";
    public static  final  String COL1="ID";
    public static  final  String COL2="NO1";
    public static  final  String COL3="NO2";
    public static  final  String COL4="NO3";

    public static  final  String COL5="NO4";

    public static  final  String COL6="BUD";
    public static  final  String COL7="BAL";



    public static  final  String COL8="NO1o";
    public static  final  String COL9="NO2o";
    public static  final  String COL10="NO3o";

    public static  final  String COL11="NO4o";

    public static  final  String COL12="BUDo";
    public static  final  String COL13="BALo";






    public DatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, 1);

    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        Log.d("TABLE ", "TABLE is being created");
        db.execSQL("CREATE TABLE "+TABLE_NAME+"(ID INTEGER PRIMARY KEY AUTOINCREMENT, NO1 LONG , NO2 LONG , NO3 LONG , NO4 LONG, BUD LONG, BAL LONG , NO1o LONG , NO2o LONG , NO3o LONG , NO4o LONG, BUDo LONG, BALo LONG )   ");
        Log.d("TABLE ", "TABLE created");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {




        db.execSQL("DROP TABLE IF EXISTS "+ TABLE_NAME);
        onCreate(db);


    }
    public boolean insertData ( Long no1, Long no2,Long no3, Long no4, Long bud, Long bal, long no1o, long no2o, long no3o, long no4o, long budo , long balo  ){

        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues cn = new ContentValues();
        cn.put(COL2, no1);
        cn.put(COL3, no2);
        cn.put(COL4, no3);
        cn.put(COL5, no4);
        cn.put(COL6, bud);
        cn.put(COL7, bal);
        cn.put(COL8, no1o);
        cn.put(COL9, no2o);
        cn.put(COL10, no3o);
        cn.put(COL11, no4o);
        cn.put(COL12, budo);
        cn.put(COL13, balo);


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

    public void updateData(int id, long no1, long no2, long no3, long no4, long bud , long bal, long no1o, long no2o, long no3o, long no4o, long budo , long balo){
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues cn = new ContentValues();
        cn.put(COL1, id);
        cn.put(COL2, no1);
        cn.put(COL3, no2);
        cn.put(COL4, no3);
        cn.put(COL5, no4);
        cn.put(COL6, bud);
        cn.put(COL7, bal);
        cn.put(COL8, no1o);
        cn.put(COL9, no2o);
        cn.put(COL10, no3o);
        cn.put(COL11, no4o);
        cn.put(COL12, budo);
        cn.put(COL13, balo);


        db.update(TABLE_NAME,cn,"ID=?",new String[] { Integer.toString(id) }); Log.d("update", "Data updated");

    }

    public Cursor getalldata(){
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cr = db.rawQuery("SELECT * from "+TABLE_NAME,null);
        return cr;
    }
}

