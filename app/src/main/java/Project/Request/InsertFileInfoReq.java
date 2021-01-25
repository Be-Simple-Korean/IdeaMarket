package Project.Request;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.toolbox.StringRequest;

import java.util.HashMap;
import java.util.Map;

public class InsertFileInfoReq extends StringRequest {
    //서버 URL 설정(php 파일 연동)
    final static private String URL = "http://teamwhi.dothome.co.kr/IdeaMarket/Project/insertFileinfo.php";
    private Map<String, String> map;

    public InsertFileInfoReq(int teamNo ,String userNick, String fn,String date, Response.Listener<String> listener) {
        super(Request.Method.POST, URL, listener, null);
        map = new HashMap<>();
        String sql="Insert into TeamData VALUES("+teamNo+",'"+userNick+"','"+fn+"','"+date+"');";
        map.put("sql",sql);

    }
    @Override
    protected Map<String, String>getParams() throws AuthFailureError {
        map.put("Content-Type", "application/json; charset=utf-8");
        return map;
    }
}