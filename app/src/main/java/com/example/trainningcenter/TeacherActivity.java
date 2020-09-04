package com.example.trainningcenter;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager.widget.ViewPager;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.VideoView;

import com.google.android.material.bottomsheet.BottomSheetDialog;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

public class TeacherActivity extends AppCompatActivity {
    ListView lstTeachers;
    DataAdapterTeacher dataAdapterTeacher;
    ShowTeacher showTeacher = new ShowTeacher();
    Teacher teacher;
    TextView txtDetailsName,txtDetailsCapacity,txtDetailsPrivateCost,txtDetailsPublicCost,txtDetailsCollegeDegree;
    public static TextView txtDetailsUserName;
    public static String teacherUserName;
    ImageView imgGetTeacherLocation;
    public static double latitude,longitude;
    public static String teacherName,gender;
    List<String> daysData = new ArrayList<String>();
    List<String> groupNo = new ArrayList<String>();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_teacher);
        lstTeachers = (ListView)findViewById(R.id.lstTeachers);

        ArrayList<Teacher> data;
        data = new ArrayList<Teacher>(ShowTeacher.showTeaceh(SubjectFragment.subjectNumber,this));
        dataAdapterTeacher = new DataAdapterTeacher(this,data);
        lstTeachers.setAdapter(dataAdapterTeacher);

        lstTeachers.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                teacher = dataAdapterTeacher.getItem(position);
                BottomSheetDialog teacherDetails = new BottomSheetDialog(TeacherActivity.this);
                LayoutInflater layoutInflater = LayoutInflater.from(TeacherActivity.this);
                View vv = layoutInflater.inflate(R.layout.bottom_sheet_details,null);
                txtDetailsName = (TextView)vv.findViewById(R.id.txtdetailsName);
                txtDetailsCapacity = (TextView)vv.findViewById(R.id.txtDetailsCapacity);
                txtDetailsPrivateCost = (TextView)vv.findViewById(R.id.txtDetailsPrivateCost);
                txtDetailsPublicCost = (TextView)vv.findViewById(R.id.txtDetailsPublicCost);
                txtDetailsCollegeDegree = (TextView)vv.findViewById(R.id.txtDetailsCollegeDegree);
                imgGetTeacherLocation = (ImageView)vv.findViewById(R.id.imgGetTeacherLocation);
                txtDetailsUserName = (TextView)vv.findViewById(R.id.txtDetailsUserName);
                Button btnVidTeacher = (Button)vv.findViewById(R.id.btnVidTeacher);
                btnVidTeacher.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        startActivity(new Intent(TeacherActivity.this,PlayVideoTeacherActivity.class));
                    }
                });
                imgGetTeacherLocation.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if((teacher.getLatitude() != null)||(teacher.getLongitude() != null)){
                            latitude =Double.parseDouble( teacher.getLatitude());
                            longitude = Double.parseDouble(teacher.getLongitude());
                            teacherName = teacher.getFullName();
                            gender = teacher.getGender();

                            startActivity(new Intent(TeacherActivity.this,TeacherMapsActivity.class));
                        }
                        else
                            Toast.makeText(TeacherActivity.this, "This teacher has no location", Toast.LENGTH_LONG).show();


                    }
                });
                if(teacher.getGender().equals("male"))
                    txtDetailsName.setText("Mr: "+teacher.getFullName());
                else
                    txtDetailsName.setText("Miss: "+teacher.getFullName());

                txtDetailsCapacity.setText("Capacity: "+teacher.getCapacity());
                txtDetailsPrivateCost.setText("private cost: "+teacher.getPrivateCost()+" LE.");
                txtDetailsPublicCost.setText("public cost: "+teacher.getPublicCost()+" LE.");
                txtDetailsCollegeDegree.setText("degree: "+teacher.getCollegeDegree());
                txtDetailsUserName.setText("UserName: "+teacher.getUserName());
                teacherUserName = teacher.getUserName();


                final ListView lstDetailsDays = (ListView)vv.findViewById(R.id.lstDetailsDays);
                final DatabaseConnection databaseConnection = new DatabaseConnection();
                Connection connection = databaseConnection.connectDB(TeacherActivity.this);
                if(connection == null){
                    Toast.makeText(TeacherActivity.this,"check internet connection",Toast.LENGTH_LONG).show();
                }
                else {
                    ResultSet rsDays = databaseConnection.getData("select * from  View_Days where TeacherUsername ='"+teacher.getUserName()+"'");

                    try{
                        while (rsDays.next()){
                            daysData.add(rsDays.getString(2)+" - "+rsDays.getString(3)+" To "+rsDays.getString(4));
                            groupNo.add(rsDays.getString(6));

                        }
                        ArrayAdapter<String> adapter = new ArrayAdapter<String>(TeacherActivity.this,android.R.layout.simple_list_item_1,daysData);
                        lstDetailsDays.setAdapter(adapter);


                    }catch (SQLException ex){
                        ;
                    }
                }
                lstDetailsDays.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> parent, View view, final int position, long id) {
                        AlertDialog.Builder m = new AlertDialog.Builder(TeacherActivity.this)
                                .setTitle("Reservation")
                                .setMessage("Do you want to reserve at this time : "+lstDetailsDays.getItemAtPosition(position).toString())
                                .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        String u;
                                        if(LoginActivity.userName==null)
                                            u = MainActivity.userNameb;
                                        else
                                            u = LoginActivity.userName;
                                        String msg = databaseConnection.runDML("insert into Reservation values('"+ Calendar.getInstance().getTime()+"','','"+groupNo.get(position)+"','"+u+"')");
                                        if(msg.equals("Done"))
                                            Toast.makeText(TeacherActivity.this, "Reservation has been done", Toast.LENGTH_LONG).show();
                                        else
                                            Toast.makeText(TeacherActivity.this, "Error "+msg, Toast.LENGTH_LONG).show();


                                        SqLiteDatabase sqLiteDatabase = new SqLiteDatabase(TeacherActivity.this);
                                        sqLiteDatabase.saveReservation(Calendar.getInstance().getTime().toString(),"",groupNo.get(position),u);

                                        DatabaseConnection databaseConnection1 = new DatabaseConnection();
                                        databaseConnection.connectDB(TeacherActivity.this);


                                    }
                                }).setNegativeButton("No",null);
                        m.create();
                        m.show();
                    }
                });
                teacherDetails.setContentView(vv);
                teacherDetails.show();


            }
        });
    }

}
