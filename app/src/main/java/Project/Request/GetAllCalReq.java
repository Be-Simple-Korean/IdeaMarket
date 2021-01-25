package Project.Request;

import com.android.volley.AuthFailureError;
import com.android.volley.Response;
import com.android.volley.toolbox.StringRequest;

import java.util.HashMap;
import java.util.Map;

public class GetAllCalReq extends StringRequest {

    //서버 URL 설정(php 파일 연동)
    final static private String URL = "http://teamwhi.dothome.co.kr/IdeaMarket/Project/getAllCal.php";
    private Map<String, String> map;

    public GetAllCalReq(int teamNo, String userNick,Response.Listener<String> listener) {
        super(Method.POST, URL, listener, null);
        map = new HashMap<>();
        map.put("teamNo", String.valueOf(teamNo));
        map.put("userNick",userNick);

    }
    @Override
    protected Map<String, String>getParams() throws AuthFailureError {
        return map;
    }
}
