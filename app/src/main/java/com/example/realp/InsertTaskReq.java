package com.example.realp;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.toolbox.StringRequest;

import java.util.HashMap;
import java.util.Map;

public class InsertTaskReq extends StringRequest {

    //서버 URL 설정(php 파일 연동)
    final static private String URL = "http://teamwhi.dothome.co.kr/IdeaMarket/Project/addTask.php";
    private Map<String, String> map;

    public InsertTaskReq(int teamNo,String status,String subject,String userid,String contents,String gFinish ,Response.Listener<String> listener) {
        super(Request.Method.POST, URL, listener, null);
        map = new HashMap<>();
        map.put("teamNo", String.valueOf(teamNo));
        map.put("status",status);
        map.put("subject",subject);
        map.put("userNick",userid);
        map.put("contents",contents);
        map.put("gFinish",gFinish);
    }

    @Override
    protected Map<String, String>getParams() throws AuthFailureError {
        return map;
    }
}