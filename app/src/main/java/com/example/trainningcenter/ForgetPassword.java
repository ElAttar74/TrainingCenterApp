package com.example.trainningcenter;

import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Properties;
import java.util.Random;

import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;

public class ForgetPassword extends AppCompatActivity {

    EditText txtMyEmailAddress;
    Button btnSendMyPassword;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forget_password);
        txtMyEmailAddress = (EditText)findViewById(R.id.txtMyEmailAddress);
        btnSendMyPassword = (Button)findViewById(R.id.btnSendMyPassword);

        btnSendMyPassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                DatabaseConnection databaseConnection = new DatabaseConnection();
                Connection connection = databaseConnection.connectDB(ForgetPassword.this);
                if(connection == null){
                    Toast.makeText(ForgetPassword.this, "Check your internet connection", Toast.LENGTH_SHORT).show();
                }
                else {
                    final ResultSet resultSetLogin = databaseConnection.getData("select * from Student where Email = '"+txtMyEmailAddress.getText()+"'");
                    try {
                        if(resultSetLogin.next()){

                            new Thread(new Runnable() {
                                @Override
                                public void run() {
                                    try {
                                        Random r=new Random();


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
                                                    InternetAddress.parse(txtMyEmailAddress.getText().toString()));

                                            message.setSubject("Forget password By Training Center Applications");
                                            message.setText("Welcome "+resultSetLogin.getString(3)+"\nYpu password is : "+resultSetLogin.getString(2));
                                            Transport.send(message);


                                        } catch (MessagingException e) {
                                            throw new RuntimeException(e);
                                        }
                                    } catch (Exception ex) {
                                        ex.printStackTrace();
                                    }
                                }
                            }).start();
                            Toast.makeText(getApplication(),"your password has been sent to you email address",Toast.LENGTH_LONG).show();


                        }
                        else{
                            AlertDialog.Builder errorLoginMsg = new AlertDialog.Builder(ForgetPassword.this);
                            errorLoginMsg.setTitle("Login Error!");
                            errorLoginMsg.setMessage("Invalid Email Address please try again");
                            errorLoginMsg.setIcon(R.drawable.errormsg);
                            errorLoginMsg.setPositiveButton("Try again",null);
                            errorLoginMsg.create();
                            errorLoginMsg.show();
                        }
                    } catch (SQLException e) {
                        e.printStackTrace();
                    }
                }

            }
        });
    }
}
