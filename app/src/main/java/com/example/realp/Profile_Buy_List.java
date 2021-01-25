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

public class Profile_Buy_List extends Fragment {
    private ArrayList<BB_ListData> arrayList;
    private RecyclerView recyclerView;
    private BB_ListAdapter BBListAdapter;
    private ArrayList<Integer> fav;
    private ArrayList<BB_ListData2> imageDataList;
    private BB_ListData BBListData;
    private BB_ListData2 BBListData2;
    CurUserInfo cui=new CurUserInfo();
    View v;
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        v = inflater.inflate(R.layout.profile_buy_list,container,false);
        Log.e("start","this");
        setHasOptionsMenu(true);
        imageDataList=new ArrayList<>();
        fav=new ArrayList<>();
        recyclerView = (RecyclerView) v.findViewById(R.id.pbl);
        recyclerView.addItemDecoration(new DividerItemDecoration(v.getContext(), 1));
        RecyclerView.LayoutManager manager = new LinearLayoutManager(getActivity());
        arrayList=new ArrayList<>();
        BBListAdapter = new BB_ListAdapter(arrayList);
        recyclerView.setLayoutManager(manager);
        recyclerView.setAdapter(BBListAdapter);
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
    public void favorites() {
        final String userID = cui.getUserNick();
        Response.Listener<String> responseListener = new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    JSONObject jsonObject = new JSONObject(response);
                    JSONArray ja = jsonObject.getJSONArray("bsNum");
                    if (ja.length() == 0) {
                        return;
                    }
                    for (int i = 0; i < ja.length(); i++) {
                        JSONObject item = ja.getJSONObject(i);
                        fav.add(item.getInt("bsNum"));
                    }


                } catch (JSONException e) {

                }
            }
        };
        BB_FavoritesListRequest bb_favoritesListRequest = new BB_FavoritesListRequest(userID, responseListener);
        RequestQueue queue = Volley.newRequestQueue(v.getContext());
        queue.add(bb_favoritesListRequest);
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

                        int bNumber=item.getInt("bNumber");
                        String imageUrl = item.getString("imageUrl");

                        BBListData2 =new BB_ListData2(bNumber,imageUrl);
                        imageDataList.add(BBListData2);
                        BBListAdapter.notifyDataSetChanged();
                    }

                } catch (JSONException e) {
                }
            }

        };
        MyApplication qwe = (MyApplication) getActivity().getApplication();

        PB_ListImageRequest plibq = new PB_ListImageRequest(cui.getUserNick(),responseListener2);
        RequestQueue queue2 = Volley.newRequestQueue(getActivity());
        queue2.add(plibq);


    }
    public void listPost() {

        imagePost();
        favorites();
        Response.Listener<String> responseListener = new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    arrayList.clear();
                    JSONObject jsonObject = new JSONObject(response);
                    JSONArray ja = jsonObject.getJSONArray("Board");
                    for (int i = 0; i < ja.length(); i++) {
                        JSONObject item = ja.getJSONObject(i);
                        String rvTitle = item.getString("bTitle");
                        String rvWriter = item.getString("userID");
                        String rvTime = item.getString("bDate");
                        String rvPrice = item.getString("price");
                        String rvViews = item.getString("bView");
                        int bNumber=item.getInt("bNumber");
                        String url="http://tmdgus95p.dothome.co.kr/imgFile/mainicon.png";
                        int image=R.drawable.star;
                        for (int z=0;z<imageDataList.size();z++){
                            BB_ListData2 b=imageDataList.get(z);
                            if (bNumber == b.getbNumber()) {
                                url=b.getImageUrl();
                                break;
                            }
                        }

                        for (int f=0;f<fav.size();f++ ){
                            int asd=fav.get(f);
                            if (bNumber==asd){
                                image=R.drawable.yellowstar;
                            }
                        }
                        BBListData = new BB_ListData(bNumber,rvPrice,rvViews,url,image,rvTitle,rvWriter,rvTime.substring(2,16),"조회수");
                        arrayList.add(BBListData);
                        BBListAdapter.notifyDataSetChanged(); //추가후새로고침
                    }
                } catch (JSONException e) {
                }
            }
        };
        MyApplication qwe = (MyApplication) getActivity().getApplication();
        PB_ListRequest lpr = new PB_ListRequest(cui.getUserNick(),responseListener);
        RequestQueue queue = Volley.newRequestQueue(getActivity());
        queue.add( lpr );

    }


}