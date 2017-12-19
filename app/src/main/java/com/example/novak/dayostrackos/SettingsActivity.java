package com.example.novak.dayostrackos;

import android.content.Context;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

public class SettingsActivity extends AppCompatActivity implements View.OnClickListener {

    Button btnSave;

    EditText nameEditText;
    EditText phraseEditText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);

        btnSave = (Button) findViewById(R.id.buttonSave);

        nameEditText = (EditText) findViewById(R.id.nameEditText);
        phraseEditText = (EditText) findViewById(R.id.phraseEditText);

        btnSave.setOnClickListener(this);
    }

    @Override
    protected void onResume() {
        super.onResume();

        SharedPreferences prefs = getSharedPreferences("daytracker_settings", Context.MODE_PRIVATE);
        String name = prefs.getString("username", "");
        String phrase = prefs.getString("phrase", "");

        nameEditText.setText(name);
        phraseEditText.setText(phrase);
    }

    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.buttonSave) {
            SharedPreferences prefs = this.getSharedPreferences("daytracker_settings", Context.MODE_PRIVATE);
            SharedPreferences.Editor editor = prefs.edit();

            if (!TextUtils.isEmpty(nameEditText.getText())) {
                editor.putString("username", nameEditText.getText().toString());
            }

            if (!TextUtils.isEmpty(phraseEditText.getText())) {
                editor.putString("phrase", phraseEditText.getText().toString());
            }

            editor.commit();

            finish();
        }

    }
}
