package com.example.realp;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.toolbox.Volley;

import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.Calendar;

public class Feedback_List extends AppCompatActivity {
    EditText editText;
    EditText editText1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.feedback);

        editText = findViewById(R.id.feedback_title);
        editText1 = findViewById(R.id.feedback_content);

        Button button = findViewById(R.id.feedback_send);
        Button button1 = findViewById(R.id.feedback_cancel);

        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sendMail();
            }
        });
        button1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

    private void sendMail(){
        String title =  editText.getText().toString();
        if(title.equals("")){
            Toast.makeText(this, "제목을 입력해주세요", Toast.LENGTH_SHORT).show();
        }
        String content = editText1.getText().toString();
        if(content.equals("")){
            Toast.makeText(this, "내용을 입력해주세요", Toast.LENGTH_SHORT).show();
        }
        Calendar cal=Calendar.getInstance();
        cal.set(Calendar.YEAR , cal.get(Calendar.YEAR));
        cal.set(Calendar.MONTH , cal.get(Calendar.MONTH)+1);
        cal.set(Calendar.DATE , cal.get(Calendar.DATE));
        cal.set(Calendar.HOUR_OF_DAY , cal.get(Calendar.HOUR));
        cal.set(Calendar.MINUTE , cal.get(Calendar.MINUTE));
        cal.set(Calendar.SECOND , cal.get(Calendar.SECOND));
        SimpleDateFormat s =new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        Response.Listener<String> responseListener = new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    JSONObject jsonObject = new JSONObject( response );
                    Boolean suc=jsonObject.getBoolean("success");
                    if(suc){
                        Log.e("앜","앜");
                        Toast.makeText(Feedback_List.this, "관리자에게 전송되었습니다", Toast.LENGTH_SHORT).show();
                        finish();
                    }
                } catch (JSONException e) {
                    Log.e("frag3 listpost","Connect Error");
                    e.printStackTrace();
                }
            }
        };
        CurUserInfo cui=new CurUserInfo();
        Log.e("send feedback",cui.getUserNick()+"/"+title+"/"+content+"/"+s.format(cal.getTime()));
        insertFeedBackReq guir = new insertFeedBackReq(cui.getUserNick(),title,content,s.format(cal.getTime()),responseListener);
        RequestQueue queue = Volley.newRequestQueue(this);
        queue.add( guir);

    }
}
