package com.example.realp;

import android.util.Log;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.toolbox.StringRequest;

import java.util.HashMap;
import java.util.Map;

public class RequestSecReq extends StringRequest {

    //서버 URL 설정(php 파일 연동)
    final static private String URL = "http://teamwhi.dothome.co.kr/IdeaMarket/Project/pushNoti.php";
    private Map<String, String> map;

    public RequestSecReq(String userNick,String title,String msg, Response.Listener<String> listener) {
        super(Request.Method.POST, URL, listener, null);
        map = new HashMap<>();
        map.put("userNick", userNick);
        map.put("title",title);
        map.put("msg", msg);
    }

    @Override
    protected Map<String, String>getParams() throws AuthFailureError {
        return map;
    }
}