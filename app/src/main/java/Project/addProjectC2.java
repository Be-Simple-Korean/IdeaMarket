package Project;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.toolbox.Volley;
import com.example.realp.R;

import Project.Request.AddTeamUserReq;
import Project.Request.GetTeamNoReq;
import Project.Data.TeamData;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import Project.Data.Projectinfo;
import Project.Request.MakeProjectReq;

public class addProjectC2 extends AppCompatActivity {

    private RecyclerView recyclerView;
   public static TeamAdapter teamAdapter;
    public static ArrayList<TeamData> ar;
    private LinearLayoutManager linearLayoutManager;
    Projectinfo p = new Projectinfo();
    int i=0;
    int pcount=0;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chapter2);
        pcount=0;
        recyclerView=(RecyclerView)findViewById(R.id.teamView);
        linearLayoutManager=new LinearLayoutManager(this);
        recyclerView.setLayoutManager(linearLayoutManager);
        ar=new ArrayList<>();
        teamAdapter=new TeamAdapter(ar);
        recyclerView.setAdapter(teamAdapter);
    }

    public void addTeamInfo(View view) {
        CustomDialog customDialog = new CustomDialog(this);
        customDialog.callFunction();
    }
    @Override
    public void onBackPressed() {
        super.onBackPressed();
        p.pWorker.clear();
        p.tChar.clear();
        p.tRank.clear();
        finish();
    }
    public void backOne(View view) {
        p.pWorker.clear();
        p.tChar.clear();
        p.tRank.clear();
        finish();
    }
    public void makeTeam(View view) {
        if(p.getTpc()<1){
            Toast.makeText(this, "본인을 포함한 팀원을 추가해주세요!", Toast.LENGTH_SHORT).show();
            return;
        }
        makeProject();
    }
    public void makeProject(){
        Response.Listener<String> responseListener = new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    JSONObject jsonObject = new JSONObject( response );
                    boolean success = jsonObject.getBoolean( "success" );
                    if(success){
                        Toast.makeText(addProjectC2.this, "프로젝트 생성!", Toast.LENGTH_SHORT).show();
                        getTeamNumber();
                    }

                } catch (JSONException e) {
                    Log.e("CE1","Connect Error");
                    e.printStackTrace();
                }
            }
        };
        MakeProjectReq mpr = new MakeProjectReq(p.getProjectName(),p.getTeamName(),p.getpTheme(),p.getStartDate(),p.getFinishDate(),p.getTpc(),responseListener);
        RequestQueue queue = Volley.newRequestQueue(this);
        queue.add( mpr );
    }
    public void getTeamNumber(){
        Response.Listener<String> responseListener = new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    JSONObject jsonObject = new JSONObject( response );
                    boolean success = jsonObject.getBoolean( "success" );
                    if(success){
                        int i = jsonObject.getInt("teamNo");
                        p.setTeamNo(i);
                        addProjectWorker();
                    }

                } catch (JSONException e) {
                    Log.e("CE2","Connect Error");
                    e.printStackTrace();
                }
            }
        };
        GetTeamNoReq gtnr = new GetTeamNoReq(p.getTeamName(),p.getProjectName(),responseListener);
        RequestQueue queue = Volley.newRequestQueue(this);
        queue.add( gtnr );
    }
    public void addProjectWorker(){
        for(i=0;i<p.getTpc();i++){
            Response.Listener<String> responseListener = new Response.Listener<String>() {
                @Override
                public void onResponse(String response) {
                    try {
                        JSONObject jsonObject = new JSONObject( response );
                        boolean success = jsonObject.getBoolean( "success" );
                        if(success){
                            Toast.makeText(addProjectC2.this, "팀 생성!", Toast.LENGTH_SHORT).show();
                            if(i==p.getTpc()){
                                pcount+=1;
                                if(pcount==p.getTpc()){
                                    Intent intent = new Intent(addProjectC2.this, MainActivity.class);
                                    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                                    intent.putExtra("status",true);
                                    startActivity(intent);
                                }
                            }
                        }
                    } catch (JSONException e) {
                        Log.e("CE3","Connect Error");
                        e.printStackTrace();
                    }
                }
            };
            AddTeamUserReq atur = new AddTeamUserReq(p.getTeamNo(),p.pWorker.get(i),p.tRank.get(i),p.tChar.get(i),responseListener);
            RequestQueue queue = Volley.newRequestQueue(this);
            queue.add( atur );
        }
    }
}
