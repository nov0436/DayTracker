package com.example.novak.dayostrackos;

import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.CalendarView;
import android.widget.Toast;

import java.util.Calendar;
import java.util.GregorianCalendar;

public class CalendarActivity extends AppCompatActivity implements CalendarView.OnDateChangeListener {

    private Calendar calendar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_calendar);

        CalendarView calendar = (CalendarView) findViewById(R.id.calendarView);
        calendar.setOnDateChangeListener(this);
    }

    @Override
    public void onSelectedDayChange( CalendarView view, int year, int month, int dayOfMonth) {
//        calendar = new GregorianCalendar( year, month, dayOfMonth );

        String dateTime = String.format("%d-%d-%d", year, month, dayOfMonth);

        Toast.makeText(this, dateTime, Toast.LENGTH_SHORT).show();

    }
}
