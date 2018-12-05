package com.backendtestapp.gautham.firebasebackendtestapp;

import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
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
import java.io.IOException;
import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class newolxpost extends AppCompatActivity {
    private static final int SELECT_FILE = 0;
    private ArrayList<String> dropdownlist ;
    private ArrayList<Uri> image_uri_list = new ArrayList<>();
    public ArrayList<String> cloudPaths = new ArrayList<>();
    DatabaseHelperuserinfo userinfo ;
    private FirebaseStorage storage = FirebaseStorage.getInstance();
    FirebaseAuth mAuth = FirebaseAuth.getInstance();
    FirebaseUser user = mAuth.getCurrentUser();
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
        final Spinner dropdown = findViewById(R.id.dropdown_list);
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_dropdown_item, dropdownlist);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        dropdown.setAdapter(adapter);

        final  ArrayList<String> bargain_boolean = new ArrayList<>();
        bargain_boolean.add("True");
        bargain_boolean.add("False");
        final  Spinner bargain = findViewById(R.id.Negotiable);
        ArrayAdapter<String> adapter1 = new ArrayAdapter<>(this,android.R.layout.simple_spinner_dropdown_item,bargain_boolean);
        adapter1.setDropDownViewResource(android.R.layout.simple_spinner_item);
        bargain.setAdapter(adapter1);

        //Code for btns
        Button add_btn = findViewById(R.id.btn_add);
        final Button post_btn = findViewById(R.id.btn_post);

        final TextInputEditText title = findViewById(R.id.title_text);
        final TextInputEditText description = findViewById(R.id.content_text);
        final EditText price = findViewById(R.id.price);

        TextView username = findViewById(R.id.username_text);
        userinfo = new DatabaseHelperuserinfo(this);
        username.setText(String.valueOf(userinfo.getUserName()));
        final DatabaseHelperOlxPosts mDatabaseHelperOlxPosts = new DatabaseHelperOlxPosts(this);

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
                final String negotiable = bargain.getSelectedItem().toString();
                final String Price = String.valueOf(price.getText());
                boolean goAhead = true;
                String Uid = userinfo.getUID();
                final ProgressDialog dialog = new ProgressDialog(newolxpost.this);
                dialog.setTitle("Uploading Post");
                dialog.setMessage("Please wait while the post is being uploaded! \n Don't do anything nasty have faith in your network connection!");
                dialog.setIndeterminate(true);
                dialog.setCanceledOnTouchOutside(false);
                dialog.show();
                if (String.valueOf(title.getText()).contentEquals("")){
                    toast("Don't be lazy type some sense into title!");
                    goAhead = false;
                    dialog.dismiss();
                }
                else if (String.valueOf(description.getText()).contentEquals("")){
                    toast("Come on buddy you can have some commitment to description atleast!");
                    goAhead = false;
                    dialog.dismiss();
                } else if (Price.contentEquals("")) {
                    toast("You are a Generous person it seems?! Enter some price idiot!");
                    goAhead = false;
                    dialog.dismiss();
                }
                dialog.setMessage("Notifying the authorities!");
                if (goAhead) {
                    final olxpost_data post_data = new olxpost_data(userinfo.getUID(), userinfo.getUserName(), String.valueOf(title.getText()), String.valueOf(description.getText()), 0);
                    post_data.setBargainable(negotiable);
                    post_data.setPrice(Price);
                    post_data.setProductType(dropdown.getSelectedItem().toString());
                    post_data.setPostUid(String.valueOf(UUID.randomUUID()));
                    if (image_uri_list.size() == 0) {
                        toast("Atleast 1 picture should be sent to aliens !");
                    } else {
                        String postUid = post_data.getPostUid();
                        for (int i = 0; i < image_uri_list.size(); i++) {
                            String path = "users/" + Uid + "/olxposts/" + postUid + "/" + postUid + String.valueOf(i) + ".jpg";
                            final StorageReference tempRef = storage.getReference(path);
                            final int finalI = i;
                            Bitmap bitmap = bitmapfromUri(image_uri_list.get(i));
                            ByteArrayOutputStream baos = new ByteArrayOutputStream();
                            bitmap.compress(Bitmap.CompressFormat.JPEG, 50, baos);
                            byte[] data = baos.toByteArray();
                            tempRef.putBytes(data).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                                @Override
                                public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                                    tempRef.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                                        @Override
                                        public void onSuccess(Uri uri) {
                                            Log.d("IMAGE UPLOAD", uri.toString());
                                            cloudPaths.add(uri.toString());
                                            dialog.setMessage("Martians got "+(finalI +1)+" out of "+image_uri_list.size()+" items. \n");
                                            if (finalI == image_uri_list.size()-1){
                                                post_data.setCloudPath(cloudPaths);
                                                mDatabaseHelperOlxPosts.addData(post_data);//Adding data abt post to local database!

                                                //Mapping post data for firebase upload
                                                Map<String, Object> obj = new HashMap<>();
                                                obj.put("POSTUID",post_data.getPostUid());
                                                obj.put("UID",post_data.getUid());
                                                obj.put("TITLE",post_data.getTitle());
                                                obj.put("CONTENT",post_data.getContent());
                                                obj.put("IMGPATH",post_data.getImg_path());
                                                obj.put("PRIORITY",post_data.getPriority());
                                                obj.put("TIMESTAMP",post_data.getTime());
                                                obj.put("LIKES",post_data.getLikes());
                                                obj.put("PRODUCTTYPE",post_data.getProductType());
                                                obj.put("BARGAINABLE",post_data.getBargainable());
                                                obj.put("PRICE",post_data.getPrice());

                                                FirebaseFirestore db = FirebaseFirestore.getInstance();
                                                CollectionReference ref_users = db.collection("users");
                                                ref_users.document(user.getUid()).collection("OlxPosts").document(post_data.getPostUid()).set(obj)
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
                                                ref_feed.document(post_data.getPostUid()).set(obj)
                                                        .addOnSuccessListener(new OnSuccessListener<Void>() {
                                                            @Override
                                                            public void onSuccess(Void avoid) {
                                                                Log.d("NEWPOST", " Document added to feed  successfully!");
                                                            }
                                                        })
                                                        .addOnFailureListener(new OnFailureListener() {
                                                            @Override
                                                            public void onFailure(@NonNull Exception e) {
                                                                Log.w("NEWPOST", "Error adding document to feed ", e);
                                                            }
                                                        });
                                                dialog.dismiss();
                                                invokeOlxfeed();
                                            }
                                        }
                                    });
                                }
                            });
                        }}}}});

                        }

    private Bitmap bitmapfromUri(Uri uri) {
        try {
            return MediaStore.Images.Media.getBitmap(this.getContentResolver(), uri);
        } catch (IOException e) {
            e.printStackTrace();
            Toast.makeText(this,"Failed to compress Image",Toast.LENGTH_LONG).show();
            return null;
        }
    }

    private void invokeOlxfeed() {
        toast("Post uploaded!");
        startActivity(new Intent(this,feed.class));
    }

    private void toast (String s){
                            Toast.makeText(this, s, Toast.LENGTH_LONG).show();
                        }

                        private void imagechooser () {
                            Toast.makeText(this, "Only 3 images allowed !", Toast.LENGTH_SHORT).show();
                            Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
                            intent.putExtra(Intent.EXTRA_ALLOW_MULTIPLE, true);
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
                        public void onActivityResult ( int requestCode, int resultCode, Intent data)
                        {
                            super.onActivityResult(requestCode, resultCode, data);
                            if (requestCode == SELECT_FILE) {
                                if (null != data) { // checking empty selection
                                    if (null != data.getClipData()) { // checking multiple selection or not
                                        for (int i = 0; (i < data.getClipData().getItemCount() && i < 3); i++) {
                                            Uri uri = data.getClipData().getItemAt(i).getUri();
                                            Log.d("IMAGES", String.valueOf(uri));
                                            image_uri_list.add(uri);
                                        }
                                        ImageView img = findViewById(R.id.displayimg);
                                        Bitmap bitmap = null;
                                        try {
                                            bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), image_uri_list.get(0));
                                        } catch (IOException e) {
                                            e.printStackTrace();
                                        }
                                        img.setImageBitmap(bitmap);
                                    } else {
                                        Uri uri = data.getData();
                                        Log.d("IMAGES", String.valueOf(uri));
                                        image_uri_list.add(uri);
                                    }
                                }
                            }
                        }
                    }

