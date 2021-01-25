package Chatting;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.realp.CurUserInfo;
import com.example.realp.R;

import java.util.ArrayList;

import Chatting.Data.Data;

public class DataAdapter extends RecyclerView.Adapter<DataAdapter.CustomViewHolder> {
    ArrayList<Data> dar;
    CurUserInfo cui=new CurUserInfo();
    public DataAdapter(ArrayList<Data> dar) {
        this.dar = dar;
    }
    @Override
    public int getItemViewType(int position) {
        if(dar.get(position).getNick().equals(cui.getUserNick())){
            return 1;
        }else{
            return 2;
        }
    }
    @NonNull
    @Override
    public CustomViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view;
        if(viewType==1){
            view= LayoutInflater.from(parent.getContext()).inflate(R.layout.chattinglist_right,parent,false);
        }else{
            view= LayoutInflater.from(parent.getContext()).inflate(R.layout.chattinglist,parent,false);
        }
        CustomViewHolder holder=new CustomViewHolder(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull CustomViewHolder holder, int position) {
        holder.user.setText(dar.get(position).getNick());
        holder.con.setText(dar.get(position).getContent());
    }

    @Override
    public int getItemCount() {
        return (null!=dar?dar.size():0);
    }

    public class CustomViewHolder extends RecyclerView.ViewHolder {
        protected TextView user;
        protected TextView con;
        public CustomViewHolder(@NonNull View itemView) {
            super(itemView);
            user=itemView.findViewById(R.id.user);
            con=itemView.findViewById(R.id.con);
        }
    }
}
