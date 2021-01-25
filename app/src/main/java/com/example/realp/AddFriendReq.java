package com.example.realp;

import com.android.volley.AuthFailureError;
import com.android.volley.Response;
import com.android.volley.toolbox.StringRequest;

import java.util.HashMap;
import java.util.Map;

public class AddFriendReq extends StringRequest {

    //서버 URL 설정(php 파일 연동)
    final static private String URL = "http://teamwhi.dothome.co.kr/IdeaMarket/addFriend.php";
    private Map<String, String> map;

    public AddFriendReq(String user1,String user2, Response.Listener<String> listener) {
        super(Method.POST, URL, listener, null);
        map = new HashMap<>();
        map.put("user1",user1);
        map.put("user2",user2);
        map.put("status","신청");
    }

    @Override
    protected Map<String, String>getParams() throws AuthFailureError {
        return map;
    }
}
