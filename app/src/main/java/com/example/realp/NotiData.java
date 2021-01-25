package com.example.realp;

class NotiData {
    String contents;
    String date;

    public String getContents() {
        return contents;
    }

    public void setContents(String contents) {
        this.contents = contents;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public NotiData(String contents, String date) {
        this.contents = contents;
        this.date = date;
    }
}
