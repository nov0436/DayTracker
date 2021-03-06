package com.example.novak.dayostrackos;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

/**
 * Created by novak on 16-Dec-17.
 */

public class SQLiteDbHelper extends SQLiteOpenHelper {

    private static final int DATABASE_VERSION = 3;
    private static final String DATABASE_NAME = "recordsDB";
    private static final String TABLE_NAME = "records";

    private static final String ID = "id";
    private static final String TITLE = "title";
    private static final String TEXT = "text";
    private static final String TYPE = "type";
    private static final String DATETIME = "datetime";
    private static final String CATEGORY = "category";
    private static final String LOCATION = "location";
    private static final String LINK_TO_RESOURCE = "link_to_resource";

    public SQLiteDbHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        Log.w("Database", "Creating database.");

        String CREATE_TABLE =
                "CREATE TABLE " + TABLE_NAME + " (" + ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                        TITLE + " TEXT, " +
                        TEXT + " TEXT, " +
                        TYPE + " TEXT, " +
                        DATETIME + " DATETIME, " +
                        CATEGORY + " TEXT, " +
                        LOCATION + " TEXT, " +
                        LINK_TO_RESOURCE + " TEXT )";
        db.execSQL(CREATE_TABLE);

    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        Log.w("Database", "Upgrading database, this will drop tables and recreate.");
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME);
        onCreate(db);
    }
}
