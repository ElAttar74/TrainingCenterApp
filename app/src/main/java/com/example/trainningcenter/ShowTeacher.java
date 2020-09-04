package com.example.trainningcenter;

import android.content.Context;
import android.widget.Toast;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class ShowTeacher {
    public  static List<Teacher> showTeaceh(String subjectNumber, Context context){
        List<Teacher> teacherData = new ArrayList<Teacher>();
        DatabaseConnection databaseConnection = new DatabaseConnection();
        Connection connection = databaseConnection.connectDB(context);
        if(connection == null)
            Toast.makeText(context,"error in network",Toast.LENGTH_LONG).show();
        else {
            ResultSet resultSet = databaseConnection.getData("select * from  Teacher where SubjectNo = '"+subjectNumber+"' and Status = 1");

            try{
                while (resultSet.next()){
                    Teacher teacher = new Teacher();
                    teacher.setUserName(resultSet.getString(1));
                    teacher.setFullName(resultSet.getString(3));
                    teacher.setPhone(resultSet.getString(4));
                    teacher.setGender(resultSet.getString(5));
                    teacher.setCollegeDegree(resultSet.getString(6));
                    teacher.setLatitude(resultSet.getString(8));
                    teacher.setLongitude(resultSet.getString(9));
                    teacher.setCapacity(resultSet.getString(10));
                    teacher.setPrivateCost(resultSet.getString(11));
                    teacher.setPublicCost(resultSet.getString(12));
                    teacher.setImgURL(resultSet.getString(13));
                    teacher.setTeacherVideo(resultSet.getString(16));
                    teacherData.add(teacher);
                }

            }catch (SQLException ex){

            }
        }
        return teacherData;
    }

}
