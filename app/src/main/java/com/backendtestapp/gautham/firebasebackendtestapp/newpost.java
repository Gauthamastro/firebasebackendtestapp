package com.backendtestapp.gautham.firebasebackendtestapp;

import android.support.design.widget.TextInputEditText;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

public class newpost extends AppCompatActivity {

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


    }
}
