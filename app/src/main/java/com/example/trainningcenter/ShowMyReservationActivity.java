package com.example.trainningcenter;

import androidx.appcompat.app.AppCompatActivity;

import android.database.Cursor;
import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import java.util.ArrayList;
import java.util.List;

public class ShowMyReservationActivity extends AppCompatActivity {
    ListView lstReservation;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_show_my_reservation);
        lstReservation = (ListView)findViewById(R.id.lstReservation);
        SqLiteDatabase db = new SqLiteDatabase(ShowMyReservationActivity.this);
        Cursor c = db.showReservation();
        List<String> data = new ArrayList<String>();
        while(c.moveToNext()){
            data.add(c.getString(0)+" / "+c.getString(1));
        }
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this,android.R.layout.simple_list_item_2,android.R.id.text1,data);
        lstReservation.setAdapter(adapter);
    }
}
