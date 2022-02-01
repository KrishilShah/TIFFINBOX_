package com.example.tiffinbox.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.FragmentActivity;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.tiffinbox.models.DishData;
import com.example.tiffinbox.R;


import java.util.ArrayList;

import de.hdodenhof.circleimageview.CircleImageView;


public class DishAdapter extends RecyclerView.Adapter<DishAdapter.ViewHolder>{

    Context context;
    ArrayList<DishData> dishData;

    public DishAdapter(FragmentActivity activity, ArrayList<DishData> dishData){
        this.dishData=dishData;
    }


    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v= LayoutInflater.from(parent.getContext()).inflate(R.layout.singlerowchefproducts,parent,false);
        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
            holder.dishname.setText(dishData.get(position).getDname());
            holder.dishdes.setText(dishData.get(position).getDdes());
            holder.dishprice.setText(dishData.get(position).getDprice());
            Glide.with(holder.itemView)
                .load(dishData.get(position).getUrl())
                .into(holder.dishimg);
    }

    @Override
    public int getItemCount() {
        return dishData.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder{
        CircleImageView dishimg;
        TextView dishdes,dishprice,dishname;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            dishimg=itemView.findViewById(R.id.dish_image);
            dishname=itemView.findViewById(R.id.dish_name);
            dishdes=itemView.findViewById(R.id.dish_description);
            dishprice=itemView.findViewById(R.id.dish_price);



        }
    }
}