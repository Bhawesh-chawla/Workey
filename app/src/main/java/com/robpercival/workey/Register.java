package com.robpercival.workey;

import android.app.ProgressDialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;

import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RadioButton;
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
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.IOException;
import java.util.UUID;


public class Register extends AppCompatActivity {
    FirebaseStorage storage;
    StorageReference storageReference;
    Uri uri;

    EditText firstName,lastName,number,password,houseNumber,street,area,city,age;
    String a = null,b = null,num,pass,fname,lname,hnumber,str,ar,ci,gen = "male",ag;
    ImageView workerPhoto;

    int flag = 0;
    Bitmap bitmap;


    FirebaseDatabase firebaseDatabase;
    DatabaseReference databaseReference;

    public void setWorkerImage(View view)
    {
        try {
            bitmap = MediaStore.Images.Media.getBitmap(this.getContentResolver(),uri);
            workerPhoto.setImageBitmap(bitmap);
            Toast.makeText(Register.this, "Image uploaded", Toast.LENGTH_SHORT).show();

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void workerImageClick(View view)
    {
        if(Build.VERSION.SDK_INT > Build.VERSION_CODES.M)
            if(checkSelfPermission(android.Manifest.permission.READ_EXTERNAL_STORAGE)!= PackageManager.PERMISSION_GRANTED)
            {
                requestPermissions(new String[]{android.Manifest.permission.READ_EXTERNAL_STORAGE},1);
            }
            else
                getPhoto();
        else
            getPhoto();
    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if(requestCode == 1 && resultCode == RESULT_OK && data != null) {
            uri = data.getData();


            flag = 1;
            setWorkerImage(workerPhoto);
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

        if(requestCode == 1) {
            if(grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
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
        String id = intent.getStringExtra("worker");

        StorageReference riversRef = storageReference.child("worker").child("images/" + num);

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
                        Toast.makeText(Register.this, "Unsuccessfull", Toast.LENGTH_SHORT).show();
                        Log.i("imagehhfhfhffhfhjf",exception.toString());
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
                                        + (int)progress + "%");
                    }
                });

    }

    public void onRadioButtonClicked(View view) {
        boolean checked = ((RadioButton) view).isChecked();
        switch(view.getId()) {
            case R.id.workerMale:
                if (checked)
                    gen = "male";
                    break;
            case R.id.workerFemale:
                if (checked)
                    gen = "female";
                    break;
        }
    }

    public void WorkerRegister(View view) {


        pass = password.getText().toString();
        fname = firstName.getText().toString();
        lname = lastName.getText().toString();
        hnumber = houseNumber.getText().toString();
        str = street.getText().toString();
        ar = area.getText().toString();
        ci = city.getText().toString();
        ag = age.getText().toString();

        if (num.isEmpty()) {
            number.setError("number can not be empty");
        }
        if (pass.isEmpty()) {
            password.setError("password can not be empty");
        }
        if (fname.isEmpty()) {
            firstName.setError("first name can not be empty");
        }
        if (lname.isEmpty()) {
            lastName.setError("last name can not be empty");
        }
        if (hnumber.isEmpty()) {
            houseNumber.setError("house number can not be empty");
        }
        if (str.isEmpty()) {
            street.setError("street can not be empty");
        }
        if (ar.isEmpty()) {
            area.setError("area can not be empty");
        }
        if (ci.isEmpty()) {
            city.setError("city can not be empty");
        }
        if(ag.isEmpty())
        {
            age.setError("age can not be empty");
        }else if(Integer.parseInt(ag) < 14)
        {
            age.setError("age should be greater than 14");
        }



        if (!num.isEmpty() && !pass.isEmpty() && !fname.isEmpty() && !lname.isEmpty() && !hnumber.isEmpty() && !str.isEmpty() && !ar.isEmpty() && !ci.isEmpty() && !ag.isEmpty() && Integer.parseInt(ag)>= 14 ) {
            databaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    String x = "";
                    for (DataSnapshot snapshot : dataSnapshot.getChildren()) {


                        x = snapshot.child("mobile").getValue().toString();


                        if (num.equals(x)) {
                            a = x;


                            break;
                        }
                    }
                    if (num.equals(a)) {

                        Toast.makeText(Register.this, "user already exist", Toast.LENGTH_SHORT).show();
                    } else {

                        if(uri!= null) {
                            uploadPicture();

                            WorkerPojo pojo = new WorkerPojo();
                            pojo.setMobile(num);
                            pojo.setPassword(pass);
                            pojo.setFirstName(fname);
                            pojo.setLastName(lname);
                            pojo.setHouseNumber(hnumber);
                            pojo.setStreet(str);
                            pojo.setArea(ar);
                            pojo.setCity(ci);
                            pojo.setGender(gen);
                            pojo.setAge(ag);

                            databaseReference.child(num).setValue(pojo);


                            Toast.makeText(Register.this, "Registered successfully", Toast.LENGTH_SHORT).show();
                            Intent intent = new Intent(Register.this, Login.class);
                            startActivity(intent);
                        }else
                            Toast.makeText(Register.this, "please upload your image", Toast.LENGTH_LONG).show();

                    }


                }

                @Override
                public void onCancelled(DatabaseError databaseError) {

                }

            });

        }

    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        setTitle("Register");
        firstName = (EditText) findViewById(R.id.workerFirstName) ;
        lastName = (EditText) findViewById(R.id.workerLastName);
        number = (EditText) findViewById(R.id.workerMobileNumber);
        password = (EditText) findViewById(R.id.workerPassword);
        houseNumber = (EditText) findViewById(R.id.workerHouseNumber);
        street = (EditText) findViewById(R.id.workerStreet);
        area = (EditText) findViewById(R.id.workerArea);
        city = (EditText) findViewById(R.id.workerCity);
        age = (EditText) findViewById(R.id.workerAge);
        Intent intent = getIntent();
        num = intent.getStringExtra("workerNumber");
        number.setText(num);
        number.setFocusable(false);

        databaseReference = FirebaseDatabase.getInstance().getReference("worker");
        workerPhoto  = (ImageView) findViewById(R.id.workerPhoto);

        storage = FirebaseStorage.getInstance();
        storageReference = storage.getReference();
    }
}

