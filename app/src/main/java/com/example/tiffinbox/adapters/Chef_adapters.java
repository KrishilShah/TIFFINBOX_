package com.example.tiffinbox.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.tiffinbox.R;
import com.example.tiffinbox.models.Chef_list_model;

import java.util.List;

public class Chef_adapters extends RecyclerView.Adapter<Chef_adapters.ViewHolder> {

    private Context context;
    private List<Chef_list_model> chef_list_modelList;

    public Chef_adapters(Context context, List<Chef_list_model> Chef_list_models) {
        this.context = context;
        this.chef_list_modelList = Chef_list_models;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new ViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.chef_list_display,parent,false));
    }


    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Glide.with(context).load(chef_list_modelList.get(position).getPurl()).into(holder.chefimage);
        holder.name.setText(chef_list_modelList.get(position).getName());
        holder.phoneNo.setText(chef_list_modelList.get(position).getPhone());
        holder.mailid.setText(chef_list_modelList.get(position).getEmail());

    }

    @Override
    public int getItemCount() {
        return chef_list_modelList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        ImageView chefimage;
        TextView name,phoneNo,mailid;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            chefimage=itemView.findViewById(R.id.img1);
            name=itemView.findViewById(R.id.list_chef_name);
            phoneNo=itemView.findViewById(R.id.chef_list_phone);
            mailid=itemView.findViewById(R.id.chef_list_mail);



        }
    }
}
