package com.example.realp;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.toolbox.Volley;
import com.example.realp.common.MyApplication;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.List;


public class Friends_List extends AppCompatActivity {
    ListView listView;
    ArrayList<String> ar;
    ArrayAdapter arrayAdapter;
    CurUserInfo cui=new CurUserInfo();
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.friends_list);
        ar=new ArrayList<>();
        arrayAdapter=new ArrayAdapter(this,android.R.layout.simple_expandable_list_item_1,ar);
        listView=findViewById(R.id.fl_lv);
        listView.setAdapter(arrayAdapter);
        Response.Listener<String> responseListener = new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    JSONObject jsonObject = new JSONObject( response );
                    JSONArray ja = jsonObject.getJSONArray("Friend");
                    if(ja.length()==0){return;}
                    for(int i=0;i<ja.length();i++){
                        JSONObject item = ja.getJSONObject(i);
                        String a=item.getString("userNick");
                        Log.e("a",a);
                        ar.add(a);
                        arrayAdapter.notifyDataSetChanged();
                    }
                } catch (JSONException e) {
                    Log.e("frag3 listpost","Connect Error");
                    e.printStackTrace();
                }
            }
        };
        getFriendListReq lpr = new getFriendListReq(cui.getUserNick(),responseListener);
        RequestQueue queue = Volley.newRequestQueue(this);
        queue.add( lpr );
    }
}