package Project.Request;

import com.android.volley.AuthFailureError;
import com.android.volley.Response;
import com.android.volley.toolbox.StringRequest;

import java.util.HashMap;
import java.util.Map;

public class DeleteDirReq extends StringRequest {

    //서버 URL 설정(php 파일 연동)
    final static private String URL = "http://teamwhi.dothome.co.kr/IdeaMarket/Project/Storage/deleteDir.php";
    private Map<String, String> map;

    public DeleteDirReq(String path, Response.Listener<String> listener) {
        super(Method.POST, URL, listener, null);
        map = new HashMap<>();
        map.put("path", path);
    }

    @Override
    protected Map<String, String>getParams() throws AuthFailureError {
        return map;
    }
}