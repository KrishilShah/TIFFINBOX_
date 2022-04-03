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

import com.example.tiffinbox.AddCouponCodesActivity;
import com.example.tiffinbox.ChefmainActivity;
import com.example.tiffinbox.DisplayFoodActivity;
import com.example.tiffinbox.R;
import com.example.tiffinbox.cheffragment.ChefOrderlistFragment;
import com.example.tiffinbox.models.ChefData;
import com.example.tiffinbox.models.CouponModel;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.List;

public class coupon extends RecyclerView.Adapter<coupon.ViewHolder>{

    private Context context;
    private List<CouponModel> couponModel;
    FirebaseFirestore db= FirebaseFirestore.getInstance();

    coupon couponAdapter;

    public coupon(Context context, List<CouponModel> couponModel) {
        this.context = context;
        this.couponModel=couponModel;
    }

    @NonNull
    @Override
    public coupon.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new ViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.activity_coupon_list,parent,false));
    }


    @Override
    public void onBindViewHolder(@NonNull coupon.ViewHolder holder, @SuppressLint("RecyclerView") int position) {
        //get data

//        String id = couponModel.get(position).getId();
//        String timestamp = couponModel.get(position).getTimestamp();
        String couponCode=couponModel.get(position).getCouponCode();
        String couponDescription  = couponModel.get(position).getCouponDescription();
        String minimumOrderPrice = couponModel.get(position).getMinimumorderprice();
        String couponExpiry= couponModel.get(position).getExpiryDate();
        String couponPrice= couponModel.get(position).getPrice();

        //set data:
        holder.coupon_Description.setText(couponDescription);
//        holder.coupon_expiry_date.setText(couponExpiry);
        holder.coupon_price.setText(couponPrice);
        holder.coupon_expiry_date.setText(couponExpiry);
        holder.coupon_name.setText(couponCode);
        holder.minimum_order_price.setText(minimumOrderPrice);

        holder.delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {


                db.collection("coupon").document(FirebaseAuth.getInstance().getCurrentUser().getUid())
                        .collection("CurrentUser").document(FirebaseAuth.getInstance().getCurrentUser().getUid()).
                        delete()
                        .addOnSuccessListener(new OnSuccessListener<Void>() {
                            @Override
                            public void onSuccess(Void unused) {
                                Intent intent = new Intent(context, ChefmainActivity.class);
                                intent.putExtra("fragment","coupon");
                                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK|Intent.FLAG_ACTIVITY_CLEAR_TASK);
                                context.startActivity(intent);


                            }
                        }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                    }
                });


            }
        });

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(context, AddCouponCodesActivity.class);
                intent.putExtra("value","update");

                intent.putExtra("position",position);
                intent.putExtra("id",couponModel.get(position).getId());
                intent.putExtra("writecode",couponModel.get(position).getCouponCode());
                intent.putExtra("desc",couponModel.get(position).getCouponDescription());
                intent.putExtra("price",couponModel.get(position).getPrice());
                intent.putExtra("expirydate",couponModel.get(position).getExpiryDate());
                intent.putExtra("minimumprice",couponModel.get(position).getMinimumorderprice());
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK|Intent.FLAG_ACTIVITY_CLEAR_TASK);
                context.startActivity(intent);

            }
        });







    }

    private void delete(int position){

        db.collection("AddToCart").document(FirebaseAuth.getInstance().getCurrentUser().getUid())
                .collection("CurrentUser").document(couponModel.get(position).getId()).
                delete()
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void unused) {


                    }
                }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
            }
        });

        couponAdapter.couponModel.remove(position);
        couponAdapter.notifyItemRemoved(position);
    }

    @Override
    public int getItemCount() {
        return couponModel.size();
    }


    public class ViewHolder extends RecyclerView.ViewHolder{

        TextView coupon_name,coupon_price,minimum_order_price,coupon_expiry_date,coupon_Description;
        ImageView delete;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            coupon_name=itemView.findViewById(R.id.coupon_name);
            coupon_price=itemView.findViewById(R.id.coupon_price);
            minimum_order_price=itemView.findViewById(R.id.minimum_order_price);
            coupon_expiry_date=itemView.findViewById(R.id.coupon_expiry_date);
            coupon_Description=itemView.findViewById(R.id.coupon_Description);
            delete=itemView.findViewById(R.id.delete);


        }




    }
}

