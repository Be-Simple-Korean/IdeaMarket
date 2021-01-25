package Chatting.Request;

import com.android.volley.AuthFailureError;
import com.android.volley.Response;
import com.android.volley.toolbox.StringRequest;

import java.util.HashMap;
import java.util.Map;

public class UpdateChatMsgReq extends StringRequest {

    //서버 URL 설정(php 파일 연동)
    final static private String URL = "http://teamwhi.dothome.co.kr/IdeaMarket/Chat/updateChatLiveMsg.php";
    private Map<String, String> map;

    public UpdateChatMsgReq(int chatNo, String nick , String msg, String cDate, int oldRs, int newRs, Response.Listener<String> listener) {
        super(Method.POST, URL, listener, null);
        map = new HashMap<>();
        map.put("chatNo", String.valueOf(chatNo));
        map.put("nick",nick);
        map.put("msg",msg);
        map.put("cDate",cDate);
        map.put("oldRs", String.valueOf(oldRs));
        map.put("newRs", String.valueOf(newRs));
    }

    @Override
    protected Map<String, String> getParams() throws AuthFailureError {
        map.put("Content-Type", "application/json; charset=utf-8");
        return map;
    }
}