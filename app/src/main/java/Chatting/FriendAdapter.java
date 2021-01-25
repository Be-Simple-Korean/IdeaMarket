package Chatting;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RadioButton;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.realp.R;

import java.util.ArrayList;

import Chatting.Data.FriendData;

class FriendAdapter extends RecyclerView.Adapter<FriendAdapter.CustomViewHolder> {
    ArrayList<FriendData> far;
    ArrayList<RadioButton> rar;
    public FriendAdapter(ArrayList<FriendData> far) {
        this.far = far;
        rar=new ArrayList<>();
    }

    @NonNull
    @Override
    public FriendAdapter.CustomViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view= LayoutInflater.from(parent.getContext()).inflate(R.layout.friendlist,parent,false);
        CustomViewHolder holder=new CustomViewHolder(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull final FriendAdapter.CustomViewHolder holder, int position) {
        final FriendList fl = new FriendList();
        fl.choseUser=far.get(position).getNickname();
        holder.nickname.setText(far.get(position).getNickname());
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(rar.get(position).isChecked()){
                    rar.get(position).setChecked(false);
                    fl.spc-=1;
                }else{
                    rar.get(position).setChecked(true);
                    fl.spc+=1;
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return (null!=far?far.size():0);
    }

    public class CustomViewHolder extends RecyclerView.ViewHolder {
        protected TextView nickname;
        protected RadioButton rb;

        public CustomViewHolder(@NonNull View itemView) {
            super(itemView);
            nickname=itemView.findViewById(R.id.nickname);
            rb=itemView.findViewById(R.id.rb);
            rar.add(rb);
        }
    }
}
