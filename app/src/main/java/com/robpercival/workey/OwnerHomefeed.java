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


public class OwnerHomefeed extends AppCompatActivity {

    DatabaseReference databaseReferenceWorker;
    String cityOfOwner, cityOfWorker, genderOfWorker, firstNameOfWorker, lastNameOfWorker, areaOfWorker, mobileNumberOfWorker;
    SimpleAdapter simpleAdapter;
    public void ToChatUserOwner(View view)
    {
        Intent intent = new Intent(OwnerHomefeed.this,ChatUsers.class);
        intent.putExtra("ownerIntentNumber",number);
        intent.putExtra("fromWhere","owner");
        startActivity(intent);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.ownermenu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.ownerProfile) {
            Intent intent = new Intent(OwnerHomefeed.this, OwnerProfile.class);
            intent.putExtra("ownerProfileNumber", number);
            startActivity(intent);
        } else if (id == R.id.ownerAdvertisment) {
            Intent intent = new Intent(OwnerHomefeed.this, OwnerPostedAdvertisment.class);
            intent.putExtra("ownerMobileNumber", number);
            startActivity(intent);
        } else if (id == R.id.ownerWishlist) {
            Intent intent = new Intent(OwnerHomefeed.this, OwnerWishlist.class);
            intent.putExtra("ownerIntentNumber", number);
            startActivity(intent);

        } else {
            SharedPreferences sharedPreferences= getSharedPreferences("LoginPreferences",MODE_PRIVATE);
            sharedPreferences.edit()
                    .putString("mobileNumber",null)
                    .putString("firstName",null)
                    .putString("lastName",null)
                    .putString("fromWhere",null)
                    .putString("city",null)
                    .apply();
            Toast.makeText(OwnerHomefeed.this, "Logout Successful", Toast.LENGTH_SHORT).show();
            Intent intent = new Intent(OwnerHomefeed.this, MainActivity.class);
            startActivity(intent);
            finish();

        }
        return super.onOptionsItemSelected(item);
    }

    String number, firstName, lastName;
    ListView ownerHomeList;
    SearchView ownerHomeSearch;

    Map<String, String[]> ownerHashMap = new LinkedHashMap<>();

    List<Map<String, String>> ownerList = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_owner_homefeed);
        Intent intent = getIntent();
        number = intent.getStringExtra("ownerMobile");
        firstName = intent.getStringExtra("ownerFirstName");
        lastName = intent.getStringExtra("ownerLastName");

        setTitle("Hi " + firstName + "!");

        ownerHomeList = (ListView) findViewById(R.id.ownerHomeListView);

        simpleAdapter = new SimpleAdapter(getApplicationContext(), ownerList, R.layout.listitem, new String[]{"First Line", "Second Line"},
                new int[]{R.id.ownerNameItem, R.id.ownerAreaItem});
        ownerHomeSearch = (SearchView) findViewById(R.id.ownerHomeSearch);

        databaseReferenceWorker = FirebaseDatabase.getInstance().getReference("worker");
        SharedPreferences sharedPreferences = getSharedPreferences("LoginPreferences",MODE_PRIVATE);
        cityOfOwner = sharedPreferences.getString("city",null);

        databaseReferenceWorker.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {


                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {


                    cityOfWorker = snapshot.child("city").getValue().toString();
                    if (cityOfOwner.equals(cityOfWorker)) {
                        firstNameOfWorker = snapshot.child("firstName").getValue().toString();
                        lastNameOfWorker = snapshot.child("lastName").getValue().toString();
                        mobileNumberOfWorker = snapshot.child("mobile").getValue().toString();
                        areaOfWorker = snapshot.child("houseNumber").getValue().toString() + " " + snapshot.child("street").getValue().toString() + " " + snapshot.child("area").getValue().toString();
                        genderOfWorker = snapshot.child("gender").getValue().toString();

                        ownerHashMap.put(mobileNumberOfWorker, new String[]{firstNameOfWorker + " " + lastNameOfWorker, "Address: " + areaOfWorker + " " + cityOfWorker});


                    }


                }

                for (Map.Entry<String, String[]> entry : ownerHashMap.entrySet()) {

                    Map<String, String> resultsMap = new LinkedHashMap<String, String>();
                    resultsMap.put("Mobile Number", entry.getKey());
                    resultsMap.put("First Line", entry.getValue()[0]);
                    resultsMap.put("Second Line", entry.getValue()[1]);
                    ownerList.add(resultsMap);
                }
                ownerHomeList.setAdapter(simpleAdapter);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });



        ownerHomeList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Intent intent = new Intent();
                intent.setClass(OwnerHomefeed.this, OwnerWorkerAccess.class);

                String send[] = ownerList.get(i).toString().split(", ");
                String resend[] = send[0].split("=");
                intent.putExtra("workerOwnerMobileNumber", resend[1]);
                intent.putExtra("ownerWishlistNumber", number);
                intent.putExtra("OwnerFromWhere","OwnerHomefeed");
                startActivity(intent);
            }
        });


        ownerHomeSearch.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
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
