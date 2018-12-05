package com.backendtestapp.gautham.firebasebackendtestapp;

import android.content.ContentValues;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

public class DatabaseHelperOlxPosts extends SQLiteOpenHelper {

    private static final String TABLE_NAME = "userolxposts";

    private static  final String postUid="POSTUID",Uid="UID",Title="TITLE",Description="DESCRIPTION",Img_path="IMGPATH";
    private static final String Priority = "PRIORITY",likes = "LIKES";
    private static final String time = "TIMESTAMP";
    private static final String producttype = "PRODUCTTYPE";
    private  static final String price = "PRICE";
    private  static  final String negotiable = "BARGAINBOOLEAN";
    public DatabaseHelperOlxPosts(Context context) {
        super(context, TABLE_NAME, null, 1);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String createTable =" CREATE TABLE "+TABLE_NAME+"("+ postUid + " TEXT PRIMARY KEY,"+
                Uid +" TEXT ,"+Title+" TEXT,"+Description+" TEXT ,"+Img_path+" TEXT,"+Priority+" INTEGER,"+time+" TEXT,"+producttype+" TEXT,"+likes+" INTEGER,"+price+" TEXT,"+negotiable+" TEXT)";
        db.execSQL(createTable);

    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP IF TABLE EXISTS "+TABLE_NAME);
        onCreate(db);

    }

    public boolean addData(olxpost_data item){
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(postUid,item.getPostUid());
        contentValues.put(Uid,item.getUid());
        contentValues.put(Title,item.getTitle());
        contentValues.put(Description,item.getContent());
        contentValues.put(Img_path,item.getImg_path());
        contentValues.put(Priority,item.getPriority());
        contentValues.put(time,String.valueOf(item.getTime()));
        contentValues.put(producttype,item.getProductType());
        contentValues.put(likes,item.getLikes());
        contentValues.put(price,item.getPrice());
        contentValues.put(negotiable,item.getBargainable());
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
