package com.robpercival.workey;

import android.content.Intent;
import android.os.CountDownTimer;

import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.gms.tasks.TaskExecutors;
import com.google.firebase.FirebaseException;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.PhoneAuthCredential;
import com.google.firebase.auth.PhoneAuthProvider;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.concurrent.TimeUnit;


public class OwnerPhoneVerification extends AppCompatActivity {


    private EditText otp, number;
    private Button submit,getOtp;
    private TextView resend;
    private ProgressBar loader;
    private String id, phoneNumber;
    private FirebaseAuth mAuth;
    String fromWhere;


    public void sendVerificationCode() {

        new CountDownTimer(60000,1000){
            @Override
            public void onTick(long l) {
                resend.setText(""+l/1000);
                resend.setEnabled(false);
            }

            @Override
            public void onFinish() {
                resend.setText(" Resend");
                resend.setEnabled(true);
            }
        }.start();


        PhoneAuthProvider.getInstance().verifyPhoneNumber(
                "+91" + phoneNumber,        // Phone number to verify
                60,                 // Timeout duration
                TimeUnit.SECONDS,   // Unit of timeout
                TaskExecutors.MAIN_THREAD,               // Activity (for callback binding)
                new PhoneAuthProvider.OnVerificationStateChangedCallbacks() {

                    @Override
                    public void onCodeSent(@NonNull String id, @NonNull PhoneAuthProvider.ForceResendingToken forceResendingToken) {
                        OwnerPhoneVerification.this.id = id;
                    }

                    @Override
                    public void onVerificationCompleted(@NonNull PhoneAuthCredential phoneAuthCredential) {
                        signInWithPhoneAuthCredential(phoneAuthCredential);
                    }

                    @Override
                    public void onVerificationFailed(@NonNull FirebaseException e) {
                        Toast.makeText(OwnerPhoneVerification.this, "Failed", Toast.LENGTH_SHORT).show();
                    }
                });        // OnVerificationStateChangedCallbacks
    }

    public void get(View view) {
        getOtp = (Button) findViewById(R.id.ownerOtpButton);
        getOtp.setAlpha(0);
        getOtp.setClickable(false);
        submit.setAlpha(1);
        loader = (ProgressBar) findViewById(R.id.verifyBar);
        phoneNumber = number.getText().toString();
        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference("owner");

        databaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                int counter = 1;
                for (DataSnapshot dataSnapshot:snapshot.getChildren()) {
                    if (dataSnapshot.getKey().equals(phoneNumber)) {
                        counter = 0;
                        break;
                    }
                }
                if (counter == 1)
                {
                    fromWhere = "register";
                    sendVerificationCode();
                }else
                {
                    fromWhere = "login";
                    sendVerificationCode();
                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    private void signInWithPhoneAuthCredential(PhoneAuthCredential credential) {
        mAuth.signInWithCredential(credential)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        loader.setVisibility(View.GONE);
                        if (task.isSuccessful()) {
                            if (fromWhere.equals("register")) {
                                Intent intent = new Intent(OwnerPhoneVerification.this, OwnerRegister.class);
                                intent.putExtra("ownerNumber", phoneNumber);
                                startActivity(intent);
                                finish();
                            }else{
                                Intent intent = new Intent(OwnerPhoneVerification.this,OwnerForgotPassword.class);
                                intent.putExtra("ownerNumber", phoneNumber);
                                startActivity(intent);
                                finish();
                            }
                            FirebaseUser user = task.getResult().getUser();
                        } else {
                            Toast.makeText(OwnerPhoneVerification.this, "Verification Failed", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_owner_phone_verification);

        otp = (EditText) findViewById(R.id.ownerOtp);
        submit = (Button) findViewById(R.id.ownerVerify);
        resend = (TextView) findViewById(R.id.ownerResendOtp);
        number = (EditText) findViewById(R.id.ownerOtpMobileNumber);
        Intent intent =getIntent();
        fromWhere = intent.getStringExtra("fromWhere");


        mAuth = FirebaseAuth.getInstance();

        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                loader.setAlpha(1);
                if(TextUtils.isEmpty(otp.getText().toString())){
                    Toast.makeText(OwnerPhoneVerification.this, "Enter Otp", Toast.LENGTH_SHORT).show();
                }
                else if(otp.getText().toString().replace(" ","").length()!=6){
                    Toast.makeText(OwnerPhoneVerification.this, "Enter right otp", Toast.LENGTH_SHORT).show();
                }
                else {
                    loader.setVisibility(View.VISIBLE);
                    PhoneAuthCredential credential = PhoneAuthProvider.getCredential(id, otp.getText().toString().replace(" ",""));
                    signInWithPhoneAuthCredential(credential);
                }
            }
        });

        resend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                sendVerificationCode();
            }
        });

    }
        }
