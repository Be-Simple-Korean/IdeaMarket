package Project;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.realp.R;

import Project.Data.ChaseData;

import java.util.ArrayList;

import Project.Request.OutMemberReq;

public class ChaseAdapter extends RecyclerView.Adapter<ChaseAdapter.CustomHolder> {
    ArrayList<ChaseData> ar;
    public ArrayList<CheckBox> car=new ArrayList<>();
    OutMemberReq outMemberReq=new OutMemberReq();
    public ChaseAdapter(ArrayList<ChaseData> ar) {
        this.ar = ar;
    }

    @NonNull
    @Override
    public ChaseAdapter.CustomHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view= LayoutInflater.from(parent.getContext()).inflate(R.layout.chaselist,parent,false);
        CustomHolder holder=new CustomHolder(view);
        return holder;
    }
    int select=10000;
    @Override
    public void onBindViewHolder(@NonNull final ChaseAdapter.CustomHolder holder, final int position) {
        holder.tv_name.setText(ar.get(position).getpWorker());
        holder.ch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(select==position){
                    car.get(position).setChecked(false);
                    return;
                }else{
                    select=position;
                }
                for(int i=0;i<car.size();i++){
                    car.get(i).setChecked(false);
                }
                car.get(position).setChecked(true);
                outMemberReq.isCheck=true;
                outMemberReq.selecter=holder.tv_name.getText().toString();
            }
        });

    }

    @Override
    public int getItemCount() {
        return (null!=ar?ar.size():0);
    }

    public class CustomHolder extends RecyclerView.ViewHolder {
        protected CheckBox ch;
        protected TextView tv_name;
        public CustomHolder(@NonNull View itemView) {
            super(itemView);
            ch=itemView.findViewById(R.id.cb_sta);
            car.add(ch);
            tv_name=itemView.findViewById(R.id.tv_pWorker);
        }
    }
}
