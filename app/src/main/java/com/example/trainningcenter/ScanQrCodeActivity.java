package com.example.trainningcenter;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.DialogInterface;
import android.content.Intent;
import android.media.Ringtone;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.google.zxing.WriterException;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class ScanQrCodeActivity extends AppCompatActivity {
    Button btnScanQrCode;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_scan_qr_code);
        btnScanQrCode = (Button)findViewById(R.id.btnScanQrCode);
        btnScanQrCode.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {

                    Intent intent = new Intent("com.google.zxing.client.android.SCAN");
                    intent.putExtra("SCAN_MODE", "QR_CODE_MODE"); // "PRODUCT_MODE for bar codes

                    startActivityForResult(intent, 0);

                } catch (Exception e) {

                    Uri marketUri = Uri.parse("market://details?id=com.google.zxing.client.android");
                    Intent marketIntent = new Intent(Intent.ACTION_VIEW,marketUri);
                    startActivity(marketIntent);

                }
            }
        });
    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 0) {

            if (resultCode == RESULT_OK) {
                String contents = data.getStringExtra("SCAN_RESULT");
                int day= Calendar.getInstance().get(Calendar.DAY_OF_MONTH);
                int month=Calendar.getInstance().get(Calendar.MONTH)+1;
                int year=Calendar.getInstance().get(Calendar.YEAR);
                String dm=(day+"-"+month+"-"+year);
                SimpleDateFormat inFormat = new SimpleDateFormat("dd-MM-yyyy");
                Date date = null;
                try {
                    date = inFormat.parse(dm);
                } catch (ParseException e) {
                    e.printStackTrace();
                }
                SimpleDateFormat outFormat = new SimpleDateFormat("EEEE");
                String goal = outFormat.format(date);
                Toast.makeText(getApplication(),goal+"",Toast.LENGTH_LONG).show();

                DatabaseConnection databaseConnection = new DatabaseConnection();
                databaseConnection.connectDB(ScanQrCodeActivity.this);
                final ResultSet rs = databaseConnection.getData("select * from View_Qrcode where StudentUsername = '"+LoginActivity.userName+"' and Day = '"+goal+"' and title = '"+contents+"'");
                try {
                    if(rs.next()){
                        AlertDialog.Builder mg = new AlertDialog.Builder(ScanQrCodeActivity.this)
                                .setTitle("Qr Details")
                                .setMessage("Welcome : "+rs.getString(7)+"\n"
                                        +"Subject now is : "+contents+"\n"
                                        +"With MR: "+rs.getString(9)+"\n"
                                        +"from "+rs.getString(3)+"To "+rs.getString(4)+"\n"
                                        +"Are you sure attendence now ?")
                                .setPositiveButton("Yes ", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        DatabaseConnection databaseConnection = new DatabaseConnection();
                                        databaseConnection.connectDB(ScanQrCodeActivity.this);
                                        try {
                                            String msg = databaseConnection.runDML("Insert into Attendance values('"+Calendar.getInstance().getTime()+"','"+LoginActivity.userName+"','"+rs.getString(5)+"')");
                                            if(msg.equals("Done"))
                                                Toast.makeText(ScanQrCodeActivity.this, "Uploaded Successfully", Toast.LENGTH_SHORT).show();
                                            else
                                                Toast.makeText(ScanQrCodeActivity.this, "Failed"+msg, Toast.LENGTH_SHORT).show();




                                        }catch (SQLException ex){
                                            ex.printStackTrace();
                                        }

                                    }
                                }).setNegativeButton("No",null);
                        mg.create();
                        mg.show();


                    } else {
                        AlertDialog.Builder mg = new AlertDialog.Builder(ScanQrCodeActivity.this)
                                .setTitle("Welcome")
                                .setMessage("you dont have any sessions now")
                                .setPositiveButton("Thanks",null);
                        mg.create();
                        mg.show();
                        Uri notification = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_ALARM);
                        Ringtone r = RingtoneManager.getRingtone(getApplicationContext(), notification);
                        r.play();

                    }
                } catch (SQLException e) {
                    e.printStackTrace();
                }


            }

            }
            if(resultCode == RESULT_CANCELED){
                //handle cancel
            }
        }
    }


