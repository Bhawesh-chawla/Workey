package com.robpercival.workey;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class OwnerForgotPassword extends AppCompatActivity {
    String ownerNumber,newPassword,confirmPassword;
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
            DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference("owner").child(ownerNumber);
            databaseReference.child("password").setValue(newPassword);
            Toast.makeText(this, "password changed", Toast.LENGTH_SHORT).show();
            startActivity(new Intent(OwnerForgotPassword.this,OwnerLogin.class));
            finish();
        }else{
            Toast.makeText(this, "please check your password", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_owner_forgot_password);
        newPass = (EditText) findViewById(R.id.ownerNewPassword);
        confirmPass = (EditText) findViewById(R.id.ownerConfirmPassword);
        Intent intent = getIntent();
        ownerNumber = intent.getStringExtra("ownerNumber");
    }
}