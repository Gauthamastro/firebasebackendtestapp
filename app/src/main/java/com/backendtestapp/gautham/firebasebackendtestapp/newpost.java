package com.backendtestapp.gautham.firebasebackendtestapp;

import android.database.Cursor;
import android.os.AsyncTask;
import android.support.design.widget.TextInputEditText;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;

import io.opencensus.tags.Tag;

public class newpost extends AppCompatActivity {

    DatabaseHelper mDatabaseHelper;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_newpost);

        Button add = findViewById(R.id.btn_add);
        Button post = findViewById(R.id.btn_post);

        ImageView img = findViewById(R.id.displayimg);
        TextInputEditText tilte = findViewById(R.id.title_text);
        TextInputEditText content = findViewById(R.id.content_text);
        TextView username = findViewById(R.id.username_text);
        mDatabaseHelper = new DatabaseHelper(this);

        Cursor data = mDatabaseHelper.getUserName();

        if (data != null & data.getCount() > 0) {
            // if  (c.moveToFirst()) {
            data.moveToFirst();
            username.setText(data.getString(0));

    }

}}
