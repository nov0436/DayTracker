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

    boolean canProceedToDetail;

    protected void onResume() {

        canProceedToDetail = true;
        this.db = new Database(this);

        Intent intent = getIntent();
        datetime = intent.getStringExtra("datetime");
        setTitle(getResources().getString(R.string.app_name) + " :    " + datetime);

        listView = (ListView) findViewById(R.id.list);

        records = new ArrayList<>();
        records = this.db.selectAllByDate(datetime);

        if (records.size() == 0)
        {
            Toast.makeText(this, getResources().getString(R.string.no_records), Toast.LENGTH_SHORT).show();
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
                        if (record.getLinkToResource() == null)
                        {
                            canProceedToDetail = false;
                        }
                        displayIntent = new Intent(getApplicationContext(), DisplayVideoActivity.class);
                        break;
                    case "photo":
                        if (record.getLinkToResource() == null)
                        {
                            canProceedToDetail = false;
                        }
                        displayIntent = new Intent(getApplicationContext(), DisplayPhotoActivity.class);
                        break;
                    case "audio":
                        if (record.getLinkToResource() == null)
                        {
                            canProceedToDetail = false;
                        }
                        displayIntent = new Intent(getApplicationContext(), DisplayVoiceActivity.class);
                        break;
                }

                if (canProceedToDetail)
                {
                    displayIntent.putExtra("recordObject", record);
                    startActivity(displayIntent);
                }
                else
                {
                    Toast.makeText(DayListActivity.this, getResources().getString(R.string.toast_no_longer_available), Toast.LENGTH_SHORT).show();
                }

                canProceedToDetail = true;

            }
        });


        super.onResume();

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_day_list);
    }

    @Override
    public boolean onItemLongClick(AdapterView<?> parent, View view, final int position, long id) {

        AlertDialog.Builder builder = new AlertDialog.Builder(this);

        builder.setTitle(getResources().getString(R.string.popup_title));
        builder.setMessage(getResources().getString(R.string.popup_question));

        builder.setPositiveButton(getResources().getString(R.string.popup_yes), new DialogInterface.OnClickListener() {

            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
                Record record = records.get(position);
                Database db = new Database(getApplicationContext());
                long deleteSuccess = db.delete(record.id);

                if (deleteSuccess != 0) {
                    Toast.makeText(DayListActivity.this, getResources().getString(R.string.toast_record_deleted), Toast.LENGTH_SHORT).show();
                    onResume();
                }
            }
        });

        builder.setNegativeButton(getResources().getString(R.string.popup_no), new DialogInterface.OnClickListener() {

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
