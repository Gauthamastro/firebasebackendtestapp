package com.backendtestapp.gautham.firebasebackendtestapp;

import android.content.Intent;
import android.nfc.Tag;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;

public class feed extends AppCompatActivity {

    FirebaseAuth mAuth;
    private GoogleSignInClient mGoogleSignInClient;
    private RecyclerView mRecyclerView;
    private RecyclerView.LayoutManager mLayoutManager;
    private RecyclerView.Adapter mAdapter;
    private ArrayList<String> mDataset;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_feed);

        mRecyclerView = findViewById(R.id.recycler_view);
        mDataset = new ArrayList<>();



        //Create data for the feed
        for (int i = 0;i<10; i++){
            mDataset.add("Welcome to #" + i);
        }

        mLayoutManager = new LinearLayoutManager(this);
        mRecyclerView.setHasFixedSize(true);
        mAdapter = new MainAdapter(mDataset);
        mRecyclerView.setLayoutManager(mLayoutManager);
        mRecyclerView.setAdapter(mAdapter);


        mAuth = FirebaseAuth.getInstance();
        final FirebaseUser user = mAuth.getCurrentUser();
        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(getString(R.string.default_web_client_id))
                .requestEmail()
                .build();
        //Then we will get the GoogleSignInClient object from GoogleSignIn class
        mGoogleSignInClient = GoogleSignIn.getClient(this, gso);

        final FloatingActionButton fab = findViewById(R.id.fabmain);
        final FloatingActionButton fabpost = findViewById(R.id.fabpost);
        final FloatingActionButton fabolx = findViewById(R.id.fabolx);
        fabpost.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                closeFAB();
                newpost();


            }
            private void closeFAB(){
                fabpost.animate().translationY(0).setDuration(170);
                fabolx.animate().translationY(0).setDuration(170);
                fab.setImageResource(R.drawable.ic_outline_keyboard_arrow_up_24px);
            }
        });
        fabolx.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                closeFAB();
                newolxpost();
            }
            private void closeFAB(){
                fabpost.animate().translationY(0).setDuration(170);
                fabolx.animate().translationY(0).setDuration(170);
                fab.setImageResource(R.drawable.ic_outline_add_24px);
            }
        });
        fab.setOnClickListener(new View.OnClickListener() {
            boolean isFABOpen = false;
            @Override
            public void onClick(View view) {
                if(!isFABOpen){
                    showFABMenu();
                }else{
                    closeFABMenu();
                }

            }

            private void closeFABMenu() {
                isFABOpen=false;
                fabpost.animate().translationY(0).setDuration(170);
                fabolx.animate().translationY(0).setDuration(170);
                fab.setImageResource(R.drawable.ic_outline_keyboard_arrow_up_24px);
            }

            private void showFABMenu() {
                isFABOpen=true;
                fabpost.animate().translationY(-getResources().getDimension(R.dimen.standard_65)).setDuration(170);
                fabolx.animate().translationY(-getResources().getDimension(R.dimen.standard_130)).setDuration(170);
                fab.setImageResource(R.drawable.ic_baseline_expand_more_24px);

            }

        });
    }

    private void newolxpost() {
        startActivity(new Intent(this, newolxpost.class));
    }

    private void newpost() {
        startActivity(new Intent(this, newpost.class));
    }

    private void revokeUser() {
        mAuth.signOut();
        mGoogleSignInClient.revokeAccess().addOnCompleteListener(this,
                new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        Toast.makeText(feed.this,"Signed Out!", Toast.LENGTH_LONG).show();
                        signout();

                    }
                });
    }

    private void signout() {
        finish();
        startActivity(new Intent(this, MainActivity.class));
    }


    @Override
    protected void onStart() {
        super.onStart();

        //if the user is not logged in
        //opening the login activity
        if (mAuth.getCurrentUser() == null) {
            finish();
            startActivity(new Intent(this, MainActivity.class));
        }
    }

}
