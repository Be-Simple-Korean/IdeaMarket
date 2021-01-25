package Chatting;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.toolbox.Volley;
import com.example.realp.AddChatUserReq;

import Project.CurProjectinfo;
import com.example.realp.CurUserInfo;
import Project.Request.InsertTeamRoomReq;
import com.example.realp.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import Chatting.Data.ChatRoomData;
import Chatting.Request.getChatCurMsgReq;
import Chatting.Request.getChatRoomReq;
import Chatting.Request.getChatUsersReq;


public class AddFromRoom extends AppCompatActivity {
    RecyclerView lv;
    ArrayList<ChatRoomData> car;
    AddAfrAdapter adapter;
    String msg="",date="",people="";
    CurUserInfo cui=new CurUserInfo();
    public static ArrayList<String> uar=new ArrayList<>();
    public static ArrayList<Integer> iar=new ArrayList<>();
    public static int curNumber;
    LinearLayoutManager manager;
    int count=0;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_from_room);
        car=new ArrayList<>();
        manager=new LinearLayoutManager(this);
        lv=findViewById(R.id.lvInAfr);
        lv.setLayoutManager(manager);
        adapter=new AddAfrAdapter(car);
        lv.setAdapter(adapter);
        Response.Listener<String> responseListener = new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    JSONObject jsonObject = new JSONObject( response );
                    JSONArray ja = jsonObject.getJSONArray("ChatNo");
                    if(ja.length()==0){return;}
                    for(int i=0;i<ja.length();i++){
                        JSONObject item = ja.getJSONObject(i);
                        final int chatNo=item.getInt("chatNo");
                        iar.add(chatNo);
                        Response.Listener<String> responseListener = new Response.Listener<String>() {
                            @Override
                            public void onResponse(String response) {
                                try {
                                    JSONObject jsonObject = new JSONObject( response );
                                    JSONArray ja = jsonObject.getJSONArray("ChatUser");
                                    if(ja.length()==0){return;}
                                    for(int i=0;i<ja.length();i++){
                                        JSONObject item = ja.getJSONObject(i);
                                        String nick = item.getString("nick");
                                        if(nick.equals(cui.getUserNick())){
                                            continue;
                                        }
                                        people=people+nick+",";
                                    }
                                    if(people.charAt((people.length()-1))==',') {
                                        people=people.substring(0,people.length()-1);
                                    }
                                    getCurMsg(people,chatNo);
                                    people="";
                                } catch (JSONException e) {
                                    Log.e("frag3 listpost","Connect Error");
                                    e.printStackTrace();
                                }
                            }
                        };
                        getChatUsersReq gcur = new getChatUsersReq(chatNo,responseListener);
                        RequestQueue queue = Volley.newRequestQueue( AddFromRoom.this);
                        queue.add(gcur);
                        stop.run();
                        stop.join();
                    }
                } catch (JSONException e) {
                    Log.e("frag4 listpost","Connect Error");
                    e.printStackTrace();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        };
        getChatRoomReq gcrr = new getChatRoomReq(cui.getUserNick(),responseListener);
        RequestQueue queue = Volley.newRequestQueue( this);
        queue.add(gcrr);
    }
    Thread stop=new Thread(new Runnable() {
        @Override
        public void run() {
            try {
                Thread.sleep(300);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    });
    private void getCurMsg(final String person, int chatNo) {
        Response.Listener<String> responseListener = new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    JSONObject jsonObject = new JSONObject( response );
                    Boolean suc=jsonObject.getBoolean("success");
                    if(suc){
                        msg=jsonObject.getString("msg");
                        date=jsonObject.getString("date");
                        int c=date.indexOf("일");
                        String d="";
                        for(int i=0;i<=c;i++) {
                            d+=date.charAt(i);
                        }
                        ChatRoomData crd=new ChatRoomData(person,msg,d);
                        car.add(crd);
                        adapter.notifyDataSetChanged();
                        people="";
                    }else{
                        ChatRoomData crd=new ChatRoomData(person,"","");
                        car.add(crd);
                        adapter.notifyDataSetChanged();
                        people="";
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                    people="";
                }
            }
        };
        getChatCurMsgReq gccmr = new getChatCurMsgReq(chatNo,responseListener);
        RequestQueue queue = Volley.newRequestQueue(this);
        queue.add(gccmr);
        stop.run();
        try {
            stop.join();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    public void backToSp(View view) {
        //해당 방에 팀원이 전부 있는지 체크, 없으면 추가
      Response.Listener<String> responseListener = new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    JSONObject jsonObject = new JSONObject( response );
                    Boolean suc=jsonObject.getBoolean("success");
                    if(suc){
                        Log.e("수행","수");
                       checkOradd();
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                    people="";
                }
            }
        };
        CurProjectinfo cpi=new CurProjectinfo();
        InsertTeamRoomReq itrr = new InsertTeamRoomReq(cpi.getTeamNo(),curNumber,responseListener);
        RequestQueue queue = Volley.newRequestQueue(this);
        queue.add(itrr);
    }
    private void checkOradd(){
        CurProjectinfo cpi=new CurProjectinfo();
        Log.e("수행","행");
        Log.e("수행",cpi.worker.size()+"");
      for(int i=0;i<cpi.worker.size();i++){
          Response.Listener<String> responseListener = new Response.Listener<String>() {
              @Override
              public void onResponse(String response) {
                  try {
                      JSONObject jsonObject = new JSONObject( response );
                      Boolean suc=jsonObject.getBoolean("success");
                      Log.e("팔",suc+"");
                      if(suc){
                          count+=1;
                          if(count==cpi.worker.size()){
                              Intent p=new Intent();
                              p.putExtra("no",curNumber);
                              setResult(RESULT_OK,p);
                              curNumber=0;
                              finish();
                          }
                      }
                  } catch (JSONException e) {
                      e.printStackTrace();
                      people="";
                  }
              }
          };
          AddChatUserReq acur = new AddChatUserReq(curNumber,cpi.worker.get(i),responseListener);
          RequestQueue queue = Volley.newRequestQueue(this);
          queue.add(acur);
      }
    }
}