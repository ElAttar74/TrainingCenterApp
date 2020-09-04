package com.example.trainningcenter;
import android.content.Context;
import android.os.StrictMode;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
public class DatabaseConnection {
    Connection sqlConnect;
    public Connection connectDB(Context connect){
        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);
        try {
            Class.forName("net.sourceforge.jtds.jdbc.Driver");
            sqlConnect = DriverManager.getConnection("jdbc:jtds:sqlserver://SQL5046.site4now.net/DB_A4CBFB_TrainningCenter",
                    "DB_A4CBFB_TrainningCenter_admin","androidapp74");
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return sqlConnect;
    }
    public ResultSet getData (String st){
        ResultSet myData=null;
        try {
            Statement statement = sqlConnect.createStatement();
            myData=statement.executeQuery(st);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return myData;
    }

    public String runDML (String st){
        try {
            Statement statement=sqlConnect.createStatement();
            statement.executeUpdate(st);
            return "Done";
        } catch (SQLException e) {
            e.printStackTrace();
            return e.getMessage();
        }
    }
}
