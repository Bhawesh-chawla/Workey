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
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class WorkerWishlist extends AppCompatActivity {
    String workerIntentNumber,ownerAdvertismentNumber,shopNameAdvertisment,workDescriptionAdvertisment,workerDescriptionAdvertisment,salaryRangeAdvertisment,addressAdvertisment;
    ListView listWishList;
    String ownerMobileAdvertisment,workerFirstName,workerLastName;
    TextView noAdvertismentToShow;
    DatabaseReference databaseReference,databaseReferenceOwnerAdvertisment,databaseReferenceWorker;

    Map<String,String[]> workerHashMap = new LinkedHashMap<>();

    List<Map<String, String>> workerList = new ArrayList<>();
    List<String> ownerNumberList = new ArrayList<>();
    Set<Map<String,String>> workerSet = new LinkedHashSet<>();
    SimpleAdapter simpleAdapter;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_worker_wishlist);
        setTitle("My Wishlist");
        Intent intent = getIntent();
        workerIntentNumber = intent.getStringExtra("workerIntentNumber");
        listWishList = (ListView) findViewById(R.id.listWishList);
        noAdvertismentToShow = (TextView) findViewById(R.id.noAdvertismentToShow);

        simpleAdapter = new SimpleAdapter(getApplicationContext(), workerList, R.layout.listitem, new String[]{"First Line", "Second Line"},
                new int[]{R.id.ownerNameItem, R.id.ownerAreaItem});

        databaseReferenceWorker= FirebaseDatabase.getInstance().getReference("worker").child(workerIntentNumber);
        databaseReferenceWorker.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                workerFirstName = snapshot.child("firstName").getValue().toString();
                workerLastName = snapshot.child("lastName").getValue().toString();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });


        databaseReference = FirebaseDatabase.getInstance().getReference("workerWishlist").child(workerIntentNumber);

        databaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {


                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    ownerAdvertismentNumber = snapshot.getValue().toString();
                    ownerNumberList.add(ownerAdvertismentNumber);

                    databaseReferenceOwnerAdvertisment = FirebaseDatabase.getInstance().getReference("ownerAdvertisment").child(ownerAdvertismentNumber);
                    databaseReferenceOwnerAdvertisment.addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(DataSnapshot dataSnapshot1) {

                            shopNameAdvertisment = dataSnapshot1.child("shopName").getValue().toString();
                            workDescriptionAdvertisment = dataSnapshot1.child("workDescription").getValue().toString();
                            workerDescriptionAdvertisment = dataSnapshot1.child("workerDescription").getValue().toString();
                            ownerMobileAdvertisment = dataSnapshot1.child("number").getValue().toString();
                            salaryRangeAdvertisment = dataSnapshot1.child("salaryRange").getValue().toString();
                            addressAdvertisment = dataSnapshot1.child("shopNumber").getValue().toString() + " " + dataSnapshot1.child("street").getValue().toString() + " " + dataSnapshot1.child("area").getValue().toString() + " " + dataSnapshot1.child("city").getValue().toString();

                            workerHashMap.put(ownerMobileAdvertisment, new String[]{"Rs " + salaryRangeAdvertisment, "Address: " + shopNameAdvertisment + ", " + addressAdvertisment});

                            workerList.clear();
                            for (Map.Entry<String, String[]> entry : workerHashMap.entrySet()) {
                                Map<String, String> resultsMap = new LinkedHashMap<String, String>();
                                resultsMap.put("Mobile Number", entry.getKey());
                                resultsMap.put("First Line", entry.getValue()[0]);
                                resultsMap.put("Second Line", entry.getValue()[1]);
                                workerList.add(resultsMap);
                                noAdvertismentToShow.setAlpha(0);
                            }
                            listWishList.setAdapter(simpleAdapter);

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

        listWishList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Intent intent = new Intent();

                intent.setClass(WorkerWishlist.this, WorkerOwnerAccess.class);


                String send[] = workerList.get(i).toString().split(", ");
                String resend[] = send[0].split("=");
                intent.putExtra("workerOwnerMobileNumber", resend[1]);
                intent.putExtra("workerWishlistNumber", workerIntentNumber);
                intent.putExtra("WorkerFromWhere","WorkerWishList");
                startActivity(intent);
                finish();
            }
        });
    }

}
