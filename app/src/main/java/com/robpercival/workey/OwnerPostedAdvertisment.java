package com.robpercival.workey;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class OwnerPostedAdvertisment extends AppCompatActivity {
    String number;
    String flag;
    String sName,workDes,workerDes,sNumber,str,ar,ci,sRange,num;
    DatabaseReference databaseReference;
    TextView shopName,shopAddress,salaryRange,workDescription,workerDescription,noAdvertisment;
    Button newAdvertisment;
    LinearLayout layout;
    String x;

    FirebaseDatabase firebaseDatabase;


    public void deleteAdvertisment(View view)
    {

        new AlertDialog.Builder(this).setIcon(android.R.drawable.ic_dialog_alert)
                .setTitle("Confirm Delete")
                .setMessage("Advertisment will be deleted permanently")
                .setPositiveButton("ok", new DialogInterface.OnClickListener() {

                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        databaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
                            @Override
                            public void onDataChange(DataSnapshot dataSnapshot) {

                                for (DataSnapshot Snapshot: dataSnapshot.getChildren()) {

                                    x = Snapshot.child("number").getValue().toString();
                                    if(number.equals(x)) {
                                        Snapshot.getRef().removeValue();
                                        noAdvertisment.setAlpha(1);
                                        layout.setAlpha(0);
                                        newAdvertisment.setText("NEW ADVERTISMENT");
                                        break;
                                    }

                                }

                                Toast.makeText(OwnerPostedAdvertisment.this, "Advertisement deleted", Toast.LENGTH_SHORT).show();
                            }

                            @Override
                            public void onCancelled(DatabaseError databaseError) {

                            }
                        });
                    }
                })
                .setNegativeButton("cancel", null)
                .show();
    }










    public void showAdvertisment()
    {

        databaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                for(DataSnapshot snapshot : dataSnapshot.getChildren())
                {


                    num = snapshot.child("number").getValue().toString();
                    if(number.equals(num) ) {
                        sName = snapshot.child("shopName").getValue().toString();
                        workDes = snapshot.child("workDescription").getValue().toString();
                        workerDes = snapshot.child("workerDescription").getValue().toString();
                        sNumber = snapshot.child("shopNumber").getValue().toString();
                        sRange = snapshot.child("salaryRange").getValue().toString();
                        str = snapshot.child("street").getValue().toString();
                        ar = snapshot.child("area").getValue().toString();
                        ci = snapshot.child("city").getValue().toString();

                        noAdvertisment.setAlpha(0);
                        layout.setAlpha(1);
                        newAdvertisment.setText("UPDATE ADVERTISMENT");

                        break;
                    }


                }



                shopName.setText(sName);
                shopAddress.setText(sNumber + ", " + str + ", " + ar + ", " + ci);
                salaryRange.setText("Salary Range: " + sRange);
                workDescription.setText("Work Description: " + workDes);
                workerDescription.setText("Worker Description: " + workerDes);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });






    }

    public void addNewAdvertisment(View view)
    {
        Intent intent = new Intent(OwnerPostedAdvertisment.this,OwnerNewAdvertisment.class);
        intent.putExtra("ownerAdvertismentMobileNumber",number);
        startActivity(intent);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_owner_posted_advertisment);
        Intent intent = getIntent();
        number = intent.getStringExtra("ownerMobileNumber");
        Intent intent1  = getIntent();
        flag = intent1.getStringExtra("flag");

        databaseReference = FirebaseDatabase.getInstance().getReference("ownerAdvertisment");

        shopName = (TextView) findViewById(R.id.shopNameShowAdvertisment);
        shopAddress = (TextView) findViewById(R.id.shopAddressShowAdvertisment);
        salaryRange = (TextView)findViewById(R.id.salaryRangeShowAdvertisment);
        workDescription = (TextView) findViewById(R.id.workDescriptionShowAdvertisment);
        workerDescription = (TextView) findViewById(R.id.workerDescriptionShowAdvertisment);
        noAdvertisment = (TextView) findViewById(R.id.ownerNoAdvertismentPosted);


        newAdvertisment = (Button) findViewById(R.id.ownerNewAdvertisment);

        layout = (LinearLayout) findViewById(R.id.ownerAdvertismentCard);
        showAdvertisment();
        setTitle("My Advertisment");

    }
}
