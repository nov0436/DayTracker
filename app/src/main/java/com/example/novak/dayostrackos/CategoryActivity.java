package com.example.novak.dayostrackos;

import android.app.Activity;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import java.util.ArrayList;
import java.util.List;

public class CategoryActivity extends AppCompatActivity implements AdapterView.OnItemClickListener {

    private ListView lv;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.d("message", "onCreate called");

        setContentView(R.layout.activity_category);

        lv = (ListView) findViewById(R.id.categoriesListView);
        lv.setOnItemClickListener(this);
    }


    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        Intent returnIntent = new Intent();

        String category = (String)parent.getItemAtPosition(position);
        returnIntent.putExtra("category", category);
        setResult(Activity.RESULT_OK, returnIntent);
        finish();
    }
}