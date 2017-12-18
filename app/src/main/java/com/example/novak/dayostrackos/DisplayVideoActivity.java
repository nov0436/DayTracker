package com.example.novak.dayostrackos;

import android.Manifest;
import android.app.Activity;
import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationManager;
import android.media.Image;
import android.media.ThumbnailUtils;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.provider.MediaStore;
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
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import org.w3c.dom.Text;

import java.io.File;
import java.text.DateFormat;
import java.text.Format;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import static com.example.novak.dayostrackos.VideoActivity.REQUEST_VIDEO_CAPTURE;

public class DisplayVideoActivity extends AppCompatActivity implements View.OnClickListener {

    private Database db;

    static final int GET_CATEGORY_FROM_LISTVIEW = 2;

    EditText titleEditText;
    EditText contentEditText;

    TextView dateTimeDisplayTextView;
    TextView dateTimeHintTextView;

    TextView selectedCategoryTextView;
    TextView locationTextView;

    ImageView btnDeleteRecord;
    ImageView btnSaveForm;
    ImageView thumbnailImageView;
    ImageView playImageView;

    Button btnSelectCategory;

    String title;
    String content;
    String type;
    String locationCity;
    String dateTime;
    String category;
    String link_to_resource;

    Record record;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_display_video);

        Intent incomingIntent = getIntent();
        record = (Record)incomingIntent.getSerializableExtra("recordObject");



        ///
        btnDeleteRecord = (ImageView) findViewById(R.id.deleteImageView);
        btnSaveForm = (ImageView) findViewById(R.id.saveImageView);
        btnSelectCategory = (Button) findViewById(R.id.buttonSelectCategory);

        btnDeleteRecord.setOnClickListener(this);
        btnSaveForm.setOnClickListener(this);
        btnSelectCategory.setOnClickListener(this);

        dateTimeDisplayTextView = (TextView) findViewById(R.id.dateTimeDisplayTextView);
        dateTimeDisplayTextView.setOnClickListener(this);

        dateTimeHintTextView = (TextView) findViewById(R.id.dateTimeTextView);
        selectedCategoryTextView = (TextView) findViewById(R.id.selectedCategoryTextView);

        // EditTexts
        titleEditText = (EditText) findViewById(R.id.titleEditText);
        contentEditText = (EditText) findViewById(R.id.noteEditText);

        dateTime = new SimpleDateFormat("yyyy-MM-dd   HH:mm").format(new Date());

        locationTextView = (TextView)findViewById(R.id.locationResultTextView);

        thumbnailImageView = (ImageView) findViewById(R.id.thumbnailImageView);
        playImageView = (ImageView) findViewById(R.id.playImageView);

        displayThumbnail();

        thumbnailImageView.setOnClickListener(this);
        playImageView.setOnClickListener(this);

        // setting of fields from the incoming object
        if (record.getLocation() != null)
            locationTextView.setText(record.getLocation());
        else
            locationTextView.setText("not specified");

        link_to_resource = record.getLinkToResource();

        titleEditText.setText(record.getTitle());
        contentEditText.setText(record.getText());
        selectedCategoryTextView.setText(record.getCategory());
        dateTimeDisplayTextView.setText(record.getDatetime());


    }

    @Override
    public void onClick(View v) {
        int id = v.getId();

        switch (id) {
            case R.id.saveImageView:
                if (formIsValid())
                {
                    saveData();
                }
                else
                {
                    Toast.makeText(getApplicationContext(), "The form is not valid. Cannot save data.", Toast.LENGTH_LONG ).show();
                }
                break;
            case R.id.deleteImageView:
                deleteData();
                break;

            case R.id.buttonSelectCategory:
                Intent intentCategory = new Intent(this, CategoryActivity.class);
                startActivityForResult(intentCategory, GET_CATEGORY_FROM_LISTVIEW);
                break;

            case R.id.thumbnailImageView:
                dispatchPlayVideoIntent();
                break;

            case R.id.playImageView:
                dispatchPlayVideoIntent();
                break;

        }
    }

    private void deleteData() {
        this.db = new Database(getApplicationContext());
        long deleteSuccess = this.db.delete(record.id);

        if (deleteSuccess != 0)
        {
            Toast.makeText(this, "The video has been successfully deleted.", Toast.LENGTH_SHORT).show();
            finish();
        }
        else
            Toast.makeText(this, "The video could not be deleted.", Toast.LENGTH_SHORT).show();
    }

    private void saveData() {
        title = titleEditText.getText().toString();
        content = contentEditText.getText().toString();
        category = selectedCategoryTextView.getText().toString();

        Record recordWithUpdatedValues = new Record(record.id, title, content, type, dateTime, category, locationCity, link_to_resource);

        this.db = new Database(getApplicationContext());
        long updateSuccess = this.db.update(recordWithUpdatedValues);

        if (updateSuccess != 0)
        {
            Toast.makeText(this, "The video has been successfully saved.", Toast.LENGTH_SHORT).show();
            finish();
        }
        else
            Toast.makeText(this, "The video could not be saved.", Toast.LENGTH_SHORT).show();
    }

    File videoFileGlobal;

    private void dispatchPlayVideoIntent() {
        Uri uri = Uri.parse(record.getLinkToResource());

        Intent videoIntent = new Intent();
        videoIntent.setAction(Intent.ACTION_VIEW);
        videoIntent.setDataAndType(uri, "video/mp4");
        startActivity(videoIntent);

    }


    private void displayThumbnail()
    {
        final int THUMBSIZE = 256;

        Uri newUri = Uri.parse(record.getLinkToResource());

        File videoFile = new File(newUri.getPath());

        Bitmap thumbImage = createThumbnailFromPath(videoFile.getAbsolutePath(), MediaStore.Images.Thumbnails.MICRO_KIND);

        thumbnailImageView.setImageBitmap(thumbImage);
    }

    public Bitmap createThumbnailFromPath(String filePath, int type){
        return ThumbnailUtils.createVideoThumbnail(filePath, type);
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
        if (TextUtils.isEmpty(record.getCategory()))
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
}
