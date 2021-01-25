package Chatting.Request;

import com.android.volley.AuthFailureError;
import com.android.volley.Response;
import com.android.volley.toolbox.StringRequest;

import java.util.HashMap;
import java.util.Map;

public class insertChatMsgReq extends StringRequest {

    //서버 URL 설정(php 파일 연동)
    final static private String URL = "http://teamwhi.dothome.co.kr/IdeaMarket/Chat/insertChatMsg.php";
    private Map<String, String> map;

    public insertChatMsgReq(int chatNo, String nick, String reciever, String msg, String cDate, int rs, Response.Listener<String> listener) {
        super(Method.POST, URL, listener, null);
        map = new HashMap<>();
        map.put("chatNo", String.valueOf(chatNo));
        map.put("nick",nick);
        map.put("reciever",reciever);
        map.put("msg",msg);
        map.put("cDate",cDate);
        map.put("rs", String.valueOf(rs));
    }

    @Override
    protected Map<String, String> getParams() throws AuthFailureError {
        map.put("Content-Type", "application/json; charset=utf-8");
        return map;
    }
}


