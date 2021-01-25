package com.example.realp;

import com.android.volley.AuthFailureError;
import com.android.volley.Response;
import com.android.volley.toolbox.StringRequest;

import java.util.HashMap;
import java.util.Map;

public class Profile_Delete extends StringRequest {
    final static private String URL = "http://myhosting102.dothome.co.kr/Profile_Delete.php";
    private Map<String, String> map;
    //private Map<String, String>parameters;

    public Profile_Delete(String userID , Response.Listener<String> listener) {
        super(Method.POST, URL, listener, null);

        map = new HashMap<>();
        map.put("userID", userID);
    }

    @Override
    protected Map<String, String>getParams() throws AuthFailureError {
        return map;
    }

}
