package com.robpercival.workey;

import android.content.Intent;
import android.content.SharedPreferences;

import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.net.URI;

public class OwnerLogin extends AppCompatActivity {
    EditText number;
    EditText password;
    String a = null, b = null, num, pass;
    URI uri;
    String x = "", y = "", q = "", w = "", t = "";
    String travel;
    SharedPreferences sharedPreferences;


    FirebaseDatabase firebaseDatabase;
    DatabaseReference databaseReference;

    public void forgotPassword(View view)
    {
        Intent intent = new Intent(OwnerLogin.this,OwnerPhoneVerification.class);
        intent.putExtra("fromWhere","login");
        startActivity(intent);

    }

    public void loginOwner(View view) {
        number = (EditText) findViewById(R.id.numberOwner);
        password = (EditText) findViewById(R.id.passwordOwner);
        num = number.getText().toString();
        pass = password.getText().toString();

        databaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {


                    x = snapshot.child("firstName").getValue().toString();
                    y = snapshot.child("lastName").getValue().toString();
                    q = snapshot.child("mobile").getValue().toString();
                    w = snapshot.child("password").getValue().toString();
                    t = snapshot.child("city").getValue().toString();

                    if (num.equals(q) && pass.equals(w)) {
                        a = q;
                        b = w;

                        break;
                    }
                }
                if (num.equals(a) && pass.equals(b)) {
                    sharedPreferences.edit().putString("mobileNumber", num)
                            .putString("firstName", x)
                            .putString("lastName", y)
                            .putString("city", t)
                            .putString("fromWhere", "owner")
                            .apply();

                    Intent intent = new Intent(OwnerLogin.this, OwnerHomefeed.class);

                    intent.putExtra("ownerMobile", num);
                    intent.putExtra("ownerFirstName", x);
                    intent.putExtra("ownerLastName", y);
                    startActivity(intent);
                    finish();

                    Toast.makeText(OwnerLogin.this, "Login successful", Toast.LENGTH_LONG).show();

                } else {
                    Toast.makeText(OwnerLogin.this, "Wrong details", Toast.LENGTH_LONG).show();

                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });


    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_owner_login);

        setTitle("Login");
        sharedPreferences = getSharedPreferences("LoginPreferences", MODE_PRIVATE);
        databaseReference = FirebaseDatabase.getInstance().getReference("owner");
        Intent intent = getIntent();
        travel = intent.getStringExtra("travel");


    }


}

