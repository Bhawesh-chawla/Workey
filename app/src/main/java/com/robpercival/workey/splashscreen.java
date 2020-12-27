package com.robpercival.workey;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

public class splashscreen extends AppCompatActivity {
    SharedPreferences sharedPreferences;
    static String solution;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        sharedPreferences = getSharedPreferences("LoginPreferences", MODE_PRIVATE);
        solution = "UserChats";
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {


        if (sharedPreferences.getString("mobileNumber", null) != null) {
            if (sharedPreferences.getString("fromWhere", null).equals("worker")) {

                Intent intent = new Intent(splashscreen.this, WorkerHomefeed.class);
                intent.putExtra("workerMobile", sharedPreferences.getString("mobileNumber", null));
                intent.putExtra("workerFirstName", sharedPreferences.getString("firstName", null));
                intent.putExtra("workerLastName", sharedPreferences.getString("lastName", null));
                startActivity(intent);
                finish();


            } else {
                Intent intent = new Intent(splashscreen.this, OwnerHomefeed.class);
                intent.putExtra("ownerMobile", sharedPreferences.getString("mobileNumber", null));
                intent.putExtra("ownerFirstName", sharedPreferences.getString("firstName", null));
                intent.putExtra("ownerLastName", sharedPreferences.getString("lastName", null));
                startActivity(intent);
                finish();
            }

        } else {
            startActivity(new Intent(splashscreen.this, MainActivity.class));
            finish();
        }
            }
        },1000);

    }
}
