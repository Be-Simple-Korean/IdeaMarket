package com.example.realp;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

class NotifiAdapter extends RecyclerView.Adapter<NotifiAdapter.CustomViewHolder> {
    ArrayList<NotiData> nar;

    public NotifiAdapter(ArrayList<NotiData> nar) {
        this.nar = nar;
    }

    @NonNull
    @Override
    public NotifiAdapter.CustomViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v= LayoutInflater.from(parent.getContext()).inflate(R.layout.noti_item,parent,false);
        CustomViewHolder holder=new CustomViewHolder(v);
        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull NotifiAdapter.CustomViewHolder holder, int position) {
        Log.e("noti",nar.get(position).getContents());
            holder.noti_con.setText(nar.get(position).getContents());
            holder.noti_date.setText(nar.get(position).getDate());
    }

    @Override
    public int getItemCount()   {
        return (null!=nar?nar.size():0);
    }

    public class CustomViewHolder extends RecyclerView.ViewHolder {
        protected TextView noti_con;
        protected TextView noti_date;
        public CustomViewHolder(@NonNull View itemView) {
            super(itemView);
            noti_con=itemView.findViewById(R.id.noti_con);
            noti_date=itemView.findViewById(R.id.noti_date);
        }
    }
}
