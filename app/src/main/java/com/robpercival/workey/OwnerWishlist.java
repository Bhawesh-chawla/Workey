package com.robpercival.workey;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TextView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

public class OwnerWishlist extends AppCompatActivity {
    String ownerIntentNumber,workerAdvertismentNumber,nameAdvertisment,addressAdvertisment;
    ListView ownerListWishList;
    String ownerFirstName,ownerLastName;
    TextView noAdvertismentToShow;
    DatabaseReference databaseReference,databaseReferenceWorkerAdvertisment,databaseReferenceOwner;

    Map<String,String[]> workerHashMap = new LinkedHashMap<>();

    List<Map<String, String>> workerList = new ArrayList<>();
    List<String> ownerNumberList = new ArrayList<>();
    SimpleAdapter simpleAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_owner_wishlist);
        Intent intent = getIntent();
        ownerIntentNumber = intent.getStringExtra("ownerIntentNumber");
        ownerListWishList = (ListView) findViewById(R.id.ownerListWishList);
        noAdvertismentToShow = (TextView) findViewById(R.id.noAdvertismentToShow);

        simpleAdapter = new SimpleAdapter(getApplicationContext(), workerList, R.layout.listitem, new String[]{"First Line", "Second Line"},
                new int[]{R.id.ownerNameItem, R.id.ownerAreaItem});

        databaseReferenceOwner= FirebaseDatabase.getInstance().getReference("owner").child(ownerIntentNumber);
        databaseReferenceOwner.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                ownerFirstName = snapshot.child("firstName").getValue().toString();
                ownerLastName = snapshot.child("lastName").getValue().toString();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });


        databaseReference = FirebaseDatabase.getInstance().getReference("ownerWishlist").child(ownerIntentNumber);

        databaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(final DataSnapshot dataSnapshot) {


                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    workerAdvertismentNumber = snapshot.getValue().toString();
                    ownerNumberList.add(workerAdvertismentNumber);

                    databaseReferenceWorkerAdvertisment = FirebaseDatabase.getInstance().getReference("worker").child(workerAdvertismentNumber);
                    databaseReferenceWorkerAdvertisment.addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(DataSnapshot dataSnapshot1) {


                            nameAdvertisment = dataSnapshot1.child("firstName").getValue().toString() +" "+ dataSnapshot1.child("lastName").getValue().toString();
                            addressAdvertisment = dataSnapshot1.child("houseNumber").getValue().toString() + " " + dataSnapshot1.child("street").getValue().toString() + " " + dataSnapshot1.child("area").getValue().toString() + " " + dataSnapshot1.child("city").getValue().toString();

                            workerHashMap.put(workerAdvertismentNumber, new String[]{"Name " + nameAdvertisment, "Address: " + addressAdvertisment});


                            for (Map.Entry<String, String[]> entry : workerHashMap.entrySet()) {

                                Map<String, String> resultsMap = new LinkedHashMap<String, String>();
                                resultsMap.put("Mobile Number", entry.getKey());
                                resultsMap.put("First Line", entry.getValue()[0]);
                                resultsMap.put("Second Line", entry.getValue()[1]);
                                workerList.add(resultsMap);
                                noAdvertismentToShow.setAlpha(0);
                            }
                            ownerListWishList.setAdapter(simpleAdapter);

                        }

                        @Override
                        public void onCancelled(DatabaseError databaseError) {


                        }
                    });
                }

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

        ownerListWishList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {



                String send[] = workerList.get(i).toString().split(", ");
                String resend[] = send[0].split("=");
                Intent intent = new Intent(OwnerWishlist.this,OwnerWorkerAccess.class);
                intent.putExtra("workerOwnerMobileNumber", resend[1]);
                intent.putExtra("ownerWishlistNumber", ownerIntentNumber);
                intent.putExtra("OwnerFromWhere","OwnerWishList");

                startActivity(intent);
                finish();
            }
        });
    }

}