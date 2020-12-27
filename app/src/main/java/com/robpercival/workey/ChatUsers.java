package com.robpercival.workey;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.SearchView;
import android.widget.TextView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.LinkedHashSet;
import java.util.Map;
import java.util.Set;

public class ChatUsers extends AppCompatActivity {
    String fromWhere, chatNumber;
    DatabaseReference databaseReference, databaseReferenceName, databaseReferenceMessage, databaseReferenceChange;
    ArrayList<String> userList = new ArrayList<>();
    ArrayList<String> userListNumber = new ArrayList<>();
    HashMap<String, String> countMessage = new LinkedHashMap<>();
    HashMap<String, String> mCountMessage = new LinkedHashMap<>();
    ArrayList<String> countNumber = new ArrayList<>();
    HashMap<String, String> countName = new LinkedHashMap<>();
    HashMap<String, String> userListSet = new LinkedHashMap<String, String>();
    Set<String> userListNumberSet = new LinkedHashSet<String>();
    SharedPreferences sharedPreferencesCount;
    int count = 0, c = 0, ccc = 0;
    ArrayList<TextView> userCounterList = new ArrayList<>();

    String mCount;
    ArrayAdapter<String> adapter;
    LinearLayout userChatListView;

    public void counter(String number) {
        countNumber.add(number);
        databaseReferenceMessage = FirebaseDatabase.getInstance().getReference("chatMessages");
        databaseReferenceMessage.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                for (String x : countNumber) {
                    count = 0;
                    for (DataSnapshot snapshot1 : dataSnapshot.getChildren()) {
                        if (snapshot1.getKey().equals(chatNumber + "_" + x)) {
                            count = (int) snapshot1.getChildrenCount();
                        }
                    }
                    countMessage.put(chatNumber + "_" + x, String.valueOf(count));


                }



            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    public void setTextTextView(String x) {
        if (countName.get(x.split("_")[1]) != null) {
            TextView textView = (TextView) findViewById(Integer.parseInt(countName.get(x.split("_")[1])));

            for (Map.Entry<String, String> entry1 : countMessage.entrySet()) {
                if (entry1.getKey().equals(chatNumber + "_" + x.split("_")[1]))
                    if (!(x.split("_")[2]).equals(" ")) {
                        textView.setBackgroundResource(R.drawable.shapechange6);
                        textView.setText(String.valueOf(Integer.parseInt(entry1.getValue().split("_")[0]) - Integer.parseInt(sharedPreferencesCount.getString(x.split("_")[1], String.valueOf(0)))));
                    }

            }
        } else {
            RelativeLayout relativeLayout = new RelativeLayout(ChatUsers.this);
            relativeLayout.setBackgroundResource(R.drawable.shapechange);
            ++ccc;
            TextView textViewCount = new TextView(ChatUsers.this);
            textViewCount.setId(ccc);

            relativeLayout.setId(ccc - (2 * ccc));
            countName.put(x.split("_")[1], String.valueOf(ccc));

            textViewCount.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT));
            TextView textView = new TextView(ChatUsers.this);
            textView.setTextSize(20);
            textView.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT));
            textViewCount.setTextSize(15);
            textView.setText(x.split("_")[0]);
            if (!(x.split("_")[2]).equals(" ")) {
                textViewCount.setText(x.split("_")[2]);
                textViewCount.setBackgroundResource(R.drawable.shapechange6);
            }
            userCounterList.add(textViewCount);
            RelativeLayout.LayoutParams rp = new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
            rp.topMargin = 15;
            relativeLayout.setLayoutParams(rp);
            rp = new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
            rp.addRule(RelativeLayout.ALIGN_PARENT_LEFT, RelativeLayout.TRUE);
            textView.setLayoutParams(rp);
            relativeLayout.addView(textView);
            rp = new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
            rp.addRule(RelativeLayout.ALIGN_PARENT_RIGHT, RelativeLayout.TRUE);

            textViewCount.setLayoutParams(rp);
            relativeLayout.addView(textViewCount);
            userChatListView.addView(relativeLayout);

            relativeLayout.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(final View view) {
                    final Intent intent = new Intent();

                    intent.setClass(ChatUsers.this, OwnerWorkerChats.class);

                    intent.putExtra("fromWhere", fromWhere);
                    if (fromWhere.equals("worker")) {
                        DatabaseReference databaseReference1 = FirebaseDatabase.getInstance().getReference("owner").child(userListNumber.get(view.getId() * (-1) - 1));
                        databaseReference1.addListenerForSingleValueEvent(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot snapshot) {
                                intent.putExtra("WorkerNumber", chatNumber);
                                intent.putExtra("OwnerNumber", userListNumber.get(view.getId() * (-1) - 1));
                                intent.putExtra("name",snapshot.child("firstName").getValue()+" "+snapshot.child("lastName").getValue());
                                TextView textView1 = (TextView) findViewById((view.getId()*(-1)));
                                textView1.setBackgroundResource(0);
                                textView1.setText("");
                                startActivity(intent);

                            }

                            @Override
                            public void onCancelled(@NonNull DatabaseError error) {

                            }
                        });

                    } else {
                        DatabaseReference databaseReference1 = FirebaseDatabase.getInstance().getReference("worker").child(userListNumber.get(view.getId() * (-1) - 1));
                        databaseReference1.addListenerForSingleValueEvent(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot snapshot) {
                                intent.putExtra("WorkerNumber", userListNumber.get(view.getId() * (-1) - 1));
                                intent.putExtra("OwnerNumber", chatNumber);
                                intent.putExtra("name",snapshot.child("firstName").getValue()+" "+snapshot.child("lastName").getValue());
                                TextView textView1 = (TextView) findViewById((view.getId()*(-1)));
                                textView1.setBackgroundResource(0);
                                textView1.setText("");
                                startActivity(intent);
                            }

                            @Override
                            public void onCancelled(@NonNull DatabaseError error) {

                            }
                        });

                    }

                }
            });
        }



    }

    public void getName(final String number) {

        if (fromWhere.equals("worker") && !number.equals(chatNumber)) {
            databaseReferenceName = FirebaseDatabase.getInstance().getReference("owner").child(number);
            databaseReferenceName.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull final DataSnapshot snapshot) {


                    mCount = sharedPreferencesCount.getString(number, String.valueOf(0));
                    for (Map.Entry<String, String> entry : countMessage.entrySet()) {
                        if (entry.getKey().equals(chatNumber + "_" + number)) {
                            c = Integer.parseInt(entry.getValue());
                            if ((c - Integer.parseInt(mCount)) != 0) {
                                userListSet.put(snapshot.child("firstName").getValue() + " " + snapshot.child("lastName").getValue() + "_" + number, String.valueOf((c - Integer.parseInt(mCount))));
                            } else {
                                userListSet.put(snapshot.child("firstName").getValue() + " " + snapshot.child("lastName").getValue() + "_" + number, " ");
                            }
                            break;
                        }


                    }
                    userListNumber.clear();

                    for (Map.Entry<String, String> entry : userListSet.entrySet()) {

                        setTextTextView(entry.getKey() + "_" + entry.getValue());

                    }
                    for (String x : userListNumberSet) {
                        userListNumber.add(x);
                    }


                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {

                }
            });
        } else if (fromWhere.equals("owner") && !number.equals(chatNumber)) {
            databaseReferenceName = FirebaseDatabase.getInstance().getReference("worker").child(number);
            databaseReferenceName.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull final DataSnapshot snapshot) {


                    mCount = sharedPreferencesCount.getString(number, String.valueOf(0));
                    for (Map.Entry<String, String> entry : countMessage.entrySet()) {
                        if (entry.getKey().equals(chatNumber + "_" + number)) {
                            c = Integer.parseInt(entry.getValue());
                            break;
                        }
                    }
                    if ((c - Integer.parseInt(mCount)) != 0) {
                        userListSet.put(snapshot.child("firstName").getValue() + " " + snapshot.child("lastName").getValue() + "_" + number, String.valueOf((c - Integer.parseInt(mCount))));
                    } else {
                        userListSet.put(snapshot.child("firstName").getValue() + " " + snapshot.child("lastName").getValue() + "_" + number, " ");
                    }
                    userListNumber.clear();

                    for (Map.Entry<String, String> entry : userListSet.entrySet()) {
                        setTextTextView(entry.getKey() + "_" + entry.getValue());
                    }
                    for (String x : userListNumberSet) {
                        userListNumber.add(x);
                    }

                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {

                }
            });
        }
    }

    public void listener() {
        databaseReference = FirebaseDatabase.getInstance().getReference("chatMessages");
        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {

                for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                    if (dataSnapshot.getKey().contains(chatNumber + "_")) {
                        counter(dataSnapshot.getKey().split("_")[1]);
                        getName(dataSnapshot.getKey().split("_")[1]);
                        userListNumberSet.add(dataSnapshot.getKey().split("_")[1]);
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
            }
        });
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat_users);
        sharedPreferencesCount = getSharedPreferences("messageCount", MODE_PRIVATE);
        userChatListView = (LinearLayout) findViewById(R.id.chatUserListView);
        adapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, userList);
        Intent intent = getIntent();
        fromWhere = intent.getStringExtra("fromWhere");
        if (fromWhere.equals("worker")) {
            chatNumber = intent.getStringExtra("workerIntentNumber");
        } else {
            chatNumber = intent.getStringExtra("ownerIntentNumber");
        }
        listener();

    }
}