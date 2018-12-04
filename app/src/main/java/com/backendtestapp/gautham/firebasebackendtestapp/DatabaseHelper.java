package com.backendtestapp.gautham.firebasebackendtestapp;

import android.content.ContentValues;
import android.content.Context;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

public class DatabaseHelper extends SQLiteOpenHelper {

    private static final String TABLE_NAME = "userinfo";
    private static final String UID = "UID";
    private static final String NAME ="NAME";
    private static final String EMAIL = "EMAIL";
    private static final String BRANCH = "BRANCH";
    private static final String YEAR = "YEAROFADMISSION";


    public DatabaseHelper(Context context) {
        super(context,TABLE_NAME,null,1);
    }

    public void onCreate(SQLiteDatabase db) {
        String createTable = "CREATE TABLE " + TABLE_NAME+ "("+UID +" TEXT PRIMARY KEY, " +
                NAME +" TEXT ,"+EMAIL+" TEXT,"+BRANCH+" TEXT ,"+YEAR+" INTEGER)";
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
        Log.d("SQLite database","AddData: adding data to "+ TABLE_NAME);
        long result = db.insert(TABLE_NAME,null,contentValues);
        if (result == -1){
            return false;
        }
        else{
            return true;
        }
    }

    public Cursor getUserName(){
        SQLiteDatabase db = this.getWritableDatabase();
        String query = "SELECT "+NAME+" FROM " + TABLE_NAME;
        Cursor data = db.rawQuery(query,null);
        return data;
    }
}
