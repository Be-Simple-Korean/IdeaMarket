package Project;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.os.StrictMode;
import android.util.Log;
import android.view.View;
import android.webkit.MimeTypeMap;
import android.widget.ImageView;
import android.widget.TextView;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.toolbox.Volley;

import Project.Request.GetTeamDataList;
import Project.Request.InsertFileInfoReq;
import Project.Data.StorageData;
import Project.Request.makeStorageDirReq;

import com.example.realp.CurUserInfo;
import com.example.realp.R;
import com.nbsp.materialfilepicker.MaterialFilePicker;
import com.nbsp.materialfilepicker.ui.FilePickerActivity;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;

import okhttp3.FormBody;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;

public class ShowFileList extends AppCompatActivity {
    CurProjectinfo cpi=new CurProjectinfo();
   public static TextView tv_isNull;
    ImageView iv_upload,iv_strBack;
    public static ArrayList<String> far=new ArrayList<>();
    RecyclerView recyclerView;
   public static StorageAdapter storageAdapter;
    public static ArrayList<StorageData> sar;
    ProgressDialog progressDialog;
    LinearLayoutManager linearLayoutManager;
    CurUserInfo cui=new CurUserInfo();
    String path="";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_show_file_list);
       tv_isNull=findViewById(R.id.tv_isNull);
       iv_strBack=findViewById(R.id.iv_strBack);
       iv_strBack.setOnClickListener(new View.OnClickListener() {
           @Override
           public void onClick(View view) {
               finish();
           }
       });
        StrictMode.VmPolicy.Builder builder = new StrictMode.VmPolicy.Builder();
        StrictMode.setVmPolicy(builder.build());
        checkDir();
        iv_upload=findViewById(R.id.iv_upload);
        iv_upload.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                new MaterialFilePicker()
                        .withRootPath("/sdcard/")
                        .withActivity(ShowFileList.this)
                        .withRequestCode(10)
                        .start();
            }
        });
    }

    private void checkDir(){
        Response.Listener<String> responseListener = new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    JSONObject jsonObject = new JSONObject( response );
                    String suc = jsonObject.getString("suc");
                    if(suc.equals("폴더 체크 완료")){
                        path="Storage"+"/"+cpi.getProjectName()+"_"+cpi.getTeamName()+"_"+cpi.getTeamNo();
                        cpi.setCurPath(path);
                        getFileList();
                    }
                } catch (JSONException e) {
                    Log.e("CE0","Connect Error");
                    finish();
                    e.printStackTrace();
                }
            }
        };
        makeStorageDirReq msdr = new makeStorageDirReq(cpi.getProjectName(),cpi.getTeamName(),cpi.getTeamNo(),responseListener);
        RequestQueue queue = Volley.newRequestQueue(this);
        queue.add(msdr);
    }

    private void getFileList() {
        recyclerView=(RecyclerView)findViewById(R.id.showFileListView);
        linearLayoutManager=new LinearLayoutManager(this);
        recyclerView.setLayoutManager(linearLayoutManager);
        sar=new ArrayList<>();
        storageAdapter=new StorageAdapter(sar);
        recyclerView.setAdapter(storageAdapter);
        Response.Listener<String> responseListener = new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    JSONObject jsonObject = new JSONObject( response );
                    JSONArray ja = jsonObject.getJSONArray("TeamData");
                    if(ja.length()>0){
                        tv_isNull.setVisibility(View.INVISIBLE);
                        for(int i=0;i<ja.length();i++) {
                            JSONObject item = ja.getJSONObject(i);
                            String userNick = item.getString("userNick");
                            String fileName=item.getString("fileName");
                            StorageData sd=new StorageData(fileName,userNick,"열기","다운로드","삭제");
                            sar.add(sd);
                            storageAdapter.notifyDataSetChanged();
                        }
                    }else{
                        tv_isNull.setVisibility(View.VISIBLE);
                    }
                } catch (JSONException e) {
                    Log.e("gtdl in sfl","Connect Error");
                    tv_isNull.setVisibility(View.VISIBLE);
                    e.printStackTrace();
                }
            }
        };
        GetTeamDataList gtdl = new GetTeamDataList(cpi.getTeamNo(),responseListener);
        RequestQueue queue = Volley.newRequestQueue(this);
        queue.add(gtdl);

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable final Intent data) {
        if(requestCode==10&&resultCode==RESULT_OK){
            progressDialog=new ProgressDialog(ShowFileList.this);
            progressDialog.setTitle("Uploading");
            progressDialog.setMessage("Please wait..");
            progressDialog.show();
            Thread two= new Thread(new Runnable() {
                @Override
                public void run() {
                    File f=new File(data.getStringExtra(FilePickerActivity.RESULT_FILE_PATH));
                    String content_type= null; //파일형식
                    try {
                        content_type = getMimeType(f.getPath());
                    } catch (UnsupportedEncodingException e) {
                        e.printStackTrace();
                    }
                    String upload_path=path+"/";
                    String file_path=f.getAbsolutePath();
                    OkHttpClient client= new OkHttpClient();
                    RequestBody file_body= RequestBody.create(MediaType.parse(content_type),f);
                    RequestBody formBody = new FormBody.Builder()
                            .add("path", upload_path)
                            .build();
                    RequestBody requestBody = new MultipartBody.Builder()
                            .setType(MultipartBody.FORM)
                            .addPart(formBody)
                            .addFormDataPart("path",upload_path)
                            .addFormDataPart("type",content_type)
                            .addFormDataPart("uploaded_file",
                                    file_path.substring(file_path.lastIndexOf("/")+1),file_body) //파일명과 확장자만 추출해서 보냄
                            .build();
                    String addF=file_path.substring(file_path.lastIndexOf("/")+1);
                    Request request =new Request.Builder()
                            .url("http://teamwhi.dothome.co.kr/IdeaMarket/Project/uploadFile.php")
                            .post(formBody)
                            .post(requestBody)
                            .build();
                    try {
                        okhttp3.Response response = client.newCall(request).execute();
                        if(!response.isSuccessful()){
                            throw new IOException("Error: " +response);
                        }
                        progressDialog.dismiss();
                        insertFileInfo(addF);
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                StorageData sd=new StorageData(addF,cui.getUserNick(),"열기","다운로드","삭제");
                                sar.add(0,sd);
                                if(far.indexOf(addF)==-1){ //중복체크 indexof=특정문자를찾는다.
                                    far.add(addF);
                                }
                                storageAdapter.notifyDataSetChanged();
                                tv_isNull.setVisibility(View.INVISIBLE);
                            }
                        });
                    }catch (IOException e){
                        e.printStackTrace();
                    }
                }
            });
            two.start();
        }
        super.onActivityResult(requestCode, resultCode, data);
    }

    private void insertFileInfo(String addF) {
        Response.Listener<String> responseListener = new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    JSONObject jsonObject = new JSONObject( response );
                    boolean suc = jsonObject.getBoolean("success");
                    if(!suc){
                        insertFileInfo(addF);
                    }
                } catch (JSONException e) {
                    Log.e("if in sfl","Connect Error");
                    e.printStackTrace();
                }
            }
        };
        SimpleDateFormat df=new SimpleDateFormat("yyyy년 MM월 dd일 HH시 mm분 ss초");
        Calendar c = Calendar.getInstance();
        String cd=df.format(c.getTime());
        InsertFileInfoReq ifir = new InsertFileInfoReq(cpi.getTeamNo(),cui.getUserNick(),addF,cd,responseListener);
        RequestQueue queue = Volley.newRequestQueue(this);
        queue.add(ifir);
    }

    private String getMimeType(String path) throws UnsupportedEncodingException {
        path= URLEncoder.encode(path,"UTF-8");
        String extension= MimeTypeMap.getFileExtensionFromUrl(path.replace("+", "%20")); //한글처리
        return  MimeTypeMap.getSingleton().getMimeTypeFromExtension(extension);
    }

}
