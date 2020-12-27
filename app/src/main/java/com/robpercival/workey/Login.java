package com.robpercival.workey;

import android.content.Intent;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class Login extends AppCompatActivity {

    EditText number;
    EditText password;
    TextView forgotPass;
    String a = null,num,pass;
    String b = null;
    String x= "",y="",q="",w="",t="";


    FirebaseDatabase firebaseDatabase;
    DatabaseReference databaseReference;
    SharedPreferences sharedPreferences;

    public void forgotPassword(View view)
    {
        Intent intent = new Intent(Login.this,WorkerPhoneVerification.class);
        intent.putExtra("fromWhere","login");
        startActivity(intent);

    }

    public void loginWorker(View view) {
        number = (EditText) findViewById(R.id.numberWorker);
        password = (EditText) findViewById(R.id.passwordWorker);

        databaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                num = number.getText().toString();
                pass = password.getText().toString();

                for(DataSnapshot snapshot : dataSnapshot.getChildren())
                {


                   x = snapshot.child("firstName").getValue().toString();
                    y = snapshot.child("lastName").getValue().toString();
                    q = snapshot.child("mobile").getValue().toString();
                    w = snapshot.child("password").getValue().toString();
                    t = snapshot.child("city").getValue().toString();

                    if(num.equals(q) && pass.equals(w)){
                        a = q;
                        b = w;

                        break;
                    }
                }
                if (num.equals(a) && pass.equals(b)) {
                    sharedPreferences.edit().putString("mobileNumber",num)
                            .putString("firstName",x)
                            .putString("lastName",y)
                            .putString("fromWhere","worker")
                            .putString("city",t)
                            .apply();
                    Intent intent = new Intent(Login.this, WorkerHomefeed.class);
                    intent.putExtra("workerMobile",num);
                    intent.putExtra("workerFirstName",x);
                    intent.putExtra("workerLastName",y);
                    startActivity(intent);
                    finish();
                    Toast.makeText(Login.this, "Login successful", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(Login.this, "Wrong details", Toast.LENGTH_SHORT).show();

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
        setContentView(R.layout.activity_login);
            setTitle("Login");
            sharedPreferences = getSharedPreferences("LoginPreferences",MODE_PRIVATE);
            forgotPass = (TextView) findViewById(R.id.workerForgotPassword);
        databaseReference = FirebaseDatabase.getInstance().getReference("worker");
    }
}
