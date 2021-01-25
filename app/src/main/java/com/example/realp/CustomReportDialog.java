package com.example.realp;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.toolbox.Volley;

import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.Calendar;

public class CustomReportDialog {
    private Context context;
    String sus;
    Dialog dlg;
    String curTag="";
    CurUserInfo cui=new CurUserInfo();
    public CustomReportDialog(Context context,String sus) {
        this.context = context;
        this.sus=sus;
    }

    // 호출할 다이얼로그 함수를 정의한다.
    public void callFunction() {
        // 커스텀 다이얼로그를 정의하기위해 Dialog클래스를 생성한다.
        dlg = new Dialog(context);
        // 액티비티의 타이틀바를 숨긴다.
        dlg.requestWindowFeature(Window.FEATURE_NO_TITLE);

        // 커스텀 다이얼로그의 레이아웃을 설정한다.
        dlg.setContentView(R.layout.activity_report);
        // 커스텀 다이얼로그를 노출한다.
        dlg.show();

        // 커스텀 다이얼로그의 각 위젯들을 정의한다.
        final Spinner spin=(Spinner)dlg.findViewById(R.id.rep_s);
        final TextView rtime=(TextView)dlg.findViewById(R.id.rep_ctime);
        final TextView tv_sus=(TextView)dlg.findViewById(R.id.rep_sus);
        final EditText con=(EditText)dlg.findViewById(R.id.rep_con);
        final TextView dlgNag=dlg.findViewById(R.id.rep_c);
        final TextView dlgPos=dlg.findViewById(R.id.rep_f);

       spin.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                curTag= String.valueOf(adapterView.getItemAtPosition(i));

            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {
                    curTag= String.valueOf(adapterView.getItemAtPosition(0));
            }
        });
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
        SimpleDateFormat sdf= new SimpleDateFormat("yyyy년 M월 dd일");
        String time=sdf.format(calendar.getTime());
        rtime.setText(time);
        tv_sus.setText(sus);


      dlgNag.setOnClickListener(new View.OnClickListener() {
          @Override
          public void onClick(View view) {
              dlg.dismiss();
          }
      });
        dlgPos.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String content=con.getText().toString();
                if(content.equals("")){
                    Toast.makeText(context, "내용을 입력해주세요.", Toast.LENGTH_SHORT).show();
                    return;
                }
                Response.Listener<String> responseListener = new Response.Listener<String>() {
                    public void onResponse(String response) {
                        try {
                            JSONObject jsonObject = new JSONObject( response );
                            boolean suc = jsonObject.getBoolean("success");
                            if(suc) {
                                Toast.makeText(context, "신고가 접수되었습니다.", Toast.LENGTH_SHORT).show();
                                dlg.dismiss();
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                };
                RecieveReportReq rrr = new RecieveReportReq(curTag,cui.getUserNick(),sus,content,time,responseListener);
                RequestQueue queue = Volley.newRequestQueue(context);
                queue.add(rrr);

            }
        });
    }


}