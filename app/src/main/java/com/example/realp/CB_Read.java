package com.example.realp;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager.widget.ViewPager;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.toolbox.Volley;
import com.bumptech.glide.Glide;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;

import Project.MainActivity;

public class CB_Read extends AppCompatActivity {
    boolean isD=false;
    private int cNumber;
    private EditText et_that;
    private Button post;
    private ImageButton fave,back;
    public static String userID;
    public String cDate,cTitle,cWrite,tag,cView,patent,hPerson,cPlace;
    private TextView t_userID,t_cDate,t_cTitle,t_cWrite,t_tag,t_cView,t_patent,t_hPerson,t_cPlace,c_del;
    com.example.realp.imageSlideAdapter imageSlideAdapter;
    ArrayList<String> imageUrlList;
    ViewPager viewPager;
    CurUserInfo cui=new CurUserInfo();
    private RecyclerView recyclerView;
    private BB_CommentAdapter BBCommentAdapter;
    private ImageView proImage;
    String url;
    ArrayList<BB_CommentData> arrayList;
    BB_CommentData BBCommentData;
    ImageView civ_af;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.coboard_read);
        isD=false;
        civ_af=findViewById(R.id.civ_af);
        proImage=findViewById(R.id.cProImage);
        t_userID=findViewById(R.id.cUserId);
        t_cDate=findViewById(R.id.cDate);
        t_cTitle=findViewById(R.id.cReadTitle);
        t_cWrite=findViewById(R.id.cRead_content);
        t_tag=findViewById(R.id.cReadCategory);
        t_cView=findViewById(R.id.cvNum);
        t_patent=findViewById(R.id.cPatent);
        t_hPerson=findViewById(R.id.personReadBoard);
        t_cPlace=findViewById(R.id.cPlace);
        et_that=findViewById(R.id.et_thatC);
        viewPager=(ViewPager)findViewById(R.id.cReadViewPager);
        post=findViewById(R.id.postC);
        fave=findViewById(R.id.cStar);
        back=findViewById(R.id.backFromSet);
        c_del=findViewById(R.id.c_del);
        civ_af.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
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
                SimpleDateFormat sdf= new SimpleDateFormat("yyyy년 M월 dd일 HH시 mm분");
                Response.Listener<String> responseListener = new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        JSONObject jsonObject = null;
                        try {
                            jsonObject = new JSONObject( response );
                            boolean success = jsonObject.getBoolean( "success" );
                            if(success) {
                                fcm(t_userID.getText().toString(),sdf.format(calendar.getTime()));
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                };
                CurUserInfo cui=new CurUserInfo();
                AddFriendReq afr = new AddFriendReq(cui.getUserNick(),t_userID.getText().toString(),responseListener);
                RequestQueue queue = Volley.newRequestQueue(CB_Read.this);
                queue.add(afr);

            }
        });
        c_del.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AlertDialog.Builder builder=new AlertDialog.Builder(CB_Read.this).setTitle("게시글 삭제")
                        .setMessage("게시글을 정말 삭제하시겠습니까? \n삭제후에는 되돌릴 수 없습니다.");
                builder.setNegativeButton("아니오", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        dialogInterface.dismiss();
                    }
                });
                builder.setPositiveButton("예", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        getFileName(cNumber);
                        dialogInterface.dismiss();
                    }
                });
                builder.show();
            }
        });
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final String userID = cui.getUserNick();
                Intent intent =new Intent(CB_Read.this, MainActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                intent.putExtra("co",true);
                intent.putExtra("userID",userID);

                startActivity(intent);

            }
        });
        recyclerView = (RecyclerView)findViewById(R.id.rvC);

        RecyclerView.LayoutManager manager = new LinearLayoutManager(this);

        arrayList = new ArrayList<>();
        BBCommentAdapter = new BB_CommentAdapter(arrayList);
        recyclerView.setLayoutManager(manager);
        recyclerView.setAdapter(BBCommentAdapter);

        imageUrlList=new ArrayList<>();

        Intent intent = getIntent(); /*데이터 수신*/
        cNumber = intent.getExtras().getInt("cNumber");

        imageSlideAdapter = new imageSlideAdapter(this,imageUrlList);
        viewPager.setAdapter(imageSlideAdapter);
        favorites();

        fave.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                final String userID = cui.getUserNick();
                if (fave.getDrawable().getConstantState().equals(getResources().getDrawable(R.drawable.star).getConstantState())){
                    fave.setImageResource(R.drawable.yellowstar);
                    Response.Listener<String> responseListener = new Response.Listener<String>() {
                        @Override
                        public void onResponse(String response) {
                            try {
                                JSONObject jasonObject = new JSONObject(response);

                                boolean success = jasonObject.getBoolean("success");

                                if (success){

                                    Toast.makeText( getApplicationContext(), "즐겨찾기에 추가 되었습니다.", Toast.LENGTH_SHORT ).show();

                                }
                                else{
                                    Toast.makeText(getApplicationContext(),"즐겨찾기 등록 실패", Toast.LENGTH_SHORT).show();
                                    return;
                                }
                            }catch (JSONException e){
                                Toast.makeText(getApplicationContext(),"즐겨찾기 등록 실패", Toast.LENGTH_SHORT).show();
                                e.printStackTrace();
                            }
                        }
                    };

                    CB_FavoritesPlusRequest cb_favoritesPlusRequest = new CB_FavoritesPlusRequest(cNumber,userID,responseListener);
                    RequestQueue queue = Volley.newRequestQueue(CB_Read.this);
                    queue.add(cb_favoritesPlusRequest);

                }else
                {
                    fave.setImageResource(R.drawable.star);
                    Response.Listener<String> responseListener = new Response.Listener<String>() {
                        @Override
                        public void onResponse(String response) {
                            try {
                                JSONObject jasonObject = new JSONObject(response);

                                boolean success = jasonObject.getBoolean("success");

                                if (success){

                                    Toast.makeText( getApplicationContext(), "즐겨찾기에서 삭제 되었습니다.", Toast.LENGTH_SHORT ).show();

                                }
                                else{
                                    Toast.makeText(getApplicationContext(),"즐겨찾기 삭제 실패", Toast.LENGTH_SHORT).show();
                                    return;
                                }
                            }catch (JSONException e){
                                Toast.makeText(getApplicationContext(),"즐겨찾기 삭제 실패", Toast.LENGTH_SHORT).show();
                                e.printStackTrace();
                            }
                        }
                    };

                    CB_FavoritesDeleteRequest cb_favoritesdeleteRequest = new CB_FavoritesDeleteRequest(cNumber,userID,responseListener);
                    RequestQueue queue = Volley.newRequestQueue(CB_Read.this);
                    queue.add(cb_favoritesdeleteRequest);

                }
            }
        });
        post.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final String userID = cui.getUserNick();
                final String ceMemo = et_that.getText().toString();
                Response.Listener<String> responseListener = new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            JSONObject jasonObject = new JSONObject(response);

                            boolean success = jasonObject.getBoolean("success");

                            if (success){

                                Toast.makeText( getApplicationContext(), "댓글을 입력하였습니다", Toast.LENGTH_SHORT ).show();
                                Intent intent = getIntent();
                                finish();
                                startActivity(intent);

                            }
                            else{
                                Toast.makeText(getApplicationContext(),"댓글 등록 실패", Toast.LENGTH_SHORT).show();
                                return;
                            }
                        }catch (JSONException e){
                            Toast.makeText(getApplicationContext(),"댓글 등록 실패", Toast.LENGTH_SHORT).show();
                            e.printStackTrace();
                        }
                    }
                };

                CB_CommentWriteRequest CBCommentWriteRequest = new CB_CommentWriteRequest(cNumber,userID,ceMemo,responseListener);
                RequestQueue queue = Volley.newRequestQueue(CB_Read.this);
                queue.add(CBCommentWriteRequest);


            }
        });

        commentList();
        bViewPlus();
        imageLIst();
        readBoard();
        profileImage();
    }

    private void getFileName(int cNumber) {

        Response.Listener<String> responseListener = new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    JSONObject jsonObject = new JSONObject( response );
                    JSONArray ja = jsonObject.getJSONArray("File");
                    if(ja.length()==0){
                        isD=true;
                    }
                    for(int i=0;i<ja.length();i++){
                        JSONObject item = ja.getJSONObject(i);
                        String imageUrl=item.getString("imageUrl");
                        int b=imageUrl.lastIndexOf("/");
                        imageUrl=imageUrl.substring(b+1);
                        Log.e("imageUrl",imageUrl);
                        if(i+1==ja.length()){
                            isD=true;
                        }
                        rmFile(imageUrl);
                    }
                    if(isD){
                        deleteData(cNumber);
                    }
                } catch (JSONException e) {
                    Log.e("gfn in Cb","Connect Error");
                    e.printStackTrace();
                }
            }
        };
        GetCFileUrlReq gfur = new GetCFileUrlReq(cNumber,responseListener);
        RequestQueue queue = Volley.newRequestQueue(this);
        queue.add(gfur);
    }

    private void deleteData(int cNumber) {
        Response.Listener<String> responseListener = new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    JSONObject jsonObject = new JSONObject( response );
                    boolean suc = jsonObject.getBoolean("success");
                    Log.e("suc in deleteData",suc+"");
                    if(suc){
                        Intent intent = new Intent(CB_Read.this,MainActivity.class);
                        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                        intent.putExtra("co",true);
                        startActivity(intent);
                    }
                } catch (JSONException e) {
                    Log.e("deleteData in bb","Connect Error");
                    e.printStackTrace();
                }
            }
        };
        Log.e("cn",cNumber+"");
        DeleteCDataReq ddr = new DeleteCDataReq(cNumber,responseListener);
        RequestQueue queue = Volley.newRequestQueue(this);
        queue.add(ddr);
    }

    private void rmFile(String imageUrl) {
        Response.Listener<String> responseListener = new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    JSONObject jsonObject = new JSONObject( response );
                    boolean suc = jsonObject.getBoolean("success");
                    Log.e("suc in rmFile",suc+"");
                    if(suc){

                    }else{
                    //    rmFile(imageUrl);
                    }
                } catch (JSONException e) {
                    Log.e("rm in bb","Connect Error");
                    e.printStackTrace();
                }
            }
        };
        Log.e("send in rm",imageUrl);
        RmCFileReq rfr = new RmCFileReq(imageUrl,responseListener);
        RequestQueue queue = Volley.newRequestQueue(this);
        queue.add(rfr);
    }


    public void profileImage(){

        Response.Listener<String> responseListener2=new Response.Listener<String>() {

            @Override
            public void onResponse(String response) {

                try {
                    JSONObject jasonObject=new JSONObject(response);
                    boolean success=jasonObject.getBoolean("success");


                    if (success) {
                        String image = jasonObject.getString("userimage");
                        url ="http://teamwhi.dothome.co.kr/IdeaMarket/Login/uploads/"+image;

                        Glide.with(CB_Read.this).load(url).into(proImage);
                    }

                    else
                        Toast.makeText( getApplicationContext(), "프로필 이미지 없음", Toast.LENGTH_SHORT ).show();

                } catch (JSONException e) {
                    Toast.makeText( getApplicationContext(), "프로필 이미지 없음", Toast.LENGTH_SHORT ).show();
                    e.printStackTrace();
                }
            }
        };

        BB_ProImageRequest bb=new BB_ProImageRequest(userID,responseListener2);
        RequestQueue q= Volley.newRequestQueue(CB_Read.this);
        q.add(bb);
    }

    public void favorites(){
        final String userID = cui.getUserNick();
        Response.Listener<String> responseListener=new Response.Listener<String>() {

            @Override
            public void onResponse(String response) {
                try {
                    JSONObject jasonObject=new JSONObject(response);
                    boolean success=jasonObject.getBoolean("success");

                    if (success) {
                        fave.setImageResource(R.drawable.yellowstar);
                        int sNum = jasonObject.getInt("sNum");
                    }
                    else
                        fave.setImageResource(R.drawable.star);

                } catch (JSONException e) {
                    Toast.makeText( getApplicationContext(), "즐찾 실패", Toast.LENGTH_SHORT ).show();
                    e.printStackTrace();
                }
            }
        };
        CB_FavoritesSelectRequest cb_favoritesSelectRequest=new CB_FavoritesSelectRequest(cNumber,userID,responseListener);
        RequestQueue queue= Volley.newRequestQueue(CB_Read.this);
        queue.add(cb_favoritesSelectRequest);

    }
    public void commentList(){

        Response.Listener<String> responseListener = new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    JSONObject jsonObject = new JSONObject(response);
                    JSONArray ja = jsonObject.getJSONArray("Board");

                    Log.e("ja.length",""+ja.length());

                    for (int i = 0; i < ja.length(); i++) {
                        JSONObject item = ja.getJSONObject(i);

                        String ceWrite = item.getString("ceWrite");
                        String ceMemo = item.getString("ceMemo");
                        String ceDate = item.getString("ceDate");


                        if (ceWrite.equals(userID)){
                            ceWrite=""+ceWrite+" (작성자)";
                        }
                        BBCommentData = new BB_CommentData(ceWrite,ceMemo,ceDate);

                        arrayList.add(BBCommentData);

                        BBCommentAdapter.notifyDataSetChanged(); //추가후새로고침
                    }

                } catch (JSONException e) {
                }
            }

        };

        CB_CommentListRequest lpr = new CB_CommentListRequest(cNumber,responseListener);
        RequestQueue queue = Volley.newRequestQueue(CB_Read.this);
        queue.add( lpr );
    }

    public void bViewPlus(){

        Response.Listener<String> responseListener=new Response.Listener<String>() {

            @Override
            public void onResponse(String response) {
                try {
                    JSONObject jasonObject=new JSONObject(response);
                    boolean success=jasonObject.getBoolean("success");


                } catch (JSONException e) {
                    Toast.makeText( getApplicationContext(), "조회수 증가 실패", Toast.LENGTH_SHORT ).show();
                    e.printStackTrace();
                }
            }
        };
        cViewPlusRequest cViewPlusRequest=new cViewPlusRequest(cNumber,responseListener);
        RequestQueue queue= Volley.newRequestQueue(CB_Read.this);
        queue.add(cViewPlusRequest);

    }

    public void imageLIst(){

        Response.Listener<String> responseListener=new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    JSONObject jsonObject = new JSONObject(response);
                    JSONArray ja = jsonObject.getJSONArray("imageUrlList");
                    Log.e("ja.length",""+ja.length());

                    for (int i = 0; i < ja.length(); i++) {

                        JSONObject item = ja.getJSONObject(i);
                        String imageUrl = item.getString("imageUrl");


                        imageUrlList.add(imageUrl);
                        imageSlideAdapter.notifyDataSetChanged();

                    }
                } catch (JSONException e) {
                    Toast.makeText( getApplicationContext(), "등록된 사진 없음", Toast.LENGTH_SHORT ).show();
                    e.printStackTrace();
                }
            }
        };
        CB_ImageUrlListRequest cimageUrlListRequest=new CB_ImageUrlListRequest(cNumber,responseListener);
        RequestQueue queue= Volley.newRequestQueue(CB_Read.this);
        queue.add(cimageUrlListRequest);
    }

    public void readBoard(){


        Response.Listener<String> responseListener = new Response.Listener<String>() {

            @Override
            public void onResponse(String response) {
                try {

                    JSONObject jasonObject=new JSONObject(response);
                    boolean success=jasonObject.getBoolean("success");

                    if (success) {
                        userID=jasonObject.getString("userID");
                        cDate = jasonObject.getString("cDate");
                        cTitle = jasonObject.getString("cTitle");
                        cWrite = jasonObject.getString("cWrite");
                        tag = jasonObject.getString("cTag");
                        cView = jasonObject.getString("cView");
                        patent = jasonObject.getString("cPatent");
                        hPerson=jasonObject.getString("hPerson");
                        cPlace=jasonObject.getString("cPlace");

                        t_userID.setText(userID);
                        t_cDate.setText(cDate.substring(2,16));
                        t_cTitle.setText(cTitle);
                        t_cWrite.setText(cWrite);
                        t_tag.setText(tag);
                        t_cView.setText("조회수 "+cView);
                        t_patent.setText("특허등록"+patent);
                        t_hPerson.setText(hPerson);
                        t_cPlace.setText(cPlace);
                        if(userID.equals(cui.getUserNick())){
                            c_del.setVisibility(View.VISIBLE);
                        }
                        if(!cui.getUserNick().equals(t_userID.getText().toString())){
                            checkFriend();
                        }
                    }
                    else{
                        Toast.makeText(getApplicationContext(), "불러오기 실패", Toast.LENGTH_SHORT).show();
                        return;

                    }
                } catch (JSONException e) {
                    Toast.makeText( getApplicationContext(), "불러오기 실패2", Toast.LENGTH_SHORT ).show();
                    e.printStackTrace();
                }
            }
        };
        CB_ReadRequest CB_readRequest =new CB_ReadRequest(cNumber,responseListener);
        RequestQueue queue= Volley.newRequestQueue(CB_Read.this);
        queue.add(CB_readRequest);
    }

    private void checkFriend() {
        Response.Listener<String> responseListener = new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    JSONObject jasonObject = new JSONObject(response);
                    boolean success = jasonObject.getBoolean("success");
                    if (!success) {
                        civ_af.setVisibility(View.VISIBLE);
                    }
                }catch (JSONException e){
                    e.printStackTrace();
                }
            }
        };
        CheckFriendReq cfr = new CheckFriendReq(cui.getUserNick(),t_userID.getText().toString(),responseListener);
        RequestQueue queue = Volley.newRequestQueue(CB_Read.this);
        queue.add(cfr);
    }
    private void fcm(String reciever,String octime){
        Response.Listener<String> responseListener = new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                JSONObject jsonObject = null;
                try {
                    jsonObject = new JSONObject( response );
                    boolean success = jsonObject.getBoolean( "success" );
                    if(success) {
                        String msg = t_userID.getText().toString() + "님에게 친구신청이 완료되었습니다.";
                        Toast.makeText(CB_Read.this, msg, Toast.LENGTH_SHORT).show();
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        };
        CurUserInfo cui=new CurUserInfo();
        RequestSecReq rsr = new RequestSecReq(reciever,"친구 신청",cui.getUserNick()+"님이 친구신청을 보냈습니다.\n일시:"+octime,responseListener);
        RequestQueue queue = Volley.newRequestQueue(this);
        queue.add(rsr);
    }

    public void c_goPro(View view) {
        Intent i =new Intent(CB_Read.this,Profile.class);
        i.putExtra("nick",t_userID.getText().toString());
        startActivity(i);
    }
}
