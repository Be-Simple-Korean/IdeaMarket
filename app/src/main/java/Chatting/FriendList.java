package Chatting;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.toolbox.Volley;
import com.example.realp.CurUserInfo;
import com.example.realp.R;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import java.util.ArrayList;
import Chatting.Data.FriendData;
import Chatting.Request.InsertChatRoomReq;
import Chatting.Request.InsertChatUserReq;
import Chatting.Request.getFriendReq;

public class FriendList extends AppCompatActivity {
    RecyclerView fv;
    FriendAdapter fa;
    ArrayList<FriendData> far;
    LinearLayoutManager linearLayoutManager;
    public static int spc=0;
    ArrayList<String> user=new ArrayList<>();
    CurUserInfo cui=new CurUserInfo();
    public static String choseUser="";

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        spc=0;
        finish();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_friend_list);
        user.add(cui.getUserNick());
        getFriends();
        findViewById(R.id.addChatRoom).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) { // 친구선택 완료버튼
                insertChatRoomData();
            }
        });
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
                        Toast.makeText(FriendList.this, "다시 시도해주세요", Toast.LENGTH_SHORT).show();
                    }
                } catch (JSONException e) {
                    Log.e("frag3 listpost","Connect Error");
                    e.printStackTrace();
                }
            }
        };
        spc=spc+1;
        InsertChatRoomReq icrr = new InsertChatRoomReq(spc,responseListener);
        RequestQueue queue = Volley.newRequestQueue( this);
        queue.add(icrr);
    }
    String people="";
    private void insertChatUser(int no) {
        for(int i=0;i<spc;i++){
            int finalI = i;
            Response.Listener<String> responseListener = new Response.Listener<String>() {
                @Override
                public void onResponse(String response) {
                    try {
                        JSONObject jsonObject = new JSONObject( response );
                        Boolean suc=jsonObject.getBoolean("success");
                        if(suc){
                            people=choseUser;
                            if(finalI +1==spc){
                                Intent it = new Intent(FriendList.this, Chating.class); //채팅방 생성완료시 채팅방으로 넘어감
                                it.putExtra("user", people);
                                it.putExtra("no", no);
                                spc = 0;
                                startActivity(it);
                            }
                        }else{
                            Toast.makeText(FriendList.this, "다시 시도해주세요", Toast.LENGTH_SHORT).show();
                            return;
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
                icur=new InsertChatUserReq(no,choseUser,responseListener); //타사용자 등록
            }
            RequestQueue queue = Volley.newRequestQueue( this);
            queue.add(icur);
        }
    }

    private void getFriends() {
        fv=findViewById(R.id.freindview);
        linearLayoutManager=new LinearLayoutManager(this);
        fv.setLayoutManager(linearLayoutManager);
        far=new ArrayList<>();
        fa=new FriendAdapter(far);
        fv.setAdapter(fa);
        Response.Listener<String> responseListener = new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    JSONObject jsonObject = new JSONObject( response );
                    JSONArray ja = jsonObject.getJSONArray("Friend");
                    if(ja.length()==0){return;}
                    for(int i=0;i<ja.length();i++){
                        JSONObject item = ja.getJSONObject(i);
                        String nick=item.getString("nick");
                        if(nick.equals(cui.getUserNick())){
                            continue;
                        }
                        FriendData fd = new FriendData(nick);
                        far.add(fd);
                        fa.notifyDataSetChanged();
                    }
                } catch (JSONException e) {
                    Log.e("frag3 listpost","Connect Error");
                    e.printStackTrace();
                }
            }
        };
        getFriendReq gfr = new getFriendReq(cui.getUserNick(),responseListener);
        RequestQueue queue = Volley.newRequestQueue( this);
        queue.add(gfr);
    }
}