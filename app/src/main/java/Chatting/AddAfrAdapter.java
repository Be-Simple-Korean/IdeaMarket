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

import Chatting.Data.ChatRoomData;


public class AddAfrAdapter extends RecyclerView.Adapter<AddAfrAdapter.CustomViewHolder> {
    ArrayList<ChatRoomData> car;
    ArrayList<RadioButton> rar;
    public AddAfrAdapter(ArrayList<ChatRoomData> car) {
        this.car = car;
        rar=new ArrayList<>();
    }

    @NonNull
    @Override
    public AddAfrAdapter.CustomViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v= LayoutInflater.from(parent.getContext()).inflate(R.layout.add_chatlist,parent,false);
        CustomViewHolder holder=new CustomViewHolder(v);
        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull AddAfrAdapter.CustomViewHolder holder, int position) {
            holder.nick.setText(car.get(position).getNick());
            holder.content.setText(car.get(position).getContent());
            holder.time.setText(car.get(position).getTime());

            holder.acr_rb.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    AddFromRoom afr=new AddFromRoom();
                    afr.curNumber= afr.iar.get(position);
                    for(int i=0;i<car.size();i++){
                        rar.get(i).setChecked(false);
                        if(i+1==car.size()){
                            holder.acr_rb.setChecked(true);
                        }
                    }

                }
            });
    }

    @Override
    public int getItemCount()  {
        return (null!=car?car.size():0);
    }

    public class CustomViewHolder extends RecyclerView.ViewHolder {
        protected TextView nick;
        protected TextView content;
        protected TextView time;
        protected RadioButton acr_rb;

        public CustomViewHolder(@NonNull View itemView) {
            super(itemView);
            nick=itemView.findViewById(R.id.nick);
            content=itemView.findViewById(R.id.content);
            time=itemView.findViewById(R.id.time);
            acr_rb=itemView.findViewById(R.id.acr_rb);
            rar.add(acr_rb);
        }
    }
}
