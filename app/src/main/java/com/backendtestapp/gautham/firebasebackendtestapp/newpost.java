package com.backendtestapp.gautham.firebasebackendtestapp;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.AsyncTask;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.design.widget.TextInputEditText;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.ByteArrayOutputStream;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.URISyntaxException;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import io.opencensus.tags.Tag;
public class newpost extends AppCompatActivity {

    DatabaseHelperuserinfo mDatabaseHelper = new DatabaseHelperuserinfo(this);
    private static final int SELECT_FILE=0;
    private FirebaseStorage storage = FirebaseStorage.getInstance();
    DatabaseHelperPosts mDatabaseHelperPosts;
    FirebaseAuth mAuth = FirebaseAuth.getInstance();
    FirebaseUser user = mAuth.getCurrentUser();
    Uri img_uri;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_newpost);

        Button add = findViewById(R.id.btn_add);
        Button post = findViewById(R.id.btn_post);

        TextView username = findViewById(R.id.username_text);
        username.setText(String.valueOf(mDatabaseHelper.getUserName()));
        mDatabaseHelperPosts = new DatabaseHelperPosts(this);
        add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ImageChooser();
            }
        });

        post.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                final ProgressDialog dialog = new ProgressDialog(newpost.this);
                dialog.setTitle("Uploading Post");
                dialog.setMessage("Please wait while the post is being uploaded! \n Don't do anything nasty have faith in your network connection!");
                dialog.setIndeterminate(true);
                dialog.setCanceledOnTouchOutside(false);
                dialog.show();

                //Get all the required data
                String Uid=user.getUid(),Username,Title,Content,Img_path=null;
                String postUid= String.valueOf(UUID.randomUUID());

                Integer Priority = 0;

                TextInputEditText tilte = findViewById(R.id.title_text);
                TextInputEditText content = findViewById(R.id.content_text);
                TextView username = findViewById(R.id.username_text);
                Title = String.valueOf(tilte.getText());
                Content = String.valueOf(content.getText());
                Username = String.valueOf(username.getText());

                final post_data mpost_data = new post_data();
                mpost_data.setContent(Content);
                mpost_data.setUid(Uid);
                mpost_data.setLikes(0);
                mpost_data.setUsername(Username);
                mpost_data.setTitle(Title);
                Date date = new Date();
                Timestamp time = new Timestamp(date.getTime());
                mpost_data.setTime(time);
                mpost_data.setPriority(Priority);
                mpost_data.setPostUid(postUid);

//COde related to image upload nad progressbar!
                if (img_uri == null){
                    toast("Select atlest one image u lazy ass!!!");
                }
                else{
                    String path = "users/"+Uid+ "/posts/"+postUid+"/"+postUid+".jpg";
                    final StorageReference tempRef = storage.getReference(path);
                    //start the  progress bar now
                    //disabe the all btns
                    Button add = findViewById(R.id.btn_add);
                    Button post = findViewById(R.id.btn_post);
                    add.setEnabled(false);
                    post.setEnabled(false);
                    Bitmap bitmap = bitmapfromUri(img_uri);
                    ByteArrayOutputStream baos = new ByteArrayOutputStream();
                    bitmap.compress(Bitmap.CompressFormat.JPEG, 50, baos);
                    byte[] data = baos.toByteArray();
                    tempRef.putBytes(data).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                            tempRef.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                                @Override
                                public void onSuccess(Uri uri) {
                                    Log.d("IMAGE UPLOAD",uri.toString());
                                    mpost_data.setImg_path(uri.toString());
                                    mDatabaseHelperPosts.addData(mpost_data);//Adding data abt post to local database!

                                    //Mapping post data for firebase upload
                                    Map<String, Object> obj = new HashMap<>();
                                    obj.put("PostUid",mpost_data.getPostUid());
                                    obj.put("Uid",mpost_data.getUid());
                                    obj.put("Title",mpost_data.getTitle());
                                    obj.put("Content",mpost_data.getContent());
                                    obj.put("Img_path",mpost_data.getImg_path());
                                    obj.put("Priority",mpost_data.getPriority());
                                    obj.put("Time",mpost_data.getTime());
                                    obj.put("Likes",mpost_data.getLikes());
                                    obj.put("Username",mpost_data.getUsername());

                                    FirebaseFirestore db = FirebaseFirestore.getInstance();
                                    CollectionReference ref_users = db.collection("users");
                                    ref_users.document(user.getUid()).collection("Posts").document(mpost_data.getPostUid()).set(obj)
                                            .addOnSuccessListener(new OnSuccessListener<Void>() {
                                                @Override
                                                public void onSuccess(Void avoid) {
                                                    Log.d("NEWUSERPOST", " Document added to user section successfully!");
                                                    dialog.setMessage("Sending your contents to aliens...");
                                                }
                                            })
                                            .addOnFailureListener(new OnFailureListener() {
                                                @Override
                                                public void onFailure(@NonNull Exception e) {
                                                    Log.w("NEWUSERPOST", "Error adding document to user section!", e);
                                                }
                                            });
                                    CollectionReference ref_feed = db.collection("feed");
                                    ref_feed.document(mpost_data.getPostUid()).set(obj)
                                            .addOnSuccessListener(new OnSuccessListener<Void>() {
                                                @Override
                                                public void onSuccess(Void avoid) {
                                                    Log.d("NEWPOST", " Document added to feed  successfully!");
                                                    dialog.setMessage("Notifying the authorities!");
                                                }
                                            })
                                            .addOnFailureListener(new OnFailureListener() {
                                                @Override
                                                public void onFailure(@NonNull Exception e) {
                                                    Log.w("NEWPOST", "Error adding document to feed ", e);
                                                }
                                            });
                                    dialog.dismiss();
                                    invokefeed();
                                }
                            });
                        }
                    });
                }
                //COde for image upload nad progress bar ends here

            }

        });

}

    private Bitmap bitmapfromUri(Uri img_uri) {
        try {
            return  MediaStore.Images.Media.getBitmap(this.getContentResolver(), img_uri);
        } catch (IOException e) {
            e.printStackTrace();
            Toast.makeText(this,"Failed to compress the image",Toast.LENGTH_LONG);
            return null;
        }
    }

    private void toast(String s) {
        Toast.makeText(this,s,Toast.LENGTH_LONG);
    }

    private void invokefeed() {
        Toast.makeText(this,"Uploaded successfully!",Toast.LENGTH_LONG).show();
        finish();
        startActivity(new Intent(this,feed.class));
    }

    private void ImageChooser() {

        Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
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

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        switch (requestCode) {
            case SELECT_FILE:
                if (resultCode == RESULT_OK) {
                    // Get the Uri of the selected file
                    Uri uri = data.getData();
                    img_uri = uri;
                    Log.d("image choosse", "File Uri: " + uri.toString());
                    try {
                        Bitmap bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(),uri);
                        ImageView img = findViewById(R.id.displayimg);
                        img.setImageBitmap(bitmap);
                        Log.d("PATH ",uri.getPath());
                    } catch (FileNotFoundException e) {
                        e.printStackTrace();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    // Get the file instance
                    // File file = new File(path);
                    // Initiate the upload
                }
                break;
        }
        super.onActivityResult(requestCode, resultCode, data);
    }


}
