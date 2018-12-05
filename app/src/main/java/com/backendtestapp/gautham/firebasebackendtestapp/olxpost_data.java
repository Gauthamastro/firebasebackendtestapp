package com.backendtestapp.gautham.firebasebackendtestapp;

import android.util.Log;
import java.util.ArrayList;
import java.util.Date;
import java.sql.Timestamp;

public class olxpost_data {

    //variables to upload
    private String postUid,Uid,Username,Title,Content,productType;
    private ArrayList CloudPath;
    private Integer Priority,likes=0;
    private Timestamp time;
    private String Price;
    private  String bargainable;
    public olxpost_data(String uid,String username,String title,String content,Integer priority){
        this.Uid = uid;
        this.Username = username;
        this.Title = title;
        this.Content = content;
        this.Priority =priority;
        Date date = new Date();
        this.time = new Timestamp(date.getTime());

    }

    public void setPrice(String price) {
        Price = price;
    }

    public void setBargainable(String bargainable) {
        this.bargainable = bargainable;
    }

    public void setPostUid(String postUid){
        this.postUid = postUid;
    }
    public void setProductType(String productType) {
        this.productType = productType;
    }
    public Integer getPriority() {
        return Priority;
    }
    public String getContent() {
        return Content;
    }
    public String getImg_path() {
        if (CloudPath.size() == 0){
            Log.d("IMG URI LIST","ITS A EMPTY LIST");
            return null;
        }
        else{
            String img_path = "";
            for(int i = 0;i < CloudPath.size();i++) {
                img_path = img_path + CloudPath.get(i)+ "\n";
            }
            return img_path;
        }

    }

    public void setCloudPath(ArrayList cloudPath) {
        this.CloudPath = cloudPath;
    }

    public String getPostUid() {
        return postUid;
    }
    public String getTitle() {
        return Title;
    }
    public String getUid() {
        return Uid;
    }
    public Timestamp getTime() {
        return time;
    }
    public Integer getLikes(){return  likes;}
    public String getProductType() {
        return productType;
    }

    public String getBargainable() {
        return bargainable;
    }

    public String getPrice() {
        return Price;
    }
}
