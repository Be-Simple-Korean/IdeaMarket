package com.example.realp;

import android.util.Log;

import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.iid.FirebaseInstanceIdService;

import java.io.IOException;

import okhttp3.FormBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;

public class FirebaseInstanceIDService extends FirebaseInstanceIdService {
    CurUserInfo cui=new CurUserInfo();
    @Override
    public void onTokenRefresh() {
        Log.e("수행","tngod");
        String token= FirebaseInstanceId.getInstance().getToken();

    }


}
