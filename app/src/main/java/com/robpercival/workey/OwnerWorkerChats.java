package com.robpercival.workey;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.text.Html;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.ScrollView;
import android.widget.SimpleAdapter;
import android.widget.TextView;
import android.widget.Toast;


import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.GenericTypeIndicator;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FileDownloadTask;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import de.hdodenhof.circleimageview.CircleImageView;

public class OwnerWorkerChats extends AppCompatActivity {

    ImageView sendMessage;
    TextView ownerWorkerChatName;
    CircleImageView ownerWorkerChatImage;
    EditText messageUser;
    TextView oChat, wChat,progressBarText;
    SimpleAdapter simpleAdapter;
    View v;
    String ownerNumber, workerNumber, fromWhere;
    LinearLayout chatList;
    DatabaseReference databaseReferenceChats, databaseReference, databaseReferenceChats2;
    int length = 0;
    LayoutInflater inflater;
    StorageReference storageReference;
    List<Map<String, String>> ownerList = new ArrayList<>();
    ProgressBar progressBar;
    ScrollView chatScroll;
    String otherName,name;
    SharedPreferences sharedPreferencesMessageCount;


    public void CallDone(View view) {
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.CALL_PHONE) == PackageManager.PERMISSION_GRANTED) {

            if (fromWhere.equals("owner")) {
                Intent i = new Intent(Intent.ACTION_CALL);
                i.setData(Uri.parse("tel:" + workerNumber));
                startActivity(i);
            } else {
                Intent i = new Intent(Intent.ACTION_CALL);
                i.setData(Uri.parse("tel:" + ownerNumber));
                startActivity(i);
            }

        } else {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.CALL_PHONE}, 0);


        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions,
                                           int[] grantResults) {
        switch (requestCode) {

            case 0: {
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    if (ActivityCompat.checkSelfPermission(OwnerWorkerChats.this,
                            Manifest.permission.CALL_PHONE) == PackageManager.PERMISSION_GRANTED) {
                        Toast.makeText(this, "Permission Granted", Toast.LENGTH_SHORT).show();

                        if (fromWhere.equals("owner")) {
                            Intent i = new Intent(Intent.ACTION_CALL);
                            i.setData(Uri.parse("tel:" + workerNumber));
                            startActivity(i);
                        } else {
                            Intent i = new Intent(Intent.ACTION_CALL);
                            i.setData(Uri.parse("tel:" + ownerNumber));
                            startActivity(i);
                        }

                    }

                } else {
                    Toast.makeText(this, "Permission Denied", Toast.LENGTH_SHORT).show();
                }


                return;
            }
        }
    }

    public void addMessageBox(String message, int type){
        TextView textView = new TextView(OwnerWorkerChats.this);
        textView.setText(Html.fromHtml(message));


        if(type == 1) {
            textView.setBackgroundResource(R.drawable.shapechange);
            textView.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT));


            LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT,
                    ViewGroup.LayoutParams.WRAP_CONTENT);

            layoutParams.setMargins(100,0,10,10);
            layoutParams.gravity = Gravity.RIGHT;
            textView.setTextColor(Color.BLACK);
            textView.setLayoutParams(layoutParams);
            messageUser.setText("");


        }
        else{
            textView.setBackgroundResource(R.drawable.shapechange5);
            textView.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT));
            LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT,
                    ViewGroup.LayoutParams.WRAP_CONTENT);

            textView.setTextColor(Color.WHITE);
            layoutParams.gravity = Gravity.LEFT;
            layoutParams.setMargins(10,0,100,10);
            textView.setLayoutParams(layoutParams);

        }

        chatList.addView(textView);
        chatScroll.post(new Runnable() {
            @Override
            public void run() {
                chatScroll.fullScroll(View.FOCUS_DOWN);
            }
        });



    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_owner_worker_chats);
        splashscreen.solution = "OwnerWorkerChats";

        sendMessage = (ImageView) findViewById(R.id.sendMessage);
        ownerWorkerChatImage = (CircleImageView) findViewById(R.id.ownerWorkerChatImage);
        ownerWorkerChatName = (TextView) findViewById(R.id.ownerWorkerChatName);
        messageUser = (EditText) findViewById(R.id.messageUser);
        chatList = (LinearLayout) findViewById(R.id.chatList);
        progressBar = (ProgressBar) findViewById(R.id.progressBar);
        progressBarText = (TextView) findViewById(R.id.progressText);
        chatScroll = (ScrollView) findViewById(R.id.chatScroll);
        sharedPreferencesMessageCount = getSharedPreferences("messageCount",MODE_PRIVATE);
        Intent intent = getIntent();
        fromWhere = intent.getStringExtra("fromWhere");
        ownerNumber = intent.getStringExtra("OwnerNumber");
        workerNumber = intent.getStringExtra("WorkerNumber");
        name = intent.getStringExtra("name");
        inflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        v = getLayoutInflater().inflate(R.layout.listchat, null);
        oChat = (TextView) v.findViewById(R.id.chatShow);
        wChat = (TextView) v.findViewById(R.id.chatShow2);
        simpleAdapter = new SimpleAdapter(getApplicationContext(), ownerList, R.layout.listchat, new String[]{"First Line", "Second Line"},
                new int[]{R.id.chatShow, R.id.chatShow2});

        if (fromWhere.equals("owner")) {
            storageReference = FirebaseStorage.getInstance().getReference().child("worker/images/" + workerNumber);
            databaseReference = FirebaseDatabase.getInstance().getReference("worker").child(workerNumber);

        } else {
            storageReference = FirebaseStorage.getInstance().getReference().child("owner/images/" + ownerNumber);
            databaseReference = FirebaseDatabase.getInstance().getReference("owner").child(ownerNumber);
        }
        try {
            final File fileLocation = File.createTempFile("img", "jpg");

            storageReference.getFile(fileLocation)
                    .addOnSuccessListener(new OnSuccessListener<FileDownloadTask.TaskSnapshot>() {
                        @Override
                        public void onSuccess(FileDownloadTask.TaskSnapshot taskSnapshot) {
                            Bitmap bitmap = BitmapFactory.decodeFile(fileLocation.getAbsolutePath());
                            ownerWorkerChatImage.setImageBitmap(bitmap);
                        }
                    })
                    .addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            Toast.makeText(OwnerWorkerChats.this, "Failure", Toast.LENGTH_SHORT).show();
                        }
                    });

        } catch (IOException e) {
            e.printStackTrace();
        }
        databaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                ownerWorkerChatName.setText(snapshot.child("firstName").getValue().toString() + " " + snapshot.child("lastName").getValue().toString());
                otherName = snapshot.child("firstName").getValue().toString() + " " + snapshot.child("lastName").getValue().toString();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
        databaseReferenceChats = FirebaseDatabase.getInstance().getReference("chatMessages").child(ownerNumber + "_" + workerNumber);
        databaseReferenceChats2 = FirebaseDatabase.getInstance().getReference("chatMessages").child(workerNumber + "_" + ownerNumber);

        sendMessage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String messageText = messageUser.getText().toString();

                if(!messageText.equals("")){
                    Map<String, String> map = new HashMap<String, String>();
                    map.put("message", messageText);
                    if (fromWhere.equals("worker")) {
                        map.put("user", workerNumber);
                    }else{
                        map.put("user", ownerNumber);
                    }
                    databaseReferenceChats.push().setValue(map);
                    databaseReferenceChats2.push().setValue(map);


                }
            }
        });

        databaseReferenceChats.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                if (!dataSnapshot.exists()){
                    progressBarText.setAlpha(0);
                    progressBar.setAlpha(0);
                }
                GenericTypeIndicator<Map<String,String>> genericTypeIndicator = new GenericTypeIndicator<Map<String, String>>() {
                };
                Map<String,String> map = dataSnapshot.getValue(genericTypeIndicator);
                String message = map.get("message").toString();
                String userName = map.get("user").toString();

                if (fromWhere.equals("owner")) {

                    String forcolor;
                    if (userName.equals(ownerNumber)) {
                        forcolor = "<font color = '#800000'><b>You</b></font><br>";

                        addMessageBox(forcolor + message, 1);
                        ++length;

                    } else {
                        forcolor = "<font color = '#FF7F50'><b>"+name+"</b></font><br>";
                        addMessageBox( forcolor + message, 2);
                        ++length;


                    }
                    if (splashscreen.solution.equals("OwnerWorkerChats")) {
                        sharedPreferencesMessageCount.edit().putString(workerNumber, String.valueOf(length))
                                .commit();
                    }
                }else{

                    String forcolor;
                    if (userName.equals(workerNumber)) {
                        forcolor = "<font color = '#800000'><b>You</b></font><br>";

                        addMessageBox(forcolor + message, 1);
                        ++length;
                    } else {
                        forcolor = "<font color = '#FF7F50'><b>"+name+"</b></font><br>";
                        addMessageBox(forcolor + message, 2);
                        ++length;
                    }if (splashscreen.solution.equals("OwnerWorkerChats")) {
                        sharedPreferencesMessageCount.edit().putString(ownerNumber, String.valueOf(length))
                                .commit();
                    }
                }

                progressBar.setAlpha(0);
                progressBarText.setAlpha(0);
            }

            @Override
            public void onChildChanged(DataSnapshot dataSnapshot, String s) {

            }

            @Override
            public void onChildRemoved(DataSnapshot dataSnapshot) {

            }

            @Override
            public void onChildMoved(DataSnapshot dataSnapshot, String s) {

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }


        });


    }

    @Override
    public void onBackPressed() {
        splashscreen.solution = "ChatUsers";
        if (fromWhere.equals("owner")){
            sharedPreferencesMessageCount.edit().putString(workerNumber, String.valueOf(length))
                    .commit();
        }else{
            sharedPreferencesMessageCount.edit().putString(ownerNumber, String.valueOf(length))
                    .commit();
        }
        super.onBackPressed();

    }

    @Override
    protected void onStop() {
        splashscreen.solution = "ChatUsers";
        if (fromWhere.equals("owner")){
            sharedPreferencesMessageCount.edit().putString(workerNumber, String.valueOf(length))
                    .commit();
        }else{
            sharedPreferencesMessageCount.edit().putString(ownerNumber, String.valueOf(length))
                    .commit();
        }
        super.onStop();

    }

    @Override
    protected void onDestroy() {
        splashscreen.solution = "ChatUsers";
        if (fromWhere.equals("owner")){
            sharedPreferencesMessageCount.edit().putString(workerNumber, String.valueOf(length))
                    .commit();
        }else{
            sharedPreferencesMessageCount.edit().putString(ownerNumber, String.valueOf(length))
                    .commit();
        }
        super.onDestroy();


    }
}