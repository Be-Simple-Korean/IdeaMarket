package com.example.realp;

import com.android.volley.AuthFailureError;
import com.android.volley.Response;
import com.android.volley.toolbox.StringRequest;

import java.util.HashMap;
import java.util.Map;

public class GetIdReq extends StringRequest {
    final static  private String URL="http://teamwhi.dothome.co.kr/getId.php";
    private Map<String,String> map;

    public GetIdReq(String sname, String semail, String sdate, int sex, Response.Listener<String>listener){
        super(Method.POST,URL,listener,null);//위 url에 post방식으로 값을 전송
        map=new HashMap<>();
        map.put("userName",sname);
        map.put("userEmail",semail);
        map.put("birthday",sdate);
        map.put("userSex", String.valueOf(sex));
    }

    @Override
    protected Map<String, String> getParams() throws AuthFailureError {
        return map;
    }
}
