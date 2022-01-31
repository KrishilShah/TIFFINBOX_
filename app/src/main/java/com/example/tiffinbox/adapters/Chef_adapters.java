package com.example.tiffinbox.adapters;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.view.menu.MenuView;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.tiffinbox.R;
import com.example.tiffinbox.ViewAllActivity;
import com.example.tiffinbox.models.Chef_list_model;
import com.google.firebase.firestore.DocumentSnapshot;

import java.util.ArrayList;
import java.util.List;

public class Chef_adapters extends RecyclerView.Adapter<Chef_adapters.ViewHolder> {
//   private AdapterView.OnItemClickListener listener;
public Chef_adapters.OnItemClickListener onItemClickListener;
    private Context context;
    private List<Chef_list_model> chef_list_modelList;

    public Chef_adapters(Context context, List<Chef_list_model> Chef_list_models,OnItemClickListener onItemClickListener) {
        this.context = context;
        this.chef_list_modelList = Chef_list_models;
        this.onItemClickListener=onItemClickListener;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new ViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.chef_list_display,parent,false));
    }


    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, @SuppressLint("RecyclerView") int position) {
        Glide.with(context).load(chef_list_modelList.get(position).getPurl()).into(holder.chefimage);
        holder.name.setText(chef_list_modelList.get(position).getName());
        holder.phoneNo.setText(chef_list_modelList.get(position).getPhone());
        holder.mailid.setText(chef_list_modelList.get(position).getEmail());


        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, ViewAllActivity.class);
                intent.putExtra("id",chef_list_modelList.get(position).getId());
                context.startActivity(intent);
            }
        });



    }

    @Override
    public int getItemCount() {
        return chef_list_modelList.size();
    }

    public interface OnItemClickListener {
        void onClick(View view,int position);

    }

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{
        ImageView chefimage;
        TextView name,phoneNo,mailid,id;
        OnItemClickListener onItemClickListener;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            this.onItemClickListener=onItemClickListener;
            itemView.setOnClickListener(this);
            chefimage=itemView.findViewById(R.id.img1);
            name=itemView.findViewById(R.id.list_chef_name);
            phoneNo=itemView.findViewById(R.id.chef_list_phone);
            mailid=itemView.findViewById(R.id.chef_list_mail);
            itemView.setOnClickListener(this);

        }

        @Override
        public void onClick(View v) {
            onItemClickListener.onClick(v,getAdapterPosition());
        }


    }



//    public void setonItemClickListener(AdapterView.OnItemClickListener listener){
//        this.listener=listener;
//
//    }
}
