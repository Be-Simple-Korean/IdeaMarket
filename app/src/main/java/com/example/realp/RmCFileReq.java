package com.example.realp;

import com.android.volley.AuthFailureError;
import com.android.volley.Response;
import com.android.volley.toolbox.StringRequest;

import java.util.HashMap;
import java.util.Map;

public class RmCFileReq extends StringRequest {

    //서버 URL 설정(php 파일 연동)
    final static private String URL = "http://teamwhi.dothome.co.kr/IdeaMarket/imgFileC/FileCheck.php";
    private Map<String, String> map;

    public RmCFileReq(String imageUrl, Response.Listener<String> listener) {
        super(Method.POST, URL, listener, null);
        map = new HashMap<>();
        map.put("imageUrl", imageUrl);
    }

    @Override
    protected Map<String, String>getParams() throws AuthFailureError {
        map.put("Content-Type", "application/json; charset=utf-8");
        return map;
    }
}
