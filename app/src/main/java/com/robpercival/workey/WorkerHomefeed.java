package com.robpercival.workey;

import android.content.Intent;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.SearchView;
import android.widget.SimpleAdapter;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

public class WorkerHomefeed extends AppCompatActivity {

    DatabaseReference databaseReferenceWorker;
    DatabaseReference databaseReferenceOwnerAdvertisment;
    String cityAdvertisment,mobileNumberAdvertisment,workerIntentNumber,shopNameAdvertisment,cityOfWorker,workDescriptionAdvertisment,workerDescriptionAdvertisment,addressAdvertisment,salaryRangeAdvertisment;
    public void ToChatUser(View view)
    {
        Intent intent = new Intent(WorkerHomefeed.this,ChatUsers.class);
        intent.putExtra("workerIntentNumber",workerIntentNumber);
        intent.putExtra("fromWhere","worker");
        startActivity(intent);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu)
    {
        getMenuInflater().inflate(R.menu.workermenu,menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected( MenuItem item) {
        int id = item.getItemId();
        if(id == R.id.workerProfile)
        {
            Intent intent = new Intent(WorkerHomefeed.this,WorkerProfile.class);
            intent.putExtra("workerIntentNumber",workerIntentNumber);
            startActivity(intent);
        }else if(id == R.id.workerWishlist)
        {
            Intent intent = new Intent(WorkerHomefeed.this,WorkerWishlist.class);
            intent.putExtra("workerIntentNumber",workerIntentNumber);
            startActivity(intent);
        }else
        {
            SharedPreferences sharedPreferences= getSharedPreferences("LoginPreferences",MODE_PRIVATE);
            sharedPreferences.edit()
                    .putString("mobileNumber",null)
                    .putString("firstName",null)
                    .putString("lastName",null)
                    .putString("fromWhere",null)
                    .putString("city",null)
                    .apply();
            Toast.makeText(WorkerHomefeed.this, "Logout Successful", Toast.LENGTH_SHORT).show();
            Intent intent = new Intent(WorkerHomefeed.this,MainActivity.class);
            startActivity(intent);
            finish();


        }
        return super.onOptionsItemSelected(item);
    }



    String firstName,lastName;

    Map<String,String[]> workerHashMap = new LinkedHashMap<>();

    List<Map<String, String>> workerList = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_worker_homefeed);

        Intent intent = getIntent();
        workerIntentNumber = intent.getStringExtra("workerMobile");
        firstName = intent.getStringExtra("workerFirstName");
        lastName = intent.getStringExtra("workerLastName");
        setTitle("Hi "+firstName+"!");
        SharedPreferences sharedPreferences = getSharedPreferences("LoginPreferences",MODE_PRIVATE);

        cityOfWorker = sharedPreferences.getString("city",null);
        final ListView workerHomeList;
        SearchView workerHomeSearch;
        workerHomeList = (ListView) findViewById(R.id.workerHomeListView);

        final SimpleAdapter simpleAdapter = new SimpleAdapter(getApplicationContext(), workerList, R.layout.listitem, new String[]{"First Line", "Second Line"},
                new int[]{R.id.ownerNameItem, R.id.ownerAreaItem});
        workerHomeSearch = (SearchView) findViewById(R.id.workerHomeSearch);

        databaseReferenceWorker = FirebaseDatabase.getInstance().getReference("worker");
        databaseReferenceOwnerAdvertisment = FirebaseDatabase.getInstance().getReference("ownerAdvertisment");


        databaseReferenceOwnerAdvertisment.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {


                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {


                    cityAdvertisment = snapshot.child("city").getValue().toString();
                    if (cityAdvertisment.equals(cityOfWorker)) {

                        shopNameAdvertisment = snapshot.child("shopName").getValue().toString();
                        workDescriptionAdvertisment = snapshot.child("workDescription").getValue().toString();
                        workerDescriptionAdvertisment = snapshot.child("workerDescription").getValue().toString();
                        mobileNumberAdvertisment = snapshot.child("number").getValue().toString();
                        salaryRangeAdvertisment = snapshot.child("salaryRange").getValue().toString();
                        addressAdvertisment = snapshot.child("shopNumber").getValue().toString() + " " + snapshot.child("street").getValue().toString() + " " + snapshot.child("area").getValue().toString();
                        workerHashMap.put(mobileNumberAdvertisment, new String[]{"Rs " + salaryRangeAdvertisment, "Address: " + shopNameAdvertisment + ", " + addressAdvertisment + " " + cityOfWorker});


                    }


                }

                for (Map.Entry<String, String[]> entry : workerHashMap.entrySet()) {

                    Map<String, String> resultsMap = new LinkedHashMap<String, String>();
                    resultsMap.put("Mobile Number", entry.getKey());
                    resultsMap.put("First Line", entry.getValue()[0]);
                    resultsMap.put("Second Line", entry.getValue()[1]);
                    workerList.add(resultsMap);
                }
                workerHomeList.setAdapter(simpleAdapter);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
        workerHomeList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Intent intent = new Intent();

                intent.setClass(WorkerHomefeed.this, WorkerOwnerAccess.class);


                String send[] = workerList.get(i).toString().split(", ");
                String resend[] = send[0].split("=");
                intent.putExtra("workerOwnerMobileNumber",resend[1]);
                intent.putExtra("workerWishlistNumber",workerIntentNumber);
                intent.putExtra("WorkerFromWhere","WorkerHomefeed");
                startActivity(intent);
            }
        });




        workerHomeSearch.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                simpleAdapter.getFilter().filter(newText);
                return false;
            }
        });

        }
    }