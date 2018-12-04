package com.backendtestapp.gautham.firebasebackendtestapp;


import java.util.Date;
import java.sql.Timestamp;


public class olxpost_data {

    //variables to upload
    private String postUid,Uid,Username,Title,Content,Img_path=null,productType;
    private Integer Priority,likes=0;
    private Timestamp time;
    public olxpost_data(String uid,String username,String title,String content,Integer priority){
        this.Uid = uid;
        this.Username = username;
        this.Title = title;
        this.Content = content;
        this.Priority =priority;
        Date date = new Date();
        this.time = new Timestamp(date.getTime());

    }
    public void setImg_path(String img_path) {
        this.Img_path = img_path;
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
        if (Img_path == null){
            return "ITS A NULL";
        }
        else{
            return Img_path;
        }

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
    public String getUsername() {
        return Username;
    }
    public Timestamp getTime() {
        return time;
    }
    public Integer getLikes(){return  likes;}
    public String getProductType() {
        return productType;
    }
}
