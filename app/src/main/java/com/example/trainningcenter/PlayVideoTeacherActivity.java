package com.example.trainningcenter;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import com.google.android.youtube.player.YouTubeBaseActivity;
import com.google.android.youtube.player.YouTubeInitializationResult;
import com.google.android.youtube.player.YouTubePlayer;
import com.google.android.youtube.player.YouTubePlayerView;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class PlayVideoTeacherActivity extends YouTubeBaseActivity {
    private static final String TAG = "PlayVideoTeacherActivit";
    YouTubePlayerView youTubePlayerView;
    Button btnPlayVideo;
    Teacher teacher;
    YouTubePlayer.OnInitializedListener mOnIntializeListener;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_play_video_teacher);
        Log.d(TAG,"onCreate Starting.");
        youTubePlayerView = (YouTubePlayerView)findViewById(R.id.youTubeVideoPlayer);
        btnPlayVideo = (Button)findViewById(R.id.btnPlayVideo);
        mOnIntializeListener = new YouTubePlayer.OnInitializedListener() {
            @Override
            public void onInitializationSuccess(YouTubePlayer.Provider provider, YouTubePlayer youTubePlayer, boolean b) {
                Log.d(TAG,"onClick: Done Initialize.");
                DatabaseConnection databaseConnection = new DatabaseConnection();
                databaseConnection.connectDB(PlayVideoTeacherActivity.this);
                ResultSet rss= databaseConnection.getData("select TeacherVideo from Teacher where TeacherUsername   = '"+TeacherActivity.teacherUserName+"'");
                try{
                    while (rss.next()){
                        String videoTeacher = rss.getString(1);
                        youTubePlayer.loadVideo(videoTeacher);

                    }

                }catch (SQLException ex){
                    ex.printStackTrace();
                }




            }

            @Override
            public void onInitializationFailure(YouTubePlayer.Provider provider, YouTubeInitializationResult youTubeInitializationResult) {
                Log.d(TAG,"onClick: Failed To Initialize.");


            }
        };
        btnPlayVideo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d(TAG,"onClick: Initialize YouTube Player.");
                youTubePlayerView.initialize(YouTubeConfigure.getApiKey(),mOnIntializeListener);

            }
        });
    }
}
