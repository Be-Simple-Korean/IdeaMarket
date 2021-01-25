package Chatting;

import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.realp.R;

import java.util.ArrayList;

import Chatting.Data.ChatRoomData;

public class ListAdapter extends RecyclerView.Adapter<ListAdapter.CustomViewHolder> {
    ArrayList<ChatRoomData> car;

    public ListAdapter(ArrayList<ChatRoomData> car) {
        this.car = car;
    }

    @NonNull
    @Override
    public CustomViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view= LayoutInflater.from(parent.getContext()).inflate(R.layout.chatlist,parent,false);
        CustomViewHolder holder =new CustomViewHolder(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull final CustomViewHolder holder, final int position) {
            holder.nick.setText(car.get(position).getNick());
            holder.content.setText(car.get(position).getContent());
            holder.time.setText(car.get(position).getTime());
            holder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Fragment4 cl=new Fragment4();
                    Intent i = new Intent(holder.itemView.getContext(), Chating.class);
                    i.putExtra("user",car.get(position).getNick());
                    i.putExtra("no",cl.iar.get(position));
                    holder.itemView.getContext().startActivity(i);
                }
            });
    }



    @Override
    public int getItemCount() {
        return (null!=car?car.size():0);
    }

    public class CustomViewHolder extends RecyclerView.ViewHolder {
        protected TextView nick;
        protected TextView content;
        protected TextView time;
        public CustomViewHolder(@NonNull View itemView) {
            super(itemView);
            nick=itemView.findViewById(R.id.nick);
            content=itemView.findViewById(R.id.content);
            time=itemView.findViewById(R.id.time);
        }
    }
}
