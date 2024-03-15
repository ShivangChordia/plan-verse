package com.example.assignment_2;

/*
File: DatabaseHelper.java
Project: Assignment 2 - Mobile App Dev
Programmers: Shivang Chordia, 8871092, schordia1092@conestogac.on.ca
First Version: 11 March, 2024
Description: It is the Java page for helper class of Database
*/

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class DatabaseHelper extends SQLiteOpenHelper {

    public static final String DATABASE_NAME = "Events.db";
    public static final String TABLE_NAME = "Events";
    public static final String COL1 = "ID";
    public static final String COL2 = "EVENT_NAME";
    public static final String COL3 = "DESCRIPTION";
    public static final String COL4 = "NUMBER_OF_PEOPLE";
    public static final String COL5 = "DATE";


    public static final String TABLE_CHECKLIST_ITEMS = "checklist_items";
    public static final String COL_CHECKLIST_ID = "id";
    public static final String COL_EVENT_ID = "event_id";
    public static final String COL_ITEM_TEXT = "item_text";

    public DatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, 4);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("CREATE TABLE IF NOT EXISTS " + TABLE_NAME + "(" +
                COL1 + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                COL2 + " TEXT, " +
                COL3 + " TEXT, " +
                COL4 + " INTEGER, " +
                COL5 + " TEXT)");

        db.execSQL("CREATE TABLE IF NOT EXISTS " + TABLE_CHECKLIST_ITEMS + "(" +
                COL_CHECKLIST_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                COL_EVENT_ID + " INTEGER, " +
                COL_ITEM_TEXT + " TEXT, " +
                "FOREIGN KEY (" + COL_EVENT_ID + ") REFERENCES " + TABLE_NAME + "(" + COL1 + "))");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME);
        onCreate(db);
    }

    public long insertData(String eventName, String description, int numberOfPeople, String date) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(COL2, eventName);
        contentValues.put(COL3, description);
        contentValues.put(COL4, numberOfPeople);
        contentValues.put(COL5, date);
        long result = db.insert(TABLE_NAME, null, contentValues);
        return result; // Return the ID of the newly inserted event
    }


    public boolean updateEvent(long eventId, String eventName, String description, int numberOfPeople, String date) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(COL2, eventName);
        values.put(COL3, description);
        values.put(COL4, numberOfPeople);
        values.put(COL5, date);


        int affectedRows = db.update(TABLE_NAME, values, COL1 + "=?", new String[]{String.valueOf(eventId)});


        return affectedRows > 0;
    }


    public void deleteEvent(long id) {
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(TABLE_NAME, COL1 + " = ?", new String[]{String.valueOf(id)});
    }


    public Cursor getAllData() {
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor res = db.rawQuery("SELECT * FROM " + TABLE_NAME, null);
        return res;
    }

    // Method to insert a checklist item
    public void addChecklistItem(int eventId, String itemText) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(COL_EVENT_ID, eventId);
        values.put(COL_ITEM_TEXT, itemText);
        db.insert(TABLE_CHECKLIST_ITEMS, null, values);
    }

    public Cursor getChecklistItemsForEvent(int eventId) {
        SQLiteDatabase db = this.getReadableDatabase();
        String selectQuery = "SELECT * FROM " + TABLE_CHECKLIST_ITEMS +
                " WHERE " + COL_EVENT_ID + " = ?";
        return db.rawQuery(selectQuery, new String[]{String.valueOf(eventId)});
    }




}
