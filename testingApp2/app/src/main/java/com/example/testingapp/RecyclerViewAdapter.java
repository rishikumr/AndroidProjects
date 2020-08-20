package com.example.testingapp;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import java.util.List;

public class RecyclerViewAdapter extends RecyclerView.Adapter<RecyclerViewAdapter.RecyclerViewHolder> {
    private List<DataModel> dataModelList;

    // declare view holder inner class
    class RecyclerViewHolder extends RecyclerView.ViewHolder {
        TextView tvID;
        TextView tvFirstName;
        TextView tvLastName;
        TextView tvContactNo;
        LinearLayout llItemView;

        public RecyclerViewHolder(View itemView) {
            super(itemView);
            llItemView = itemView.findViewById(R.id.llItemView);
            tvID = itemView.findViewById(R.id.tvID);
            tvFirstName = itemView.findViewById(R.id.tvFirstName);
            tvLastName = itemView.findViewById(R.id.tvLastName);
            tvContactNo = itemView.findViewById(R.id.tvContactNo);
        }
    }

    //create constructor with list
    public RecyclerViewAdapter(List<DataModel> dataModelList) {
        this.dataModelList = dataModelList;

    }

    @NonNull
    @Override
    public RecyclerViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        RecyclerViewHolder viewHolder;
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.recyclerview_list_item, parent, false);
        viewHolder = new RecyclerViewHolder(view);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerViewHolder holder, int position) {
        holder.tvID.setText(String.valueOf(dataModelList.get(position).getId()));
        holder.tvFirstName.setText(dataModelList.get(position).getFirstName());
        holder.tvLastName.setText(dataModelList.get(position).getLastName());
        holder.tvContactNo.setText(dataModelList.get(position).getContactNo());
    }

    @Override
    public long getItemId(int position) {
        return super.getItemId(position);
    }

    @Override
    public int getItemCount() {
        return dataModelList.size();
    }

}