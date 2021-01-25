package com.example.realp;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

class ReportAdapter extends RecyclerView.Adapter<ReportAdapter.CustomHolder> {
    ArrayList<ReportData> rar;

    public ReportAdapter(ArrayList<ReportData> rar) {
        this.rar = rar;
    }

    @NonNull
    @Override
    public ReportAdapter.CustomHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v= LayoutInflater.from(parent.getContext()).inflate(R.layout.report_item,parent,false);
        CustomHolder holder=new CustomHolder(v);
        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull ReportAdapter.CustomHolder holder, int position) {
        holder.reportTag.setText(rar.get(position).getReportTag());
        holder.reportDate.setText(rar.get(position).getReportDate());
    }

    @Override
    public int getItemCount() {
        return (null!=rar?rar.size():0);
    }

    public class CustomHolder extends RecyclerView.ViewHolder {
        protected TextView reportTag;
        protected TextView reportDate;
        public CustomHolder(@NonNull View itemView) {
            super(itemView);
            reportDate=itemView.findViewById(R.id.reportDate);
            reportTag=itemView.findViewById(R.id.reportTag);
        }
    }
}
