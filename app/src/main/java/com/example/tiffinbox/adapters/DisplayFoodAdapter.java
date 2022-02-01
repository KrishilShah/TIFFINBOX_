package com.example.tiffinbox.adapters;



import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.tiffinbox.OrderFoodActivity;
import com.example.tiffinbox.R;
import com.example.tiffinbox.models.DishData;

import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

public class DisplayFoodAdapter extends RecyclerView.Adapter<DisplayFoodAdapter.ViewHolder> {
    Context context;

    List<DishData> list;
    public DisplayFoodAdapter.OnItemClickListener onItemClickListener;

    public DisplayFoodAdapter(Context context, List<DishData> list, OnItemClickListener onItemClickListener) {
        this.context = context;
        this.list = list;
        this.onItemClickListener=onItemClickListener;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new ViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.singlerowchefproducts,parent,false));
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, @SuppressLint("RecyclerView") int position) {

        holder.udishname.setText(list.get(position).getDname());
        holder.udishdes.setText(list.get(position).getDdes());
        holder.udishprice.setText(list.get(position).getDprice());
        Glide.with(context).load(list.get(position).getUrl()).into(holder.udishimg);

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, OrderFoodActivity.class);
                intent.putExtra("dishname",list.get(position).getDname());
                intent.putExtra("dishdes",list.get(position).getDdes());
                intent.putExtra("dishurl",list.get(position).getUrl());
                intent.putExtra("dishprice",list.get(position).getDprice());
                context.startActivity(intent);
            }
        });

    }

    @Override
    public int getItemCount() {
            return list.size();
    }
    public interface OnItemClickListener {
        void onClick(View view,int position);

    }

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        CircleImageView udishimg;
        TextView udishdes,udishprice,udishname;
        DisplayFoodAdapter.OnItemClickListener onItemClickListener;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            this.onItemClickListener=onItemClickListener;
            udishimg=itemView.findViewById(R.id.dish_image);
            udishname=itemView.findViewById(R.id.dish_name);
            udishdes=itemView.findViewById(R.id.dish_description);
            udishprice=itemView.findViewById(R.id.dish_price);
            itemView.setOnClickListener(this);


        }



        @Override
        public void onClick(View v) {
            onItemClickListener.onClick(v,getAdapterPosition());
        }
    }

}
