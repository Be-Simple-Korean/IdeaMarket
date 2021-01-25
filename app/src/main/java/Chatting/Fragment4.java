package Chatting;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
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

import Chatting.Data.ChatRoomData;
import Chatting.Request.getChatCurMsgReq;
import Chatting.Request.getChatRoomReq;
import Chatting.Request.getChatUsersReq;

public class Fragment4 extends Fragment {
    private View view;
    RecyclerView recyclerView;
    ListAdapter listAdapter;
    ArrayList<ChatRoomData> car;
    LinearLayoutManager linearLayoutManager;
    String msg="",date="",people="";
    CurUserInfo cui=new CurUserInfo();
    public static ArrayList<Integer> iar=new ArrayList<>();
    Thread stop=new Thread(new Runnable() {
        @Override
        public void run() {
            try {
                Thread.sleep(300);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    });
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view= inflater.inflate(R.layout.frag4,container,false);
        view.findViewById(R.id.add_chat).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) { //채팅방 추가 버튼 클릭 이벤트
                Intent i = new Intent(view.getContext(), FriendList.class);
                startActivity(i);
            }
        });
        recyclerView=view.findViewById(R.id.rec);
        linearLayoutManager=new LinearLayoutManager(view.getContext());
        recyclerView.setLayoutManager(linearLayoutManager);
        car=new ArrayList<>();
        listAdapter=new ListAdapter(car);
        recyclerView.setAdapter(listAdapter);

        Response.Listener<String> responseListener = new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    JSONObject jsonObject = new JSONObject( response );
                    JSONArray ja = jsonObject.getJSONArray("ChatNo");
                    if(ja.length()==0){return;}
                    for(int i=0;i<ja.length();i++){
                        JSONObject item = ja.getJSONObject(i);
                        final int chatNo=item.getInt("chatNo");
                        Log.e("chatNo in f4",chatNo+"");
                        iar.add(chatNo);
                        Response.Listener<String> responseListener = new Response.Listener<String>() {
                            @Override
                            public void onResponse(String response) {
                                try {
                                    JSONObject jsonObject = new JSONObject( response );
                                    JSONArray ja = jsonObject.getJSONArray("ChatUser");
                                    if(ja.length()==0){return;}
                                    for(int i=0;i<ja.length();i++){
                                        JSONObject item = ja.getJSONObject(i);
                                        String nick = item.getString("nick");
                                        if(nick.equals(cui.getUserNick())){
                                            continue;
                                        }
                                        people=people+nick+",";
                                    }
                                    if(people.charAt((people.length()-1))==',') {
                                        people=people.substring(0,people.length()-1);
                                    }
                                    getCurMsg(people,chatNo);
                                    people="";
                                } catch (JSONException e) {
                                    Log.e("frag3 listpost","Connect Error");
                                    e.printStackTrace();
                                }/* catch (InterruptedException e) {
                                    e.printStackTrace();
                                }*/
                            }
                        };
                        getChatUsersReq gcur = new getChatUsersReq(chatNo,responseListener);
                        RequestQueue queue = Volley.newRequestQueue( view.getContext());
                        queue.add(gcur);
                        stop.run();
                        stop.join();
                    }
                } catch (JSONException e) {
                    Log.e("frag4 listpost","Connect Error");
                    e.printStackTrace();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        };
        getChatRoomReq gcrr = new getChatRoomReq(cui.getUserNick(),responseListener);
        RequestQueue queue = Volley.newRequestQueue( view.getContext());
        queue.add(gcrr);
        return view;
    }

    private void getCurMsg(final String person, int chatNo) {
        Response.Listener<String> responseListener = new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    JSONObject jsonObject = new JSONObject( response );
                    Boolean suc=jsonObject.getBoolean("success");
                    if(suc){
                        msg=jsonObject.getString("msg");
                        date=jsonObject.getString("date");
                        int c=date.indexOf("일");
                        String d="";
                        for(int i=0;i<=c;i++) {
                            d+=date.charAt(i);
                        }
                        ChatRoomData crd=new ChatRoomData(person,msg,d);
                        car.add(crd);
                        listAdapter.notifyDataSetChanged();
                        people="";
                    }else{
                        ChatRoomData crd=new ChatRoomData(person,"","");
                        car.add(crd);
                        listAdapter.notifyDataSetChanged();
                        people="";
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                    people="";
                }
            }
        };
        getChatCurMsgReq gccmr = new getChatCurMsgReq(chatNo,responseListener);
        RequestQueue queue = Volley.newRequestQueue( view.getContext());
        queue.add(gccmr);
        stop.run();
        try {
            stop.join();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}
