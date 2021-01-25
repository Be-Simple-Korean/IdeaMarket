package com.example.realp;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.toolbox.StringRequest;

import java.util.HashMap;
import java.util.Map;

public class InsertCalReq extends StringRequest {

    //서버 URL 설정(php 파일 연동)
    final static private String URL = "http://teamwhi.dothome.co.kr/IdeaMarket/Project/addCal.php";
    private Map<String, String> map;

    public InsertCalReq(int teamNo, String sDate,String time,String contents,String userNick,String aStatus, Response.Listener<String> listener) {
        super(Request.Method.POST, URL, listener, null);
        map = new HashMap<>();
        map.put("teamNo", String.valueOf(teamNo));
        map.put("sDate",sDate);
        map.put("time",time);
        map.put("contents",contents);
        map.put("userNick",userNick);
        map.put("aStatus",aStatus);

    }

    @Override
    protected Map<String, String>getParams() throws AuthFailureError {
        return map;
    }
}