package Project;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.toolbox.Volley;
import com.example.realp.CurUserInfo;
import com.example.realp.R;
import com.example.realp.getFriendListReq;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import Project.Data.TeamData;
import Project.Data.Projectinfo;

public class CustomDialog {
    public static int getCount() {
        return count;
    }

    public static void setCount(int count) {
        CustomDialog.count = count;
    }
    View dialogView;
    String selectF="";
    static int count=0;
    private Context context;
    addProjectC2 ac2=new addProjectC2();
    int tpc=0;
    ArrayList<String> far=new ArrayList<>();
    public static int edsta=0; //1이면 설정에서수행
    Projectinfo p = new Projectinfo();
    public CustomDialog(Context context) {
        this.context = context;
    }
    ArrayAdapter<String> fadapter;
    public int getEdsta() {
        return edsta;
    }

    public void setEdsta(int edsta) {
        this.edsta = edsta;
    }

    public void callFunction() {
        final Dialog dlg = new Dialog(context);
        dlg.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dlg.setContentView(R.layout.activity_show_friend_list);
        dlg.show();
        final ImageView iv_addName=(ImageView)dlg.findViewById(R.id.iv_getFName);
        final TextView member = dlg.findViewById(R.id.member);
        final EditText dlgEdt1 = (EditText) dlg.findViewById(R.id.dlgEdt1);
        final Spinner spinner=dlg.findViewById(R.id.teamSpin);
        final TextView dlgNag=dlg.findViewById(R.id.dlgNeg);
        final TextView dlgPos=dlg.findViewById(R.id.dlgPos);
        ArrayList<String> ar=new ArrayList<>();
            ar.add("팀장");
            ar.add("팀원");
            ArrayAdapter<String> adapter=new ArrayAdapter<>(dlg.getContext(),android.R.layout.simple_spinner_dropdown_item,ar);
        spinner.setAdapter(adapter);
        iv_addName.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialogView=(View)View.inflate(context,R.layout.frdialog,null);
                AlertDialog.Builder dlg=new AlertDialog.Builder(context);
                ListView listView=(ListView)dialogView.findViewById(R.id.lv);
                CurUserInfo cui=new CurUserInfo();
                far.add(cui.getUserNick());
                getFriendList(cui.getUserNick());
                fadapter=new ArrayAdapter<>(context,android.R.layout.simple_list_item_single_choice,far);
                listView.setAdapter(fadapter);
                listView.setChoiceMode(ListView.CHOICE_MODE_SINGLE);
                listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                        selectF=far.get(i);
                    }
                });
                dlg.setView(dialogView);
                dlg.setPositiveButton("확인", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        if(selectF.equals("")){
                            Toast.makeText(context, "친구를 선택해주세요", Toast.LENGTH_SHORT).show();
                            return;
                        }else{
                            member.setVisibility(View.VISIBLE);
                            member.setText(selectF);
                            dialogInterface.dismiss();
                        }
                    }
                });
                dlg.setNegativeButton("취소", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        selectF="";
                        dialogInterface.dismiss();
                    }
                });
                dlg.show();
            }
        });
      dlgNag.setOnClickListener(new View.OnClickListener() {
          @Override
          public void onClick(View view) {
              dlg.dismiss();
          }
      });
        dlgPos.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String mName=selectF;
                if(mName.equals("")){
                    Toast.makeText(context, "팀원을 선택해주세요", Toast.LENGTH_SHORT).show();
                    return;
                }
                String mChar=dlgEdt1.getText().toString();
                if(mChar.equals("")){
                    Toast.makeText(context, "역할을 적어주세요", Toast.LENGTH_SHORT).show();
                    return;
                }
                String mRank=spinner.getSelectedItem().toString();
                if(count>=1){
                   boolean isCheck= checkRank(mRank,getEdsta());
                   if(!isCheck){
                       return;
                   }
                }
                if(getEdsta()==0){
                    TeamData teamData=new TeamData(mName,mChar,mRank,R.drawable.clearmember,0,true);
                    p.pWorker.add(mName);
                    p.tChar.add(mChar);
                    p.tRank.add(mRank);
                    ac2.ar.add(teamData);
                    ac2.teamAdapter.notifyDataSetChanged();
                    tpc=ac2.ar.size();
                    p.setTpc(tpc);
                    count+=1;
                    dlg.dismiss();
                }else{
                    EditProject ep=new EditProject();
                    CurProjectinfo c=new CurProjectinfo();
                    Log.e("추가전",c.getTpc()+"");
                    TeamData wd=new TeamData(mName,mChar,mRank,R.drawable.clearmember,1,true);
                    ep.tar.add(wd);
                    ep.teamAdapter.notifyDataSetChanged();
                    c.worker.add(mName);ep.tmpuser.add(mName);
                    c.wChar.add(mChar);ep.tmpchar.add(mChar);
                    c.wRank.add(mRank);ep.tmprank.add(mRank);
                    c.setTpc(c.worker.size());
                    Log.e("추가후",c.getTpc()+"");
                    edsta=0;
                    count=0;
                    ep.setAddCount(ep.getAddCount()+1);
                    dlg.dismiss();
                }
            }
        });
    }

    private void getFriendList(String userNick) {
        Response.Listener<String> responseListener = new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    JSONObject jsonObject = new JSONObject( response );
                    JSONArray ja = jsonObject.getJSONArray("Friend");
                    if(ja.length()==0){return;}
                    for(int i=0;i<ja.length();i++){
                        JSONObject item = ja.getJSONObject(i);
                        String a=item.getString("userNick");
                        far.add(a);
                        fadapter.notifyDataSetChanged();
                    }
                } catch (JSONException e) {
                    Log.e("customDialog","Connect Error");
                    e.printStackTrace();
                }
            }
        };
        getFriendListReq lpr = new getFriendListReq(userNick,responseListener);
        RequestQueue queue = Volley.newRequestQueue(context);
        queue.add( lpr );
    }

    private boolean checkRank(String rank,int psta){
        CurProjectinfo c =new CurProjectinfo();
        if(psta==1){
            for(int i=0;i<c.wRank.size();i++){
                String tmp=c.wRank.get(i);
                if(rank.equals("팀장")&&tmp.equals(rank)){
                    Toast.makeText(context, "팀장은 한명만 가능합니다.", Toast.LENGTH_SHORT).show();
                    return false;
                }
            }
        }else{
            for(int i=0;i<p.tRank.size();i++){
                String tmp=p.tRank.get(i);
                if(rank.equals("팀장")&&tmp.equals(rank)){
                    Toast.makeText(context, "팀장은 한명만 가능합니다.", Toast.LENGTH_SHORT).show();
                    return false;
                }
            }
        }
        return true;
    }
}