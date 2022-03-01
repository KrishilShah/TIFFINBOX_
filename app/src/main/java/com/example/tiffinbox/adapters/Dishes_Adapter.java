package com.example.tiffinbox.adapters;


import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.tiffinbox.DishesActivity;
import com.example.tiffinbox.DisplayFoodActivity;
import com.example.tiffinbox.models.DishData;
import com.example.tiffinbox.R;


import java.util.ArrayList;
import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

public class Dishes_Adapter extends RecyclerView.Adapter<Dishes_Adapter.ViewHolder> {
    private Context context;
    public Dishes_Adapter.OnItemClickListener onItemClickListener;
    private List<DishData> dishData;
    int totalView=1;
    int totalPrice=0;


    public Dishes_Adapter(Context context, List<DishData> dishData, OnItemClickListener onItemClickListener) {
        this.dishData = dishData;
        this.context = context;
        this.onItemClickListener = onItemClickListener;
    }

    public Dishes_Adapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new ViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.item_design, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, @SuppressLint("RecyclerView") int position) {
        holder.dname.setText(dishData.get(position).getDname());
        holder.price.setText(dishData.get(position).getDprice());
        Glide.with(holder.itemView)
                .load(dishData.get(position).getUrl())
                .into(holder.dishimg);

        holder.remove.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                totalView= Integer.parseInt(holder.quantity.getText().toString());
                if(totalView>1){
                    totalView--;
                    holder.quantity.setText(String.valueOf(totalView));
                    totalPrice= Integer.parseInt(dishData.get(position).getDprice())*Integer.parseInt(holder.quantity.getText().toString());
                    holder.price.setText(String.valueOf(totalPrice));
                }
            }
        });

        holder.add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                totalView= Integer.parseInt(holder.quantity.getText().toString());
                totalView++;
                holder.quantity.setText(String.valueOf(totalView));
                totalPrice= Integer.parseInt(dishData.get(position).getDprice())*Integer.parseInt(holder.quantity.getText().toString());
                holder.price.setText(String.valueOf(totalPrice));
            }
        });

//        holder.itemView.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
////                Toast.makeText(context,dishData.get(position).getDname(), Toast.LENGTH_SHORT).show();
//                // Intent intent = new Intent(context, DishesActivity.class);
//                // intent.putExtra("id",dishData.get(position).getDishId());
//                // context.startActivity(intent);
//            }
//        });

    }
    public int getItemCount() {
        return dishData.size();
    }

    public interface OnItemClickListener {
        void onClick(View view,int position);

    }
    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{
        CircleImageView dishimg;
        TextView dname,price,quantity;
        ImageView add,remove;
        Dishes_Adapter.OnItemClickListener onItemClickListener;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            this.onItemClickListener=onItemClickListener;
            itemView.setOnClickListener(this);
            dishimg=itemView.findViewById(R.id.dish_image);
            dname=itemView.findViewById(R.id.dish_name);
            price=itemView.findViewById(R.id.dish_totalPrice);
            quantity=itemView.findViewById(R.id.view);
            add=itemView.findViewById(R.id.add_item_1);
            remove=itemView.findViewById(R.id.remove_item);

        }

        @Override
        public void onClick(View v) {
            onItemClickListener.onClick(v,getAdapterPosition());
        }


    }

}