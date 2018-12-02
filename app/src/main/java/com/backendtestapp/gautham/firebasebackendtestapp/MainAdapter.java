package com.backendtestapp.gautham.firebasebackendtestapp;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
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
    public  void onBindViewHolder(MainAdapter.ViewHolder holder,int position){
        holder.mTitle.setText(mDataset.get(position));

    }
    @Override
    public int getItemCount(){
        return mDataset.size();
    }
    public class ViewHolder extends RecyclerView.ViewHolder{

        public TextView mTitle;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            mTitle = itemView.findViewById(R.id.post_username);
        }
    }
}
