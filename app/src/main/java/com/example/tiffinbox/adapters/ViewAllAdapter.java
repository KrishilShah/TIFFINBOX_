package com.example.tiffinbox.adapters;



import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.tiffinbox.R;
import com.example.tiffinbox.models.ViewAllModel;

import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

public class ViewAllAdapter extends RecyclerView.Adapter<ViewAllAdapter.ViewHolder> {
    Context context;
    List<ViewAllModel> list;

    public ViewAllAdapter(Context context, List<ViewAllModel> list) {
        this.context = context;
        this.list = list;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new ViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.view_all_item,parent,false));
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {

        holder.udishname.setText(list.get(position).getDname());
        holder.uchefname.setText(list.get(position).getDdes());
        holder.udishprice.setText(String.valueOf(list.get(position).getDprice()));
        Glide.with(context).load(list.get(position).getUrl()).into(holder.udishimg);

    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        CircleImageView udishimg;
        TextView uchefname,udishprice,udishname;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            udishimg=itemView.findViewById(R.id.u_dish_image);
            udishname=itemView.findViewById(R.id.u_dish_name);
            uchefname=itemView.findViewById(R.id.u_chef_name);
            udishprice=itemView.findViewById(R.id.u_dish_price);


        }
    }
}
