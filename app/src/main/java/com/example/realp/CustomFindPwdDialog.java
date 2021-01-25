package com.example.realp;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.AsyncTask;
import android.os.StrictMode;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.EditText;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.toolbox.Volley;

import org.json.JSONException;
import org.json.JSONObject;

import javax.mail.MessagingException;
import javax.mail.SendFailedException;

public class CustomFindPwdDialog {
    private Context context;
    TextView fr_id;
    int select_s=1;
    Dialog dlg;

    public CustomFindPwdDialog(Context context) {
        this.context = context;
    }
    String rpw="",remail="";
    // 호출할 다이얼로그 함수를 정의한다.
    public void callFunction() {
        // 커스텀 다이얼로그를 정의하기위해 Dialog클래스를 생성한다.
        dlg = new Dialog(context);
        // 액티비티의 타이틀바를 숨긴다.
        dlg.requestWindowFeature(Window.FEATURE_NO_TITLE);

        // 커스텀 다이얼로그의 레이아웃을 설정한다.
        dlg.setContentView(R.layout.activity_find_pwd);
        // 커스텀 다이얼로그를 노출한다.
        dlg.show();

        // 커스텀 다이얼로그의 각 위젯들을 정의한다.
        final EditText name=(EditText) dlg.findViewById(R.id.find_pwd_name);
        final EditText et_id=(EditText) dlg.findViewById(R.id.find_pwd_id);
        final EditText fi1=(EditText) dlg.findViewById(R.id.fp1);
        final EditText fi2=(EditText) dlg.findViewById(R.id.fp2);
        final EditText fi3=(EditText) dlg.findViewById(R.id.fp3);

        final RadioGroup rg=(RadioGroup)dlg.findViewById(R.id.fpg);
        rg.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup radioGroup, int i) {
                if(i==R.id.fpm){
                    select_s=1;
                }else{
                    select_s=2;
                }
            }
        });

        final TextView dlgNag=dlg.findViewById(R.id.fpc);
        final TextView dlgPos=dlg.findViewById(R.id.fpf);

      dlgNag.setOnClickListener(new View.OnClickListener() {
          @Override
          public void onClick(View view) {
              dlg.dismiss();
          }
      });
        dlgPos.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String sname="";
                String sid="";
                if(name.getText().toString().equals("")||name.getText().toString()==null){
                    Toast.makeText(context, "이름을 제대로 입력해주세요", Toast.LENGTH_SHORT).show();
                    return;
                }else{
                    sname=name.getText().toString();
                }
                if(et_id.getText().toString().equals("")||et_id.getText().toString()==null){
                    Toast.makeText(context, "아이디를 제대로 입력해주세요", Toast.LENGTH_SHORT).show();
                    return;
                }else{
                    sid=et_id.getText().toString();
                }
                if(fi1.getText().toString().equals("")||fi1.getText().toString()==null){
                    Toast.makeText(context, "년도를 제대로 입력해주세요", Toast.LENGTH_SHORT).show();
                    return;
                }
                if(fi2.getText().toString().equals("")||fi2.getText().toString()==null){
                    Toast.makeText(context, "월자를 제대로 입력해주세요", Toast.LENGTH_SHORT).show();
                    return;
                }
                String m=fi2.getText().toString();
                if(m.charAt(0)=='0'){
                  m= String.valueOf(m.charAt(1));
                }
                if(fi3.getText().toString().equals("")||fi3.getText().toString()==null){
                    Toast.makeText(context, "일자를 제대로 입력해주세요", Toast.LENGTH_SHORT).show();
                    return;
                }
                String sdate=fi1.getText().toString()+"-"+m+"-"+fi3.getText().toString();
                getPwd(sname,sid,sdate,select_s);


            }
        });
    }

    private void getPwd(String sname, String sid, String sdate, int sex) {
        Response.Listener<String> responseListener = new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    JSONObject jsonObject = new JSONObject( response );
                    boolean suc = jsonObject.getBoolean("success");
                    if(suc){
                        rpw=jsonObject.getString("password");
                        remail=jsonObject.getString("userEmail");
                        rpw=getBase64decode(rpw);
                        StrictMode.setThreadPolicy(new StrictMode.ThreadPolicy.Builder()
                                .permitDiskReads()
                                .permitDiskWrites()
                                .permitNetwork().build());
                        new SendMail().execute();
                    }else{
                        Toast.makeText(context, "비밀번호를 찾을수 없습니다.", Toast.LENGTH_SHORT).show();
                    }
                } catch (JSONException e) {
                    Log.e("getPInfo in showp","Connect Error");
                    e.printStackTrace();
                }
            }
        };
       GetPwdReq gpr = new GetPwdReq(sname,sid,sdate,sex,responseListener);
        RequestQueue queue = Volley.newRequestQueue(context);
        queue.add(gpr);
    }
    public static String getBase64decode(String content){
        return new String(Base64.decode(content, 0));
    }

    private class SendMail extends AsyncTask<Void,String,String> {
        ProgressDialog pd;
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            pd=ProgressDialog.show(context,"","메일을 전송중입니다...");
        }

        @Override
        protected String doInBackground(Void... voids) {
            try {
                GmailSender gMailSender = new GmailSender("cw03256@gmail.com", "+jh10309");
                gMailSender.sendMail("아이디어 마켓 비밀번호 안내", "회원님의 비밀번호는 ' " + rpw + " ' 입니다.", remail);
                return "Success";
            } catch (SendFailedException e) {
                Toast.makeText(context, "이메일 형식이 잘못되었습니다.", Toast.LENGTH_SHORT).show();
                return "Error E";
            } catch (MessagingException e) {
                e.printStackTrace();
                Toast.makeText(context, "인터넷 연결을 확인해주십시오", Toast.LENGTH_SHORT).show();
                return "Error I";
            } catch (Exception e) {
                e.printStackTrace();
                return "Error";
            }
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            pd.dismiss();
            if(s.equals("Success")){
                View dv=View.inflate(context,R.layout.find_pwd_result,null);
                AlertDialog.Builder dlg1=new AlertDialog.Builder(context);
                dlg1.setView(dv);
                dlg1.setNegativeButton("닫기", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        dialogInterface.dismiss();
                    }
                });
                dlg1.show();
                dlg.dismiss();
            }else if(s.equals("Error E")){
                Toast.makeText(context, "이메일 형식이 잘못되었습니다.", Toast.LENGTH_SHORT).show();
            }else if(s.equals("Error I")){
                Toast.makeText(context, "인터넷 연결을 확인해주십시오", Toast.LENGTH_SHORT).show();
            }else{
                Toast.makeText(context, "알수없는 오류", Toast.LENGTH_SHORT).show();
            }
        }
    }
}