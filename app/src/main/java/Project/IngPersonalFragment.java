package Project;

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
import Project.Request.GetIngTogDataReq;

import com.example.realp.IngTogData;
import com.example.realp.R;
import Project.Request.UpdateTaskReq;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Calendar;

public class IngPersonalFragment extends Fragment {
    RecyclerView recyclerView;
    CurProjectinfo c=new CurProjectinfo();
   public static IngTogAdapter ingTogAdapter;
    public static ArrayList<IngTogData> ipar;
    LinearLayoutManager linearLayoutManager;
    private View view;
    private String status="진행";
    private String subject="개인";
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view=inflater.inflate(R.layout.ing_personal_frag,container,false);
        ingTogList();
        return view;
    }
    public void ingTogList(){
            recyclerView=(RecyclerView)view.findViewById(R.id.ingPersonalView);
        linearLayoutManager=new LinearLayoutManager(getActivity());
        recyclerView.setLayoutManager(linearLayoutManager);
        ipar=new ArrayList<>();
        ingTogAdapter=new IngTogAdapter(ipar);
        recyclerView.setAdapter(ingTogAdapter);

        Response.Listener<String> responseListener = new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    JSONObject jsonObject = new JSONObject( response );
                    JSONArray ja = jsonObject.getJSONArray("TeamTask");
                    if(ja.length()==0){
                        Toast.makeText(view.getContext(), "데이터가 없습니다", Toast.LENGTH_SHORT).show();
                        return;}
                    for(int i=0;i<ja.length();i++){
                        JSONObject item = ja.getJSONObject(i);
                        String userid=item.getString("userNick");
                        String contents = item.getString("contents");
                        String gFinish = item.getString("gFinish");
                        String[] tmp=gFinish.split("[.]");
                        Calendar cal = Calendar.getInstance();
                        int y=cal.get(Calendar.YEAR);
                        int m =cal.get(Calendar.MONTH)+1;
                        int d =cal.get(Calendar.DATE);
                        if(y>Integer.parseInt(tmp[0])){ //현재년도가 가져온데이터의 년도보다 크면
                            Log.e(""+i,"년도수행/개인");
                            updateStatus(status,subject,userid,contents,gFinish,"완료","개인",userid,contents,gFinish);
                            continue;
                        }else if(Integer.parseInt(tmp[0])==y&&m>Integer.parseInt(tmp[1])){
                            Log.e(""+i,"월별수행/개인");
                            updateStatus(status,subject,userid,contents,gFinish,"완료","개인",userid,contents,gFinish);
                            continue;
                        }else if(Integer.parseInt(tmp[0])==y&&m==Integer.parseInt(tmp[1])&&d>Integer.parseInt(tmp[2])){
                            Log.e(""+i,"일별수행/개인");
                            updateStatus(status,subject,userid,contents,gFinish,"완료","개인",userid,contents,gFinish);
                            continue;
                        }
                        IngTogData itd= new IngTogData(userid,contents,gFinish,R.drawable.clearmember);
                        ipar.add(itd);
                        ingTogAdapter.notifyDataSetChanged();
                    }
                } catch (JSONException e) {
                    Log.e("CE0","Connect Error");
                    e.printStackTrace();
                }
            }
        };
       GetIngTogDataReq gitdr = new GetIngTogDataReq(c.getTeamNo(),status,subject,responseListener);
        RequestQueue queue = Volley.newRequestQueue( getActivity() );
        queue.add( gitdr );
    }
    private void updateStatus(String oldsta,String oldsub,String olduser,String oldcon,String olddate,String newsta,String newsub,String newuser,String newcon,String newdate){
        Response.Listener<String> responseListener = new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    JSONObject jsonObject = new JSONObject( response );
                    boolean suc = jsonObject.getBoolean("success");
                    if(suc){
                        Log.e("개인/진행에서 완료로","수행");
                    }
                } catch (JSONException e) {
                    Log.e("CE0","Connect Error");
                    e.printStackTrace();
                }
            }
        };
        UpdateTaskReq utr=new UpdateTaskReq(c.getTeamNo(),oldsta,oldsub,olduser,oldcon,olddate,newsta,newsub,newuser,newcon,newdate,responseListener);
        RequestQueue queue = Volley.newRequestQueue(getActivity());
        queue.add(utr);
    }
}
