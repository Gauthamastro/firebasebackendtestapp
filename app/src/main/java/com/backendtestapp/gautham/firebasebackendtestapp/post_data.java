package com.backendtestapp.gautham.firebasebackendtestapp;


import java.util.Date;


public class post_data {

    //variables to upload
    private String PostUid,Uid,Username,Title,Content,Img_path=null;
    private Integer Priority,Likes=0;
    private Date Time;
    public post_data(){

    }

    public void setContent(String content) {
        this.Content = content;
    }

    public void setLikes(Integer likes) {
        this.Likes = likes;
    }

    public void setPriority(Integer priority) {
        this.Priority = priority;
    }

    public void setTime(Date time) {
        this.Time = time;
    }

    public void setTitle(String title) {
        this.Title = title;
    }

    public void setUid(String uid) {
        this.Uid = uid;
    }

    public void setUsername(String username) {
        this.Username = username;
    }

    public void setImg_path(String img_path) {
        this.Img_path = img_path;
    }

    public String getUsername() {
        return Username;
    }

    public void setPostUid(String postUid){
        this.PostUid = postUid;
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
        return PostUid;
    }

    public String getTitle() {
        return Title;
    }

    public String getUid() {
        return Uid;
    }

    public Date getTime() {
        return Time;
    }
    public Integer getLikes(){return  Likes;}
}
