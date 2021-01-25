package com.example.realp;

class ReportData {
    String reportTag;
    String reportDate;

    public String getReportTag() {
        return reportTag;
    }

    public void setReportTag(String reportTag) {
        this.reportTag = reportTag;
    }

    public String getReportDate() {
        return reportDate;
    }

    public void setReportDate(String reportDate) {
        this.reportDate = reportDate;
    }

    public ReportData(String reportTag, String reportDate) {
        this.reportTag = reportTag;
        this.reportDate = reportDate;
    }
}
