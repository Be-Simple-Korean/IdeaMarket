package com.example.realp;

import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.widget.ListView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

public class NoticeList extends AppCompatActivity {
    NoticeAdapter noticeAdapter;
    ArrayList<NoticeData> nar;
    RecyclerView recyclerView;
    LinearLayoutManager layoutManager;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.notice_list);
        recyclerView=findViewById(R.id.rec_notice);
        nar=new ArrayList<>();
        layoutManager=new LinearLayoutManager(this);
        noticeAdapter=new NoticeAdapter(nar);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setAdapter(noticeAdapter);
        Response.Listener<String> responseListener = new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    JSONObject jsonObject = new JSONObject( response );
                    JSONArray ja = jsonObject.getJSONArray("Notice");
                    if(ja.length()==0){return;}
                    for(int i=0;i<ja.length();i++){
                        JSONObject item = ja.getJSONObject(i);
                        int num=item.getInt("num");
                        String title=item.getString("title");
                        String time=item.getString("time");
                        NoticeData nd=new NoticeData(String.valueOf(num),title,time);
                        nar.add(nd);
                        noticeAdapter.notifyDataSetChanged();

                    }
                } catch (JSONException e) {
                    Log.e("frag3 listpost","Connect Error");
                    e.printStackTrace();
                }
            }
        };
        getNoticeListReq gnlr = new getNoticeListReq(responseListener);
        RequestQueue queue = Volley.newRequestQueue( this);
        queue.add(gnlr);
    }


}
