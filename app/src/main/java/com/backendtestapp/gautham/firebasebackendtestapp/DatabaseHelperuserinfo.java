package com.backendtestapp.gautham.firebasebackendtestapp;

import android.content.ContentValues;
import android.content.Context;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

public class DatabaseHelperuserinfo extends SQLiteOpenHelper {

    private static final String TABLE_NAME = "userinfo";
    private static final String UID = "UID";
    private static final String NAME ="NAME";
    private static final String EMAIL = "EMAIL";
    private static final String BRANCH = "BRANCH";
    private static final String YEAR = "YEAROFADMISSION";
    private static final String PRIORITY = "PRIORITY";


    public DatabaseHelperuserinfo(Context context) {
        super(context,TABLE_NAME,null,1);
    }

    public void onCreate(SQLiteDatabase db) {
        String createTable = "CREATE TABLE " + TABLE_NAME+ "("+UID +" TEXT PRIMARY KEY, " +
                NAME +" TEXT ,"+EMAIL+" TEXT,"+BRANCH+" TEXT ,"+YEAR+" INTEGER,"+PRIORITY+" INTEGER)";
        db.execSQL(createTable);

    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP IF TABLE EXISTS "+TABLE_NAME);
        onCreate(db);
    }
    public SQLiteDatabase getdb(){
        SQLiteDatabase db = this.getWritableDatabase();
        return db;
    }

    public boolean addData(userobj item){
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(UID,item.getUID());
        contentValues.put(NAME,item.getUserName());
        contentValues.put(BRANCH,item.getBranch());
        contentValues.put(YEAR,item.getYear());
        contentValues.put(EMAIL,item.getUserEmail());
        contentValues.put(PRIORITY,item.getPriority());
        Log.d("SQLite database","AddData: adding data to "+ TABLE_NAME);
        long result = db.insert(TABLE_NAME,null,contentValues);
        if (result == -1){
            return false;
        }
        else{
            return true;
        }
    }

    public String getUserName(){
        SQLiteDatabase db = this.getWritableDatabase();
        String query = "SELECT "+NAME+" FROM " + TABLE_NAME;
        Cursor cursor = db.rawQuery(query,null);
        if (cursor != null & cursor.getCount() > 0) {
            // if  (c.moveToFirst()) {
            cursor.moveToFirst();
            String name =cursor.getString(0);
            cursor.close();
            return name;
        }
        else {
            cursor.close();
            return null;
        }
    }

    public  String getUID() {
        SQLiteDatabase db = this.getWritableDatabase();
        String query = "SELECT " + UID + " FROM " + TABLE_NAME;
        Cursor cursor = db.rawQuery(query, null);
        if (cursor != null & cursor.getCount() > 0) {
            // if  (c.moveToFirst()) {
            cursor.moveToFirst();
            String uid =cursor.getString(0);
            cursor.close();
            return uid;
        }
        else {
            cursor.close();
            return null;
        }
    }

    public  String getBRANCH() {
        SQLiteDatabase db = this.getWritableDatabase();
        String query = "SELECT "+BRANCH+" FROM " + TABLE_NAME;
        Cursor cursor = db.rawQuery(query,null);
        if (cursor != null & cursor.getCount() > 0) {
            // if  (c.moveToFirst()) {
            cursor.moveToFirst();
            String br =cursor.getString(0);
            cursor.close();
            return br;
        }
        else {
            cursor.close();
            return null;
        }

    }

    public  String getEMAIL() {
        SQLiteDatabase db = this.getWritableDatabase();
        String query = "SELECT "+EMAIL+" FROM " + TABLE_NAME;
        Cursor cursor = db.rawQuery(query,null);
        if (cursor != null & cursor.getCount() > 0) {
            // if  (c.moveToFirst()) {
            cursor.moveToFirst();
            cursor.close();
            String email =cursor.getString(0);
            cursor.close();
            return (email);
        }else {
            cursor.close();
            return null;
        }


    }


    public  Integer getPRIORITY() {
        SQLiteDatabase db = this.getWritableDatabase();
        String query = "SELECT "+PRIORITY+" FROM " + TABLE_NAME;
        Cursor cursor = db.rawQuery(query,null);
        if (cursor != null & cursor.getCount() > 0) {
            // if  (c.moveToFirst()) {
            cursor.moveToFirst();
            String priority =cursor.getString(0);
            cursor.close();
            return Integer.parseInt(priority);
        }
        else {
            cursor.close();
            return null;
        }
    }

    public  Integer getYEAR() {
        SQLiteDatabase db = this.getWritableDatabase();
        String query = "SELECT "+YEAR+" FROM " + TABLE_NAME;
        Cursor cursor = db.rawQuery(query,null);
        if (cursor != null & cursor.getCount() > 0) {
            // if  (c.moveToFirst()) {
            cursor.moveToFirst();
            String year =cursor.getString(0);
            cursor.close();
            return Integer.parseInt(year);
        }
        else {
            cursor.close();
            return null;
        }
    }

}
