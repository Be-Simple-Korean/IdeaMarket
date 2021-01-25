package com.example.realp;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class Profile_Report_List extends Fragment {
    RecyclerView recyclerView;
    ReportAdapter reportAdapter;
    ArrayList<ReportData> rar;
    LinearLayoutManager layoutManager;
    View v;
    CurUserInfo cui= new CurUserInfo();
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        v = inflater.inflate(R.layout.profile_report_list,container,false);
        lisPost();
        return v;
    }

    private void lisPost() {
        recyclerView=v.findViewById(R.id.prl);
        rar=new ArrayList<>();
        layoutManager=new LinearLayoutManager(v.getContext());
        reportAdapter=new ReportAdapter(rar);
        recyclerView.setAdapter(reportAdapter);
        recyclerView.setLayoutManager(layoutManager);
        Response.Listener<String> responseListener = new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    JSONObject jsonObject = new JSONObject( response );
                    JSONArray ja = jsonObject.getJSONArray("Report");
                    if(ja.length()==0){
                        Toast.makeText(v.getContext(), "신고 받은 내역이 없습니다.", Toast.LENGTH_SHORT).show();
                        return;
                    }
                    Toast.makeText(v.getContext(),  ja.length()+"건의 신고를 받으셨습니다", Toast.LENGTH_SHORT).show();
                    for(int i=0;i<ja.length();i++){
                        JSONObject item = ja.getJSONObject(i);
                        String rTag=item.getString("rTag");
                        String rtime=item.getString("rtime");
                        ReportData rd=new ReportData(rTag,rtime);
                        rar.add(rd);
                        reportAdapter.notifyDataSetChanged();
                    }
                } catch (JSONException e) {
                    Log.e("profile report connect","Connect Error");
                    e.printStackTrace();
                }
            }
        };
        getReportLogReq grlr = new  getReportLogReq(cui.getUserNick(),responseListener);
        RequestQueue queue = Volley.newRequestQueue( v.getContext());
        queue.add( grlr);

    }
}
