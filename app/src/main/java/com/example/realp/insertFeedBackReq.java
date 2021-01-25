package com.example.realp;

import com.android.volley.AuthFailureError;
import com.android.volley.Response;
import com.android.volley.toolbox.StringRequest;

import java.util.HashMap;
import java.util.Map;

class insertFeedBackReq extends StringRequest{
    //서버 url 설정(php파일 연동)
    final static  private String URL="http://teamwhi.dothome.co.kr/insertFeedback.php";
    private Map<String,String> map;

    public insertFeedBackReq(String userNick,String title,String content,String time,Response.Listener<String>listener){
        super(Method.POST,URL,listener,null);//위 url에 post방식으로 값을 전송
        map=new HashMap<>();
        map.put("userNick",userNick);
        map.put("title",title);
        map.put("content",content);
        map.put("time",time);
    }

    @Override
    protected Map<String, String> getParams() throws AuthFailureError {
        return map;
    }

}
