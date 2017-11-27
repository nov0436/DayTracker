package com.example.novak.dayostrackos;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

public class WriteNoteActivity extends AppCompatActivity implements View.OnClickListener {

    static final int GET_DATE_TIME = 1;

    TextView dateTimeDisplayTextView;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_write_note);

        dateTimeDisplayTextView = (TextView) findViewById(R.id.dateTimeDisplayTextView);
        dateTimeDisplayTextView.setOnClickListener(this);


    }

    @Override
    public void onClick(View v) {
        int id = v.getId();

        switch (id)
        {
            case R.id.dateTimeDisplayTextView:
                Intent intent = new Intent(this, DateTimeActivity.class);
                startActivityForResult(intent, GET_DATE_TIME);
                break;
        }
    }

    private void startActivityForResult() {

    }
}
