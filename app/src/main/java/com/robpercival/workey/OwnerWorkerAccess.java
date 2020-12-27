package com.robpercival.workey;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;


import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FileDownloadTask;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.io.File;
import java.io.IOException;

public class OwnerWorkerAccess extends AppCompatActivity {
    DatabaseReference databaseReferenceWorker, databaseReferenceWishlist;
    StorageReference storageReferenceWorker;
    String number, workerName, workerAge, workerGender, workerAddress, ownerWishNumber,fromWhere;
    TextView nameWorker, ageWorker, addressWorker, genderWorker;
    ImageView workerImageDisplay,toWorkerCall;
    Button ownerWishlist;

    public void ToWorkerCall(View view)
    {
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.CALL_PHONE) == PackageManager.PERMISSION_GRANTED) {

            Intent i = new Intent(Intent.ACTION_CALL);
            i.setData(Uri.parse("tel:" + number));
            startActivity(i);

        }else{
            ActivityCompat.requestPermissions(this,new String[]{Manifest.permission.CALL_PHONE},0);



        }

    }
    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions,
                                           int[] grantResults) {
        switch (requestCode) {

            case 0:{
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    if (ActivityCompat.checkSelfPermission(OwnerWorkerAccess.this,
                            Manifest.permission.CALL_PHONE) == PackageManager.PERMISSION_GRANTED) {
                        Toast.makeText(this, "Permission Granted", Toast.LENGTH_SHORT).show();

                        Intent i = new Intent(Intent.ACTION_CALL);
                        i.setData(Uri.parse("tel:" + number));
                        startActivity(i);
                    }

                } else {
                    Toast.makeText(this, "Permission Denied", Toast.LENGTH_SHORT).show();
                }


                return;
            }
        }
    }


    public void OwnerWishlist(View view) {
        if (ownerWishlist.getText().toString().equals("add to wishlist")) {
            databaseReferenceWishlist.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    databaseReferenceWishlist.child(number).setValue(number);
                    ownerWishlist.setText("remove from wishlist");
                    Toast.makeText(OwnerWorkerAccess.this, "Added to Wishlist", Toast.LENGTH_SHORT).show();
                }


                @Override
                public void onCancelled(DatabaseError databaseError) {

                }


            });
        } else {
            databaseReferenceWishlist.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {

                    databaseReferenceWishlist.getRef().child(number).removeValue();
                }


                @Override
                public void onCancelled(DatabaseError databaseError) {

                }


            });
            Toast.makeText(this, "Removed from your wishlist", Toast.LENGTH_SHORT).show();
            ownerWishlist.setText("add to wishlist");


        }
    }
    public void ToChatsOwner(View view)
    {
        Intent intent5 = new Intent(OwnerWorkerAccess.this,OwnerWorkerChats.class);
        intent5.putExtra("WorkerNumber",number);
        intent5.putExtra("OwnerNumber",ownerWishNumber);
        intent5.putExtra("fromWhere","owner");
        intent5.putExtra("name",workerName);
        startActivity(intent5);
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_owner_worker_access);
        ownerWishlist = (Button) findViewById(R.id.ownerAddToWishlist);


        toWorkerCall = (ImageView) findViewById(R.id.toWorkerCall);
        Intent intent = getIntent();
        number = intent.getStringExtra("workerOwnerMobileNumber");
        ownerWishNumber = intent.getStringExtra("ownerWishlistNumber");
        fromWhere = intent.getStringExtra("OwnerFromWhere");
        workerImageDisplay = (ImageView) findViewById(R.id.ownerWorkerImage);
        databaseReferenceWishlist = FirebaseDatabase.getInstance().getReference("ownerWishlist").child(ownerWishNumber);
        databaseReferenceWorker = FirebaseDatabase.getInstance().getReference("worker").child(number);

        storageReferenceWorker = FirebaseStorage.getInstance().getReference().child("worker/images/" + number);

        try {
            final File fileLocation = File.createTempFile("img", "jpg");

            storageReferenceWorker.getFile(fileLocation)
                    .addOnSuccessListener(new OnSuccessListener<FileDownloadTask.TaskSnapshot>() {
                        @Override
                        public void onSuccess(FileDownloadTask.TaskSnapshot taskSnapshot) {
                            Bitmap bitmap = BitmapFactory.decodeFile(fileLocation.getAbsolutePath());
                            workerImageDisplay.setImageBitmap(bitmap);
                        }
                    })
                    .addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            Toast.makeText(OwnerWorkerAccess.this, "Failure", Toast.LENGTH_SHORT).show();
                        }
                    });

        } catch (IOException e) {
            e.printStackTrace();
        }
        databaseReferenceWishlist.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                if (dataSnapshot.exists() && dataSnapshot.child(number).exists()) {
                    ownerWishlist.setText("remove from wishlist");
                }
            }


            @Override
            public void onCancelled(DatabaseError databaseError) {

            }


        });


        databaseReferenceWorker.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {


                workerName = dataSnapshot.child("firstName").getValue().toString() + " " + dataSnapshot.child("lastName").getValue().toString();
                workerAge = dataSnapshot.child("age").getValue().toString();
                workerAddress = dataSnapshot.child("houseNumber").getValue().toString() + " " +
                        dataSnapshot.child("street").getValue().toString() + " " +
                        dataSnapshot.child("area").getValue().toString() + " " +
                        dataSnapshot.child("city").getValue().toString();
                workerGender = dataSnapshot.child("gender").getValue().toString();


                nameWorker = (TextView) findViewById(R.id.ownerWorkerName);
                ageWorker = (TextView) findViewById(R.id.ownerWorkerAge);
                addressWorker = (TextView) findViewById(R.id.ownerWorkerAddress);
                genderWorker = (TextView) findViewById(R.id.ownerWorkerGender);

                nameWorker.setText("NAME:          " + workerName);
                ageWorker.setText("AGE:             " + workerAge);
                addressWorker.setText("ADDRESS:    " + workerAddress);
                genderWorker.setText("GENDER:      " + workerGender);


            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });


        setTitle("Worker Details");

    }

    @Override
    public void onBackPressed() {
        if(fromWhere.equals("OwnerHomefeed"))
        super.onBackPressed();
        else if (fromWhere.equals("OwnerWishList"))
        {
                    Intent intent2 = new Intent(OwnerWorkerAccess.this, OwnerWishlist.class);
                    intent2.putExtra("ownerIntentNumber", ownerWishNumber);
                    Log.i("owner number", ownerWishNumber);
                    startActivity(intent2);
                    finish();
        }
    }
}
