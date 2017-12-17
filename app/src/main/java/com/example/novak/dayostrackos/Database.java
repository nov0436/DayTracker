package com.example.novak.dayostrackos;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteStatement;

/**
 * Created by novak on 16-Dec-17.
 */

public class Database {

    private Context context;
    private SQLiteDatabase db;

    /*
        public String id;
    public String title;
    public String text;
    public String type;
    public String datetime;
    public String category;
    public String location;
    public String link_to_resource;
     */


    private SQLiteStatement selectStmt;
    private SQLiteStatement insertStmt;
    private SQLiteStatement updateStmt;
    private SQLiteStatement deleteStmt;

    private static final String DATABASE_NAME = "recordsDB";
    private static final String TABLE_NAME = "records";

    private static final String SELECT = "SELECT title, text, type, datetime, category, location, link_to_resource FROM " + TABLE_NAME +
            " WHERE type = (?) AND datetime = (?)";
    private static final String INSERT = "insert into " + TABLE_NAME + "(title, text, type, datetime, category, location, link_to_resource) " +
            "values ((?), (?), (?), (?), (?), (?), (?))";
//
//    private static final String UPDATE = "update " + TABLE_NAME + " SET name = (?), fueltype = (?), price = (?) WHERE id = (?)";
//    private static final String DELETE = "delete from " + TABLE_NAME + " where id = (?)";

    private Cursor c;

    public Database(Context context) {

        this.context = context;
        SQLiteDbHelper openHelper = new SQLiteDbHelper(this.context);

        this.db = openHelper.getWritableDatabase();



        this.selectStmt = this.db.compileStatement(SELECT);
        this.insertStmt = this.db.compileStatement(INSERT);
//        this.updateStmt = this.db.compileStatement(UPDATE);
//        this.deleteStmt = this.db.compileStatement(DELETE);

//        openHelper.onUpgrade(db, 0, 1);
    }


//    private static final String INSERT = "insert into " + TABLE_NAME + "(title, text, type, datetime, category, location, link_to_resource) " +
//            "values ((?), (?), (?), (?), (?), (?), (?))";
//
    public long insert(Record r){
        this.insertStmt.bindString(1, r.title);
        this.insertStmt.bindString(2, r.text);
        this.insertStmt.bindString(3, r.type);
        this.insertStmt.bindString(4, r.datetime);
        this.insertStmt.bindString(5, r.category);
        if (r.location != null)
            this.insertStmt.bindString(6, r.location);
        else
            this.insertStmt.bindNull(6);
        if (r.location != null)
            this.insertStmt.bindString(7, r.link_to_resource);
        else
            this.insertStmt.bindNull(7);

        return this.insertStmt.executeInsert();
    }

    public Record getFirst() {

        c = this.db.query(TABLE_NAME, new String[] { "title", "text", "type", "datetime", "category", "location", "link_to_resource" },
                null, null, null, null, null);

        if (c.moveToFirst())
        {
            String title = c.getString(0);
            String text = (c.getString(1));
            String type = (c.getString(2));
            String datetime = (c.getString(3));
            String category  = (c.getString(4));
            String location = (c.getString(5));
            String link_to_resource = (c.getString(6));

            Record record = new Record(title, text, type, datetime, category, location, link_to_resource);

            return record;
        }
        return null;
    }


    /*


    public Record getFirst() {

       c = this.db.query(TABLE_NAME, new String[] { "id", "name", "fueltype", "price" },
                null, null, null, null, "name desc");

        if (c.moveToFirst())
        {
            int id = c.getInt(0);
            String name = (c.getString(1));
            String fueltype = (c.getString(2));
            int price = c.getInt(3);
            FuelItem item = new FuelItem(id, name, fueltype, price);
            return item;
        }
        return null;
    }

    public FuelItem getNext() {
        if (c.moveToNext())
        {
            int id = c.getInt(0);
            String name = (c.getString(1));
            String fueltype = (c.getString(2));
            int price = c.getInt(3);
            FuelItem item = new FuelItem(id, name, fueltype, price);
            return item;
        }
        return null;
    }

    public String getPrev() {
        if (c.moveToPrevious())
            return (c.getString(0));

        return "no more records";
    }

*/



}