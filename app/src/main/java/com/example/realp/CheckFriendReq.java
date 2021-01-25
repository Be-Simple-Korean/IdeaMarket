package com.example.realp;

import com.android.volley.AuthFailureError;
import com.android.volley.Response;
import com.android.volley.toolbox.StringRequest;

import java.util.HashMap;
import java.util.Map;

public class CheckFriendReq extends StringRequest {
    final static  private String URL="http://teamwhi.dothome.co.kr/IdeaMarket/checkFriend.php";
    private Map<String,String> map;

    public CheckFriendReq(String nick1,String nick2, Response.Listener<String>listener){
        super(Method.POST,URL,listener,null);
        map=new HashMap<>();
        map.put("user1",nick1);
        map.put("user2",nick2);
    }

    @Override
    protected Map<String,String> getParams() throws AuthFailureError {
        return map;
    }
}
