package com.example.tiffinbox.adapters;

import android.media.Image;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.tiffinbox.R;
import com.example.tiffinbox.models.ModelChef;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;

import de.hdodenhof.circleimageview.CircleImageView;

public class AdapterChef extends FirebaseRecyclerAdapter<ModelChef,AdapterChef.myviewholder> {
    View img;
    TextView chefname,price,dishname;


    public AdapterChef(@NonNull FirebaseRecyclerOptions<ModelChef> options) {
        super(options);
    }

    @Override
    protected void onBindViewHolder(@NonNull myviewholder holder, int position, @NonNull ModelChef model) {
    holder.price.setText(model.getDprice());
    holder.chefname.setText(model.getDdes());
    holder.dishname.setText(model.getDname());
    Glide.with(holder.img.getContext()).load(model.getUrl()).into(holder.img);
    }

    @NonNull
    @Override
    public myviewholder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.singlerowchefproducts,parent,false);
        return new myviewholder(view);


    }

    class myviewholder extends RecyclerView.ViewHolder{
        CircleImageView img;
        TextView chefname,price,dishname;
        public myviewholder(@NonNull View itemView) {
            super(itemView);
            img=itemView.findViewById(R.id.dish_image);
            chefname=itemView.findViewById(R.id.chef_name);
            price=itemView.findViewById(R.id.dish_price);
            dishname=itemView.findViewById(R.id.dish_name);


        }
    }
}
