package Project;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Toast;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.toolbox.Volley;
import com.example.realp.CurUserInfo;
import com.example.realp.R;

import org.json.JSONException;
import org.json.JSONObject;
import Project.Request.DeleteDirReq;
import Project.Request.DeleteProjectReq;
import Project.Request.GetAResultReq;
import Project.Request.GetCStatusReq;
import Project.Request.OutMemberReq;

public class SettingList extends AppCompatActivity {
    CurProjectinfo c = new CurProjectinfo();
    String reciever;
    CurUserInfo cui=new CurUserInfo();
    String rank="";
    private String getReciever() {
        for(int i =0;i<c.worker.size();i++){
            if(c.wRank.get(i).equals("팀장")){
                return c.worker.get(i);
            }
        }
        return "";
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setting_list);
        reciever=getReciever();
        rank =cui.getUserRank();
        findViewById(R.id.tv_outTeam).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
              getResult(cui.getUserNick());
            }
        });
        findViewById(R.id.outMember).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(rank.equals("팀원")){
                    Toast.makeText(SettingList.this, "팀장만 사용가능합니다.", Toast.LENGTH_SHORT).show();
                    return;
                }
                getStatus();
            }
        });
        findViewById(R.id.backFromSet).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
        findViewById(R.id.tv_setproject).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(rank.equals("팀원")){
                    Toast.makeText(SettingList.this, "팀장만 사용가능합니다.", Toast.LENGTH_SHORT).show();
                    return;
                }
                Intent i = new Intent(SettingList.this, EditProject.class);
                startActivity(i);
            }
        });
    }
    private void showSecDialog(){
        AlertDialog.Builder builder=new AlertDialog.Builder(SettingList.this).setTitle("팀 탈퇴 요청")
                .setMessage("팀 탈퇴는 팀장의 승인이 있어야 가능합니다.\n팀에서 정말 탈퇴하시겠습니까?");
        builder.setNegativeButton("아니오", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                dialogInterface.dismiss();
            }
        });
        builder.setPositiveButton("네", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                dialogInterface.dismiss();
                Intent intent = new Intent(SettingList.this, RequestSecDialog.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_SINGLE_TOP);
                startActivity(intent);
            }
        });
        builder.show();
    }
    private void getResult(String user){
        Response.Listener<String> responseListener = new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                JSONObject jsonObject = null;
                try {
                    jsonObject = new JSONObject( response );
                    boolean success = jsonObject.getBoolean( "success" );
                    if(success){
                        String a_result=jsonObject.getString("a_result");
                        if(a_result.equals("대기")){
                            Toast.makeText(SettingList.this, "탈퇴요청이 진행중입니다.", Toast.LENGTH_SHORT).show();
                            return;
                        }else{
                            showSecDialog();
                        }
                    }else{
                        showSecDialog();
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }
        };
        GetAResultReq garr=new GetAResultReq("탈퇴 요청",user,reciever,responseListener);
        RequestQueue queue = Volley.newRequestQueue(this);
        queue.add(garr);
    }
    public void deleteProject(View view) {
        if(rank.equals("팀원")){
            Toast.makeText(SettingList.this, "팀장만 사용가능합니다.", Toast.LENGTH_SHORT).show();
            return;
        }
        AlertDialog.Builder builder=new AlertDialog.Builder(SettingList.this).setTitle("프로젝트 종료")
                .setMessage("프로젝트를 정말 종료하시겠습니까?.\n종료시 프로젝트내의 모든 데이터가 삭제됩니다.");
        builder.setNegativeButton("아니오", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                dialogInterface.dismiss();
            }
        });
        builder.setPositiveButton("네", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                dialogInterface.dismiss();
                deleting();
            }
        });
        builder.show();

    }

    private void deleting() {
        Response.Listener<String> responseListener = new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    JSONObject jsonObject = new JSONObject( response );
                    boolean success = jsonObject.getBoolean( "success" );
                    if(success){
                        String path=c.getProjectName()+"_"+c.getTeamName()+"_"+c.getTeamNo();
                        Log.e("path",path);
                        deleteDir(path);
                    }
                } catch (JSONException e) {
                    Log.e("deleteproject","Connect Error");
                    e.printStackTrace();
                }
            }
        };
        Log.e("c.getTeamNo",c.getTeamNo()+"");
        DeleteProjectReq dpr= new DeleteProjectReq(c.getTeamNo(),responseListener);
        RequestQueue queue = Volley.newRequestQueue(this);
        queue.add( dpr );
    }

    private void deleteDir(String path) {
        Response.Listener<String> responseListener = new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    JSONObject jsonObject = new JSONObject( response );
                    boolean success = jsonObject.getBoolean( "suc" );
                    if(success){
                        Intent intent = new Intent(SettingList.this, MainActivity.class);
                        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                        intent.putExtra("status",true);
                        startActivity(intent);
                    }
                } catch (JSONException e) {
                    Log.e("deleteDir","Connect Error");
                    e.printStackTrace();
                }
            }
        };
        DeleteDirReq ddr= new DeleteDirReq(path,responseListener);
        RequestQueue queue = Volley.newRequestQueue(this);
        queue.add( ddr );

    }

    @Override
    public void onBackPressed() {
        ShowProject sp =new ShowProject();
        sp.ar.clear();
        sp.showWorker();
        finish();
    }

    private void getStatus(){
        Response.Listener<String> responseListener = new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                JSONObject jsonObject = null;
                try {
                    jsonObject = new JSONObject( response );
                    boolean success = jsonObject.getBoolean( "success" );
                    if(success){
                        String cStatus=jsonObject.getString("cStatus");
                        String target=jsonObject.getString("target");
                        if(cStatus.equals("진행")){
                            Toast.makeText(SettingList.this, "'"+target+"'팀원에 대한 추방이 진행중입니다.", Toast.LENGTH_SHORT).show();
                            return;
                        }else{
                            Intent i = new Intent(SettingList.this, OutMemberReq.class);
                            startActivity(i);
                        }
                    }else{
                        Intent i = new Intent(SettingList.this,OutMemberReq.class);
                        startActivity(i);
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }
        };
        //현재 사용자로 바꾸기
        GetCStatusReq gcsr=new GetCStatusReq(c.getTeamNo(),cui.getUserNick(),"진행",responseListener);
        RequestQueue queue = Volley.newRequestQueue(this);
        queue.add(gcsr);
    }
}
