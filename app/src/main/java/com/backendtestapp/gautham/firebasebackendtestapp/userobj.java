package com.backendtestapp.gautham.firebasebackendtestapp;

public class userobj {
    String UID ;
    String Name;
    String Email;
    String Branch;
    Integer Year,Priority;

    public userobj(String id,String name,String email,String branch,Integer year,Integer priority){
        this.UID = id;
        this.Name = name;
        this.Email = email;
        this.Branch = branch;
        this.Year = year;
        this.Priority = priority;
    }

    public String getBranch() {
        return Branch;
    }

    public Integer getYear() {
        return Year;
    }

    public String getUserEmail() {
        return Email;
    }

    public String getUserName() {
        return Name;
    }

    public String getUID() {
        return UID;
    }

    public Integer getPriority() {
        return Priority;
    }
}
