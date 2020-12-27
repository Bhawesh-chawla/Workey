package com.robpercival.workey;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class ForgotPassword extends AppCompatActivity {
    String workerNumber,newPassword,confirmPassword;
    EditText newPass,confirmPass;
    public void passwordConfirm(View view)
    {
        newPassword = newPass.getText().toString();
        confirmPassword = confirmPass.getText().toString();
        if (newPassword.isEmpty())
            newPass.setError("enter new password");
        if (confirmPassword.isEmpty())
            confirmPass.setError("confirm your password");
        if (newPassword.equals(confirmPassword))
        {
            DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference("worker").child(workerNumber);
            databaseReference.child("password").setValue(newPassword);
            Toast.makeText(this, "password changed", Toast.LENGTH_SHORT).show();
            startActivity(new Intent(ForgotPassword.this,Login.class));
            finish();
        }else{
            Toast.makeText(this, "please check your password", Toast.LENGTH_SHORT).show();
        }
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forgot_password);
        newPass = (EditText) findViewById(R.id.workerNewPassword);
        confirmPass = (EditText) findViewById(R.id.workerConfirmPassword);
        Intent intent = getIntent();
        workerNumber = intent.getStringExtra("workerNumber");

    }
}