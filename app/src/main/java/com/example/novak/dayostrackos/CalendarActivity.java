package com.example.novak.dayostrackos;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.ViewTreeObserver;
import android.widget.CalendarView;
import android.widget.Toast;

import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;

public class CalendarActivity extends AppCompatActivity implements  CalendarView.OnDateChangeListener {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_calendar);

        CalendarView calendar = (CalendarView) findViewById(R.id.calendarView);
        calendar.setOnDateChangeListener(this);
    }

    @Override
    public void onSelectedDayChange( CalendarView view, int year, int month, int dayOfMonth) {
        month += 1;

        String selectedDate = String.format("%d-%d-%d", year, month, dayOfMonth);

        Intent dayIntent = new Intent(this, DayListActivity.class);
        dayIntent.putExtra("datetime", selectedDate);
        startActivity(dayIntent);
    }


}
