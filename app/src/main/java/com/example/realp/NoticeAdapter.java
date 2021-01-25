package com.example.realp;

import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

class NoticeAdapter extends RecyclerView.Adapter<NoticeAdapter.CutstomNoticeHolder> {
    ArrayList<NoticeData> nar;

    public NoticeAdapter(ArrayList<NoticeData> nar) {
        this.nar = nar;
    }
    View v;
    @NonNull
    @Override
    public NoticeAdapter.CutstomNoticeHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        v= LayoutInflater.from(parent.getContext()).inflate(R.layout.notice_item,parent,false);
        CutstomNoticeHolder holder=new CutstomNoticeHolder(v);
        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull NoticeAdapter.CutstomNoticeHolder holder, int position) {
        holder.notice_no.setText(nar.get(position).getNotice_no());
        holder.notice_title.setText(nar.get(position).getNotice_title());
        holder.notice_date.setText(nar.get(position).getNotice_date());
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i =new Intent(v.getContext(),ShowNotice.class);
                i.putExtra("no",nar.get(position).getNotice_no());
                v.getContext().startActivity(i);
            }
        });
    }

    @Override
    public int getItemCount()  {
        return (null!=nar?nar.size():0);
    }

    public class CutstomNoticeHolder extends RecyclerView.ViewHolder {
        protected TextView notice_no;
        protected TextView notice_title;
        protected TextView notice_date;
        public CutstomNoticeHolder(@NonNull View itemView) {
            super(itemView);
            notice_no=itemView.findViewById(R.id.notice_no);
            notice_title=itemView.findViewById(R.id.notice_title);
            notice_date=itemView.findViewById(R.id.notice_date);
        }
    }
}
