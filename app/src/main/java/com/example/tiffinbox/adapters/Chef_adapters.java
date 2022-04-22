package com.example.tiffinbox.adapters;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.tiffinbox.R;
import com.example.tiffinbox.DisplayFoodActivity;
import com.example.tiffinbox.models.ChefData;

import java.util.List;

public class Chef_adapters extends RecyclerView.Adapter<Chef_adapters.ViewHolder> {
    //   private AdapterView.OnItemClickListener listener;
    public Chef_adapters.OnItemClickListener onItemClickListener;
    private Context context;
    private List<ChefData> chefData;



    public Chef_adapters(Context context, List<ChefData> chefData, OnItemClickListener onItemClickListener) {
        this.context = context;
        this.chefData = chefData;
        this.onItemClickListener=onItemClickListener;


    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new ViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.chef_list_display,parent,false));
    }


    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, @SuppressLint("RecyclerView") int position) {
//        Glide.with(context).load(chefData.get(position).getPurl()).into(holder.chefimage);
        holder.name.setText(chefData.get(position).getName());
        holder.speciality.setText(chefData.get(position).getSpeciality());
//        holder.phoneNo.setText(chefData.get(position).getPhone());
//        holder.mailid.setText(chefData.get(position).getEmail());


        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, DisplayFoodActivity.class);
                intent.putExtra("id",chefData.get(position).getId());
                context.startActivity(intent);
            }
        });



    }

    @Override
    public int getItemCount() {
        return chefData.size();
    }

    public interface OnItemClickListener {
        void onClick(View view,int position);

    }

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{
        ImageView chefimage;
        TextView name,phoneNo,mailid,id, speciality;
        OnItemClickListener onItemClickListener;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            this.onItemClickListener=onItemClickListener;
            itemView.setOnClickListener(this);
            chefimage=itemView.findViewById(R.id.img1);
            name=itemView.findViewById(R.id.list_chef_name);
            speciality = itemView.findViewById(R.id.chef_list_speciality);
//            phoneNo=itemView.findViewById(R.id.chef_list_phone);
//            mailid=itemView.findViewById(R.id.chef_list_mail);
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
