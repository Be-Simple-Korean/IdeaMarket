package com.example.realp;

import android.app.Activity;
import android.app.AlarmManager;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.PendingIntent;
import android.app.TimePickerDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RadioGroup;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.toolbox.Volley;

import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.Calendar;

public class CustomFindIdDialog {
    private Context context;
    TextView fr_id;
    int select_s=1;
    Dialog dlg;
    public CustomFindIdDialog(Context context) {
        this.context = context;
    }

    // 호출할 다이얼로그 함수를 정의한다.
    public void callFunction() {
        // 커스텀 다이얼로그를 정의하기위해 Dialog클래스를 생성한다.
        dlg = new Dialog(context);
        // 액티비티의 타이틀바를 숨긴다.
        dlg.requestWindowFeature(Window.FEATURE_NO_TITLE);

        // 커스텀 다이얼로그의 레이아웃을 설정한다.
        dlg.setContentView(R.layout.activity_find_id);
        // 커스텀 다이얼로그를 노출한다.
        dlg.show();

        // 커스텀 다이얼로그의 각 위젯들을 정의한다.
        final EditText name=(EditText) dlg.findViewById(R.id.find_id_name);
        final EditText email=(EditText) dlg.findViewById(R.id.find_id_email);
        final EditText fi1=(EditText) dlg.findViewById(R.id.fi1);
        final EditText fi2=(EditText) dlg.findViewById(R.id.fi2);
        final EditText fi3=(EditText) dlg.findViewById(R.id.fi3);

        final RadioGroup rg=(RadioGroup)dlg.findViewById(R.id.fig);
        rg.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup radioGroup, int i) {
                if(i==R.id.fim){
                    select_s=1;
                }else{
                    select_s=2;
                }
            }
        });

        final TextView dlgNag=dlg.findViewById(R.id.fic);
        final TextView dlgPos=dlg.findViewById(R.id.fif);

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
                String semail="";
                if(name.getText().toString().equals("")||name.getText().toString()==null){
                    Toast.makeText(context, "이름을 제대로 입력해주세요", Toast.LENGTH_SHORT).show();
                    return;
                }else{
                    sname=name.getText().toString();
                }
                if(email.getText().toString().equals("")||email.getText().toString()==null){
                    Toast.makeText(context, "이메일을 제대로 입력해주세요", Toast.LENGTH_SHORT).show();
                    return;
                }else{
                    semail=email.getText().toString();
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
                getID(sname,semail,sdate,select_s);


            }
        });
    }

    private void getID(String sname, String semail, String sdate, int sex) {
        Response.Listener<String> responseListener = new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    JSONObject jsonObject = new JSONObject( response );
                    boolean suc = jsonObject.getBoolean("success");
                    if(suc){
                        dlg.dismiss();
                        String id=jsonObject.getString("userID");
                        Log.e("getID DATA",id);
                        View dv=View.inflate(context,R.layout.find_id_result,null);
                        AlertDialog.Builder dlg1=new AlertDialog.Builder(context);
                        dlg1.setView(dv);
                        fr_id=dv.findViewById(R.id.fr_id);
                        fr_id.setText("' "+id+" '");
                        dlg1.setNegativeButton("닫기", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                dialogInterface.dismiss();
                            }
                        });
                        dlg1.show();
                    }else{
                        Toast.makeText(context, "아이디를 찾을수 없습니다.", Toast.LENGTH_SHORT).show();
                    }
                } catch (JSONException e) {
                    Log.e("getPInfo in showp","Connect Error");
                    e.printStackTrace();
                }
            }
        };
       GetIdReq gir = new GetIdReq(sname,semail,sdate,sex,responseListener);
        RequestQueue queue = Volley.newRequestQueue(context);
        queue.add(gir);
    }
}