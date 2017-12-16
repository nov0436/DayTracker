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

    private static final String UPDATE = "update " + TABLE_NAME + " SET name = (?), fueltype = (?), price = (?) WHERE id = (?)";
    private static final String DELETE = "delete from " + TABLE_NAME + " where id = (?)";

    private Cursor c;

    public Database(Context context) {

        this.context = context;
        SQLiteDbHelper openHelper = new SQLiteDbHelper(this.context);

        this.db = openHelper.getWritableDatabase();

        this.selectStmt = this.db.compileStatement(SELECT);
        this.insertStmt = this.db.compileStatement(INSERT);
        this.updateStmt = this.db.compileStatement(UPDATE);
        this.deleteStmt = this.db.compileStatement(DELETE);
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
