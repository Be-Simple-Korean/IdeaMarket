package com.example.realp;

import com.android.volley.AuthFailureError;
import com.android.volley.Response;
import com.android.volley.toolbox.StringRequest;

import java.util.HashMap;
import java.util.Map;

class UpdateUserInfoReq extends StringRequest {
    final static private String URL ="http://teamwhi.dothome.co.kr/updateUser.php";

    private Map<String, String> map;
    public UpdateUserInfoReq(String sql, Response.Listener<String> listener) {
        super(Method.POST, URL, listener, null);
        map = new HashMap<>();
        map.put("sql",sql);
    }

    @Override
    protected Map<String, String>getParams() throws AuthFailureError {
        return map;
    }

}
