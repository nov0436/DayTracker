package com.example.novak.dayostrackos;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.provider.ContactsContract;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.app.AppCompatDialog;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toolbar;

import java.util.ArrayList;

public class DayListActivity extends AppCompatActivity {

    ArrayList<Record> records;
    ListView listView;

    private Database db;
    private static CustomAdapter adapter;
    String datetime;

    boolean firstRun = true;

    protected void onResume() {
//        if (!firstRun)
//        {
            this.db = new Database(this);

            Intent intent = getIntent();
            datetime = intent.getStringExtra("datetime");
        setTitle("DayTracker:   " + datetime);

        listView=(ListView)findViewById(R.id.list);

        records= new ArrayList<>();
            records = this.db.selectAllByDate(datetime);


            adapter= new CustomAdapter(records,getApplicationContext());

            listView.setAdapter(adapter);
            listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                    Record record= records.get(position);

                    Intent displayIntent = new Intent(getApplicationContext(), DisplayNoteActivity.class);
                    displayIntent.putExtra("recordObject", record);
                    startActivity(displayIntent);

                }
            });
//            firstRun = false;
//        }

        super.onResume();

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_day_list);

//        this.db = new Database(this);
//
//        Intent intent = getIntent();
//        datetime = intent.getStringExtra("datetime");
//        setTitle("DayTracker:   " + datetime);
//
//        listView=(ListView)findViewById(R.id.list);
//
//        records= new ArrayList<>();
//        records = this.db.selectAllByDate(datetime);
//
//
//        adapter= new CustomAdapter(records,getApplicationContext());
//
//        listView.setAdapter(adapter);
//        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
//            @Override
//            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
//
//                Record record= records.get(position);
//
//                Intent displayIntent = new Intent(getApplicationContext(), DisplayNoteActivity.class);
//                displayIntent.putExtra("recordObject", record);
//                startActivity(displayIntent);
//
//            }
//        });
    }
}
