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
import android.os.StrictMode;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import androidx.core.view.GravityCompat;
import androidx.appcompat.app.ActionBarDrawerToggle;
import android.view.MenuItem;
import com.google.android.material.navigation.NavigationView;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import android.view.Menu;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.Spinner;
import android.widget.Toast;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;
import java.util.Random;

import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;

public class RegisterActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {
    List<String> gradeName = new ArrayList<String>();
    List<String> gradeNumber = new ArrayList<String>();
    Spinner spnGrades;
    Button btnSend,btnRegister;
    int activecode;
    EditText txtFullName,txtUsername,txtPassword,txtphoneNumber,txtEmailAddress,txtActiveCode;
    RadioButton rdMale,rdFemale;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        txtEmailAddress = (EditText)findViewById(R.id.txtEmailAddress);
        txtPassword = (EditText)findViewById(R.id.txtPassword);
        txtphoneNumber = (EditText)findViewById(R.id.txtPhoneNumber);
        txtFullName = (EditText)findViewById(R.id.txtFullName);
        txtUsername = (EditText)findViewById(R.id.txtUsername);

        txtActiveCode = (EditText)findViewById(R.id.txtActiveCode);
        spnGrades = (Spinner)findViewById(R.id.spnGrades);
        btnSend = (Button)findViewById(R.id.btnSend);
        btnRegister = (Button)findViewById(R.id.btnRegister);
        rdMale = (RadioButton)findViewById(R.id.rdMale);
        rdFemale = (RadioButton)findViewById(R.id.rdFemale);
        btnRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if(txtFullName.getText().toString().isEmpty())
                    txtFullName.setError("You must enter your full name");
                else {
                    if (txtUsername.getText().toString().isEmpty())
                        txtUsername.setError("You must enter username");
                    else {
                        if (txtPassword.getText().toString().isEmpty())
                            txtPassword.setError("You must enter password");
                        else {
                            if (txtphoneNumber.getText().toString().isEmpty())
                                txtphoneNumber.setError("You must enter your phone number");
                            else {
                                if (txtEmailAddress.getText().toString().isEmpty())
                                    txtEmailAddress.setError("You must enter your Email address");
                                else {
                                    if (txtActiveCode.getText().toString().isEmpty())
                                        txtActiveCode.setError("You must enter the active code that sending to your email");
                                    else {
                                        if (Integer.parseInt(txtActiveCode.getText().toString())==( activecode)) {
                                            DatabaseConnection databaseConnection = new DatabaseConnection();
                                            Connection connection = databaseConnection.connectDB(RegisterActivity.this);

                                            if (connection == null) {
                                                createNetErrorDialog();

                                            } else {
                                                String gender;
                                                if (rdMale.isChecked())
                                                    gender = "Male";
                                                else
                                                    gender = "Female";
                                                String msg = databaseConnection.runDML("insert into Student values('" + txtUsername.getText() + "','" + txtPassword.getText() + "','" + txtFullName.getText() + "','" + txtphoneNumber.getText() + "','" + txtEmailAddress.getText() + "','" + gender + "','" + GetMyCurrentLocation.latitude + "','" + GetMyCurrentLocation.longitude + "','" + gradeNumber.get(spnGrades.getSelectedItemPosition()) + "')");
                                                if (msg.equals("Done")) {
                                                    AlertDialog.Builder doneRegister = new AlertDialog.Builder(RegisterActivity.this);
                                                    doneRegister.setMessage("Successfully Registeration!")
                                                            .setTitle("Registeration")
                                                            .setCancelable(false)
                                                            .setIcon(R.drawable.thanks)
                                                            .setPositiveButton("Thanks", new DialogInterface.OnClickListener() {
                                                                @Override
                                                                public void onClick(DialogInterface dialog, int which) {
                                                                    startActivity(new Intent(RegisterActivity.this,LoginActivity.class));
                                                                }
                                                            });
                                                    doneRegister.create();
                                                    doneRegister.show();
                                                } else if(msg.contains("PK_Student")){
                                                    AlertDialog.Builder doneRegister = new AlertDialog.Builder(RegisterActivity.this);
                                                    doneRegister.setMessage("Sorry this username "+txtUsername.getText()+"Already exists.\n Please try again")
                                                            .setTitle("Registeration")
                                                            .setCancelable(false)
                                                            .setIcon(R.drawable.thanks)
                                                            .setPositiveButton("Thanks", null);
                                                    doneRegister.create();
                                                    doneRegister.show();

                                                }
                                                else
                                                    Toast.makeText(RegisterActivity.this, "Error in" + msg, Toast.LENGTH_LONG).show();

                                            }
                                        } else
                                            Toast.makeText(RegisterActivity.this, "Wrong active code!", Toast.LENGTH_LONG).show();
                                    }
                                }
                            }
                        }
                    }
                }



            }
        });

       /* txtEmailAddress.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {

                if(hasFocus == false){
                    String match = "[a-zA-Z0-9.!#$%&'*+/=?^_`{|}~-]+@[a-z0-9.-]+\\.[a-z]{2,}$";
                    if(txtEmailAddress.getText().toString().matches(match)){

                    }
                    else
                        txtEmailAddress.setError("Invalid email formating");

                }

            }
        });

        */

       txtEmailAddress.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {

                String match = "[a-zA-Z0-9.!#$%&'*+/=?^_`{|}~-]+@[a-z0-9.-]+\\.[a-z]{2,}$";
                if (txtEmailAddress.getText().toString().matches(match)) {

                } else
                    txtEmailAddress.setError("Invalid email formating");
            }


        });

       txtPassword.addTextChangedListener(new TextWatcher() {
           @Override
           public void beforeTextChanged(CharSequence s, int start, int count, int after) {

           }

           @Override
           public void onTextChanged(CharSequence s, int start, int before, int count) {

           }

           @Override
           public void afterTextChanged(Editable s) {
               String match = "^(?=.*[a-z])(?=.*[A-Z])(?=.*\\d)(?=.*[@$!%*?&])[A-Za-z\\d@$!%*?&]{8,}$";
               if(txtPassword.getText().toString().matches(match)){

               }
               else
                   txtPassword.setError("Password must contain Minimum eight characters, at least one uppercase letter, one lowercase letter, one number and one special character");

           }
       });
       txtphoneNumber.addTextChangedListener(new TextWatcher() {
           @Override
           public void beforeTextChanged(CharSequence s, int start, int count, int after) {

           }

           @Override
           public void onTextChanged(CharSequence s, int start, int before, int count) {

           }

           @Override
           public void afterTextChanged(Editable s) {
               String match = "^[0][1-9]\\d{9}$|^[1-9]\\d{9}$";
               if(txtphoneNumber.getText().toString().matches(match)){

               }
               else
                   txtphoneNumber.setError("Invalid phone number");

           }
       });




        btnSend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        try {
                            Random r=new Random();

                            activecode=r.nextInt(99999-12345+1)-12345;
                            final String username = "yourmobileapp2017@gmail.com";
                            final String password = "okok2017";
                            Properties props = new Properties();
                            props.put("mail.smtp.auth", "true");
                            props.put("mail.smtp.starttls.enable", "true");
                            props.put("mail.smtp.host", "smtp.gmail.com");
                            props.put("mail.smtp.port", "587");

                            Session session = Session.getInstance(props,
                                    new javax.mail.Authenticator() {

                                        protected PasswordAuthentication getPasswordAuthentication() {
                                            return new PasswordAuthentication(username, password);
                                        }
                                    });

                            try {
                                Message message = new MimeMessage(session);
                                message.setFrom(new InternetAddress("yourmobileapp2017@gmail.com"));
                                message.setRecipients(Message.RecipientType.TO,
                                        InternetAddress.parse(txtEmailAddress.getText().toString()));

                                message.setSubject("Activation Code By Training Center Applications");
                                message.setText("Welcome "+txtFullName.getText()+"\nActivation Code is : "+activecode);
                                Transport.send(message);


                            } catch (MessagingException e) {
                                throw new RuntimeException(e);
                            }
                        } catch (Exception ex) {
                            ex.printStackTrace();
                        }
                    }
                }).start();
                Toast.makeText(getApplication(),"Activation code has been sent Check your Email",Toast.LENGTH_LONG).show();


            }
        });


        DatabaseConnection databaseConnection = new DatabaseConnection();
        Connection connection = databaseConnection.connectDB(this);
        if(connection == null)
            createNetErrorDialog();
        else
        {
            ResultSet resultSetGrade = databaseConnection.getData("SELECT * FROM Grades");
            try{
                while (resultSetGrade.next()){
                    gradeName.add(resultSetGrade.getString(2)+" "+resultSetGrade.getString(3));
                    gradeNumber.add(resultSetGrade.getString(1));
                }
                ArrayAdapter<String> gradeAndPhases = new ArrayAdapter<String>(this,android.R.layout.simple_spinner_item,gradeName);
                spnGrades.setAdapter(gradeAndPhases);
            }catch (SQLException e){
            }
        }



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
        else if(id == R.id.action_english){
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
