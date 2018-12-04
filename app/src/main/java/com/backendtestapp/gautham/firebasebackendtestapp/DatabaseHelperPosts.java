package com.backendtestapp.gautham.firebasebackendtestapp;

import android.content.ContentValues;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

public class DatabaseHelperPosts extends SQLiteOpenHelper {

    private static final String TABLE_NAME = "userposts";

    private static  final String postUid="POSTUID",Uid="UID",Title="TITLE",Content="CONTENT",Img_path="IMGPATH";
    private String Priority = "PRIORITY";
    private String time = "TIMESTAMP";
    public DatabaseHelperPosts(Context context) {
        super(context, TABLE_NAME, null, 1);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String createTable =" CREATE TABLE "+TABLE_NAME+"("+ postUid + " TEXT PRIMARY KEY,"+
        Uid +" TEXT ,"+Title+" TEXT,"+Content+" TEXT ,"+Img_path+" TEXT,"+Priority+" INTEGER,"+time+" TEXT)";
        db.execSQL(createTable);

    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP IF TABLE EXISTS "+TABLE_NAME);
        onCreate(db);

    }

    public boolean addData(post_data item){
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(postUid,item.getPostUid());
        contentValues.put(Uid,item.getUid());
        contentValues.put(Title,item.getTitle());
        contentValues.put(Content,item.getContent());
        contentValues.put(Img_path,item.getImg_path());
        contentValues.put(Priority,item.getPriority());
        contentValues.put(time,String.valueOf(item.getTime()));
        Log.d("SQLite database","AddData: adding data to "+ TABLE_NAME);
        long result = db.insert(TABLE_NAME,null,contentValues);
        if (result == -1){
            return false;
        }
        else{
            return true;
        }
    }


}
