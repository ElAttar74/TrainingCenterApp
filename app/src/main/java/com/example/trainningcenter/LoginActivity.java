package com.example.trainningcenter;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.wifi.WifiManager;
import android.os.Bundle;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;
import android.view.View;
import androidx.core.view.GravityCompat;
import androidx.appcompat.app.ActionBarDrawerToggle;
import android.view.MenuItem;
import com.google.android.material.navigation.NavigationView;
import androidx.drawerlayout.widget.DrawerLayout;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import android.view.Menu;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.TextView;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;

public class LoginActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    EditText txtLoginUsername,txtLoginPassword;
    Button btnLogin;
    CheckBox chkRememberMe;
    TextView txtVForgetPassword;
    public static String gradeNumber,userName;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        txtLoginUsername = (EditText)findViewById(R.id.txtLoginUsername);
        txtLoginPassword = (EditText)findViewById(R.id.txtLoginPassword);
        chkRememberMe = (CheckBox)findViewById(R.id.chkRememberMe);
        txtVForgetPassword = (TextView)findViewById(R.id.txtVForgetPassword);
        btnLogin = (Button)findViewById(R.id.btnLogin);

        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DatabaseConnection databaseConnection = new DatabaseConnection();
                Connection connection = databaseConnection.connectDB(LoginActivity.this);
                if(connection == null){
                    createNetErrorDialog();
                }
                else {
                    if(txtLoginUsername.getText().toString().isEmpty())
                        txtLoginUsername.setError("Please enter your username");
                    else if(txtLoginPassword.getText().toString().isEmpty())
                        txtLoginPassword.setError("please enter your password");
                    else {

                        ResultSet resultSetLogin = databaseConnection.getData("select * from Student where StudentUsername = '" + txtLoginUsername.getText() + "' and Password = '" + txtLoginPassword.getText() + "'");
                        try {
                            if (resultSetLogin.next()) {
                                if (chkRememberMe.isChecked()) {
                                    getSharedPreferences("TrainningShared", MODE_PRIVATE)
                                            .edit()
                                            .putString("Username", txtLoginUsername.getText().toString())
                                            .putString("Password", txtLoginPassword.getText().toString())
                                            .putString("gradeNumber",resultSetLogin.getString(9))
                                            .commit();
                                }

                                gradeNumber = resultSetLogin.getString(9);
                                userName = resultSetLogin.getString(1);
                                startActivity(new Intent(LoginActivity.this,MainUserActivity.class));


                            } else {
                                AlertDialog.Builder errorLoginMsg = new AlertDialog.Builder(LoginActivity.this);
                                errorLoginMsg.setTitle("Login Error!");
                                errorLoginMsg.setMessage("Please check your username or password");
                                errorLoginMsg.setIcon(R.drawable.errormsg);
                                errorLoginMsg.setPositiveButton("Try again", null);
                                errorLoginMsg.create();
                                errorLoginMsg.show();
                            }
                        } catch (SQLException e) {
                            e.printStackTrace();
                        }
                    }
                }
            }
        });

        txtVForgetPassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(LoginActivity.this,ForgetPassword.class));
            }
        });

        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        NavigationView navigationView = findViewById(R.id.nav_view);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();
        navigationView.setNavigationItemSelectedListener(this);
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_arabic) {
            return true;
        }
        else if (id == R.id.action_english)
        {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_home) {
            startActivity(new Intent(this,MainActivity.class));
        } else if (id == R.id.nav_rgistration) {
            startActivity(new Intent(this,RegisterActivity.class));
        } else if (id == R.id.nav_login) {
            startActivity(new Intent(this,LoginActivity.class));
        } else if(id == R.id.nav_location){
            startActivity(new Intent(this,GetMyCurrentLocation.class));
        } else if (id == R.id.nav_aboutUs) {

        } else if (id == R.id.nav_contactUs) {

        }

        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }
    protected void createNetErrorDialog() {

        android.app.AlertDialog.Builder builder = new android.app.AlertDialog.Builder(this);
        builder.setMessage("You need internet connection for this app. Please turn on mobile network or Wi-Fi in Settings.")
                .setTitle("Unable to connect")
                .setCancelable(false)
                .setPositiveButton("Enabled Data",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                setMobileDataState(true);
                                WifiManager wifiManager = (WifiManager) getApplicationContext().getSystemService(Context.WIFI_SERVICE);
                                wifiManager.setWifiEnabled(false);
                            }
                        }
                )
                .setNegativeButton("Enabled Wifi",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                WifiManager wifiManager = (WifiManager) getApplicationContext().getSystemService(Context.WIFI_SERVICE);
                                wifiManager.setWifiEnabled(true);
                                setMobileDataState(false);

                            }
                        }
                );
        AlertDialog alert = builder.create();
        alert.show();
    }


    public void setMobileDataState(boolean mobileDataEnabled)
    {
        ConnectivityManager dataManager;
        dataManager  = (ConnectivityManager)getSystemService(Context.CONNECTIVITY_SERVICE);
        Method dataMtd = null;
        try {
            dataMtd =ConnectivityManager.class.getDeclaredMethod("setMobileDataEnabled", boolean.class);

            dataMtd.setAccessible(mobileDataEnabled);
            dataMtd.invoke(dataManager, mobileDataEnabled);
        } catch (NoSuchMethodException e) {
            e.printStackTrace();
        } catch (InvocationTargetException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }

    }
}
