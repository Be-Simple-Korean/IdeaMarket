package com.example.realp;

public class profile_data {
     String name;
     String birthday;
     String Pnum;
     String email;
     String userID;

    public profile_data(String name, String birthday,String userID) {
        this.name = name;
        this.birthday = birthday;
        this.userID = userID;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getBirthday() {
        return birthday;
    }

    public void setBirthday(String birthday) {
        this.birthday = birthday;
    }

    public String getPnum() {
        return Pnum;
    }

    public void setPnum(String pnum) {
        this.Pnum = pnum;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getUserID() {
        return userID;
    }

    public void setUserID(String userID) {
        this.userID = userID;
    }
}
