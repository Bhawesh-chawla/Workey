package com.robpercival.workey;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.io.IOException;
import java.util.List;
import java.util.Locale;

public class WorkerOwnerAccess extends AppCompatActivity {
    DatabaseReference databaseReferenceOwnerAdvertisment, databaseReferenceWishlist;
    String number;
    TextView shopNameAdvertisment, workDescriptionAdvertisment, workerDescriptionAdvertisment, addressAdvertisment, salaryRangeAdvertisment;
    ImageView ownerImageDisplay, toOwnerCall,workerOwnerChats;
    String ownerNumber, address, workerDesciption, workDescription, salaryRange, shopName, fromWhere;
    Button workerWishlist;
    String workerWishNumber;

    final StringBuilder strReturnedAddress = new StringBuilder("");

    public void toOwnerShop(View view) {


        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            try {
                MapsSwitch();
                Uri uri = Uri.parse("http://www.google.co.in/maps/dir/" + strReturnedAddress.toString() + "/" + address);
                Intent intentmap = new Intent(Intent.ACTION_VIEW, uri);
                intentmap.setPackage("com.google.android.apps.maps");
                intentmap.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(intentmap);


            } catch (ActivityNotFoundException e) {
                Uri uri = Uri.parse("http://play.google.com/store/apps/details?id=com.google.android.apps.maps");
                Intent intentPlay = new Intent(Intent.ACTION_VIEW, uri);
                intentPlay.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(intentPlay);
            }
        }
    }

    public void WorkerWishlist(View view) {
        if (workerWishlist.getText().toString().equals("add to wishlist")) {
            databaseReferenceWishlist.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    databaseReferenceWishlist.child(ownerNumber).setValue(ownerNumber);
                    workerWishlist.setText("remove from wishlist");
                    Toast.makeText(WorkerOwnerAccess.this, "Added to Wishlist", Toast.LENGTH_SHORT).show();

                }


                @Override
                public void onCancelled(DatabaseError databaseError) {

                }


            });
        } else {
            databaseReferenceWishlist.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {

                    databaseReferenceWishlist.getRef().child(ownerNumber).removeValue();
                }


                @Override
                public void onCancelled(DatabaseError databaseError) {

                }


            });
            Toast.makeText(this, "Removed from your wishlist", Toast.LENGTH_SHORT).show();
            workerWishlist.setText("add to wishlist");


        }
    }

    public void MapsSwitch() {
        FusedLocationProviderClient fusedLocationProviderClient;
        fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(WorkerOwnerAccess.this);
        if (ActivityCompat.checkSelfPermission(WorkerOwnerAccess.this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
            fusedLocationProviderClient.getLastLocation()
                    .addOnCompleteListener(new OnCompleteListener<Location>() {
                        @SuppressLint("LongLogTag")
                        @Override
                        public void onComplete(@NonNull Task<Location> task) {
                            Location location = task.getResult();
                            if (location != null) {


                                try {
                                    Geocoder geocoder = new Geocoder(WorkerOwnerAccess.this, Locale.getDefault());
                                    List<Address> addresses = geocoder.getFromLocation(
                                            location.getLatitude(), location.getLongitude(), 1

                                    );
                                    if (addresses != null) {
                                        Address returnedAddress = addresses.get(0);


                                        for (int i = 0; i <= returnedAddress.getMaxAddressLineIndex(); i++) {
                                            strReturnedAddress.append(returnedAddress.getAddressLine(i)).append("\n");
                                        }
                                        String strAdd = strReturnedAddress.toString();
                                        Log.w("Current loction", strReturnedAddress.toString());
                                    }
                                } catch (IOException e) {
                                    e.printStackTrace();
                                }


                            }


                        }
                    });


        } else {
            Toast.makeText(WorkerOwnerAccess.this, "please on location", Toast.LENGTH_SHORT).show();
            ActivityCompat.requestPermissions(WorkerOwnerAccess.this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, 1);
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions,
                                           int[] grantResults) {
        switch (requestCode) {
            case 1: {
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    if (ActivityCompat.checkSelfPermission(WorkerOwnerAccess.this,
                            Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
                        Toast.makeText(this, "Permission Granted", Toast.LENGTH_SHORT).show();
                    }
                } else {
                    Toast.makeText(this, "Permission Denied", Toast.LENGTH_SHORT).show();
                }
                return;
            }
            case 0:{
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    if (ActivityCompat.checkSelfPermission(WorkerOwnerAccess.this,
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

    public void ToOwnerCall(View view) {

        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.CALL_PHONE) == PackageManager.PERMISSION_GRANTED) {

            Intent i = new Intent(Intent.ACTION_CALL);
            i.setData(Uri.parse("tel:" + number));
            startActivity(i);

        }else{
            ActivityCompat.requestPermissions(this,new String[]{Manifest.permission.CALL_PHONE},0);



        }

    }
    public void ToChats(View view)
    { DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference("owner").child(number);
    databaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
        @Override
        public void onDataChange(@NonNull DataSnapshot snapshot) {
            Intent intent5 = new Intent(WorkerOwnerAccess.this,OwnerWorkerChats.class);
            intent5.putExtra("WorkerNumber",workerWishNumber);
            intent5.putExtra("OwnerNumber",number);
            intent5.putExtra("name",snapshot.child("firstName").getValue()+" "+snapshot.child("lastName").getValue());
            intent5.putExtra("fromWhere","worker");
            startActivity(intent5);
        }

        @Override
        public void onCancelled(@NonNull DatabaseError error) {

        }
    });

    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_worker_owner_access);
        workerWishlist = (Button) findViewById(R.id.workerAddToWishlist);
        toOwnerCall = (ImageView) findViewById(R.id.toOwnerCall);
        workerOwnerChats = (ImageView) findViewById(R.id.ownerWorkerChats);

        Intent intent = getIntent();
        number = intent.getStringExtra("workerOwnerMobileNumber");
        ownerImageDisplay = (ImageView) findViewById(R.id.workerOwnerImage);
        workerWishNumber = intent.getStringExtra("workerWishlistNumber");
        fromWhere = intent.getStringExtra("WorkerFromWhere");
        databaseReferenceOwnerAdvertisment = FirebaseDatabase.getInstance().getReference("ownerAdvertisment");
        databaseReferenceWishlist = FirebaseDatabase.getInstance().getReference("workerWishlist").child(workerWishNumber);

        databaseReferenceWishlist.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                if(dataSnapshot.exists() && dataSnapshot.child(number).exists())
                {
                    workerWishlist.setText("remove from wishlist");
                }
            }


            @Override
            public void onCancelled(DatabaseError databaseError) {

            }


        });


        databaseReferenceOwnerAdvertisment.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {



                for(DataSnapshot snapshot : dataSnapshot.getChildren())
                {


                    ownerNumber = snapshot.child("number").getValue().toString();
                    if(number.equals(ownerNumber)) {
                        shopName = snapshot.child("shopName").getValue().toString();
                        workerDesciption = snapshot.child("workerDescription").getValue().toString();
                        address = snapshot.child("shopNumber").getValue().toString() + ", " +
                                snapshot.child("street").getValue().toString() + ", " +
                                snapshot.child("area").getValue().toString() + ", " +
                                snapshot.child("city").getValue().toString();
                        salaryRange = snapshot.child("salaryRange").getValue().toString();
                        workDescription = snapshot.child("workDescription").getValue().toString();
                        break;
                    }

                }
                shopNameAdvertisment  = (TextView) findViewById(R.id.workerOwnerShopName);
                workerDescriptionAdvertisment = (TextView) findViewById(R.id.workerOwnerWorkerDescription);
                workDescriptionAdvertisment = (TextView) findViewById(R.id.workerOwnerWorkDescription);
                addressAdvertisment = (TextView) findViewById(R.id.workerOwnerAddress);
                salaryRangeAdvertisment = (TextView) findViewById(R.id.workerOwnerSalaryRange);

                shopNameAdvertisment.setText("SHOP NAME: "+shopName);
                workDescriptionAdvertisment.setText("WORK DESCRIPTION: "+workDescription);
                workerDescriptionAdvertisment.setText("WORKER DESCRIPTION: "+workerDesciption);
                addressAdvertisment.setText("ADDRESS: "+address);
                salaryRangeAdvertisment.setText("SALARY RANGE: Rs " + salaryRange);


            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });





        setTitle("Owner Shop Details");

    }

    @Override
    public void onBackPressed() {
        if(fromWhere.equals("WorkerHomefeed"))
        super.onBackPressed();
        else if (fromWhere.equals("WorkerWishList"))
        {
            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    Intent intent2 = new Intent(WorkerOwnerAccess.this,WorkerWishlist.class);
                    intent2.putExtra("workerIntentNumber",workerWishNumber);
                    Log.i("worker number",workerWishNumber);
                    startActivity(intent2);
                    finish();
                }
            },1000);

        }
    }
}