package com.example.realp;

import android.util.Log;

import com.android.volley.AuthFailureError;
import com.android.volley.Response;
import com.android.volley.toolbox.StringRequest;

import java.util.HashMap;
import java.util.Map;

public class RecieveReportReq extends StringRequest {

    //서버 URL 설정(php 파일 연동)
    final static private String URL = "http://teamwhi.dothome.co.kr/IdeaMarket/recieveReport.php";
    private Map<String, String> map;

    public RecieveReportReq(String tag,String reporter,String sus,String content,String time, Response.Listener<String> listener) {
        super(Method.POST, URL, listener, null);
        map = new HashMap<>();
        String sql ="insert into report values(default,'"+tag+"','"+reporter+"','"+sus+"','"+content+"','신청','"+time+"');";
        map.put("sql",sql);
    }

    @Override
    protected Map<String, String>getParams() throws AuthFailureError {
        map.put("Content-Type", "application/json; charset=utf-8");
        return map;
    }
}
