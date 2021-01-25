package com.example.realp;

import android.content.res.Resources;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.toolbox.Volley;
import com.example.realp.common.MyApplication;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.util.ArrayList;

import android.util.Base64;
public class Editor_profile extends AppCompatActivity {
    EditText editID,editPwd,editPok,editNick,editPnum,editName,editSelf,editEmail;
  CurUserInfo cui=new CurUserInfo();
    Spinner edit_ys,edit_ms,edit_ds;
    private String work = "공무원";
    private Spinner work_spinner = null;
    private ArrayAdapter mArrayYearAdapter = null;
    private ArrayAdapter mArrayMonthAdapter = null;
    private ArrayAdapter mArrayDayAdapter = null;
    private ArrayAdapter mArrayWorkAdapter = null;
    String mYear="1950",mMonth="1",mDay="1";
    int mSex=1;
    private RadioGroup radioGroup = null;
    ArrayList<String> yar;
    ArrayList<String> mar;
    ArrayList<String> dar;
    ArrayList<String> jar;
    ArrayList<Integer> iar;
    String gid,gpwd,gname,gnickname,gemail,gbirth,gpnum,gself,gjob;
    int gsex;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.edit_selfinfo);
        jar=new ArrayList<>();
        dar=new ArrayList<>();
        mar=new ArrayList<>();
        yar=new ArrayList<>();
        iar=new ArrayList<>();
        work_spinner = findViewById(R.id.edit_work_spinner);
        editID=findViewById(R.id.edit_ID);
        editPwd=findViewById(R.id.edit_password);
        editPok=findViewById(R.id.edit_pass_ok);
        editNick=findViewById(R.id.edit_nickname);
        editPnum=findViewById(R.id.edit_pnum);
        editName=findViewById(R.id.edit_name);
        editSelf=findViewById(R.id.edit_Comment);
        editEmail=findViewById(R.id.edit_Email);
        radioGroup = findViewById(R.id.radioGroup);
        edit_ys=findViewById(R.id.edit_year_spinner);
        edit_ms=findViewById(R.id.edit_month_spinner);
        edit_ds=findViewById(R.id.edit_day_spinner);
        radioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                if (checkedId == R.id.mail_radio) {
                    mSex = 1;
                } else if (checkedId == R.id.femail_radio) {
                    mSex = 2;
                }
            }
        });
        mArrayYearAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_dropdown_item,yar);
        for (int i = 1950; i <= 2010; i++) {
            yar.add(Integer.toString(i));
        }
        edit_ys.setAdapter(mArrayYearAdapter);
        edit_ys.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                mYear =(String)edit_ys.getSelectedItem();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
        mArrayMonthAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_dropdown_item,mar);
        for (int i = 1; i <= 12; i++) {
            mar.add(Integer.toString(i));
        }
        edit_ms.setAdapter(mArrayMonthAdapter);
        edit_ms.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                mMonth = (String)edit_ms.getSelectedItem();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
        mArrayDayAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_dropdown_item,dar);

        for (int i = 1; i <= 31; i++) {
            dar.add(Integer.toString(i));
        }
        edit_ds.setAdapter(mArrayDayAdapter);
        edit_ds.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                mDay = (String)edit_ds.getSelectedItem();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
        Resources res = getResources();
        String[] arrString = res.getStringArray(R.array.my_array);
        for(String s:arrString){
            jar.add(s);
        }
        mArrayWorkAdapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_dropdown_item, jar);
        work_spinner.setAdapter(mArrayWorkAdapter);
        work_spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                work = jar.get(position);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
        getInfo();
    }
    public static String getBase64decode(String content){
        return new String(Base64.decode(content, 0));
    }


    private void getInfo() {
        Response.Listener<String> responseListener = new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    JSONObject jsonObject = new JSONObject( response );
                    Boolean suc=jsonObject.getBoolean("success");
                    if(suc){
                        gid=jsonObject.getString("id");
                        gpwd=jsonObject.getString("pwd");
                        gname=jsonObject.getString("name");
                        gnickname=jsonObject.getString("nickname");
                        gemail=jsonObject.getString("email");
                        gbirth=jsonObject.getString("birth");
                        gpnum=jsonObject.getString("pnum");
                        gself=jsonObject.getString("self");
                        gsex=jsonObject.getInt("sex");
                        gjob=jsonObject.getString("job");
                        if(gsex==1){
                            RadioButton rb=findViewById(R.id.mail_radio);
                            rb.setChecked(true);
                            mSex=1;
                        }else{
                            RadioButton rb=findViewById(R.id.femail_radio);
                            rb.setChecked(true);
                            mSex=2;
                        }
                        gpwd=getBase64decode(gpwd);
                        editID.setText(gid);
                        editPwd.setText(gpwd);
                        editPok.setText(gpwd);
                        editName.setText(gname);
                        editNick.setText(gnickname);
                        editEmail.setText(gemail);
                        editPnum.setText(gpnum);
                        editSelf.setText(gself);
                        String[] b=gbirth.split("-");
                        work_spinner.setSelection(jar.indexOf(gjob));
                        edit_ys.setSelection(yar.indexOf(b[0]));
                        edit_ms.setSelection(mar.indexOf(b[1]));
                        edit_ds.setSelection(dar.indexOf(b[2]));
                    }
                } catch (JSONException e) {
                    Log.e("frag3 listpost","Connect Error");
                    e.printStackTrace();
                }
            }
        };

        getUserInfoReq guir = new getUserInfoReq(cui.getUserNick(),responseListener);
        RequestQueue queue = Volley.newRequestQueue(this);
        queue.add( guir);
    }

    public void edit_back(View view) {
        finish();
    }
    String date="";

    public void updateSql(View view) {
        if(!gid.equals(editID.getText().toString())&&!editID.getText().toString().equals("")){
            iar.add(1);
        }
        if(!gpwd.equals(editPwd.getText().toString())&&!editPwd.getText().toString().equals("")&&editPwd.getText().toString().equals(editPok.getText().toString())){
            iar.add(2);
        }
        if(!gnickname.equals(editNick.getText().toString())&&!editNick.getText().toString().equals("")){
            iar.add(3);
        }
        if(!gpnum.equals(editPnum.getText().toString())&&!editPnum.getText().toString().equals("")){
            iar.add(4);
        }
        if(!gname.equals(editName.getText().toString())&&!editName.getText().toString().equals("")){
            iar.add(5);
        }
        date=mYear+"-"+mMonth+"-"+mDay;
        if(!gbirth.equals(date)){
            iar.add(6);
        }
        if(gsex!=mSex){
            iar.add(7);
        }
        if(!gemail.equals(editEmail.getText().toString())&&!editEmail.getText().toString().equals("")){
            iar.add(8);
        }
        if(!gjob.equals(work)){
            iar.add(9);
        }
        if(!gself.equals(editSelf.getText().toString())&&!editSelf.getText().toString().equals("")){
            iar.add(10);
        }
        for(int i=0;i<iar.size();i++){
            updateData(iar.get(i));
        }
    }

    private void updateData(int i) {
        String sql="";
        switch (i){
            case 1:
                sql="update User set userID='"+editID.getText().toString()+"' where id='"+gid+"' and nickname='"+cui.getUserNick()+"';";
                break;
            case 2:
                sql="update User set password='"+editPwd.getText().toString()+"' where password='"+gpwd+"' and nickname='"+cui.getUserNick()+"';";
                break;
            case 3:
                sql="update User set nickname='"+editNick.getText().toString()+"' where nickname='"+gnickname+"';";
                break;
            case 4:
                sql="update User set userPnum='"+editPnum.getText().toString()+"' where userPnum='"+gpnum+"' and nickname='"+cui.getUserNick()+"';";
                break;
            case 5:
                sql="update User set userName='"+editName.getText().toString()+"' where userName='"+gname+"' and nickname='"+cui.getUserNick()+"';";
                break;
            case 6:
                sql="update User set birthday='"+date+"' where birthday='"+gbirth+"' and nickname='"+cui.getUserNick()+"';";
                break;
            case 7:
                sql="update User set userSex="+mSex+" where userSex='"+gsex+"' and nickname='"+cui.getUserNick()+"';";
                break;
            case 8:
                sql="update User set userEmail='"+editEmail.getText().toString()+"' where userEmail='"+gemail+"' and nickname='"+cui.getUserNick()+"';";
                break;
            case 9:
                sql="update User set userJob='"+work+"' where userJob='"+gjob+"' and nickname='"+cui.getUserNick()+"';";
                break;
            case 10:
                sql="update User set userSelf='"+editSelf.getText().toString()+"' where userSelf='"+gself+"' and nickname='"+cui.getUserNick()+"';";
                break;
            default:
                return;
        }
        Response.Listener<String> responseListener = new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    JSONObject jsonObject = new JSONObject(response);
                    Boolean suc=jsonObject.getBoolean("success");
                    if(suc){
                        if(i==3){
                            cui.setUserNick(editNick.getText().toString());
                        }
                        if(iar.get(iar.size()-1)==i){
                            Toast.makeText(Editor_profile.this, "정보수정 완료", Toast.LENGTH_SHORT).show();
                            iar.clear();
                            finish();
                        }
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        };
        UpdateUserInfoReq uuir = new UpdateUserInfoReq(sql,responseListener);
        RequestQueue queue = Volley.newRequestQueue(this);
        queue.add(uuir);
    }
}

