package com.robpercival.workey;


import android.Manifest;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FileDownloadTask;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.File;
import java.io.IOException;
import java.util.UUID;

public class WorkerProfile extends AppCompatActivity {
    String workerIntentNumber;
    EditText workerProfileAge,workerProfileOldPassword, workerProfileNewPassword, workerProfileConfirmPassword, workerProfileFirstName, workerProfileLastName, workerProfileGender, workerProfileHouseNumber, workerProfileStreet, workerProfileArea, workerProfileCity, workerProfileNumber;

    String ProfileAge,ProfileFirstName, ProfileLastName, ProfileGender, ProfileHouseNumber, ProfileStreet, ProfileArea, ProfileCity, ProfileNumber, ProfileOldPassword, ProfileNewPassword, ProfileConfirmPassword;

    String num, gen, fname, lname, str, snumber, ar, ci, pass,oldPassword,age;
    DatabaseReference databaseReferenceProfile;
    ImageView workerImageDisplay;
    Uri uri;
    Bitmap bitmap;
    StorageReference storageReference;
    Button workerPasswordChange;

    Button workerProfileButton;

    public void setWorkerImage(View view) {
        try {
            bitmap = MediaStore.Images.Media.getBitmap(this.getContentResolver(), uri);
            workerImageDisplay.setImageBitmap(bitmap);
            Toast.makeText(WorkerProfile.this, "Image uploaded", Toast.LENGTH_SHORT).show();

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void workerImageClick(View view) {
        if (Build.VERSION.SDK_INT > Build.VERSION_CODES.M)
            if (checkSelfPermission(Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
                requestPermissions(new String[]{android.Manifest.permission.READ_EXTERNAL_STORAGE}, 1);
            } else
                getPhoto();
        else
            getPhoto();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == 1 && resultCode == RESULT_OK && data != null) {
            uri = data.getData();


            setWorkerImage(workerImageDisplay);
        }
    }

    public void getPhoto() {
        Intent intent1 = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        startActivityForResult(intent1, 1);


        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(intent, 1);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        if (requestCode == 1) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                getPhoto();
            }
        }
    }


    public void uploadPicture() {
        final ProgressDialog pd = new ProgressDialog(this);
        pd.setTitle("Progressing....");
        pd.show();

        final String randomKey = UUID.randomUUID().toString();
        StorageReference riversRef = storageReference.child("worker").child("images/" + workerIntentNumber);

        riversRef.putFile(uri)
                .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                    @Override
                    public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception exception) {
                        pd.dismiss();
                        Toast.makeText(WorkerProfile.this, "Image uploading Unsuccessfull", Toast.LENGTH_SHORT).show();
                    }
                })
                .addOnProgressListener(new OnProgressListener<UploadTask.TaskSnapshot>() {
                    @Override
                    public void onProgress(UploadTask.TaskSnapshot taskSnapshot) {
                        double progress
                                = (100.0
                                * taskSnapshot.getBytesTransferred()
                                / taskSnapshot.getTotalByteCount());
                        pd.setMessage(
                                "Uploaded "
                                        + (int) progress + "%");
                    }
                });

    }


    public void workerProfileUpdate(View view) {
        if (workerProfileButton.getText().equals("Edit Profile")) {
            workerImageDisplay.setClickable(true);
            workerProfileArea.setAlpha(1);
            workerProfileCity.setAlpha(1);
            workerProfileFirstName.setAlpha(1);
            workerProfileAge.setAlpha(1);
            workerProfileLastName.setAlpha(1);
            workerProfileGender.setAlpha(1);
            workerProfileHouseNumber.setAlpha(1);
            workerProfileStreet.setAlpha(1);
            workerImageDisplay.setEnabled(true);

            workerProfileArea.setFocusableInTouchMode(true);
            workerProfileCity.setFocusableInTouchMode(true);
            workerProfileFirstName.setFocusableInTouchMode(true);
            workerProfileLastName.setFocusableInTouchMode(true);
            workerProfileAge.setFocusableInTouchMode(true);
            workerProfileGender.setFocusableInTouchMode(true);
            workerProfileHouseNumber.setFocusableInTouchMode(true);
            workerProfileStreet.setFocusableInTouchMode(true);
            workerProfileButton.setText("Update Details");
        } else {

            fname = workerProfileFirstName.getText().toString();
            lname = workerProfileLastName.getText().toString();
            gen = workerProfileGender.getText().toString();
            snumber =workerProfileHouseNumber.getText().toString();
            age = workerProfileAge.getText().toString();
            str = workerProfileStreet.getText().toString();
            ar = workerProfileArea.getText().toString();
            ci = workerProfileCity.getText().toString();


            if (gen.isEmpty()) {
                workerProfileGender.setError("gender can not be empty");
            }
            if (fname.isEmpty()) {
                workerProfileFirstName.setError("first name can not be empty");
            }
            if (lname.isEmpty()) {
                workerProfileLastName.setError("last name can not be empty");
            }
            if (snumber.isEmpty()) {
                workerProfileHouseNumber.setError("shop number can not be empty");
            }
            if (str.isEmpty()) {
                workerProfileStreet.setError("street can not be empty");
            }
            if (ar.isEmpty()) {
                workerProfileArea.setError("area can not be empty");
            }
            if (ci.isEmpty()) {
                workerProfileCity.setError("city can not be empty");
            }
            if(age.isEmpty())
            {
                workerProfileAge.setError("age can not be empty");
            }

            if (!age.isEmpty() && !gen.isEmpty() && !fname.isEmpty() && !lname.isEmpty() && !snumber.isEmpty() && !str.isEmpty() && !ar.isEmpty() && !ci.isEmpty()) {
                databaseReferenceProfile.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        String x = "";
                        for (DataSnapshot snapshot : dataSnapshot.getChildren()) {


                            x = snapshot.child("mobile").getValue().toString();


                            if (workerIntentNumber.equals(x)) {
                                pass = snapshot.child("password").getValue().toString();

                                if (uri != null) {
                                    uploadPicture();
                                }
                                snapshot.getRef().child("firstName").setValue(fname);
                                snapshot.getRef().child("lastName").setValue(lname);
                                snapshot.getRef().child("gender").setValue(gen);
                                snapshot.getRef().child("houseNumber").setValue(snumber);
                                snapshot.getRef().child("age").setValue(age);
                                snapshot.getRef().child("street").setValue(str);
                                snapshot.getRef().child("area").setValue(ar);
                                snapshot.getRef().child("city").setValue(ci);

                                workerProfileButton.setText("Edit Profile");

                                Toast.makeText(WorkerProfile.this, "Details updated", Toast.LENGTH_SHORT).show();
                                Intent intent = new Intent(WorkerProfile.this, WorkerProfile.class);
                                intent.putExtra("workerIntentNumber", workerIntentNumber);
                                startActivity(intent);
                                finish();
                                break;
                            }
                        }

                    }


                    @Override
                    public void onCancelled(DatabaseError databaseError) {

                    }

                });

            }


        }


    }

    public void WorkerChangePassword(View view)
    {
        if(workerPasswordChange.getText().toString().equals("change password"))
        {   workerProfileOldPassword.setAlpha(1);
            workerProfileOldPassword.setFocusableInTouchMode(true);
            workerPasswordChange.setText("verify");
        }else if(workerPasswordChange.getText().toString().equals("verify"))
        {

            ProfileOldPassword = workerProfileOldPassword.getText().toString();
            if(ProfileOldPassword.isEmpty())
                workerProfileOldPassword.setError("password can not be empty");
            else {

                databaseReferenceProfile.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {

                        for(DataSnapshot snapshot : dataSnapshot.getChildren())
                        {


                            ProfileNumber = snapshot.child("mobile").getValue().toString();
                            if(workerIntentNumber.equals(ProfileNumber)){
                                oldPassword = snapshot.child("password").getValue().toString();break;
                            }
                        }
                        if(!ProfileOldPassword.equals(oldPassword)) {

                            workerProfileOldPassword.setError("check your password");
                        }else
                        {
                            workerPasswordChange.setText("update");
                            workerProfileOldPassword.setAlpha(0);
                            workerProfileOldPassword.setFocusableInTouchMode(false);
                            workerProfileNewPassword.setAlpha(1);
                            workerProfileNewPassword.setFocusableInTouchMode(true);
                            workerProfileConfirmPassword.setAlpha(1);
                            workerProfileConfirmPassword.setFocusableInTouchMode(true);

                        }
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {

                    }
                });


            }
        }else
        {

            ProfileNewPassword = workerProfileNewPassword.getText().toString();
            ProfileConfirmPassword = workerProfileConfirmPassword.getText().toString();

            if(ProfileNewPassword.isEmpty())
                workerProfileNewPassword.setError("password can not be empty");
            if(ProfileConfirmPassword.isEmpty())
                workerProfileConfirmPassword.setError("confirm password can not be empty");
            if(!ProfileConfirmPassword.equals(ProfileNewPassword))
                Toast.makeText(this, "Password not matched", Toast.LENGTH_LONG).show();
            if(!ProfileNewPassword.isEmpty() && !ProfileConfirmPassword.isEmpty() && ProfileConfirmPassword.equals(ProfileNewPassword) ) {
                databaseReferenceProfile.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {

                        for (DataSnapshot snapshot : dataSnapshot.getChildren()) {


                            ProfileNumber = snapshot.child("mobile").getValue().toString();
                            if (workerIntentNumber.equals(ProfileNumber)) {
                                snapshot.getRef().child("password").setValue(ProfileConfirmPassword);
                                break;
                            }
                        }
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {

                    }
                });
                workerProfileNewPassword.setAlpha(0);
                workerProfileNewPassword.setFocusableInTouchMode(false);
                workerProfileConfirmPassword.setAlpha(0);
                workerProfileConfirmPassword.setFocusableInTouchMode(false);
                workerPasswordChange.setText("change password");
                Toast.makeText(WorkerProfile.this, "Password updated", Toast.LENGTH_SHORT).show();

            }
        }

    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_worker_profile);

        Intent intent = getIntent();
        workerIntentNumber = intent.getStringExtra("workerIntentNumber");
        storageReference = FirebaseStorage.getInstance().getReference();

        workerProfileOldPassword = (EditText) findViewById(R.id.workerProfileOldPassword);
        workerProfileNewPassword = (EditText) findViewById(R.id.workerProfileNewPassword);
        workerProfileConfirmPassword = (EditText) findViewById(R.id.workerProfileConfirmPassword);
        workerPasswordChange = (Button) findViewById(R.id.workerChangePassword);
        workerProfileButton = (Button) findViewById(R.id.workerProfileButton);
        workerProfileFirstName = (EditText) findViewById(R.id.workerProfileFirstName);
        workerProfileLastName = (EditText) findViewById(R.id.workerProfileLastName);
        workerProfileGender = (EditText) findViewById(R.id.workerProfileGender);
        workerProfileHouseNumber = (EditText) findViewById(R.id.workerProfileHouseNumber);
        workerProfileStreet = (EditText) findViewById(R.id.workerProfileStreet);
        workerProfileAge = (EditText) findViewById(R.id.workerProfileAge);
        workerProfileArea = (EditText) findViewById(R.id.workerProfileArea);
        workerProfileCity = (EditText) findViewById(R.id.workerProfileCity);
        workerProfileNumber = (EditText) findViewById(R.id.workerProfileNumber);

        databaseReferenceProfile = FirebaseDatabase.getInstance().getReference("worker");

        databaseReferenceProfile.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                for(DataSnapshot snapshot : dataSnapshot.getChildren())
                {


                    ProfileNumber = snapshot.child("mobile").getValue().toString();
                    if(workerIntentNumber.equals(ProfileNumber)){
                        ProfileFirstName = snapshot.child("firstName").getValue().toString();
                        ProfileLastName = snapshot.child("lastName").getValue().toString();
                        ProfileGender = snapshot.child("gender").getValue().toString();
                        ProfileHouseNumber = snapshot.child("houseNumber").getValue().toString();
                        ProfileStreet = snapshot.child("street").getValue().toString();
                        ProfileAge = snapshot.child("age").getValue().toString();
                        ProfileArea = snapshot.child("area").getValue().toString();
                        ProfileCity = snapshot.child("city").getValue().toString();break;
                    }
                }
                workerProfileFirstName.setText(ProfileFirstName);
                workerProfileLastName.setText(ProfileLastName);
                workerProfileGender.setText(ProfileGender);
                workerProfileNumber.setText(ProfileNumber);
                workerProfileAge.setText(ProfileAge);
                workerProfileHouseNumber.setText(ProfileHouseNumber);
                workerProfileArea.setText(ProfileArea);
                workerProfileStreet.setText(ProfileStreet);
                workerProfileCity.setText(ProfileCity);


            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

        StorageReference storageReference = FirebaseStorage.getInstance().getReference().child("worker/images/" + workerIntentNumber);
        workerImageDisplay = (ImageView) findViewById(R.id.workerProfileImageDisplay);
        workerImageDisplay.setFocusableInTouchMode(false);
        workerImageDisplay.setEnabled(false);

        try {
            final File file = File.createTempFile("image","jpg");
            storageReference.getFile(file).addOnSuccessListener(new OnSuccessListener<FileDownloadTask.TaskSnapshot>() {
                        @Override
                        public void onSuccess(FileDownloadTask.TaskSnapshot taskSnapshot) {

                    Bitmap bitmap = BitmapFactory.decodeFile(file.getAbsolutePath());

                    workerImageDisplay.setImageBitmap(bitmap);

                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    Toast.makeText(WorkerProfile.this, "image failed to load", Toast.LENGTH_SHORT).show();
                }
            });
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
