package com.robpercival.workey;

import android.content.Intent;

import android.os.Bundle;
import android.view.View;
import android.widget.ExpandableListAdapter;
import android.widget.ExpandableListView;

import androidx.appcompat.app.AppCompatActivity;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

public class MainActivity extends AppCompatActivity {

    private ExpandableListView expandableListView;
    List<String> childItem;
    Map<String,List<String>> listHashMap;

    List<String> parentLIst = new ArrayList<>();
    {
        parentLIst.add("Worker");
        parentLIst.add("Owner");


    }
    String[] class1 = {"Login","Register"};
    String[] class2 = {"Login","Register"};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        setTitle("Workey");
        expandableListView = (ExpandableListView) findViewById(R.id.exListView);
        listHashMap = new LinkedHashMap<>();
        for(String holdItem:parentLIst){

            if(holdItem.equals("Worker"))
                loadChild(class1);

            else if (holdItem.equals("Owner"))
                loadChild(class2);


            listHashMap.put(holdItem,childItem);

        }

        ExpandableListAdapter adapter = new MainAdapter(this , parentLIst,(HashMap<String,List<String>>)listHashMap);

        expandableListView.setAdapter(adapter);
        expandableListView.setOnChildClickListener(new ExpandableListView.OnChildClickListener() {
            @Override
            public boolean onChildClick(ExpandableListView expandableListView, View view, int i, int i1, long l) {

                if (i == 0) {
                    if (i1 == 0) {
                        Intent intent = new Intent(MainActivity.this, Login.class);
                        startActivity(intent);

                    }
                    else{
                        Intent intent = new Intent(MainActivity.this,WorkerPhoneVerification
                                .class);
                        intent.putExtra("fromWhere","register");
                        startActivity(intent);

                    }

                }
                else{
                    if (i1 == 0) {
                        Intent intent = new Intent(MainActivity.this, OwnerLogin.class);
                        intent.putExtra("travel","main");
                        startActivity(intent);

                    }
                    else{
                        Intent intent = new Intent(MainActivity.this, OwnerPhoneVerification.class);
                        intent.putExtra("fromWhere","register");
                        startActivity(intent);


                    }

                }
                return false;
            }
        });




    }


    private void loadChild(String[]parentElementName){

        childItem = new ArrayList<>();
        Collections.addAll(childItem,parentElementName);
    }
}
