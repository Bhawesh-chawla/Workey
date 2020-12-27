package com.robpercival.workey;

import android.content.Intent;

import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class OwnerNewAdvertisment extends AppCompatActivity {

    EditText shopName,workDescription,workerDescription,shopNumber,street,area,city,salaryRange;
    String ownerNumber,sName,workDes,workerDes,sNumber,str,ar,ci,sRange;
    String flag = "0";

    FirebaseDatabase firebaseDatabase;
    DatabaseReference databaseReference;
    public void postAdvertisment(View view)
    {

        sName = shopName.getText().toString();
        workerDes = workerDescription.getText().toString();
        workDes = workDescription.getText().toString();
        sNumber = shopNumber.getText().toString();
        str = street.getText().toString();
        ar = area.getText().toString();
        ci = city.getText().toString();
        sRange = salaryRange.getText().toString();

        if (sName.isEmpty()) {
            shopName.setError("Shop Name can not be empty");
        }
        if (workDes.isEmpty()) {
            workDescription.setError("Work Description can not be empty");
        }
        if (workerDes.isEmpty()) {
            workerDescription.setError("Worker Description can not be empty");
        }
        if (sNumber.isEmpty()) {
            shopNumber.setError("Shop Number can not be empty");
        }
        if (str.isEmpty()) {
            street.setError("street can not be empty");
        }
        if (ar.isEmpty()) {
            area.setError("area can not be empty");
        }
        if (ci.isEmpty()) {
            city.setError("city can not be empty");
        }
        if(sRange.isEmpty())
        {
            salaryRange.setError("Salary Range can not be empty");
        }


        if (!sName.isEmpty() && !workDes.isEmpty() && !workerDes.isEmpty() && !sNumber.isEmpty() && !sRange.isEmpty() && !str.isEmpty() && !ar.isEmpty() && !ci.isEmpty())

    {

        flag = "1";
        OwnerAdvertismentPojo pojo = new OwnerAdvertismentPojo();
        pojo.setShopName(sName);
        pojo.setWorkDescription(workDes);
        pojo.setWorkerDescription(workerDes);
        pojo.setShopNumber(sNumber);
        pojo.setStreet(str);
        pojo.setArea(ar);
        pojo.setCity(ci);
        pojo.setSalaryRange(sRange);
        pojo.setNumber(ownerNumber);
        databaseReference.child(ownerNumber).setValue(pojo);
        Toast.makeText(OwnerNewAdvertisment.this, "Advertisment posted", Toast.LENGTH_LONG).show();

        Intent intent = new Intent(OwnerNewAdvertisment.this, OwnerPostedAdvertisment.class);
        intent.putExtra("ownerMobileNumber",ownerNumber);
        startActivity(intent);
        finish();
    }


    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_owner_new_advertisment);
        setTitle("Advertisment");
        shopName = (EditText) findViewById(R.id.ownerAdvertismentShopName);
        workDescription = (EditText) findViewById(R.id.ownerAdvertismentWorkDescription);
        workerDescription = (EditText) findViewById(R.id.ownerAdvertismentWorkerDescription);
        shopNumber = (EditText) findViewById(R.id.ownerAdvertismentShopNumber);
        street = (EditText) findViewById(R.id.ownerAdvertismentStreet);
        area = (EditText) findViewById(R.id.ownerAdvertismentArea);
        city = (EditText) findViewById(R.id.ownerAdvertismentCity);
        salaryRange = (EditText) findViewById(R.id.ownerAdvertismentSalaryRange);
        Intent intent = getIntent();
        ownerNumber = intent.getStringExtra("ownerAdvertismentMobileNumber");

        databaseReference = FirebaseDatabase.getInstance().getReference("ownerAdvertisment");

    }
}
