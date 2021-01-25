package Chatting.Request;

import com.android.volley.AuthFailureError;
import com.android.volley.Response;
import com.android.volley.toolbox.StringRequest;

import java.util.HashMap;
import java.util.Map;

public class getChatRoomReq extends StringRequest {

    //서버 URL 설정(php 파일 연동)
    final static private String URL = "http://teamwhi.dothome.co.kr/IdeaMarket/Chat/getChatNos.php";
    private Map<String, String> map;

    public getChatRoomReq(String nick, Response.Listener<String> listener) {
        super(Method.POST, URL, listener, null);
        map = new HashMap<>();
        map.put("nick",nick);
    }

    @Override
    protected Map<String, String> getParams() throws AuthFailureError {
        map.put("Content-Type", "application/json; charset=utf-8");
        return map;
    }
}
