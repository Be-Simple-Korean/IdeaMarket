package Project;

import android.content.Intent;
import android.os.Bundle;
import android.text.method.ScrollingMovementMethod;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.toolbox.Volley;
import com.example.realp.CurUserInfo;
import com.example.realp.DeleteWorkerReq;
import com.example.realp.R;
import com.example.realp.RequestSecReq;
import com.example.realp.UpdateSecReq;
import com.example.realp.UpdateTpcReq;

import org.json.JSONException;
import org.json.JSONObject;

public class ResultSecDialog extends AppCompatActivity {
    TextView sec_sender,sec_octime;
    String user,octime,contents;
    CurProjectinfo c = new CurProjectinfo();
    CurUserInfo cui=new CurUserInfo();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);

        WindowManager.LayoutParams layoutParams = new WindowManager.LayoutParams();

        layoutParams.flags = WindowManager.LayoutParams.FLAG_DIM_BEHIND;

        layoutParams.dimAmount = 0.7f;

        getWindow().setAttributes(layoutParams);
        setContentView(R.layout.activity_sec_dialog);
        Intent i =getIntent();
        contents=i.getStringExtra("contents");
        user=i.getStringExtra("sender");
        octime=i.getStringExtra("sendtime");
        TextView tv_c_c=findViewById(R.id.sec_content);
        tv_c_c.setMovementMethod(new ScrollingMovementMethod());
        tv_c_c.setText(contents);
        sec_sender=findViewById(R.id.sec_sender);
        sec_octime=findViewById(R.id.sec_octime);
        sec_sender.setText(user);
        sec_octime.setText(octime);
    }

    public void closeAlert(View view) {
        //TODO:요청자에게 거부됐다는 알림이 가며 테이블에서 요청에대한 결과를 저장해야함(update)\
        updateSecData("close");
    }
    private void updateSecData(String status){
        Response.Listener<String> responseListener = new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                JSONObject jsonObject = null;
                try {
                    jsonObject = new JSONObject( response );
                    boolean success = jsonObject.getBoolean( "success" );
                    if(success){
                        if(status.equals("close")){
                            rejectFcm("탈퇴 거부","탈퇴요청이 거부되었습니다.");
                            finish();
                        }else if(status.equals("delete")){
                            deleteWorker();
                        }
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
    };
    UpdateSecReq rsr = new UpdateSecReq("탈퇴 요청",user,cui.getUserNick(),octime,contents,status,responseListener);
    RequestQueue queue = Volley.newRequestQueue(this);
        queue.add(rsr);
}
    private void deleteWorker(){
        Response.Listener<String> responseListener = new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                JSONObject jsonObject = null;
                try {
                    jsonObject = new JSONObject( response );
                    boolean success = jsonObject.getBoolean( "success" );
                    if(success){
                        updateTpc();
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        };
        DeleteWorkerReq dwr = new DeleteWorkerReq(c.getTeamNo(),user,responseListener);
        RequestQueue queue = Volley.newRequestQueue(this);
        queue.add(dwr);
    }
    public void deleteUser(View view) {
       updateSecData("delete");
    }
    private void updateTpc(){
        Response.Listener<String> responseListener = new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                JSONObject jsonObject = null;
                try {
                    jsonObject = new JSONObject( response );
                    boolean success = jsonObject.getBoolean( "success" );
                    if(success){
                        c.setTpc(c.getTpc()-1);
                        int index=c.worker.indexOf(user);
                        c.worker.remove(index);
                        c.wChar.remove(index);
                        c.wRank.remove(index);
                       rejectFcm("탈퇴 승인","프로젝트에서 탈퇴되셨습니다");
                       finish();
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        };
        UpdateTpcReq utr = new UpdateTpcReq(c.getTeamNo(),c.getTpc()-1,responseListener);
        RequestQueue queue = Volley.newRequestQueue(this);
        queue.add(utr);
    }
    private void rejectFcm(String title,String msg){
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