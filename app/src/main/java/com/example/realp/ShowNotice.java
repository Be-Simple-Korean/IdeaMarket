package com.example.realp;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class ShowNotice extends AppCompatActivity {
    TextView sn_title;
    TextView sn_contents,sn_writer,sn_date;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_show_notice);
        sn_title=findViewById(R.id.sn_title);
        sn_writer=findViewById(R.id.sn_writer);
        sn_date=findViewById(R.id.sn_date);
        sn_contents=findViewById(R.id.sn_contents);
        Intent i =getIntent();
        String num=i.getStringExtra("no");
        findViewById(R.id.backNList).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
        Response.Listener<String> responseListener = new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    JSONObject jsonObject = new JSONObject( response );
                    Boolean suc=jsonObject.getBoolean("success");
                    if(suc){
                        String title=jsonObject.getString("title");
                        String writer=jsonObject.getString("writer");
                        String time=jsonObject.getString("time");
                        String contents=jsonObject.getString("contents");

                        sn_title.setText(title);
                        sn_contents.setText(contents);
                        sn_date.setText(time);
                        sn_writer.setText(writer);
                    }
                } catch (JSONException e) {
                    Log.e("frag3 listpost","Connect Error");
                    e.printStackTrace();
                }
            }
        };
        getNoticePostReq gnpr = new getNoticePostReq(num,responseListener);
        RequestQueue queue = Volley.newRequestQueue( this);
        queue.add(gnpr);
    }
}