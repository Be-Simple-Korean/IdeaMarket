package com.example.realp;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Build;
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

import Chatting.Chating;
import Chatting.Request.InsertChatRoomReq;
import Chatting.Request.InsertChatUserReq;
import Project.MainActivity;


public class BB_Read extends AppCompatActivity {
   private int bNumber,bView;
   private EditText et_that;
   private Button post;
   private ImageButton fave,back;
    private ImageView proImage;
   public String bDate,bTitle,bWrite,tag,patent,price;
   private TextView t_userID,t_bDate,t_bTitle,t_bWrite,t_tag,t_bView,t_patent,t_price,b_del;
   ImageView iv_af;
   com.example.realp.imageSlideAdapter imageSlideAdapter;
   ArrayList<String> imageUrlList;
   ViewPager viewPager;
    CurUserInfo cui=new CurUserInfo();
    private RecyclerView recyclerView;
    private BB_CommentAdapter BBCommentAdapter;
    boolean isD=false;
    ArrayList<BB_CommentData> arrayList;
    BB_CommentData BBCommentData;
    String url="";
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.buyboard_read);
        isD=false;
        proImage=findViewById(R.id.proImage);
        t_userID=findViewById(R.id.userId);
        t_bDate=findViewById(R.id.bDate);
        t_bTitle=findViewById(R.id.ideaTitle);
        t_bWrite=findViewById(R.id.read_content);
        t_tag=findViewById(R.id.read_category);
        t_bView=findViewById(R.id.vNum);
        t_patent=findViewById(R.id.patent);
        t_price=findViewById(R.id.priceReadBoard);
        et_that=findViewById(R.id.et_that);
        post=findViewById(R.id.post);
        fave=findViewById(R.id.fav);
        b_del=findViewById(R.id.b_del);
        viewPager=(ViewPager)findViewById(R.id.ReadViewPager);
        back=(ImageButton)findViewById(R.id.backFromSet);
        iv_af=findViewById(R.id.biv_addFriend);
        iv_af.setOnClickListener(new View.OnClickListener() {
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
                RequestQueue queue = Volley.newRequestQueue(BB_Read.this);
                queue.add(afr);
            }
        });
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final String userID = cui.getUserNick();
                Intent intent =new Intent(BB_Read.this, MainActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                intent.putExtra("buy",true);
                intent.putExtra("userID",userID);
                startActivity(intent);

            }
        });
        recyclerView = (RecyclerView)findViewById(R.id.rvComment);
        RecyclerView.LayoutManager manager = new LinearLayoutManager(this);
        arrayList = new ArrayList<>();
        BBCommentAdapter = new BB_CommentAdapter(arrayList);
        recyclerView.setLayoutManager(manager);
        recyclerView.setAdapter(BBCommentAdapter);
        imageUrlList=new ArrayList<>();
        Intent intent = getIntent(); /*데이터 수신*/
        bNumber = intent.getExtras().getInt("bNumber");
        b_del.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AlertDialog.Builder builder=new AlertDialog.Builder(BB_Read.this).setTitle("게시글 삭제")
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
                        getFileName(bNumber);
                        dialogInterface.dismiss();
                    }
                });
                builder.show();
            }
        });
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
                            }
                        }
                    };

                    BB_FavoritesPlusRequest bb_favoritesPlusRequest = new BB_FavoritesPlusRequest(bNumber,userID,responseListener);
                    RequestQueue queue = Volley.newRequestQueue(BB_Read.this);
                    queue.add(bb_favoritesPlusRequest);

                }else
                {
                    fave.setImageResource(R.drawable.star);
                    Response.Listener<String> responseListener = new Response.Listener<String>() {
                        @Override
                        public void onResponse(String response) {
                            try {
                                JSONObject jasonObject = new JSONObject(response);
                                boolean success = jasonObject.getBoolean("success");
                                if (success) {
                                    Toast.makeText(getApplicationContext(), "즐겨찾기에서 삭제 되었습니다.", Toast.LENGTH_SHORT).show();
                                } else {
                                    Toast.makeText(getApplicationContext(), "즐겨찾기 삭제 실패", Toast.LENGTH_SHORT).show();
                                    return;
                                }
                            } catch (JSONException e) {
                                Toast.makeText(getApplicationContext(), "즐겨찾기 삭제 실패", Toast.LENGTH_SHORT).show();
                            }
                        }
                    };
                    BB_FavoritesDeleteRequest bb_favoritesdeleteRequest = new BB_FavoritesDeleteRequest(bNumber, userID, responseListener);
                    RequestQueue queue = Volley.newRequestQueue(BB_Read.this);
                    queue.add(bb_favoritesdeleteRequest);

                }
            }
        });
        post.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final String userID = cui.getUserNick();
                final String reMemo = et_that.getText().toString();
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
                        }
                    }
                };
                BB_CommentWriteRequest BBCommentWriteRequest = new BB_CommentWriteRequest(bNumber,userID,reMemo,responseListener);
                RequestQueue queue = Volley.newRequestQueue(BB_Read.this);
                queue.add(BBCommentWriteRequest);
            }
        });

        bViewPlus();
        imageLIst();
        readBoard();
        commentList();
        profileImage();
    }

    private void checkFriend() {
        Response.Listener<String> responseListener = new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    JSONObject jasonObject = new JSONObject(response);
                    boolean success = jasonObject.getBoolean("success");
                    if (!success) {
                        iv_af.setVisibility(View.VISIBLE);
                    }
                }catch (JSONException e){
                    e.printStackTrace();
                }
            }
        };
        CheckFriendReq cfr = new CheckFriendReq(cui.getUserNick(),t_userID.getText().toString(),responseListener);
        RequestQueue queue = Volley.newRequestQueue(BB_Read.this);
        queue.add(cfr);
    }

    private void getFileName(int bNumber) {

        Response.Listener<String> responseListener = new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    JSONObject jsonObject = new JSONObject( response );
                    JSONArray ja = jsonObject.getJSONArray("File");
                    if(ja.length()==0){isD=true;}
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
                        deleteData(bNumber);
                    }
                } catch (JSONException e) {
                    Log.e("gfn in bb","Connect Error");

                }
            }
        };
        GetFileUrlReq gfur = new GetFileUrlReq(bNumber,responseListener);
        RequestQueue queue = Volley.newRequestQueue(this);
        queue.add(gfur);
    }

    private void deleteData(int bNumber) {
        Response.Listener<String> responseListener = new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    JSONObject jsonObject = new JSONObject( response );
                    boolean suc = jsonObject.getBoolean("success");
                    Log.e("suc in deleteData",suc+"");
                    if(suc){
                        Intent intent = new Intent(BB_Read.this,MainActivity.class);
                        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                        intent.putExtra("buy",true);
                        startActivity(intent);
                    }
                } catch (JSONException e) {
                    Log.e("deleteData in bb","Connect Error");
                    e.printStackTrace();
                }
            }
        };
        DeleteDataReq ddr = new DeleteDataReq(bNumber,responseListener);
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
                        rmFile(imageUrl);
                    }
                } catch (JSONException e) {
                    Log.e("rm in bb","Connect Error");
                    e.printStackTrace();
                }
            }
        };
        Log.e("send in rm",imageUrl);
        RmFileReq rfr = new RmFileReq(imageUrl,responseListener);
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
                        Glide.with(BB_Read.this).load(url).into(proImage);
                    }
                    else {
                        Toast.makeText(getApplicationContext(), "프로필 이미지 없음", Toast.LENGTH_SHORT).show();
                    }
                } catch (JSONException e) {
                    Toast.makeText( getApplicationContext(), "프로필 이미지 없음", Toast.LENGTH_SHORT ).show();

                }
            }
        };
        BB_ProImageRequest bb=new BB_ProImageRequest(cui.getUserNick(),responseListener2);
        RequestQueue q= Volley.newRequestQueue(BB_Read.this);
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
                        int bsNum = jasonObject.getInt("bsNum");
                    }
                    else
                        fave.setImageResource(R.drawable.star);

                } catch (JSONException e) {
                    Toast.makeText( getApplicationContext(), "즐찾 실패", Toast.LENGTH_SHORT ).show();
                }
            }
        };
        BB_FavoritesSelectRequest bb_favoritesSelectRequest=new BB_FavoritesSelectRequest(bNumber,userID,responseListener);
        RequestQueue queue= Volley.newRequestQueue(BB_Read.this);
        queue.add(bb_favoritesSelectRequest);

    }

    public void commentList(){
        Response.Listener<String> responseListener = new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    JSONObject jsonObject = new JSONObject(response);
                    JSONArray ja = jsonObject.getJSONArray("BoardC");
                    if(ja.length()!=0){
                        for (int i = 0; i < ja.length(); i++) {
                            JSONObject item = ja.getJSONObject(i);
                            String reWrite = item.getString("reWrite");
                            String reMemo = item.getString("reMemo");
                            String reDate = item.getString("reDate");

                            if (reWrite.equals(t_userID.getText().toString())){
                                reWrite=" "+reWrite+" (작성자)";
                            }

                            BBCommentData = new BB_CommentData(reWrite,reMemo,reDate);

                            arrayList.add(BBCommentData);

                            BBCommentAdapter.notifyDataSetChanged(); //추가후새로고침
                        }
                    }
                } catch (JSONException e) {
                }
            }
        };
        BB_CommentListRequest lpr = new BB_CommentListRequest(bNumber,responseListener);
        RequestQueue queue = Volley.newRequestQueue(BB_Read.this);
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

            }
        }
    };
    bViewPlusRequest bViewPlusRequest=new bViewPlusRequest(bNumber,responseListener);
    RequestQueue queue= Volley.newRequestQueue(BB_Read.this);
    queue.add(bViewPlusRequest);

}

    public void imageLIst(){
        Response.Listener<String> responseListener=new Response.Listener<String>() {

            @Override
            public void onResponse(String response) {
                try {
                    JSONObject jsonObject = new JSONObject(response);
                    JSONArray ja = jsonObject.getJSONArray("imageUrlList");
                    for (int i = 0; i < ja.length(); i++) {
                        JSONObject item = ja.getJSONObject(i);

                        String imageUrl = item.getString("imageUrl");

                        imageUrlList.add(imageUrl);

                        imageSlideAdapter.notifyDataSetChanged();

                    }
                } catch (JSONException e) {
                    Toast.makeText( getApplicationContext(), "등록된 사진 없음", Toast.LENGTH_SHORT ).show();
                }
            }
        };
        BB_ImageUrlListRequest BBImageUrlListRequest =new BB_ImageUrlListRequest(bNumber,responseListener);
        RequestQueue queue= Volley.newRequestQueue(BB_Read.this);
        queue.add(BBImageUrlListRequest);
    }


    public void readBoard(){
        Response.Listener<String> responseListener=new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    JSONObject jasonObject=new JSONObject(response);
                    boolean success=jasonObject.getBoolean("success");
                    if (success) {
                        String userID=jasonObject.getString("userID");
                        bDate = jasonObject.getString("bDate");
                        bTitle = jasonObject.getString("bTitle");
                        bWrite = jasonObject.getString("bWrite");
                        tag = jasonObject.getString("tag");
                        bView = jasonObject.getInt("bView");
                        patent = jasonObject.getString("patent");
                        price=jasonObject.getString("price");
                        t_userID.setText(userID);
                        t_bDate.setText(bDate.substring(2,16));
                        t_bTitle.setText(bTitle);
                        t_bWrite.setText(bWrite);
                        t_tag.setText("카테고리 "+tag);
                        t_bView.setText("조회수 "+bView);
                        t_patent.setText("특허등록 "+patent);
                        t_price.setText(price);
                        if(userID.equals(cui.getUserNick())){
                            b_del.setVisibility(View.VISIBLE);
                        }
                        if(!cui.getUserNick().equals(t_userID.getText().toString())){
                            checkFriend();
                        }
                    }
                    else{
                        Toast.makeText(getApplicationContext(), "불러오기 실패", Toast.LENGTH_SHORT).show();
                        finish();
                        return;

                    }
                } catch (JSONException e) {
                    Toast.makeText( getApplicationContext(), "불러오기 실패2", Toast.LENGTH_SHORT ).show();
                    finish();

                }
            }
        };
        BB_ReadRequest BB_readRequest =new BB_ReadRequest(bNumber,responseListener);
        RequestQueue queue= Volley.newRequestQueue(BB_Read.this);
        queue.add(BB_readRequest);
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
                        String msg= jsonObject.getString("msg");
                        Log.e("msg",msg+"");
                        String msg1 = t_userID.getText().toString() + "님에게 친구신청이 완료되었습니다.";
                        Toast.makeText(BB_Read.this, msg1, Toast.LENGTH_SHORT).show();
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

    public void conChat(View view) {
        String user1=cui.getUserNick();
        String user2=t_userID.getText().toString();
        ArrayList<String> a=new ArrayList();
        a.add(user1);
        a.add(user2);
        Log.e("a0",a.get(0));
        Log.e("a1",a.get(1));
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            a.sort(null);
            Log.e("sort","수행");
            Log.e("a0",a.get(0));
            Log.e("a1",a.get(1));
        }

        Response.Listener<String> responseListener = new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    JSONObject jsonObject = new JSONObject( response );
                    boolean suc = jsonObject.getBoolean("success");
                    Log.e("suc in bb",suc+"");
                    if(suc){
                        int chatNo=jsonObject.getInt("chatNo");
                        Log.e("chatNo in bb",chatNo+"");
                        String nick1=jsonObject.getString("nick1");
                        String nick2=jsonObject.getString("nick2");
                        Log.e("getData",nick1+"/"+nick2);
                        ArrayList nar=new ArrayList();
                        String people="";
                        nar.add(nick1);
                        nar.add(nick2);
                        for(int i=0;i<nar.size();i++){
                            if(!cui.getUserNick().equals(nar.get(i))){
                                people=people+nar.get(i)+",";
                            }
                        }
                        /*JSONArray ja = jsonObject.getJSONArray("ChatInfo");
                        Log.e("ja in bb",ja.length()+"");
                        ArrayList nar;
                        if(ja.length()==0){
                            return;
                        }else{
                            nar=new ArrayList();
                        }
                        String people="";
                        for(int i=0;i<ja.length();i++){
                            JSONObject item = ja.getJSONObject(i);
                            String nick=item.getString("nickname");
                            Log.e("nick in bb",nick);
                            nar.add(nick);
                            if(!cui.getUserNick().equals(nick)){
                                people=people+nick+",";
                            }
                        }*/
                        if(people.charAt((people.length()-1))==',') {
                            people=people.substring(0,people.length()-1);
                        }
                        Log.e("people",people);
                        Intent i = new Intent(BB_Read.this, Chating.class);
                        i.putExtra("user",people);
                        i.putExtra("no",chatNo);
                        startActivity(i);
                    }
                    else{ //채팅방 생성
                        insertChatRoomData();
                    }
                } catch (JSONException e) {
                    Log.e("frag3 listpost","Connect Error");
                    e.printStackTrace();
                }
            }
        };
        CheckChatReq ccr = new CheckChatReq(a.get(0),a.get(1),responseListener);
        RequestQueue queue = Volley.newRequestQueue( this);
        queue.add(ccr);
    }
    private void insertChatRoomData() {
        Response.Listener<String> responseListener = new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    JSONObject jsonObject = new JSONObject( response );
                    Boolean suc=jsonObject.getBoolean("success");
                    if(suc){
                        int no=jsonObject.getInt("chatNo");
                        insertChatUser(no);
                    }else{
                        Toast.makeText(BB_Read.this, "다시 시도해주세요", Toast.LENGTH_SHORT).show();
                    }
                } catch (JSONException e) {
                    Log.e("frag3 listpost","Connect Error");
                    e.printStackTrace();
                }
            }
        };
        InsertChatRoomReq icrr = new InsertChatRoomReq(2,responseListener);
        RequestQueue queue = Volley.newRequestQueue( this);
        queue.add(icrr);
    }
    private void insertChatUser(int no) {
        String other=t_userID.getText().toString();
        for(int i=0;i<2;i++){
            int finalI = i;
            Response.Listener<String> responseListener = new Response.Listener<String>() {
                @Override
                public void onResponse(String response) {
                    try {
                        JSONObject jsonObject = new JSONObject( response );
                        Boolean suc=jsonObject.getBoolean("success");
                        Log.e("suc in icu/bb",suc+"");
                        if(suc){
                            if(finalI +1==2){
                                Intent it = new Intent(BB_Read.this, Chating.class); //채팅방 생성완료시 채팅방으로 넘어감
                                it.putExtra("user", other);
                                it.putExtra("no", no);
                                startActivity(it);
                            }
                        }else{
                           insertChatUser(no);
                        }
                    } catch (JSONException e) {
                        Log.e("frag3 listpost","Connect Error");
                        e.printStackTrace();
                        return;
                    }
                }
            };
            InsertChatUserReq icur;
            if(i==0){
                icur = new InsertChatUserReq(no,cui.getUserNick(),responseListener); //본인 먼저 처리
            }else{
                icur=new InsertChatUserReq(no,other,responseListener); //타사용자 등록
            }
            RequestQueue queue = Volley.newRequestQueue( this);
            queue.add(icur);
        }
    }

    public void goPro(View view) {
        Intent i =new Intent(BB_Read.this, Profile.class);
        i.putExtra("nick",t_userID.getText().toString());
        startActivity(i);
    }
}
