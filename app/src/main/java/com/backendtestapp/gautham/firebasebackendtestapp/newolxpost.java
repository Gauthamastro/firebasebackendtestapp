package com.backendtestapp.gautham.firebasebackendtestapp;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.Spinner;

import java.lang.reflect.Array;
import java.util.ArrayList;

public class newolxpost extends AppCompatActivity {
    private ArrayList<String> dropdownlist ;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_newolxpost);

        dropdownlist = new ArrayList<>();
        dropdownlist.add("Books");
        dropdownlist.add("TextBooks");
        dropdownlist.add("Electronics");
        dropdownlist.add("Coupons");


        Spinner dropdown = findViewById(R.id.dropdown_list);
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_dropdown_item, dropdownlist);
// Specify the layout to use when the list of choices appears
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
// Apply the adapter to the spinner
        dropdown.setAdapter(adapter);
    }
}
