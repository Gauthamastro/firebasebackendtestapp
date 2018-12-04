package com.backendtestapp.gautham.firebasebackendtestapp;

import android.content.Context;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;


import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.GoogleAuthProvider;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.io.FileOutputStream;
import java.util.HashMap;
import java.util.Map;


public class MainActivity extends AppCompatActivity {

    //a constant for detecting the login intent result
    private static final int RC_SIGN_IN = 234;
    private static final String TAG = "BackendTestapp";
    //creating a GoogleSignInClient object
    GoogleSignInClient mGoogleSignInClient;
    //And also a Firebase Auth object
    FirebaseAuth mAuth;
    DatabaseHelperuserinfo mDatabaseHelper;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        //first we intialized the FirebaseAuth object
        mAuth = FirebaseAuth.getInstance();

        //Then we need a GoogleSignInOptions object
        //And we need to build it as below
        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(getString(R.string.default_web_client_id))
                .requestEmail()
                .build();

        //Then we will get the GoogleSignInClient object from GoogleSignIn class
        mGoogleSignInClient = GoogleSignIn.getClient(this, gso);

        //Now we will attach a click listener to the sign_in_button
        //and inside onClick() method we are calling the signIn() method that will open
        //google sign in intent

        final Button signin = findViewById(R.id.signin);
        signin.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                // Code here executes on main thread after user presses button
                signInfn();
            }

        });

        mDatabaseHelper = new DatabaseHelperuserinfo(this);


    }

    @Override
    protected void onStart() {
        super.onStart();

        //if the user is already signed in
        //we will close this activity
        //and take the user to profile activity
        if (mAuth.getCurrentUser() != null) {
            finish();
            startActivity(new Intent(this, feed.class));
        }
    }

    //this method is called on click
    private void signInfn() {
        //getting the google signin intent
        Intent signInIntent = mGoogleSignInClient.getSignInIntent();

        //starting the activity for result
        startActivityForResult(signInIntent, RC_SIGN_IN);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        //if the requestCode is the Google Sign In code that we defined at starting
        if (requestCode == RC_SIGN_IN) {

            //Getting the GoogleSignIn Task
            Task<GoogleSignInAccount> task = GoogleSignIn.getSignedInAccountFromIntent(data);
            try {
                //Google Sign In was successful, authenticate with Firebase
                GoogleSignInAccount account = task.getResult(ApiException.class);
                launchfeedAct(account);
            } catch (ApiException e) {
                Toast.makeText(MainActivity.this, e.getMessage(), Toast.LENGTH_SHORT).show();
            }

        }
    }

    protected void launchfeedAct(GoogleSignInAccount account) {
        AuthCredential credential = GoogleAuthProvider.getCredential(account.getIdToken(), null);
        mAuth.signInWithCredential(credential)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // Sign in success, update UI with the signed-in user's information
                            Log.d(TAG, "signInWithCredential:success");
                            FirebaseUser user = mAuth.getCurrentUser();
                            if (user.getEmail().contains("@nitc.ac.in")) {

                                feedfn(user);

                            } else {
                                revokeUser(user);
                            }

                        } else {
                            // If sign in fails, display a message to the user.
                            Log.w(TAG, "Sign In Failed! ", task.getException());
                        }
                    }
                });
    }

    private void revokeUser(FirebaseUser user) {
        user.delete()
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if (task.isSuccessful()) {
                            Log.d(TAG, "User account deleted.");
                            //Toast.makeText(MainActivity.this,"User Account deleted! ",Toast.LENGTH_LONG);
                        }
                    }
                });
        mGoogleSignInClient.revokeAccess().addOnCompleteListener(this,
                new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        Toast.makeText(MainActivity.this, "You are not an NITCian! Get the hell out!", Toast.LENGTH_LONG).show();
                    }
                });
    }


    private void feedfn(FirebaseUser user) {

        // Access a Cloud Firestore instance from your Activity
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        //Users info
        String username = user.getDisplayName();
        String email = user.getEmail();
        String UID = user.getUid();
        final String[] splited = email.split("@");
        final String[] splited1 = splited[0].split("_");
        final char[] splitted_array = splited1[1].toCharArray();
        final int year = Integer.parseInt(("20" + String.valueOf(splitted_array[1]) + splitted_array[2]));
        String branch = String.valueOf(splitted_array[splitted_array.length - 2]) + splitted_array[splitted_array.length - 1];
        if (branch.equals("me")) {
            branch = "Mechanical Engg";
        } else if (branch.equals("cs")) {
            branch = "Computer Science Engg";
        } else if (branch.equals("ee")) {
            branch = "Electrical and Electronics Engg";
        } else if (branch.equals("ec")) {
            branch = "Electronics and Communication Engg";
        } else if (branch.equals("ce")) {
            branch = "Civil Engg";
        } else if (branch.equals("ar")) {
            branch = "B. Arch";
        } else if (branch.equals("ch")) {
            branch = "Chemical Engg";
        } else if (branch.equals("io")) {
            branch = "Biotechnology Engg";
        }

        userobj userinfo = new userobj(UID, username, email, branch, year,0);
        if (mDatabaseHelper.addData(userinfo)){
            Toast.makeText(this,"Database created successfully!",Toast.LENGTH_LONG);
        }
        else{
            Toast.makeText(this,"Database creation failed!",Toast.LENGTH_LONG);
        }
        //User info obj
        Map<String, Object> obj = new HashMap<>();
        obj.put("Name", username);
        obj.put("Email", email);
        obj.put("Year of Admission ", year);
        obj.put("Branch", branch);
        obj.put("Priority",0);

// Add a new document with a generated ID
        db.collection("users")
                .document(user.getUid()).set(obj)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void avoid) {
                        Log.d(TAG, " Document added successfully!");
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.w(TAG, "Error adding document", e);
                    }
                });


    finish();

    startActivity(new Intent(this, feed .class));
}
    }

