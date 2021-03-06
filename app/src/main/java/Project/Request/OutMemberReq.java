package Project.Request;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.DialogInterface;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.toolbox.Volley;

import Project.ChaseAdapter;
import Project.Data.ChaseData;
import com.example.realp.CurUserInfo;
import com.example.realp.R;
import com.example.realp.RequestSecReq;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import Project.CurProjectinfo;

public class OutMemberReq extends AppCompatActivity {
    RecyclerView recyclerView;
    ChaseAdapter chaseAdapter;
    ArrayList<ChaseData> cdar;
    CurProjectinfo c=new CurProjectinfo();
    LinearLayoutManager linearLayoutManager;
    public static String selecter="";
    public static Boolean isCheck;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        isCheck=false;
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_out_member_req);
        showPWorker();
        findViewById(R.id.iv_backtoList).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                chaseAdapter.car.clear();
                finish();
            }
        });
        findViewById(R.id.tv_d_user).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(!isCheck||selecter.equals("")){
                    Toast.makeText(OutMemberReq.this, "추방할 팀원을 선택해주세요", Toast.LENGTH_SHORT).show();
                    return;
                }
                if(c.getTpc()==2){
                    Toast.makeText(OutMemberReq.this, "팀이 2명일경우 추방이 불가합니다.", Toast.LENGTH_SHORT).show();
                    return;
                }
                AlertDialog.Builder builder=new AlertDialog.Builder(OutMemberReq.this).setTitle("팀원 추방")
                        .setMessage("'"+selecter+"'팀원을 추방하시겠습니까?\n다른 팀원의 전원 동의가 있어야만 추방이 가능합니다");
                builder.setNegativeButton("아니오", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        dialogInterface.dismiss();
                    }
                });
                builder.setPositiveButton("네", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        Toast.makeText(OutMemberReq.this, "다른 팀원에게 알림이 전달되었습니다.", Toast.LENGTH_SHORT).show();
                        makeChaseData();
                        finish();
                        dialogInterface.dismiss();
                    }
                });
                builder.show();
            }
        });
    }

    private void makeChaseData() {
        CurUserInfo cui=new CurUserInfo();
        CurProjectinfo cpi=new CurProjectinfo();
        String cur=cui.getUserNick();
        Response.Listener<String> responseListener = new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                JSONObject jsonObject = null;
                try {
                    jsonObject = new JSONObject( response );
                    boolean success = jsonObject.getBoolean( "success" );
                    if(success){
                        for(int j=0;j<c.getTpc();j++){
                            if(cur.equals(c.worker.get(j))){
                                continue;
                            }
                            if(c.worker.get(j).equals(selecter)){
                                Log.e("selecter",selecter+" 넘김");
                                continue;
                            }
                            rejectFcm(cpi.worker.get(j),"팀원 추방","'"+cur+"'팀장이 '"+selecter+"' 팀원을 추방하려고 합니다.\n클릭해서 추방에 대한 의견을 결정해주세요");
                        }
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        };
        InsertChaseReq icr = new InsertChaseReq(c.getTeamNo(),selecter,cur,1,"진행",responseListener);
        RequestQueue queue = Volley.newRequestQueue(this);
        queue.add(icr);
    }

    @Override
    public void onBackPressed() {
        isCheck=false;
        selecter="";
        chaseAdapter.car.clear();
        finish();
    }

    private void showPWorker() {
        recyclerView=(RecyclerView)findViewById(R.id.ChaseView);
        linearLayoutManager=new LinearLayoutManager(this);
        recyclerView.setLayoutManager(linearLayoutManager);
        cdar=new ArrayList<>();
        chaseAdapter=new ChaseAdapter(cdar);
        recyclerView.setAdapter(chaseAdapter);
        CurUserInfo cui=new CurUserInfo();
        Response.Listener<String> responseListener = new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    JSONObject jsonObject = new JSONObject( response );
                    JSONArray ja = jsonObject.getJSONArray("TeamUser");
                    for(int i=0;i<ja.length();i++) {
                        JSONObject item = ja.getJSONObject(i);
                        String curuser=cui.getUserNick();
                        String userID = item.getString("userID");
                        if(curuser.equals(userID)){
                            continue;
                        }
                        ChaseData cd=new ChaseData(userID);
                        cdar.add(cd);
                        chaseAdapter.notifyDataSetChanged();
                    }
                } catch (JSONException e) {
                    Log.e("getWorker in chase","Connect Error");
                    e.printStackTrace();
                }
            }
        };
        GetTeamInfoReq gtir = new GetTeamInfoReq(c.getTeamNo(),responseListener);
        RequestQueue queue = Volley.newRequestQueue(this);
        queue.add(gtir);
    }

    private void rejectFcm(String user,String title,String msg){
        Response.Listener<String> responseListener = new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {

            }
        };
        RequestSecReq rsr = new RequestSecReq(user,title,msg,responseListener);
        RequestQueue queue = Volley.newRequestQueue(this);
        queue.add(rsr);
    }
}