package com.example.realp;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.toolbox.StringRequest;

import java.util.HashMap;
import java.util.Map;

class GetACountReq extends StringRequest {

    //서버 URL 설정(php 파일 연동)
    final static private String URL = "http://teamwhi.dothome.co.kr/IdeaMarket/Project/getACount.php";
    private Map<String, String> map;

    public GetACountReq(int teamNo,String tLeader,String cStatus, Response.Listener<String> listener) {
        super(Request.Method.POST, URL, listener, null);
        map = new HashMap<>();
        map.put("teamNo", String.valueOf(teamNo));
        map.put("tLeader",tLeader);
        map.put("cStatus",cStatus);
    }

    @Override
    protected Map<String, String>getParams() throws AuthFailureError {
        return map;
    }
}
