package Project;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.toolbox.Volley;
import com.example.realp.CurUserInfo;
import com.example.realp.R;
import com.example.realp.WorkerAdapter;
import com.example.realp.WorkerData;

import Project.Request.CheckRoomReq;
import Project.Request.GetTeamInfoReq;
import Project.Request.InsertTeamRoomReq;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import Chatting.AddFromRoom;
import Chatting.Chating;
import Chatting.Request.InsertChatRoomReq;
import Chatting.Request.InsertChatUserReq;
import Chatting.Request.getChatUsersReq;
import Project.Request.GetRankReq;
import Project.Request.getProjectInfoReq;


public class ShowProject extends AppCompatActivity {
    public static RecyclerView recyclerView;
    public static WorkerAdapter workerAdapter;
    public static ArrayList<WorkerData> ar;
    public static LinearLayoutManager linearLayoutManager;
    TextView barName,tv_tName,tv_inTheme,tv_inStorage,tv_inCal;
    ImageButton iv_back;
    ImageView iv_menu,close_draw;
    CurProjectinfo cpi=new CurProjectinfo();
    CurUserInfo cui=new CurUserInfo();
    private DrawerLayout drawerLayout;
    private View drawerView;
    String other,people="";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_show_project);
        recyclerView=(RecyclerView)findViewById(R.id.showTeamList);
        linearLayoutManager=new LinearLayoutManager(this);
        recyclerView.setLayoutManager(linearLayoutManager);
        ar=new ArrayList<>();
        workerAdapter=new WorkerAdapter(ar);
        recyclerView.setAdapter(workerAdapter);
        getProjectInfo();
        close_draw=findViewById(R.id.close_draw);
        drawerLayout=findViewById(R.id.drawer_layout);
        tv_inTheme=findViewById(R.id.tv_inTheme);
        tv_inStorage=findViewById(R.id.tv_inStrage);
        tv_tName=findViewById(R.id.tv_tName);
        drawerView=findViewById(R.id.show_team);
        barName=findViewById(R.id.barname);
        barName.setText(cpi.getProjectName());
        iv_back=findViewById(R.id.iv_back);
        iv_menu=findViewById(R.id.iv_menu);
        tv_inCal=findViewById(R.id.tv_inCalendar);
        findViewById(R.id.set_project).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                drawerLayout.closeDrawers();
                Intent i =new Intent(ShowProject.this, SettingList.class);
                startActivity(i);
            }
        });
        tv_inCal.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(ShowProject.this, CalManage.class);
                startActivity(i);
            }
        });

        iv_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                cpi.worker.clear();
                cpi.wChar.clear();
                cpi.wRank.clear();
                Intent intent = new Intent(ShowProject.this, MainActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                intent.putExtra("status",true);
                startActivity(intent);
            }
        });
        tv_inStorage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i= new Intent(ShowProject.this, ShowFileList.class);
                startActivity(i);
            }
        });
        close_draw.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                drawerLayout.closeDrawers();
            }
        });
        iv_menu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
               // showWorker();
                drawerLayout.openDrawer(drawerView);

            }
        });
            drawerLayout.setDrawerListener(listener);
            drawerView.setOnTouchListener(new View.OnTouchListener() {
                @Override
                public boolean onTouch(View view, MotionEvent motionEvent) {
                    return true;
                }
            });

        }

    private void getRank(String userNick) {
        Response.Listener<String> responseListener = new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    JSONObject jsonObject = new JSONObject( response );
                    boolean suc = jsonObject.getBoolean("success");
                    if(suc){
                        String rank=jsonObject.getString("teamRank");
                        cui.setUserRank(rank);
                    }
                } catch (JSONException e) {
                    Log.e("getRank in showp","Connect Error");
                    finish();
                    e.printStackTrace();
                }
            }
        };
        GetRankReq grr = new GetRankReq(userNick,cpi.getTeamNo(),responseListener);
        RequestQueue queue = Volley.newRequestQueue(this);
        queue.add(grr);
    }

    DrawerLayout.DrawerListener listener= new DrawerLayout.DrawerListener() {
            @Override
            public void onDrawerSlide(@NonNull View drawerView, float slideOffset) {

            }

            @Override
            public void onDrawerOpened(@NonNull View drawerView) {

            }

            @Override
            public void onDrawerClosed(@NonNull View drawerView) {

            }

            @Override
            public void onDrawerStateChanged(int newState) {

            }
        };
    private void getProjectInfo(){
        Response.Listener<String> responseListener = new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    JSONObject jsonObject = new JSONObject( response );
                    boolean suc = jsonObject.getBoolean("success");
                    if(suc){
                        int teamNo=jsonObject.getInt("teamNo");
                        String teamName= jsonObject.getString("teamName");
                        tv_tName.setText(teamName);
                        String pName= jsonObject.getString("pName");
                        String pTheme= jsonObject.getString("pTheme");
                        String startDate= jsonObject.getString("startDate");
                        String finishDate= jsonObject.getString("finishDate");
                        int tpc=jsonObject.getInt("tpc");
                        cpi.setTeamNo(teamNo);
                        cpi.setTeamName(teamName);
                        cpi.setProjectName(pName);
                        cpi.setPTheme(pTheme);
                        if(cpi.getpTheme().equals("미정")){
                            tv_inTheme.append(cpi.getpTheme());
                        }else{
                            tv_inTheme.setText(cpi.getpTheme());
                        }
                        cpi.setStartDate(startDate);
                        cpi.setFinishDate(finishDate);
                        cpi.setTpc(tpc);
                        getTeamInfo();
                        getRank(cui.getUserNick());
                    }
                } catch (JSONException e) {
                    Log.e("getPInfo in showp","Connect Error");
                    finish();
                    e.printStackTrace();
                }
            }
        };
        getProjectInfoReq gpir = new getProjectInfoReq(cpi.getProjectName(),cpi.getTeamName(),responseListener);
        RequestQueue queue = Volley.newRequestQueue(this);
        queue.add(gpir);
    }
    private void getTeamInfo(){
        Response.Listener<String> responseListener = new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    JSONObject jsonObject = new JSONObject( response );
                    JSONArray ja = jsonObject.getJSONArray("TeamUser");
                    for(int i=0;i<ja.length();i++) {
                        JSONObject item = ja.getJSONObject(i);
                        String userID = item.getString("userID");
                        cpi.worker.add(userID);
                        String teamChar = item.getString("teamChar");
                        cpi.wChar.add(teamChar);
                        String teamRank = item.getString("teamRank");
                        cpi.wRank.add(teamRank);
                    }
                    showWorker();
                } catch (JSONException e) {
                    Log.e("getTInfo in showp","Connect Error");
                    e.printStackTrace();
                }
            }
        };
        GetTeamInfoReq gtir = new GetTeamInfoReq(cpi.getTeamNo(),responseListener);
        RequestQueue queue = Volley.newRequestQueue(this);
        queue.add(gtir);
    }
    public void showWorker(){
            for(int i=0;i<cpi.getTpc();i++){
                WorkerData wd=new WorkerData(cpi.worker.get(i),cpi.wChar.get(i),cpi.wRank.get(i));
                ar.add(wd);
                workerAdapter.notifyDataSetChanged();
            }
    }
    @Override
    public void onBackPressed() {
        if(drawerLayout.isDrawerOpen(drawerView)){
            drawerLayout.closeDrawers();
        }else{
            cpi.worker.clear();
            cpi.wChar.clear();
            cpi.wRank.clear();
            Intent intent = new Intent(this,MainActivity.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            intent.putExtra("status",true);
            this.startActivity(intent);
        }
    }
    public void openTask(View view) {
        task();
    }
    public void task(){
        Intent i = new Intent(ShowProject.this, ShowTask.class);
        startActivity(i);
    }

    public void conCRoom(View view) {
        //1.전용채팅방이 디비에 등록되어있는지 체크
        // 되어있다면 상대방까지 읽어오고 인텐트
        //안되어있다면 팀원에겐 간단한 토스트 / 팀장에겐 등록할것인지 다이얼로그로 안내
        Response.Listener<String> responseListener = new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    JSONObject jsonObject = new JSONObject( response );
                    Boolean suc=jsonObject.getBoolean("success");
                    if(suc){
                        int chatNo=jsonObject.getInt("chatNo");
                        getOtherUser(chatNo);
                    }else{  //안되어있는 경우
                        if(cui.getUserRank().equals("팀원")) {
                            Toast.makeText(ShowProject.this, "등록되어있는 채팅방이 없습니다.", Toast.LENGTH_SHORT).show();
                        }else if(cui.getUserRank().equals("팀장")){
                            View dv=(View)View.inflate(ShowProject.this,R.layout.guide_cr,null);
                            AlertDialog.Builder builder=new AlertDialog.Builder(ShowProject.this);
                            builder.setView(dv);
                            final AlertDialog  alertDialog=builder.create();
                            TextView tv_br=dv.findViewById(R.id.tv_br);//기존
                            tv_br.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View view) {
                                    alertDialog.dismiss();
                                    Intent i=new Intent(ShowProject.this, AddFromRoom.class);
                                    startActivityForResult(i,3000);
                                }
                            });
                            TextView tv_nr=dv.findViewById(R.id.tv_nr);//새로생성
                            tv_nr.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View view) {
                                    alertDialog.dismiss();
                                    insertChatRoomData();
                                }
                            });
                            TextView tv_cc=dv.findViewById(R.id.tv_ccInGc); //취소
                            tv_cc.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View view) {
                                    Log.e("수행","취소");
                                   alertDialog.dismiss();
                                }
                            });
                            alertDialog.show();
                        }
                    }
                } catch (JSONException e) {
                    Log.e("frag3 listpost","Connect Error");
                    e.printStackTrace();
                    return;
                }
            }
        };
        CheckRoomReq crr = new CheckRoomReq(cpi.getTeamNo(),responseListener); //본인 먼저 처리
        RequestQueue queue = Volley.newRequestQueue( this);
        queue.add(crr);
    }

    private void getOtherUser(int chatNo) {
        people="";
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
                        if(i+1==ja.length()){
                            if(people.charAt((people.length()-1))==',') {
                                people=people.substring(0,people.length()-1);
                            }
                            Intent it =new Intent(ShowProject.this, Chating.class);
                            it.putExtra("user",people);
                            it.putExtra("no",chatNo);
                            it.putExtra("team",true);
                            startActivity(it);
                        }
                    }
                } catch (JSONException e) {
                    Log.e("frag3 listpost","Connect Error");
                    e.printStackTrace();
                }/* catch (InterruptedException e) {
                                    e.printStackTrace();
                                }*/
            }
        };
        getChatUsersReq gcur = new getChatUsersReq(chatNo,responseListener);
        RequestQueue queue = Volley.newRequestQueue( this);
        queue.add(gcur);
    }
    private void insertChatRoomData() {
        Response.Listener<String> responseListener = new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    JSONObject jsonObject = new JSONObject( response );
                    Boolean suc=jsonObject.getBoolean("success");
                    if(suc){
                        int no=jsonObject.getInt("chatNo");
                        insertChatUser(no);
                    }else{
                        Toast.makeText(ShowProject.this, "다시 시도해주세요", Toast.LENGTH_SHORT).show();
                    }
                } catch (JSONException e) {
                    Log.e("frag3 listpost","Connect Error");
                    e.printStackTrace();
                }
            }
        };
        InsertChatRoomReq icrr = new InsertChatRoomReq(cpi.worker.size(),responseListener);
        RequestQueue queue = Volley.newRequestQueue( this);
        queue.add(icrr);
    }
    public void addTeamRoom(int no) {
        //해당 방에 팀원이 전부 있는지 체크, 없으면 추가
        Response.Listener<String> responseListener = new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    JSONObject jsonObject = new JSONObject( response );
                    Boolean suc=jsonObject.getBoolean("success");
                    if(suc){
                        Log.e("수행","수");
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        };
        CurProjectinfo cpi=new CurProjectinfo();
        InsertTeamRoomReq itrr = new InsertTeamRoomReq(cpi.getTeamNo(),no,responseListener);
        RequestQueue queue = Volley.newRequestQueue(this);
        queue.add(itrr);
    }
    private void insertChatUser(int no) {
        for(int i=0;i<cpi.worker.size();i++){
            int finalI = i;
            Response.Listener<String> responseListener = new Response.Listener<String>() {
                @Override
                public void onResponse(String response) {
                    try {
                        JSONObject jsonObject = new JSONObject( response );
                        Boolean suc=jsonObject.getBoolean("success");
                        if(suc){
                            if(finalI +1==cpi.worker.size()){
                                if(other.charAt((other.length()-1))==',') {
                                    other=other.substring(0,other.length()-1);
                                }
                                addTeamRoom(no);
                                Intent it = new Intent(ShowProject.this, Chating.class); //채팅방 생성완료시 채팅방으로 넘어감
                                it.putExtra("user", other);
                                it.putExtra("no", no);
                                it.putExtra("team",true);
                                startActivity(it);
                            }
                        }else{
                            Toast.makeText(ShowProject.this, "다시 시도해주세요", Toast.LENGTH_SHORT).show();
                            return;
                        }
                    } catch (JSONException e) {
                        Log.e("frag3 listpost","Connect Error");
                        e.printStackTrace();
                        return;
                    }
                }
            };
            InsertChatUserReq icur;
            String choseUser=cpi.worker.get(i);
            if(i==0){
                icur = new InsertChatUserReq(no,cui.getUserNick(),responseListener); //본인 먼저 처리
            }else{
                if(choseUser.equals(cui.getUserNick())){
                    continue;
                }else{
                        other=other+choseUser+",";
                        icur=new InsertChatUserReq(no,choseUser,responseListener); //타사용자 등록
                }
            }
            RequestQueue queue = Volley.newRequestQueue( this);
            queue.add(icur);
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(resultCode==RESULT_OK){
            switch (requestCode){
                case 3000:
                    Toast.makeText(this, "등록이 완료되었습니다!", Toast.LENGTH_SHORT).show();
                    break;
            }
        }
    }
}

