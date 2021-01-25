package com.example.realp;

import com.android.volley.AuthFailureError;
import com.android.volley.Response;
import com.android.volley.toolbox.StringRequest;

import java.util.HashMap;
import java.util.Map;

class GetPwdReq extends StringRequest {
    final static  private String URL="http://teamwhi.dothome.co.kr/getPwd.php";
    private Map<String,String> map;

    public GetPwdReq(String sname, String sid, String sdate, int sex, Response.Listener<String>listener){
        super(Method.POST,URL,listener,null);//위 url에 post방식으로 값을 전송
        map=new HashMap<>();
        map.put("userName",sname);
        map.put("userID",sid);
        map.put("birthday",sdate);
        map.put("userSex", String.valueOf(sex));
    }

    @Override
    protected Map<String, String> getParams() throws AuthFailureError {
        return map;
    }
}
