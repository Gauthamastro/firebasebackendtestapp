package com.backendtestapp.gautham.firebasebackendtestapp;

import android.app.ProgressDialog;
import android.content.Intent;
import android.nfc.Tag;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Parcelable;
import android.os.StrictMode;
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
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QuerySnapshot;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class feed extends AppCompatActivity {

    FirebaseAuth mAuth;
    private GoogleSignInClient mGoogleSignInClient;
    private RecyclerView mRecyclerView;
    private RecyclerView.LayoutManager mLayoutManager;
    private RecyclerView.Adapter mAdapter;
    private ArrayList<post_data> mDataset;
    private FirebaseFirestore fs;
    String Url;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_feed);
        mRecyclerView = findViewById(R.id.recycler_view);
        mDataset = new ArrayList<>();

        mAuth = FirebaseAuth.getInstance();
        //final FirebaseUser user = mAuth.getCurrentUser();
        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(getString(R.string.default_web_client_id))
                .requestEmail()
                .build();
        //Then we will get the GoogleSignInClient object from GoogleSignIn class
        mGoogleSignInClient = GoogleSignIn.getClient(this, gso);

        final FloatingActionButton fab = findViewById(R.id.fabmain);
        final FloatingActionButton fabpost = findViewById(R.id.fabpost);
        final FloatingActionButton fabolx = findViewById(R.id.fabolx);
        final ProgressDialog dialog = new ProgressDialog(this);
        dialog.setIndeterminate(true);
        dialog.setTitle("Getting data from the Aliens");
        dialog.setMessage("Wait for it!!! wait for it!");
        dialog.setCanceledOnTouchOutside(false);
        dialog.show();
        mLayoutManager = new LinearLayoutManager(this);
        mRecyclerView.setHasFixedSize(true);
        mAdapter = new MainAdapter(mDataset);
        mRecyclerView.setLayoutManager(mLayoutManager);
        mRecyclerView.setAdapter(mAdapter);
        fs = FirebaseFirestore.getInstance();
        fs.collection("feed").orderBy("Time",Query.Direction.DESCENDING).get()
                .addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                    @Override
                    public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                        if (!queryDocumentSnapshots.isEmpty()){
                            List<DocumentSnapshot> list = queryDocumentSnapshots.getDocuments();
                            for(DocumentSnapshot d: list){
                                post_data data = d.toObject(post_data.class);
                                Log.d("CLASSTEST",data.getImg_path());
                                Log.d("TITLETEST",data.getTitle());
                                mDataset.add(data);

                            }
                            mRecyclerView.setAdapter(mAdapter);
                            mAdapter.notifyDataSetChanged();
                            dialog.dismiss();
                        }
                        else{
                            post_data data = new post_data();
                            Date date = new Date();
                            Timestamp time = new Timestamp(date.getTime());
                            data.setTime(time);
                            data.setTitle("THIS IS A TEST TITLE");
                            data.setLikes(0);
                            data.setPostUid("hgodrhgohgodhgouhgoahghi");
                            data.setImg_path("https://firebasestorage.googleapis.com/v0/b/clg-app-1fab1.appspot.com/o/users%2Fx9Qby9cEeJf9wDL4vRv9qwj05Fz2%2Fposts%2F98322d2c-e309-411b-9844-4563cf479524%2F98322d2c-e309-411b-9844-4563cf479524.jpg?alt=media&token=be6c77b7-a138-4d12-afc6-bcf98e0264cb");
                            data.setContent("THIS IS A TEST CONTENT");
                            data.setUsername("TEST USERNAME");
                            mDataset.add(data);
                            mRecyclerView.setAdapter(mAdapter);
                            mAdapter.notifyDataSetChanged();
                            dialog.dismiss();

                        }
                    }
                });
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
                fab.setImageResource(R.drawable.ic_outline_keyboard_arrow_up_24px);
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

    public class DownloadimageTask extends AsyncTask<String,String,String>
    {

        @Override
        protected String doInBackground(String... strings) {

            return null;
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
        }

        @Override
        protected void onProgressUpdate(String... values) {
            super.onProgressUpdate(values);
        }
    }

}

