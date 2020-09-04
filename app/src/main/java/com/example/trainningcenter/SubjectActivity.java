package com.example.trainningcenter;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.google.android.gms.tasks.Continuation;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.IOException;

import dmax.dialog.SpotsDialog;

public class SubjectActivity extends AppCompatActivity {
    private static final int PICK_IMAGE_REQUEST = 1234;
    EditText txtSubTitle,txtSubDetails,txtSubVideo,txtGradeSub,txtImgUrlSub;
    Button btnChoosePhoto,btnAddSubToFirebase;
    ImageView imgSubPhoto;
    Uri filePath;
    AlertDialog dialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_subject);
        dialog = new SpotsDialog.Builder().setContext(this).build();
        txtSubTitle = (EditText)findViewById(R.id.txtSubTitle);
        txtSubDetails = (EditText)findViewById(R.id.txtSubDetails);
        txtSubVideo = (EditText)findViewById(R.id.txtSubVideo);
        txtGradeSub = (EditText)findViewById(R.id.txtSubGrade);
        txtImgUrlSub = (EditText)findViewById(R.id.txtImgUrlSub);

        imgSubPhoto = (ImageView)findViewById(R.id.imgSubPhoto);
        btnChoosePhoto = (Button)findViewById(R.id.btnChoosePhoto);
        btnAddSubToFirebase = (Button)findViewById(R.id.btnAddSubToFirebase);

        btnChoosePhoto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent();
                intent.setType("image/*");
                intent.setAction(Intent.ACTION_GET_CONTENT);
                startActivityForResult(Intent.createChooser(intent, "Choose Image"), PICK_IMAGE_REQUEST);
            }
        });
        btnAddSubToFirebase.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                uploadMyImage();

            }
        });
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == PICK_IMAGE_REQUEST && resultCode == RESULT_OK && data != null && data.getData() != null) {
            filePath = data.getData();
            try {
                Bitmap sirisha = MediaStore.Images.Media.getBitmap(getApplication().getContentResolver(), filePath);
                imgSubPhoto.setImageBitmap(sirisha);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
    /*
    private void uploadFile() {
        //if there is a file to upload
        if (filePath != null) {
            //displaying a progress dialog while upload is going on

            dialog.setTitle("Uploading");
            dialog.show();
            //change the url according to your firebase app
            FirebaseStorage storage = FirebaseStorage.getInstance();

            StorageReference storageRef = storage.getReference();


            final StorageReference riversRef = storageRef.child("Images/"+txtSubTitle.getText());

            riversRef.putFile(filePath)
                    .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                            dialog.dismiss();

                            Task<Uri> downloadUri = riversRef.getDownloadUrl();
                            Log.d("DIRECTLINK", String.valueOf(downloadUri));
                            String generatedFilePath = downloadUri.toString(); ///
                            DatabaseConnection databaseConnection = new DatabaseConnection();
                            databaseConnection.connectDB(SubjectActivity.this);
                            String msg = databaseConnection.runDML("Insert into Subject values('"+txtSubTitle.getText()+"','"+txtSubDetails.getText()+"','"+generatedFilePath+"','"+txtSubVideo.getText()+"','"+txtGradeSub.getText()+"')");
                            if (msg.equals("Done"))
                                Toast.makeText(SubjectActivity.this, "Subject has been Uploaded successfully", Toast.LENGTH_SHORT).show();
                            else
                                Toast.makeText(SubjectActivity.this, "Error"+msg, Toast.LENGTH_SHORT).show();



                        }
                    })
                    .addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception exception) {
                            //if the upload is not successfull
                            //hiding the progress dialog
                            dialog.dismiss();

                            //and displaying error message
                            Toast.makeText(SubjectActivity.this, exception.getMessage(), Toast.LENGTH_LONG).show();
                        }
                    })
                    .addOnProgressListener(new OnProgressListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onProgress(UploadTask.TaskSnapshot taskSnapshot) {
                            //calculating progress percentage


                            //displaying percentage in progress dialog

                        }
                    });
        }
        //if there is not any file
        else {
            //you can display an error toast
        }
    }

     */
    public void uploadMyImage(){
        if (filePath != null) {

            //displaying a progress dialog while upload is going on

            dialog.setTitle("Uploading");
            dialog.show();
            //change the url according to your firebase app
            FirebaseStorage storage = FirebaseStorage.getInstance();

            StorageReference storageRef = storage.getReference();
            final StorageReference riversRef = storageRef.child("Images/"+txtSubTitle.getText());
            riversRef.putFile(filePath).continueWithTask(new Continuation<UploadTask.TaskSnapshot, Task<Uri>>() {
                @Override
                public Task<Uri> then(@NonNull Task<UploadTask.TaskSnapshot> task) throws Exception {
                    if (!task.isSuccessful()){
                        Toast.makeText(SubjectActivity.this, "Failed", Toast.LENGTH_SHORT).show();
                    }
                    return riversRef.getDownloadUrl();
                }
            }).addOnCompleteListener(new OnCompleteListener<Uri>() {
                @Override
                public void onComplete(@NonNull Task<Uri> task) {
                    if (task.isSuccessful()){
                        String downUri = task.getResult().toString();
                        Log.d("DIRECTLINK",downUri);
                        String generatedFilePath = downUri;
                        txtImgUrlSub.setText(generatedFilePath);
                        dialog.dismiss();
                        DatabaseConnection databaseConnection = new DatabaseConnection();
                        databaseConnection.connectDB(SubjectActivity.this);
                        String msg = databaseConnection.runDML("Insert into Subject values('"+txtSubTitle.getText()+"','"+txtSubDetails.getText()+"','"+generatedFilePath+"','"+txtSubVideo.getText()+"','"+txtGradeSub.getText()+"')");
                        if (msg.equals("Done"))
                            Toast.makeText(SubjectActivity.this, "Subject has been Uploaded successfully", Toast.LENGTH_SHORT).show();
                        else
                            Toast.makeText(SubjectActivity.this, "Error"+msg, Toast.LENGTH_SHORT).show();
                    }
                }
            });

        }



        }
}
