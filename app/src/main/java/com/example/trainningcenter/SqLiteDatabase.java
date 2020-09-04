package com.example.trainningcenter;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import androidx.annotation.Nullable;

public class SqLiteDatabase extends SQLiteOpenHelper {
    public SqLiteDatabase(@Nullable Context context) {
        super(context, "CenterDb", null, 1);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("CREATE TABLE [Reservation] (ReservationNo integer PRIMARY KEY AUTOINCREMENT,ReservationDate text,Notes text,GroupNo  integer,StudentUsernaem text)");

    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }
    public String saveReservation(String ReservationDate,String Notes,String GroupNo,String StudentUsernaem){
        SQLiteDatabase db = getWritableDatabase();
        ContentValues cn = new ContentValues();
        cn.put("ReservationDate",ReservationDate);
        cn.put("Notes",Notes);
        cn.put("GroupNo",GroupNo);
        cn.put("StudentUsernaem",StudentUsernaem);
        db.insert("Reservation",null,cn);
        return "ok";

    }
    public Cursor showReservation(){
        SQLiteDatabase db = getReadableDatabase();
        Cursor c = db.rawQuery("select * from Reservation",null);
        return c;
    }
}
