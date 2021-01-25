package com.example.realp;

public class Edit_Proifle_data {
    String userID;
    String password;
    String userName;
    String userSex;
    String userJob;
    String userSelf;
    String userEmail;
    String userPnum;
    String birthday;
    String nickname;
    String userImage;
    int logintype;
    String userToken;

    public Edit_Proifle_data(String userID, String password, String userName, String userSex, String userJob, String userSelf, String userEmail, String userPnum, String birthday, String nickname, String userImage, int logintype, String userToken) {
        this.userID = userID;
        this.password = password;
        this.userName = userName;
        this.userSex = userSex;
        this.userJob = userJob;
        this.userSelf = userSelf;
        this.userEmail = userEmail;
        this.userPnum = userPnum;
        this.birthday = birthday;
        this.nickname = nickname;
        this.userImage = userImage;
        this.logintype = logintype;
        this.userToken = userToken;
    }

    public String getUserID() {
        return userID;
    }

    public void setUserID(String userID) {
        this.userID = userID;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getUserSex() {
        return userSex;
    }

    public void setUserSex(String userSex) {
        this.userSex = userSex;
    }

    public String getUserJob() {
        return userJob;
    }

    public void setUserJob(String userJob) {
        this.userJob = userJob;
    }

    public String getUserSelf() {
        return userSelf;
    }

    public void setUserSelf(String userSelf) {
        this.userSelf = userSelf;
    }

    public String getUserEmail() {
        return userEmail;
    }

    public void setUserEmail(String userEmail) {
        this.userEmail = userEmail;
    }

    public String getUserPnum() {
        return userPnum;
    }

    public void setUserPnum(String userPnum) {
        this.userPnum = userPnum;
    }

    public String getBirthday() {
        return birthday;
    }

    public void setBirthday(String birthday) {
        this.birthday = birthday;
    }

    public String getNickname() {
        return nickname;
    }

    public void setNickname(String nickname) {
        this.nickname = nickname;
    }

    public String getUserImage() {
        return userImage;
    }

    public void setUserImage(String userImage) {
        this.userImage = userImage;
    }

    public int getLogintype() {
        return logintype;
    }

    public void setLogintype(int logintype) {
        this.logintype = logintype;
    }

    public String getUserToken() {
        return userToken;
    }

    public void setUserToken(String userToken) {
        this.userToken = userToken;
    }
}
