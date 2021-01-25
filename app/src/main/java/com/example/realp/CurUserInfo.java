package com.example.realp;

public class CurUserInfo {
    public static String userNick;
    public static String userRank;

    public static String getUserRank() {
        return userRank;
    }

    public static void setUserRank(String userRank) {
        CurUserInfo.userRank = userRank;
    }

    public String getUserNick() {
        return userNick;
    }

    public void setUserNick(String userNick) {
        this.userNick = userNick;
    }

}
