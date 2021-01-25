package Chatting;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.toolbox.Volley;
import Project.CurProjectinfo;
import com.example.realp.CurUserInfo;
import com.example.realp.DelChatReq;
import com.example.realp.DelRoomReq;
import com.example.realp.GetPlayerReq;
import Project.MainActivity;
import com.example.realp.R;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;

import Chatting.Data.Data;
import Chatting.Request.GetCpcReq;
import Chatting.Request.UpdateChatMsgReq;
import Chatting.Request.getChatLiveMsgReq;
import Chatting.Request.getChatMsgsReq;
import Chatting.Request.insertChatMsgReq;

public class Chating extends AppCompatActivity {
    RecyclerView recyclerView;
    DataAdapter dataAdapter;
    ArrayList<Data> dar;
    LinearLayoutManager layoutManager;
    ArrayList<String> par;
    EditText msging;
    CurUserInfo cui=new CurUserInfo();
    int ccpc=0,no;
    Boolean isThread=false,isS=false,backTeam=false;
    Thread t;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chating);

        msging=findViewById(R.id.edtext);
        par=new ArrayList<>();
        recyclerView=findViewById(R.id.chatView);
        layoutManager=new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);
        dar=new ArrayList<>();
        dataAdapter=new DataAdapter(dar);
        recyclerView.setAdapter(dataAdapter);
        TextView tv_user=findViewById(R.id.tv_user);

        Intent i = getIntent();
        final String nick=i.getStringExtra("user");
        no=i.getIntExtra("no",0);
        backTeam=i.getBooleanExtra("team",false);
        if(backTeam&&cui.getUserRank().equals("팀원")){
            findViewById(R.id.tv_del).setVisibility(View.INVISIBLE);
        }
        getCpc(no);
        tv_user.setText(nick);
        getMsgs(no);
        findViewById(R.id.send).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final String msg=msging.getText().toString(); //입력창에 있는 데이터를 가져옴
                if(msg.equals("")){ //데이터가 없으면 수행정지
                    Toast.makeText(Chating.this, "메시지를 입력해주세요", Toast.LENGTH_SHORT).show();
                    return;
                }
                //전송시간
                Calendar calendar= Calendar.getInstance();
                SimpleDateFormat day=new SimpleDateFormat("yyyy년 M월 dd일");
                String cd=day.format(calendar.getTime());
                SimpleDateFormat time=new SimpleDateFormat(" H:mm:ss");
                String ct=time.format(calendar.getTime());
                //전송
                for(int i=0;i<ccpc;i++){
                    final int finalI = i;
                    Response.Listener<String> responseListener = new Response.Listener<String>() {
                        @Override
                        public void onResponse(String response) {
                            try {
                                JSONObject jsonObject = new JSONObject( response );
                                Boolean suc=jsonObject.getBoolean("success");
                                if(suc){
                                    Log.e("fina",finalI+"/"+(ccpc-1));
                                    if(finalI ==ccpc-1){
                                        Data d=new Data(cui.getUserNick(),msg);
                                        dar.add(d);
                                        dataAdapter.notifyDataSetChanged();
                                        Toast.makeText(Chating.this, "전송 성공", Toast.LENGTH_SHORT).show();
                                        msging.setText("");
                                        recyclerView.scrollToPosition(dar.size()-1);
                                    }
                                }else{
                                }
                            } catch (JSONException e) {
                                Log.e("sendMessages","Connect Error");
                                e.printStackTrace();
                            }
                        }
                    };
                    int j;
                    if(cui.getUserNick().equals(par.get(i))){
                        j=0;
                    }else{
                        j=1;
                    }
                    insertChatMsgReq icmr= new insertChatMsgReq(no,cui.getUserNick(),par.get(i),msg,cd+ct,j,responseListener);
                    RequestQueue queue = Volley.newRequestQueue( Chating.this);
                    queue.add(icmr);
                }
            }
        });

    }
    private void getPlayer(int no){
        Response.Listener<String> responseListener = new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    JSONObject jsonObject = new JSONObject( response );
                    JSONArray ja = jsonObject.getJSONArray("Player");
                    for(int i=0;i<ja.length();i++) {
                        JSONObject item = ja.getJSONObject(i);
                        par.add(item.getString("player"));
                    }
                } catch (JSONException e) {
                    Log.e("getPlayers","Connect Error");
                    e.printStackTrace();
                }
            }
        };
        GetPlayerReq gpr = new GetPlayerReq(no,responseListener);
        RequestQueue queue = Volley.newRequestQueue( Chating.this);
        queue.add(gpr);
    };

    private void getCpc(final int no) {
        Response.Listener<String> responseListener = new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    JSONObject jsonObject = new JSONObject( response );
                    Boolean suc=jsonObject.getBoolean("success");
                    if(suc){
                        int cpc=jsonObject.getInt("cpc");
                        ccpc=cpc;
                        getPlayer(no);
                    }else{
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        };
       GetCpcReq gcr = new GetCpcReq(no,responseListener);
        RequestQueue queue = Volley.newRequestQueue(Chating.this);
        queue.add(gcr);
    } //현 채팅방 인원수 get

    private void getMsgs(final int no) {
        final Response.Listener<String> responseListener = new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    JSONObject jsonObject = new JSONObject( response );
                    JSONArray ja = jsonObject.getJSONArray("Messages");
                    if(ja.length()==0){
                        getRecieving(no);
                        return;
                    }
                    for(int i=0;i<ja.length();i++) {
                        JSONObject item = ja.getJSONObject(i);
                        String nick = item.getString("nick");
                        String msg=item.getString("msg");
                        int rStatus=item.getInt("rStatus");
                        String cDate=item.getString("cDate");
                        Data d=new Data(nick,msg);
                        dar.add(d);
                        dataAdapter.notifyDataSetChanged();
                        recyclerView.scrollToPosition(dar.size()-1);
                       if(i==ja.length()-1){
                            isS=true;
                        }
                      /*  if(!nick.equals(cui.getNick())&&rStatus!=0){
                            updateRs(no,nick,msg,cDate,rStatus,rStatus-1);
                        }*/
                    }
                    if(isS){
                        getRecieving(no);
                    }
                } catch (JSONException e) {
                    Log.e("getchatmsgs","Connect Error");
                    e.printStackTrace();
                }
            }
        };
        getChatMsgsReq gcmr = new getChatMsgsReq(no,cui.getUserNick(),responseListener);
        RequestQueue queue = Volley.newRequestQueue( Chating.this);
        queue.add(gcmr);
    }

    private void getRecieving(final int no) {
        isThread=true;
        t=new Thread(){
            @Override
            public void run() {
                while(isThread){
                    Log.e("메시지","수신중");
                    Response.Listener<String> responseListener = new Response.Listener<String>() {
                        @Override
                        public void onResponse(String response) {
                            try {
                                JSONObject jsonObject = new JSONObject( response );
                                JSONArray ja = jsonObject.getJSONArray("Message");
                                Log.e(" get rec/ja",ja.length()+"");
                                if(ja.length()!=0) {
                                    for (int i = 0; i < ja.length(); i++) {
                                        JSONObject item = ja.getJSONObject(i);
                                        String nick = item.getString("nick");
                                        String msg = item.getString("msg");
                                        String cDate = item.getString("cDate");
                                        int rStatus = item.getInt("rStatus");
                                        Log.e("getLiveMsg",nick+"/"+msg+"/"+cDate+"/"+rStatus);
                                        Data d = new Data(nick, msg);
                                        dar.add(d);
                                        dataAdapter.notifyDataSetChanged();
                                        recyclerView.scrollToPosition(dar.size()-1);
                                       /* for (int j = 0; j < dar.size(); j++) {
                                            if (dar.get(j).getNick().equals(nick) && dar.get(j).getContent().equals(msg)&&dar.get(j).getContent().equals(cDate)) {
                                                Log.e("break","in getrec");
                                                break;
                                            } else if (j == dar.size() - 1) {
                                                Data d = new Data(nick, msg);
                                                dar.add(d);
                                                dataAdapter.notifyDataSetChanged();
                                            }
                                        }*/
                                        updateRs(no, cui.getUserNick(), msg, cDate, rStatus, rStatus - 1);
                                    }
                                }
                            } catch (JSONException e) {
                                Log.e("getchatLiveMessages","Connect Error");
                                e.printStackTrace();
                            }
                        }
                    };
                    getChatLiveMsgReq gclmr = new getChatLiveMsgReq(no,cui.getUserNick(),responseListener);
                    RequestQueue queue = Volley.newRequestQueue( Chating.this);
                    queue.add(gclmr);
                    try {
                        sleep(5000);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            }
        };
        t.start();
    }

    private void updateRs(int no, String nick, String msg, String cDate, int rStatus, int newRs) {
        Response.Listener<String> responseListener = new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    JSONObject jsonObject = new JSONObject( response );
                    Boolean suc=jsonObject.getBoolean("success");
                    if(suc){
                        Log.e("updateRs","성공적");
                    }else{
                    }
                } catch (JSONException e) {
                    Log.e("updaters","Connect Error");
                    e.printStackTrace();
                }
            }
        };
        Log.e("sendMsg",nick+"/"+msg+"/"+cDate+"/"+rStatus+"/"+newRs);
        UpdateChatMsgReq ucmr= new UpdateChatMsgReq(no,nick,msg,cDate,rStatus,newRs,responseListener);
        RequestQueue queue = Volley.newRequestQueue( Chating.this);
        queue.add(ucmr);
    }

    @Override
    public void onBackPressed() {
        isThread = false;
        if(backTeam){
            finish();
        }else{
            Intent i = new Intent(Chating.this, MainActivity.class);
            i.putExtra("f4", true);
            i.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            startActivity(i);
        }
    }

    public void delChatRoom(View view) { //삭제 텍스트뷰 클릭 이벤트
        if(backTeam){
            Response.Listener<String> responseListener = new Response.Listener<String>() {
                @Override
                public void onResponse(String response) {
                    try {
                        JSONObject jsonObject = new JSONObject( response );
                        Boolean suc=jsonObject.getBoolean("success");
                        Log.e("팔",suc+"");
                        if(suc){
                            isThread = false;
                            finish();
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
            };
            CurProjectinfo cpi=new CurProjectinfo();
            DelRoomReq acur = new DelRoomReq(cpi.getTeamNo(),no,responseListener);
            RequestQueue queue = Volley.newRequestQueue(this);
            queue.add(acur);
        }else {
            AlertDialog.Builder builder = new AlertDialog.Builder(this)
                    .setTitle("채팅방 나가기")
                    .setMessage("채팅방을 정말 나가시겠습니까?\n※삭제시 상대방과의 대화내용은 복구할 수 없습니다.");
            builder.setNegativeButton("아니오", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface, int i) {
                    dialogInterface.dismiss();
                }
            });
            builder.setPositiveButton("예", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface, int i) {
                    Response.Listener<String> responseListener = new Response.Listener<String>() {
                        @Override
                        public void onResponse(String response) {
                            try {
                                JSONObject jsonObject = new JSONObject(response);
                                Boolean suc = jsonObject.getBoolean("success");
                                if (suc) {
                                    isThread = false;
                                    dialogInterface.dismiss();
                                    Intent i = new Intent(Chating.this, MainActivity.class);
                                    i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                                    i.putExtra("f4", true);
                                    startActivity(i);
                                } else {
                                }
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        }
                    };
                    DelChatReq dcr = new DelChatReq(no, responseListener);
                    RequestQueue queue = Volley.newRequestQueue(Chating.this);
                    queue.add(dcr);
                }
            });
            builder.show();
        }
    }
}