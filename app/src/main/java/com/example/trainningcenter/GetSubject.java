package com.example.trainningcenter;

import android.content.Context;
import android.widget.Toast;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import javax.security.auth.Subject;

public class GetSubject {

    public static List<Subjects> getSubjectData(Context context,String gradeNumber){
        List<Subjects> subjectData = new ArrayList<Subjects>();
        DatabaseConnection databaseConnection = new DatabaseConnection();
        Connection connection = databaseConnection.connectDB(context);
        if(connection == null){
            Toast.makeText(context,"Check you internet connection",Toast.LENGTH_LONG).show();
        }
        else {
            ResultSet resultSet = databaseConnection.getData("select * from  View_subjects where GradeNo = '"+gradeNumber+"'");
            try {
                while (resultSet.next()){
                    Subjects subjects = new Subjects();
                    subjects.setSubjectNumber(resultSet.getString(1));
                    subjects.setSubjectName(resultSet.getString(2));
                    subjects.setDetails(resultSet.getString(3));
                    subjects.setImgURL(resultSet.getString(4));
                    subjects.setVideoURL(resultSet.getString(5));
                    subjects.setGradeNumber(resultSet.getString(6));
                    subjects.setGradeName(resultSet.getString(7));
                    subjects.setPhase(resultSet.getString(8));
                    subjectData.add(subjects);


                }

            }
            catch (SQLException ex){
                ;
            }

        }
        return subjectData;

    }
}
