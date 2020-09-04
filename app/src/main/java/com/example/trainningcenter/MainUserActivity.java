package com.example.trainningcenter;

import android.content.Intent;
import android.os.Bundle;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;
import com.google.android.material.tabs.TabLayout;
import androidx.appcompat.widget.Toolbar;
import androidx.viewpager.widget.ViewPager;
import androidx.appcompat.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import com.example.trainningcenter.ui.main.SectionsPagerAdapter;

public class MainUserActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_user);
        SectionsPagerAdapter sectionsPagerAdapter = new SectionsPagerAdapter(this, getSupportFragmentManager());
        ViewPager viewPager = findViewById(R.id.view_pager);
        viewPager.setAdapter(sectionsPagerAdapter);
        TabLayout tabs = findViewById(R.id.tabs);
        tabs.setupWithViewPager(viewPager);
        Toolbar toolbar = findViewById(R.id.mtToolBar);
        setSupportActionBar(toolbar);

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater menuInflater = getMenuInflater();
        menuInflater.inflate(R.menu.menu_main_user,menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if(id==R.id.item_logout){
            getSharedPreferences("TrainningShared", MODE_PRIVATE)
                    .edit()
                    .clear()
                    .commit();
            startActivity(new Intent(MainUserActivity.this,LoginActivity.class));
        } else if (id == R.id.item_QrCode)
            startActivity(new Intent(MainUserActivity.this,GenerateMyQrCode.class));
        else if (id == R.id.item_read)
            startActivity(new Intent(MainUserActivity.this,ScanQrCodeActivity.class));
        else if (id == R.id.item_reservation)
            startActivity(new Intent(MainUserActivity.this,ShowMyReservationActivity.class));

        return super.onOptionsItemSelected(item);
    }


}