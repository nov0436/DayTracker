package com.example.novak.dayostrackos;

import android.Manifest;
import android.app.Activity;
import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.location.Location;
import android.media.ThumbnailUtils;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.annotation.RequiresApi;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class VideoActivity extends AppCompatActivity implements View.OnClickListener, DatePickerDialog.OnDateSetListener,
        TimePickerDialog.OnTimeSetListener {

    static final int GET_CATEGORY_FROM_LISTVIEW = 2;
    private Database db;

    int year, month, day, hour, minute;
    int yearFinal, monthFinal, dayFinal, hourFinal, minuteFinal;

    String title;
    String content;
    String type;
    String locationCity;
    String dateTime;
    String category;
    String link_to_resource;

    EditText titleEditText;
    EditText contentEditText;

    TextView dateTimeDisplayTextView;
    TextView dateTimeHintTextView;

    TextView selectedCategoryTextView;

    Button btnSaveForm;
    Button btnSelectCategory;
    CheckBox checkBoxSaveLocation;

    ImageView btnRecordVideo;
    ImageView thumbnailImageView;

    String linkToVideoToSave;

    static final int REQUEST_VIDEO_CAPTURE = 1;
    Uri videoUri;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_video);

        /// jednotna cast
        btnSaveForm = (Button) findViewById(R.id.buttonSave);
        btnSelectCategory = (Button) findViewById(R.id.buttonSelectCategory);
        checkBoxSaveLocation = (CheckBox) findViewById(R.id.locationCheckBox);

        btnSelectCategory.setOnClickListener(this);
        checkBoxSaveLocation.setOnClickListener(this);
        btnSaveForm.setOnClickListener(this);

        dateTimeDisplayTextView = (TextView) findViewById(R.id.dateTimeDisplayTextView);
        dateTimeDisplayTextView.setOnClickListener(this);

        dateTimeHintTextView = (TextView) findViewById(R.id.dateTimeTextView);
        selectedCategoryTextView = (TextView) findViewById(R.id.selectedCategoryTextView);

        // EditTexts
        titleEditText = (EditText) findViewById(R.id.titleEditText);
        contentEditText = (EditText) findViewById(R.id.noteEditText);

        dateTime = new SimpleDateFormat("yyyy-MM-dd   HH:mm").format(new Date());
        dateTimeDisplayTextView.setText(dateTime);
        btnSaveForm = (Button) findViewById(R.id.buttonSave);

        /// jednotna cast end

        thumbnailImageView = (ImageView) findViewById(R.id.thumbnailImageView);
        btnRecordVideo = (ImageView) findViewById(R.id.videoImageView);
        btnRecordVideo.setOnClickListener(this);
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

                DatePickerDialog datePickerDialog = new DatePickerDialog(VideoActivity.this, VideoActivity.this, year, month, day);
                datePickerDialog.show();
                break;

            case R.id.buttonSave:
                if (formIsValid()) {
                    SaveData();
                } else {
                    Toast.makeText(getApplicationContext(), getResources().getString(R.string.form_not_valid), Toast.LENGTH_LONG).show();
                }
                break;

            case R.id.buttonSelectCategory:
                Intent intentCategory = new Intent(this, CategoryActivity.class);
                startActivityForResult(intentCategory, GET_CATEGORY_FROM_LISTVIEW);
                break;

            case R.id.locationCheckBox:
                if (checkBoxSaveLocation.isChecked()) {
                    ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, 123);

                    Locator locator = new Locator(getApplicationContext());
                    Location location = locator.getLocation();

                    if (location != null) {
                        double lat = location.getLatitude();
                        double lon = location.getLongitude();
                        locationCity = locator.getCityNameAtLocation(lat, lon);
                        Toast.makeText(getApplicationContext(), locationCity, Toast.LENGTH_LONG).show();
                    } else {
                        Toast.makeText(getApplicationContext(), getResources().getString(R.string.unable_to_use_gps), Toast.LENGTH_LONG).show();
                    }
                } else {
                    locationCity = null;
                }
                break;


            /////////////////////////
            //// INDIVIDUAL
            /////////////////////////
            case R.id.videoImageView:
                dispatchRecordVideoIntent();
                break;

        }
    }

    private void SaveData() {

        title = titleEditText.getText().toString();
        content = contentEditText.getText().toString();
        type = "video";
        category = selectedCategoryTextView.getText().toString();
        link_to_resource = linkToVideoToSave;

        Record record = new Record(title, content, type, dateTime, category, locationCity, link_to_resource);

        this.db = new Database(getApplicationContext());
        long insertSuccess = this.db.insert(record);

        if (insertSuccess != 0) {
            Toast.makeText(this, getResources().getString(R.string.toast_video_saved), Toast.LENGTH_SHORT).show();
            finish();
        } else
            Toast.makeText(this, getResources().getString(R.string.toast_video_not_saved), Toast.LENGTH_SHORT).show();

    }

    private boolean formIsValid() {
        if (TextUtils.isEmpty(titleEditText.getText().toString())) {
            return false;
        }
        if (TextUtils.isEmpty(contentEditText.getText().toString())) {
            return false;
        }
        if (TextUtils.isEmpty(category)) {
            return false;
        }
        if (TextUtils.isEmpty(linkToVideoToSave)) {
            return false;
        }
        return true;
    }

    File videoFileGlobal;

    private void dispatchRecordVideoIntent() {
        Intent videoIntent = new Intent(MediaStore.ACTION_VIDEO_CAPTURE);

        File videoDirectory = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DCIM);
        String videoName = getVideoName();
        File videoFile = new File(videoDirectory, videoName);
        videoFileGlobal = videoFile;

        videoUri = Uri.fromFile(videoFile);

        linkToVideoToSave = videoUri.toString();

        videoIntent.putExtra(MediaStore.EXTRA_OUTPUT, videoUri);
        startActivityForResult(videoIntent, REQUEST_VIDEO_CAPTURE);

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {

        if (requestCode == GET_CATEGORY_FROM_LISTVIEW) {
            if (resultCode == Activity.RESULT_OK) {
                category = data.getStringExtra("category");
                selectedCategoryTextView.setText(category);
            }
        }
        if (requestCode == REQUEST_VIDEO_CAPTURE && resultCode == RESULT_OK) {
            displayThumbnail();
        }
    }

    private String getVideoName() {
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
        String videoFileName = "VID_" + timeStamp + ".3gp";
        return videoFileName;
    }


    private void displayThumbnail() {
        final int THUMBSIZE = 256;

        Bitmap thumbImage = createThumbnailFromPath(videoFileGlobal.getAbsolutePath(), MediaStore.Images.Thumbnails.MICRO_KIND);

        thumbnailImageView.setImageBitmap(thumbImage);
        thumbnailImageView.setVisibility(View.VISIBLE);
    }

    public Bitmap createThumbnailFromPath(String filePath, int type) {
        return ThumbnailUtils.createVideoThumbnail(filePath, type);
    }

    ////////////////////////////////////
    // DATETIME
    ////////////////////////////////////
    @Override
    public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
        yearFinal = year;
        monthFinal = month + 1;
        dayFinal = dayOfMonth;

        Calendar c = Calendar.getInstance();
        hour = c.get(Calendar.HOUR_OF_DAY);
        minute = c.get(Calendar.MINUTE);

        TimePickerDialog timePickerDialog = new TimePickerDialog(VideoActivity.this, VideoActivity.this,
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
