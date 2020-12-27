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
import android.util.Log;
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

public class OwnerProfile extends AppCompatActivity {
    EditText ownerProfileOldPassword, ownerProfileNewPassword, ownerProfileConfirmPassword, ownerProfileFirstName, ownerProfileLastName, ownerProfileGender, ownerProfileShopNumber, ownerProfileStreet, ownerProfileArea, ownerProfileCity, ownerProfileNumber;

    String ProfileFirstName, ProfileLastName, ProfileGender, ProfileShopNumber, ProfileStreet, ProfileArea, ProfileCity, ProfileNumber, ownerIntentNumber, ProfileOldPassword, ProfileNewPassword, ProfileConfirmPassword;

    String num, gen, fname, lname, str, snumber, ar, ci, pass,oldPassword;
    DatabaseReference databaseReferenceProfile;
    ImageView ownerImageDisplay;
    Uri uri;
    Bitmap bitmap;
    StorageReference storageReference;
    Button ownerPasswordChange;

    Button ownerProfileButton;

    public void setOwnerImage(View view) {
        try {
            bitmap = MediaStore.Images.Media.getBitmap(this.getContentResolver(), uri);
            ownerImageDisplay.setImageBitmap(bitmap);
            Toast.makeText(OwnerProfile.this, "Image uploaded", Toast.LENGTH_SHORT).show();

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void ownerImageClick(View view) {
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


            setOwnerImage(ownerImageDisplay);
        }
    }

    public void getPhoto() {



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
        Intent intent = getIntent();
        String id = intent.getStringExtra("owner");

        StorageReference riversRef = storageReference.child("owner").child("images/" + num);

        riversRef.putFile(uri)
                .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                    @Override
                    public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                        pd.dismiss();

                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception exception) {
                        pd.dismiss();
                        Toast.makeText(OwnerProfile.this, "Image uploading Unsuccessfull", Toast.LENGTH_SHORT).show();
                        Log.i("imagehhfhfhffhfhjf", exception.toString());
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


    public void ownerProfileUpdate(View view) {
        if (ownerProfileButton.getText().equals("Edit Profile")) {
            ownerImageDisplay.setClickable(true);
            ownerProfileArea.setAlpha(1);
            ownerProfileCity.setAlpha(1);
            ownerProfileFirstName.setAlpha(1);
            ownerProfileLastName.setAlpha(1);
            ownerProfileGender.setAlpha(1);
            ownerProfileShopNumber.setAlpha(1);
            ownerProfileStreet.setAlpha(1);
            ownerImageDisplay.setEnabled(true);

            ownerProfileArea.setFocusableInTouchMode(true);
            ownerProfileCity.setFocusableInTouchMode(true);
            ownerProfileFirstName.setFocusableInTouchMode(true);
            ownerProfileLastName.setFocusableInTouchMode(true);
            ownerProfileGender.setFocusableInTouchMode(true);
            ownerProfileShopNumber.setFocusableInTouchMode(true);
            ownerProfileStreet.setFocusableInTouchMode(true);
            ownerProfileButton.setText("Update Details");
        } else {

            fname = ownerProfileFirstName.getText().toString();
            lname = ownerProfileLastName.getText().toString();
            gen = ownerProfileGender.getText().toString();
            snumber = ownerProfileShopNumber.getText().toString();
            str = ownerProfileStreet.getText().toString();
            ar = ownerProfileArea.getText().toString();
            ci = ownerProfileCity.getText().toString();


            if (gen.isEmpty()) {
                ownerProfileGender.setError("gender can not be empty");
            }
            if (fname.isEmpty()) {
                ownerProfileFirstName.setError("first name can not be empty");
            }
            if (lname.isEmpty()) {
                ownerProfileLastName.setError("last name can not be empty");
            }
            if (snumber.isEmpty()) {
                ownerProfileShopNumber.setError("shop number can not be empty");
            }
            if (str.isEmpty()) {
                ownerProfileStreet.setError("street can not be empty");
            }
            if (ar.isEmpty()) {
                ownerProfileArea.setError("area can not be empty");
            }
            if (ci.isEmpty()) {
                ownerProfileCity.setError("city can not be empty");
            }

            if (!gen.isEmpty() && !fname.isEmpty() && !lname.isEmpty() && !snumber.isEmpty() && !str.isEmpty() && !ar.isEmpty() && !ci.isEmpty()) {
                databaseReferenceProfile.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        String x ="";
                        for (DataSnapshot snapshot : dataSnapshot.getChildren()) {


                            x = snapshot.child("mobile").getValue().toString();


                            if (ownerIntentNumber.equals(x)) {
                                pass = snapshot.child("password").getValue().toString();

                                if (uri != null) {


                                    uploadPicture();
                                }
                                snapshot.getRef().child("firstName").setValue(fname);
                                snapshot.getRef().child("lastName").setValue(lname);
                                snapshot.getRef().child("gender").setValue(gen);
                                snapshot.getRef().child("shopNumber").setValue(snumber);
                                snapshot.getRef().child("street").setValue(str);
                                snapshot.getRef().child("area").setValue(ar);
                                snapshot.getRef().child("city").setValue(ci);

                                ownerProfileButton.setText("Edit Profile");

                                Toast.makeText(OwnerProfile.this, "Details updated", Toast.LENGTH_SHORT).show();
                                Intent intent = new Intent(OwnerProfile.this, OwnerProfile.class);
                                intent.putExtra("ownerProfileNumber", ownerIntentNumber);
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

    public void changePassword(View view)
    {
        if(ownerPasswordChange.getText().toString().equals("change password"))
        {   ownerProfileOldPassword.setAlpha(1);
            ownerProfileOldPassword.setFocusableInTouchMode(true);
            ownerPasswordChange.setText("verify");
        }else if(ownerPasswordChange.getText().toString().equals("verify"))
        {

            ProfileOldPassword = ownerProfileOldPassword.getText().toString();
            if(ProfileOldPassword.isEmpty())
                ownerProfileOldPassword.setError("password can not be empty");
            else {

                databaseReferenceProfile.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {

                    for(DataSnapshot snapshot : dataSnapshot.getChildren())
                    {


                        ProfileNumber = snapshot.child("mobile").getValue().toString();
                        if(ownerIntentNumber.equals(ProfileNumber)){
                            oldPassword = snapshot.child("password").getValue().toString();break;
                        }
                    }
                    if(!ProfileOldPassword.equals(oldPassword)) {

                        ownerProfileOldPassword.setError("check your password");
                    }else
                    {
                        ownerPasswordChange.setText("update");
                        ownerProfileOldPassword.setAlpha(0);
                        ownerProfileOldPassword.setFocusableInTouchMode(false);
                        ownerProfileNewPassword.setAlpha(1);
                        ownerProfileNewPassword.setFocusableInTouchMode(true);
                        ownerProfileConfirmPassword.setAlpha(1);
                        ownerProfileConfirmPassword.setFocusableInTouchMode(true);

                    }
                }

                @Override
                public void onCancelled(DatabaseError databaseError) {

                }
            });


            }
        }else
        {

            ProfileNewPassword = ownerProfileNewPassword.getText().toString();
            ProfileConfirmPassword = ownerProfileConfirmPassword.getText().toString();

            if(ProfileNewPassword.isEmpty())
                ownerProfileNewPassword.setError("password can not be empty");
            if(ProfileConfirmPassword.isEmpty())
                ownerProfileConfirmPassword.setError("confirm password can not be empty");
            if(!ProfileConfirmPassword.equals(ProfileNewPassword))
                Toast.makeText(this, "Password not matched", Toast.LENGTH_LONG).show();
            if(!ProfileNewPassword.isEmpty() && !ProfileConfirmPassword.isEmpty() && ProfileConfirmPassword.equals(ProfileNewPassword) ) {
                databaseReferenceProfile.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {

                        for (DataSnapshot snapshot : dataSnapshot.getChildren()) {


                            ProfileNumber = snapshot.child("mobile").getValue().toString();
                            if (ownerIntentNumber.equals(ProfileNumber)) {
                                snapshot.getRef().child("password").setValue(ProfileConfirmPassword);
                                break;
                            }
                        }
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {

                    }
                });
                ownerProfileNewPassword.setAlpha(0);
                ownerProfileNewPassword.setFocusableInTouchMode(false);
                ownerProfileConfirmPassword.setAlpha(0);
                ownerProfileConfirmPassword.setFocusableInTouchMode(false);
                ownerPasswordChange.setText("change password");
                Toast.makeText(OwnerProfile.this, "Password updated", Toast.LENGTH_SHORT).show();

            }
        }

    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_owner_profile);
        Intent intent = getIntent();
        ownerIntentNumber = intent.getStringExtra("ownerProfileNumber");

        storageReference = FirebaseStorage.getInstance().getReference();

        ownerProfileOldPassword = (EditText) findViewById(R.id.ownerProfileOldPassword);
        ownerProfileNewPassword = (EditText) findViewById(R.id.ownerProfileNewPassword);
        ownerProfileConfirmPassword = (EditText) findViewById(R.id.ownerProfileConfirmPassword);
        ownerPasswordChange = (Button) findViewById(R.id.ownerChangePassword);
        ownerProfileButton = (Button) findViewById(R.id.ownerProfileButton);
        ownerProfileFirstName = (EditText) findViewById(R.id.ownerProfileFirstName);
        ownerProfileLastName = (EditText) findViewById(R.id.ownerProfileLastName);
        ownerProfileGender = (EditText) findViewById(R.id.ownerProfileGender);
        ownerProfileShopNumber = (EditText) findViewById(R.id.ownerProfileShopNumber);
        ownerProfileStreet = (EditText) findViewById(R.id.ownerProfileStreet);
        ownerProfileArea = (EditText) findViewById(R.id.ownerProfileArea);
        ownerProfileCity = (EditText) findViewById(R.id.ownerProfileCity);
        ownerProfileNumber = (EditText) findViewById(R.id.ownerProfileNumber);

        databaseReferenceProfile = FirebaseDatabase.getInstance().getReference("owner");

        databaseReferenceProfile.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                for(DataSnapshot snapshot : dataSnapshot.getChildren())
                {


                    ProfileNumber = snapshot.child("mobile").getValue().toString();
                    if(ownerIntentNumber.equals(ProfileNumber)){
                    ProfileFirstName = snapshot.child("firstName").getValue().toString();
                    ProfileLastName = snapshot.child("lastName").getValue().toString();
                    ProfileGender = snapshot.child("gender").getValue().toString();
                    ProfileShopNumber = snapshot.child("shopNumber").getValue().toString();
                    ProfileStreet = snapshot.child("street").getValue().toString();
                    ProfileArea = snapshot.child("area").getValue().toString();
                    ProfileCity = snapshot.child("city").getValue().toString();break;
                    }
                }
                ownerProfileFirstName.setText(ProfileFirstName);
                ownerProfileLastName.setText(ProfileLastName);
                ownerProfileGender.setText(ProfileGender);
                ownerProfileNumber.setText(ProfileNumber);
                ownerProfileShopNumber.setText(ProfileShopNumber);
                ownerProfileArea.setText(ProfileArea);
                ownerProfileStreet.setText(ProfileStreet);
                ownerProfileCity.setText(ProfileCity);


            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

        FirebaseStorage storage = FirebaseStorage.getInstance();


        StorageReference storageReference = FirebaseStorage.getInstance().getReference().child("owner/images/" + ownerIntentNumber);
        ownerImageDisplay = (ImageView) findViewById(R.id.ownerProfileImageDisplay);
        ownerImageDisplay.setEnabled(false);

        try {
            final File file = File.createTempFile("image","jpg");
            storageReference.getFile(file).addOnSuccessListener(new OnSuccessListener<FileDownloadTask.TaskSnapshot>() {
                @Override
                public void onSuccess(FileDownloadTask.TaskSnapshot taskSnapshot) {

                    Bitmap bitmap = BitmapFactory.decodeFile(file.getAbsolutePath());

                    ownerImageDisplay.setImageBitmap(bitmap);

                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    Toast.makeText(OwnerProfile.this, "image failed to load", Toast.LENGTH_SHORT).show();
                }
            });
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
