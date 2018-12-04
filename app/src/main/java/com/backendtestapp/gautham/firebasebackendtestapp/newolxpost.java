package com.backendtestapp.gautham.firebasebackendtestapp;

import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.provider.MediaStore;
import android.support.design.widget.TextInputEditText;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import java.io.IOException;
import java.lang.reflect.Array;
import java.util.ArrayList;

public class newolxpost extends AppCompatActivity {
    private static final int SELECT_FILE = 0;
    private ArrayList<String> dropdownlist ;
    private ArrayList<Uri> image_uri_list = new ArrayList<>();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_newolxpost);
        //Spinner code
        dropdownlist = new ArrayList<>();
        dropdownlist.add("Books");
        dropdownlist.add("TextBooks");
        dropdownlist.add("Electronics");
        dropdownlist.add("Coupons");
        Spinner dropdown = findViewById(R.id.dropdown_list);
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_dropdown_item, dropdownlist);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        dropdown.setAdapter(adapter);

        //Code for btns
        Button add_btn = findViewById(R.id.btn_add);
        Button post_btn = findViewById(R.id.btn_post);

        TextInputEditText title = findViewById(R.id.title_text);
        TextInputEditText description = findViewById(R.id.content_text);
        TextView username = findViewById(R.id.username_text);

        add_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                imagechooser();
            }
        });

        final ImageView img = findViewById(R.id.displayimg);
        img.setOnClickListener(new View.OnClickListener() {
            int counter = 0;
            @Override
            public void onClick(View v) {
                if (image_uri_list.size() != 0){
                    if (counter >= image_uri_list.size()){
                        counter = 0;
                    }
                    Bitmap bitmap = null;
                    try {
                        bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(),image_uri_list.get(counter));
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    img.setImageBitmap(bitmap);
                    counter++;
                }
            }
        });

        post_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                
            }
        });



    }

    private void imagechooser() {
        Toast.makeText(this,"Only 3 images allowed !",Toast.LENGTH_SHORT).show();
        Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
        intent.putExtra(Intent.EXTRA_ALLOW_MULTIPLE,true);
        intent.setType("image/*");

        try {
            startActivityForResult(
                    Intent.createChooser(intent, "Select an Image to Upload"),
                    SELECT_FILE);
        } catch (android.content.ActivityNotFoundException ex) {
            // Potentially direct the user to the Market with a Dialog
            Toast.makeText(this, "Please install a File Manager.",
                    Toast.LENGTH_SHORT).show();
        }
    }
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode == SELECT_FILE) {
            if(null != data) { // checking empty selection
                if(null != data.getClipData()) { // checking multiple selection or not
                    for(int i = 0; (i < data.getClipData().getItemCount() && i <3); i++) {
                        Uri uri = data.getClipData().getItemAt(i).getUri();
                        Log.d("IMAGES",String.valueOf(uri));
                        image_uri_list.add(uri);
                    }
                    ImageView img = findViewById(R.id.displayimg);
                    Bitmap bitmap = null;
                    try {
                        bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(),image_uri_list.get(0));
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    img.setImageBitmap(bitmap);
                } else {
                    Uri uri = data.getData();
                    Log.d("IMAGES",String.valueOf(uri));
                    image_uri_list.add(uri);
                }
            }
        }
    }
}
