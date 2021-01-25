package com.example.realp;

import android.os.Bundle;
import android.util.Log;
import android.view.Menu;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class Notification_List extends AppCompatActivity {
    RecyclerView recyclerView;
    ArrayList<NotiData> nar;
    NotifiAdapter notifiAdapter;
    LinearLayoutManager layoutManager;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.notification_list);
        recyclerView=findViewById(R.id.notilist_rv);
        layoutManager=new LinearLayoutManager(this);
        nar=new ArrayList<>();
        notifiAdapter=new NotifiAdapter(nar);
        recyclerView.setAdapter(notifiAdapter);
        recyclerView.setLayoutManager(layoutManager);

        Response.Listener<String> responseListener = new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    JSONObject jsonObject = new JSONObject( response );
                    JSONArray ja = jsonObject.getJSONArray("Noti");
                    Log.e("ja",ja.length()+"");
                    if(ja.length()==0){return;}
                    for(int i=0;i<ja.length();i++){
                        JSONObject item = ja.getJSONObject(i);
                        String contents=item.getString("contents");
                        Log.e("noti",contents);
                        String date=item.getString("octime");
                        NotiData nd=new NotiData(contents,date);
                        nar.add(nd);
                        notifiAdapter.notifyDataSetChanged();
                    }
                } catch (JSONException e) {
                    Log.e("frag3 listpost","Connect Error");
                    e.printStackTrace();
                }
            }
        };
        CurUserInfo cui=new CurUserInfo();
        getNotiListReq gnlr = new getNotiListReq(cui.getUserNick(),responseListener);
        RequestQueue queue = Volley.newRequestQueue(this);
        queue.add( gnlr );

    }

}
