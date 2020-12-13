package com.example.coinapocalypse;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import java.util.ArrayList;

public class ScoreActivity extends AppCompatActivity {

    DBHelper mydb;
    ListView itemListView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_score);

        mydb = new DBHelper(this);
        ArrayList<String> arrayList = new ArrayList<String>();
        arrayList = mydb.getItemList();
        ArrayAdapter<String> arrayAdapter=new ArrayAdapter<String>(this,android.R.layout.simple_list_item_1, arrayList);

        itemListView = findViewById(R.id.listView1);
        itemListView.setAdapter(arrayAdapter);
    }
}