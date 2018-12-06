package com.backendtestapp.gautham.firebasebackendtestapp;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;

public class MainAdapter  extends RecyclerView.Adapter<MainAdapter.ViewHolder> {

    private ArrayList<post_data> mDataset;

    public MainAdapter(ArrayList<post_data> mDataset) {
        this.mDataset = mDataset;
    }

    @Override
    public MainAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType){
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.post_card,parent,false);
        return new ViewHolder(v);
    }

    @Override
    public  void onBindViewHolder(final MainAdapter.ViewHolder holder, final int position){

        final int[] likes_count = {mDataset.get(position).getLikes()};

        holder.post_username.setText(mDataset.get(position).getUsername());
        holder.post_content.setText(mDataset.get(position).getContent());
        holder.post_likes.setText(String.valueOf(mDataset.get(position).getLikes())+" likes");
        holder.post_time.setText(String.valueOf(mDataset.get(position).getTime()));
        holder.post_title.setText(String.valueOf(mDataset.get(position).getTitle()));
        Picasso.get()
                .load(mDataset.get(position).getImg_path())
                .fit()
                .error(R.drawable.test).into(holder.img);
        holder.post_likes.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                // Code here executes on main thread after user presses button
                if (true) { // condition to check whether the user already liked it !
                    likes_count[0] = likes_count[0] + 1;
                    holder.post_likes.setText(String.valueOf(likes_count[0]) + " likes");
                }
                else{
                    likes_count[0] = likes_count[0] - 1;
                    holder.post_likes.setText(String.valueOf(likes_count[0]) + " likes");
                }
            }

        });
    }
    @Override
    public int getItemCount(){
        return mDataset.size();
    }
    public class ViewHolder extends RecyclerView.ViewHolder{

        public final TextView post_username,post_content,post_time,post_title;
        public final Button post_likes;
        public final ImageView img;


        public ViewHolder(@NonNull View itemView) {
            super(itemView);


            post_username = itemView.findViewById(R.id.post_username);
            post_content = itemView.findViewById(R.id.post_content);
            post_likes = itemView.findViewById(R.id.post_likes);
            post_time = itemView.findViewById(R.id.posted_time);
            img = itemView.findViewById(R.id.imageView);
            post_title = itemView.findViewById(R.id.post_card_title);



        }
    }
}
