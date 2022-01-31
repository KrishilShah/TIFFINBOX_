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
import com.example.tiffinbox.models.DishData;

import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

public class DisplayFoodAdapter extends RecyclerView.Adapter<DisplayFoodAdapter.ViewHolder> {
    Context context;

    List<DishData> list;

    public DisplayFoodAdapter(Context context, List<DishData> list) {
        this.context = context;
        this.list = list;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new ViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.singlerowchefproducts,parent,false));
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {

        holder.udishname.setText(list.get(position).getDname());
        holder.udishdes.setText(list.get(position).getDdes());
        holder.udishprice.setText(list.get(position).getDprice());
        Glide.with(context).load(list.get(position).getUrl()).into(holder.udishimg);

    }

    @Override
    public int getItemCount() {
            return list.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        CircleImageView udishimg;
        TextView udishdes,udishprice,udishname;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            udishimg=itemView.findViewById(R.id.dish_image);
            udishname=itemView.findViewById(R.id.dish_name);
            udishdes=itemView.findViewById(R.id.dish_description);
            udishprice=itemView.findViewById(R.id.dish_price);


        }
    }
}
