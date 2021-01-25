package com.example.realp;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.PopupMenu;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.toolbox.Volley;
import com.example.realp.common.MyApplication;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class Profile_Co_List extends Fragment {
    public ArrayList<CB_ListData> arrayList;
    private ArrayList<CB_ListData2> imageDataList;
    private ArrayList<Integer> fav;
    private RecyclerView recyclerView;
    private CB_ListAdapter CBListAdapter;
    View v;
    CurUserInfo cui=new CurUserInfo();
    CB_ListData CBData;
    CB_ListData2 CBListData2;
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        v =  inflater.inflate(R.layout.profile_co_list,container,false);
        recyclerView = (RecyclerView) v.findViewById(R.id.pcl);
        imageDataList= new ArrayList<>();
        fav=new ArrayList<>();
        recyclerView.addItemDecoration(new DividerItemDecoration(v.getContext(), 1));
        RecyclerView.LayoutManager manager = new LinearLayoutManager(getActivity());
        arrayList=new ArrayList<>();
        CBListAdapter = new CB_ListAdapter(arrayList);
        recyclerView.setLayoutManager(manager);
        recyclerView.setAdapter(CBListAdapter);
        return v;
    }
    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        imagePost();
    }
    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        listPost();
    }

    public void favorites(){
        final String userID = cui.getUserNick();
        Response.Listener<String> responseListener=new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    JSONObject jsonObject = new JSONObject(response);
                    JSONArray ja = jsonObject.getJSONArray("cNum");
                    Log.e("leng in favor",ja.length()+"");
                    if(ja.length()==0){
                        return;}
                    for (int i = 0; i < ja.length(); i++) {
                        JSONObject item = ja.getJSONObject(i);
                        fav.add(item.getInt("cNum"));
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        };
        CB_FavoritesListRequest cb_favoritesListRequest=new CB_FavoritesListRequest(userID,responseListener);
        RequestQueue queue= Volley.newRequestQueue(getActivity());
        queue.add(cb_favoritesListRequest);
    }

    public void imagePost() {

        Response.Listener<String> responseListener2 = new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {

                    JSONObject jsonObject = new JSONObject(response);
                    JSONArray ja = jsonObject.getJSONArray("Board");
                    for (int i = 0; i < ja.length(); i++) {
                        JSONObject item = ja.getJSONObject(i);
                        int cNumber=item.getInt("cNumber");
                        String imageUrl = item.getString("imageUrl");
                        CBListData2 =new CB_ListData2(cNumber,imageUrl);
                        imageDataList.add(CBListData2);
                        CBListAdapter.notifyDataSetChanged();
                    }

                } catch (JSONException e) {
                    Toast.makeText(getActivity(), "리스트 불러오기 실패", Toast.LENGTH_SHORT).show();
                    e.printStackTrace();
                }
            }

        };
        PC_ListImageRequest pclir = new PC_ListImageRequest(cui.getUserNick(),responseListener2);
        RequestQueue queue2 = Volley.newRequestQueue(getActivity());
        queue2.add( pclir );

    }

    public void listPost() {

        favorites();
        imagePost();

        Response.Listener<String> responseListener = new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {

                    arrayList.clear();
                    JSONObject jsonObject = new JSONObject(response);
                    JSONArray ja = jsonObject.getJSONArray("Board");
                    Log.e("ja.length",""+ja.length());
                    if(ja.length()==0){return;}
                    for (int i = 0; i < ja.length(); i++) {
                        JSONObject item = ja.getJSONObject(i);

                        String cTitle = item.getString("cTitle");
                        String cWriter = item.getString("userID");
                        String cTime = item.getString("cDate");
                        String hPerson = item.getString("hPerson");
                        String cViews = item.getString("cView");

                        int cNumber = item.getInt("cNumber");
                        String url ="http://tmdgus95p.dothome.co.kr/imgFile/mainicon.png";
                        int image=R.drawable.star;

                        for (int z=0;z<imageDataList.size();z++){
                            CB_ListData2 b=imageDataList.get(z);
                            if (cNumber == b.getcNumber()) {
                                url=b.getImageUrl();
                                break;
                            }
                        }
                        for (int f=0;f<fav.size();f++ ){
                            int asd=fav.get(f);
                            if (cNumber==asd){
                                image=R.drawable.yellowstar;
                            }
                        }

                        CBData = new CB_ListData(cNumber,image,url,cTitle,cWriter,cTime.substring(2,16),"조회수",hPerson,cViews);

                        arrayList.add(CBData);

                        CBListAdapter.notifyDataSetChanged(); //추가후새로고침
                    }

                } catch (JSONException e) {
                    Toast.makeText(getActivity(), "리스트 불러오기 실패", Toast.LENGTH_SHORT).show();
                    e.printStackTrace();
                }
            }

        };
        PC_ListRequest pcr = new PC_ListRequest(cui.getUserNick(),responseListener);
        RequestQueue queue = Volley.newRequestQueue(getActivity());
        queue.add( pcr );

    }

}

