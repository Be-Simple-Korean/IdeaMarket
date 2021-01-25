package com.example.realp;

import android.app.NotificationManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.toolbox.Volley;

import org.json.JSONException;
import org.json.JSONObject;

public class DisFriendReciever extends BroadcastReceiver {
    private Context context;
    NotificationManager manager;
    int notificationId;
    @Override
    public void onReceive(Context context, Intent intent) {
        this.context=context;
        String msg = intent.getStringExtra("msg");
        notificationId = intent.getIntExtra("notificationId", 0);
        String g_sender=msg;
        int b=g_sender.lastIndexOf("님이 친구");
        g_sender=g_sender.substring(0,b);
        deleteFriend(g_sender);
    }

    private void deleteFriend(String g_sender) {
        Response.Listener<String> responseListener = new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                JSONObject jsonObject = null;
                try {
                    jsonObject = new JSONObject( response );
                    boolean success = jsonObject.getBoolean( "success" );
                    if(success){
                        manager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
                        manager.cancel(notificationId);
                    }
                } catch (JSONException e) {
                    Log.e("ERROR","in getACount");
                    e.printStackTrace();
                }
            }
        };
        DelFriendingReq dfr=new DelFriendingReq(g_sender,responseListener);
        RequestQueue queue = Volley.newRequestQueue(context);
        queue.add(dfr);
    }
}
