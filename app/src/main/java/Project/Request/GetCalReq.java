package Project.Request;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.toolbox.StringRequest;

import java.util.HashMap;
import java.util.Map;

public class GetCalReq extends StringRequest {

    //서버 URL 설정(php 파일 연동)
    final static private String URL = "http://teamwhi.dothome.co.kr/IdeaMarket/Project/getCalList.php";
    private Map<String, String> map;

    public GetCalReq(int teamNo, String sDate,String userNick, Response.Listener<String> listener) {
        super(Request.Method.POST, URL, listener, null);
        map = new HashMap<>();
        map.put("teamNo", String.valueOf(teamNo));
        map.put("sDate",sDate);
        map.put("userNick",userNick);

    }
    @Override
    protected Map<String, String>getParams() throws AuthFailureError {
        return map;
    }
}
