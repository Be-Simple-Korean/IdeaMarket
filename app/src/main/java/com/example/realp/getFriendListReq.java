package com.example.realp;

import com.android.volley.AuthFailureError;
import com.android.volley.Response;
import com.android.volley.toolbox.StringRequest;

import java.util.HashMap;
import java.util.Map;

class getFriendListReq  extends StringRequest {

    //서버 URL 설정(php 파일 연동)
    final static private String URL = "http://teamwhi.dothome.co.kr/getFriendList.php";
    private Map<String, String> map;

    public getFriendListReq(String userNick,Response.Listener<String> listener) {
        super(Method.POST, URL, listener, null);
        map = new HashMap<>();
        map.put("userNick",userNick);
        map.put("status","완료");

    }

    @Override
    protected Map<String, String>getParams() throws AuthFailureError {
        map.put("Content-Type", "application/json; charset=utf-8");
        return map;
    }
}
