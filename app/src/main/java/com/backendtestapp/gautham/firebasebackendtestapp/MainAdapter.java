package com.backendtestapp.gautham.firebasebackendtestapp;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;

public class MainAdapter  extends RecyclerView.Adapter<MainAdapter.ViewHolder> {

    private ArrayList<String> mDataset;

    public MainAdapter(ArrayList<String> mDataset) {
        this.mDataset = mDataset;
    }

    @Override
    public MainAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType){
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.post_card,parent,false);
        return new ViewHolder(v);
    }

    @Override
    public  void onBindViewHolder(final MainAdapter.ViewHolder holder, final int position){

        final int[] likes_count = {position};

        holder.post_username.setText(mDataset.get(position));
        holder.post_content.setText("Content goes here");
        holder.post_likes.setText(String.valueOf(position)+" likes");
        holder.post_time.setText("Time goes here");
        holder.img.setImageResource(R.drawable.test);
        holder.post_likes.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                // Code here executes on main thread after user presses button
                if (true) { // condition to check whether the user already liked it !
                    likes_count[0] = likes_count[0] + 1;
                    holder.post_likes.setText(String.valueOf(likes_count[0]) + " likes");
                }
                else{
                    likes_count[0] = likes_count[0] -1;
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

        public final TextView post_username,post_content,post_time;
        public final Button post_likes;
        public final ImageView img;


        public ViewHolder(@NonNull View itemView) {
            super(itemView);


            post_username = itemView.findViewById(R.id.post_username);
            post_content = itemView.findViewById(R.id.post_content);
            post_likes = itemView.findViewById(R.id.post_likes);
            post_time = itemView.findViewById(R.id.posted_time);
            img = itemView.findViewById(R.id.imageView);



        }
    }
}
