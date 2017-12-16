package com.example.novak.dayostrackos;

import android.Manifest;
import android.app.Activity;
import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationManager;
import android.os.Build;
import android.support.annotation.NonNull;
import android.support.annotation.RequiresApi;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import java.text.DateFormat;
import java.text.Format;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class WriteNoteActivity extends AppCompatActivity implements View.OnClickListener, DatePickerDialog.OnDateSetListener,
        TimePickerDialog.OnTimeSetListener {

    private static final int MY_PERMISSION_REQUEST_LOCATION = 1;

    static final int GET_DATE_TIME = 1;
    static final int GET_CATEGORY_FROM_LISTVIEW = 2;

    EditText titleEditText;
    EditText contentEditText;

    TextView dateTimeDisplayTextView;
    TextView dateTimeHintTextView;

    TextView selectedCategoryTextView;
    TextView cityNameTextView;

    Button btnSaveForm;
    Button btnSelectCategory;
    CheckBox checkBoxSaveLocation;

    String locationCity;
    String dateTime;
    String category;

    int year, month, day, hour, minute;
    int yearFinal, monthFinal, dayFinal, hourFinal, minuteFinal;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_write_note);

        btnSaveForm = (Button) findViewById(R.id.buttonSave);
        btnSelectCategory = (Button) findViewById(R.id.buttonSelectCategory);
        checkBoxSaveLocation = (CheckBox) findViewById(R.id.locationCheckBox);

        btnSaveForm.setOnClickListener(this);
        btnSelectCategory.setOnClickListener(this);
        checkBoxSaveLocation.setOnClickListener(this);

        dateTimeDisplayTextView = (TextView) findViewById(R.id.dateTimeDisplayTextView);
        dateTimeDisplayTextView.setOnClickListener(this);

        dateTimeHintTextView = (TextView) findViewById(R.id.dateTimeTextView);
        selectedCategoryTextView = (TextView) findViewById(R.id.selectedCategoryTextView);
        cityNameTextView = (TextView) findViewById(R.id.cityTextView);

        // EditTexts
        titleEditText = (EditText) findViewById(R.id.titleEditText);
        contentEditText = (EditText) findViewById(R.id.noteEditText);

        String currentDateAndTime = new SimpleDateFormat("yyyy-MM-dd   HH:mm").format(new Date());
        dateTimeDisplayTextView.setText(currentDateAndTime);
    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    @Override
    public void onClick(View v) {
        int id = v.getId();

        switch (id) {
            case R.id.dateTimeDisplayTextView:
                Calendar c = Calendar.getInstance();
                year = c.get(Calendar.YEAR);
                month = c.get(Calendar.MONTH);
                day = c.get(Calendar.DAY_OF_MONTH);

                DatePickerDialog datePickerDialog = new DatePickerDialog(WriteNoteActivity.this, WriteNoteActivity.this, year, month, day);
                datePickerDialog.show();
                break;

            case R.id.buttonSave:
                // save the form
                if (formIsValid())
                {
                    Toast.makeText(getApplicationContext(), "Can save", Toast.LENGTH_LONG ).show();
                }
                else
                {
                    Toast.makeText(getApplicationContext(), "Cannot save", Toast.LENGTH_LONG ).show();
                }
                break;

            case R.id.buttonSelectCategory:
                Intent intentCategory = new Intent(this, CategoryActivity.class);
                startActivityForResult(intentCategory, GET_CATEGORY_FROM_LISTVIEW);
                break;

            case R.id.locationCheckBox:
                if (checkBoxSaveLocation.isChecked()) {
                    ActivityCompat.requestPermissions(this, new String[] {Manifest.permission.ACCESS_FINE_LOCATION}, 123);

                    Locator locator = new Locator(getApplicationContext());
                    Location location = locator.getLocation();

                    if (location != null)
                    {
                        double lat = location.getLatitude();
                        double lon = location.getLongitude();
                        locationCity = locator.getCityNameAtLocation(lat, lon);
                        Toast.makeText(getApplicationContext(), locationCity, Toast.LENGTH_LONG ).show();
                    }
                    else
                    {
                        Toast.makeText(getApplicationContext(), "Unable to get your location.", Toast.LENGTH_LONG ).show();
                    }
                }
                break;
        }
    }

    private boolean formIsValid() {
        if (TextUtils.isEmpty(titleEditText.getText().toString()))
        {
            return false;
        }
        if (TextUtils.isEmpty(contentEditText.getText().toString()))
        {
            return false;
        }
        if (TextUtils.isEmpty(category))
        {
            return false;
        }
        return true;
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {

        if (requestCode == GET_CATEGORY_FROM_LISTVIEW) {
            if(resultCode == Activity.RESULT_OK){
                category = data.getStringExtra("category");
                selectedCategoryTextView.setText(category);
            }
        }
    }


    @Override
    public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
        yearFinal = year;
        monthFinal = month + 1;
        dayFinal = dayOfMonth;

        Calendar c = Calendar.getInstance();
        hour = c.get(Calendar.HOUR_OF_DAY);
        minute = c.get(Calendar.MINUTE);

        TimePickerDialog timePickerDialog = new TimePickerDialog(WriteNoteActivity.this, WriteNoteActivity.this,
                hour, minute, android.text.format.DateFormat.is24HourFormat(this));
        timePickerDialog.show();

    }

    @Override
    public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
        hourFinal = hourOfDay;
        minuteFinal = minute;

        dateTime = String.format("%d-%d-%d   %d:%d", yearFinal, monthFinal, dayFinal, hourFinal, minuteFinal);
        dateTimeDisplayTextView.setText(dateTime);

    }
}
