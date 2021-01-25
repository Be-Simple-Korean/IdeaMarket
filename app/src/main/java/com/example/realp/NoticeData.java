package com.example.realp;

class NoticeData {
    String notice_no;
    String notice_title;
    String notice_date;

    public String getNotice_no() {
        return notice_no;
    }

    public void setNotice_no(String notice_no) {
        this.notice_no = notice_no;
    }

    public String getNotice_title() {
        return notice_title;
    }

    public void setNotice_title(String notice_title) {
        this.notice_title = notice_title;
    }

    public String getNotice_date() {
        return notice_date;
    }

    public void setNotice_date(String notice_date) {
        this.notice_date = notice_date;
    }

    public NoticeData(String notice_no, String notice_title, String notice_date) {
        this.notice_no = notice_no;
        this.notice_title = notice_title;
        this.notice_date = notice_date;
    }
}
