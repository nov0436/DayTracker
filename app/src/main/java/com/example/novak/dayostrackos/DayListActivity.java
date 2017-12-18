package com.example.novak.dayostrackos;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.provider.ContactsContract;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.app.AppCompatDialog;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;
import android.widget.Toolbar;

import java.util.ArrayList;

public class DayListActivity extends AppCompatActivity implements AdapterView.OnItemLongClickListener {

    ArrayList<Record> records;
    ListView listView;

    private Database db;
    private static CustomAdapter adapter;
    String datetime;


    protected void onResume() {

        this.db = new Database(this);

        Intent intent = getIntent();
        datetime = intent.getStringExtra("datetime");
        setTitle("DayTracker:   " + datetime);

        listView = (ListView) findViewById(R.id.list);

        records = new ArrayList<>();
        records = this.db.selectAllByDate(datetime);

        if (records.size() == 0)
        {
            Toast.makeText(this, "There are no records for this day.", Toast.LENGTH_SHORT).show();
        }


        adapter = new CustomAdapter(records, getApplicationContext());

        listView.setAdapter(adapter);
        listView.setOnItemLongClickListener(this);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                Record record = records.get(position);
                Intent displayIntent = null;

                switch (record.getType())
                {
                    case "note":
                        displayIntent = new Intent(getApplicationContext(), DisplayNoteActivity.class);
                        break;
                    case "video":
                        displayIntent = new Intent(getApplicationContext(), DisplayVideoActivity.class);
                        break;
                    case "photo":
                        displayIntent = new Intent(getApplicationContext(), DisplayPhotoActivity.class);
                        break;
                    case "audio":
                        displayIntent = new Intent(getApplicationContext(), DisplayVoiceActivity.class);
                        break;
                }

                displayIntent.putExtra("recordObject", record);
                startActivity(displayIntent);
            }
        });


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


    @Override
    public boolean onItemLongClick(AdapterView<?> parent, View view, final int position, long id) {

//        Toast.makeText(this, "position: " + position, Toast.LENGTH_SHORT).show();

        AlertDialog.Builder builder = new AlertDialog.Builder(this);

        builder.setTitle("Confirm");
        builder.setMessage("Do you want to delete this item?");

        builder.setPositiveButton("YES", new DialogInterface.OnClickListener() {

            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
                Record record = records.get(position);
                Database db = new Database(getApplicationContext());
                long deleteSuccess = db.delete(record.id);

                if (deleteSuccess != 0) {
                    Toast.makeText(DayListActivity.this, "The record has been successfully deleted.", Toast.LENGTH_SHORT).show();
                    onResume();
                }
            }
        });

        builder.setNegativeButton("NO", new DialogInterface.OnClickListener() {

            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });

        AlertDialog alert = builder.create();
        alert.show();

        return true;
    }
}
