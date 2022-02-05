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
import com.example.tiffinbox.models.MyCartModel;

import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

public class MyCartAdapter extends RecyclerView.Adapter<MyCartAdapter.viewHolder> {


    Context context;
    List<MyCartModel> cartModelList;
    public MyCartAdapter(Context context, List<MyCartModel> cartModelList){
        this.context = context;
        this.cartModelList = cartModelList;
    }

    @NonNull
    @Override
    public viewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new viewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.my_cart_item,parent, Boolean.parseBoolean("false")));

    }

    @Override
    public void onBindViewHolder(@NonNull viewHolder holder, int position) {

          Glide.with(holder.itemView)
                .load(cartModelList.get(position).getdurl())
                .into(holder.dishImage);

        holder.name.setText(cartModelList.get(position).getDishName());
        holder.totalPrice.setText(String.valueOf(cartModelList.get(position).getTotalPrice()));
        holder.totalQuantity.setText(String.valueOf(cartModelList.get(position).gettotalQuantity()));
        holder.date.setText(cartModelList.get(position).getdishDate());

    }

    @Override
    public int getItemCount() {
        return cartModelList.size();
    }

    public class viewHolder extends RecyclerView.ViewHolder {

        TextView name,date,totalPrice,totalQuantity;
        CircleImageView dishImage;

        public viewHolder(@NonNull View itemView) {
            super(itemView);
            dishImage=itemView.findViewById(R.id.dish_image);
            name=itemView.findViewById(R.id.dish_name);
            date=itemView.findViewById(R.id.dish_date);
            totalPrice=itemView.findViewById(R.id.dish_totalPrice);
            totalQuantity=itemView.findViewById(R.id.dish_QTY);


        }
    }
}
