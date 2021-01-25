package com.example.realp;


import android.Manifest;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.AsyncTask;
import android.os.Bundle;
import android.provider.MediaStore;
import android.text.method.ScrollingMovementMethod;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.widget.Toolbar;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.toolbox.Volley;
import com.example.realp.common.MyApplication;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.messaging.FirebaseMessaging;

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
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.List;
import java.util.StringTokenizer;


public class Profile extends AppCompatActivity {
    Menu menu;
    private Profile_Buy_List profile_buy_list = new Profile_Buy_List();
    private Profile_Co_List profile_co_list = new Profile_Co_List();
    private Profile_Report_List profile_report_list = new Profile_Report_List();
    TextView uSelf,userName,userEmail,userBirthday,userPnum,title;
    CurUserInfo cui=new CurUserInfo();
    String curNick="";
    Toolbar toolbar;
    ActionBar actionBar;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.profile);
        title=findViewById(R.id.tv_p_title);
        userName=findViewById(R.id.userName);
        userEmail=findViewById(R.id.userEmail);
        userBirthday=findViewById(R.id.userbirthday);
        userPnum=findViewById(R.id.userPnum);
        uSelf=findViewById(R.id.userProfile_self);

        Intent i=getIntent();
        curNick=i.getStringExtra("nick");
        if(curNick==null){
            curNick=cui.getUserNick();
        }else{ //내 정보가 아닌 경우
            if (!curNick.equals(cui.getUserNick())) {
                title.setText("사용자 프로필");
                toolbar = findViewById(R.id.toolbar1);
                setSupportActionBar(toolbar);
                actionBar = getSupportActionBar();
                actionBar.setDisplayShowCustomEnabled(true);
                actionBar.setDisplayShowTitleEnabled(false);//기본 제목을 없애줍니다.
            }
        }
        settingInfo();
        if(!cui.getUserNick().equals(curNick)){
            checkFriend();
        }
        uSelf.setMovementMethod(new ScrollingMovementMethod());

        //기본 프레그먼트 지정
        openFragment(profile_buy_list);
        BottomNavigationView bottomNavigationView = findViewById(R.id.bottom_nevigation);
        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch (item.getItemId()){
                    case R.id.SellList:
                        openFragment(profile_buy_list);
                        break;
                    case R.id.CollaborationList:
                        openFragment(profile_co_list);
                        break;
                    case R.id.reportList1:
                        openFragment(profile_report_list);
                        break;
                }
                return true;
            }
        });

    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater menuInflater = getMenuInflater();
        menuInflater.inflate(R.menu.rep_friend, menu);
        this.menu=menu;
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()){
            case R.id.report:
                CustomReportDialog crd=new CustomReportDialog(this,userName.getText().toString());
                crd.callFunction();
                break;
            case R.id.friend:
                Calendar calendar=Calendar.getInstance();
                int y=calendar.get(Calendar.YEAR);
                int m =calendar.get(Calendar.MONTH)+1;
                int d=calendar.get(Calendar.DATE);
                int h=calendar.get(Calendar.HOUR_OF_DAY);
                int mn=calendar.get(Calendar.MINUTE);
                calendar.set(Calendar.YEAR,y);
                calendar.set(Calendar.MONTH,m-1);
                calendar.set(Calendar.DATE,d);
                calendar.set(Calendar.HOUR_OF_DAY,h);
                calendar.set(Calendar.MINUTE,mn);
                SimpleDateFormat sdf= new SimpleDateFormat("yyyy년 M월 dd일 HH시 mm분");
                Response.Listener<String> responseListener = new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        JSONObject jsonObject = null;
                        try {
                            jsonObject = new JSONObject( response );
                            boolean success = jsonObject.getBoolean( "success" );
                            if(success) {
                                fcm(curNick,sdf.format(calendar.getTime()));
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                };
                CurUserInfo cui=new CurUserInfo();
                AddFriendReq afr = new AddFriendReq(cui.getUserNick(),curNick,responseListener);
                RequestQueue queue = Volley.newRequestQueue(this);
                queue.add(afr);
                break;
        }
        return super.onOptionsItemSelected(item);
    }
    private void fcm(String reciever,String octime){
        Response.Listener<String> responseListener = new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                JSONObject jsonObject = null;
                try {
                    jsonObject = new JSONObject( response );
                    boolean success = jsonObject.getBoolean( "success" );
                    if(success) {
                        String msg= jsonObject.getString("msg");
                        Log.e("msg",msg+"");
                        String msg1 = curNick + "님에게 친구신청이 완료되었습니다.";
                        Toast.makeText(Profile.this, msg1, Toast.LENGTH_SHORT).show();
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        };
        CurUserInfo cui=new CurUserInfo();
        RequestSecReq rsr = new RequestSecReq(reciever,"친구 신청",cui.getUserNick()+"님이 친구신청을 보냈습니다.\n일시:"+octime,responseListener);
        RequestQueue queue = Volley.newRequestQueue(this);
        queue.add(rsr);
    }
    private void settingInfo() {
        Response.Listener<String> responseListener = new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    JSONObject jsonObject = new JSONObject( response );
                    Boolean suc=jsonObject.getBoolean("success");
                    if(suc){
                        String name=jsonObject.getString("nickname");
                        String birth=jsonObject.getString("birth");
                        String email=jsonObject.getString("email");
                        String pnum=jsonObject.getString("job");
                        String self=jsonObject.getString("self");
                        String[] dar=birth.split("-");
                        String day="";
                        Calendar calendar=Calendar.getInstance();
                        day=(calendar.get(Calendar.YEAR)-(Integer.parseInt(dar[0]))+1)+"살";
                        userName.setText(name);
                        uSelf.setText(self);
                        userEmail.setText(email);
                        userBirthday.setText(day);
                        if(curNick.equals(cui.getUserNick())){
                            userPnum.setText(pnum);
                        }

                    }
                } catch (JSONException e) {
                    Log.e("frag3 listpost","Connect Error");
                    e.printStackTrace();
                }
            }
        };

        getUserInfoReq guir = new getUserInfoReq(curNick,responseListener);
        RequestQueue queue = Volley.newRequestQueue(this);
        queue.add( guir);
    }
    private void checkFriend() {
        Response.Listener<String> responseListener = new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    JSONObject jasonObject = new JSONObject(response);
                    boolean success = jasonObject.getBoolean("success");
                    if (!success) {
                        menu.findItem(R.id.friend).setVisible(true);
                    }
                }catch (JSONException e){
                    e.printStackTrace();
                }
            }
        };
        CheckFriendReq cfr = new CheckFriendReq(cui.getUserNick(),curNick,responseListener);
        RequestQueue queue = Volley.newRequestQueue(this);
        queue.add(cfr);
    }
    @Override
    public void onBackPressed() {
        finish();
    }

    private void openFragment(final Fragment fragment)   {
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction transaction = fragmentManager.beginTransaction();
        transaction.replace(R.id.frame_Layout, fragment);
        transaction.addToBackStack(null);
        transaction.commit();
    }
}
