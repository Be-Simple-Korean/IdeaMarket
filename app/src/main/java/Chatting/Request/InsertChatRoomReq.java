package Chatting.Request;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.toolbox.StringRequest;

import java.util.HashMap;
import java.util.Map;

public class InsertChatRoomReq extends StringRequest {

    //서버 URL 설정(php 파일 연동)
    final static private String URL = "http://teamwhi.dothome.co.kr/IdeaMarket/Chat/insertCR.php";
    private Map<String, String> map;

    public InsertChatRoomReq(int cpc,Response.Listener<String> listener) {
        super(Request.Method.POST, URL, listener, null);
        map = new HashMap<>();
        map.put("cpc", String.valueOf(cpc));
        map.put("rs", "진행");
    }

    @Override
    protected Map<String, String> getParams() throws AuthFailureError {
        return map;
    }
}