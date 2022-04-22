package com.example.tiffinbox.adapters;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.tiffinbox.cheffragment.*;

import java.util.*;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.tiffinbox.R;
import com.example.tiffinbox.models.MyCartModel;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.List;
import com.example.tiffinbox.userFragment.UserCartFragment;
import com.google.firebase.firestore.Transaction;

import de.hdodenhof.circleimageview.CircleImageView;

public class MyCartAdapter extends RecyclerView.Adapter<MyCartAdapter.viewHolder> {

    FirebaseFirestore db= FirebaseFirestore.getInstance();
    FirebaseAuth auth = FirebaseAuth.getInstance();
    DocumentReference documentReference;
    Context context;
    List<MyCartModel> cartModelList;
    int totalView=1;
    int totalPrice=0;
    double total_price=0;
    ConstraintLayout constrain1,constrain2;
    TextView totalCharge,totalBill,taxCharge,deliveryService;
    public OnItemClickListener onItemClickListener;

    String name,dishDate,durl,totalQuantity,dishPrice,orderStatus;

    public MyCartAdapter(Context context, List<MyCartModel> cartModelList, MyCartAdapter.OnItemClickListener onItemClickListener,TextView totalCharge,TextView taxCharge,TextView deliveryService,TextView totalBill,ConstraintLayout constrain1,ConstraintLayout constrain2){
        this.context = context;
        this.cartModelList = cartModelList;
        this.onItemClickListener = onItemClickListener;
        this.totalBill=totalBill;
        this.totalCharge=totalCharge;
        this.deliveryService=deliveryService;
        this.taxCharge=taxCharge;
        this.constrain1=constrain1;
        this.constrain2=constrain2;
    }

    @NonNull
    @Override
    public viewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view=LayoutInflater.from(parent.getContext()).inflate(R.layout.my_cart_item,parent, Boolean.parseBoolean("false"));
        return new viewHolder(view).linkAdapter(this);

    }



    @Override
    public void onBindViewHolder(@NonNull viewHolder holder, @SuppressLint("RecyclerView") final int position) {

        durl=cartModelList.get(position).getDurl();

        Glide.with(holder.itemView)
                .load(durl)
                .into(holder.dishImage);


        name=cartModelList.get(position).getDishName();
        dishPrice=cartModelList.get(position).getDishPrice();
        dishDate=cartModelList.get(position).getDishDate();
        orderStatus="In Progress";


        holder.name.setText(name);
        holder.totalPrice.setText("Rs "+cartModelList.get(position).getTotalPrice());
        holder.dishPrice.setText("Rs "+dishPrice);
        holder.totalQuantity.setText(String.valueOf(cartModelList.get(position).getTotalQuantity()));
        holder.date.setText(dishDate);

//        holder.removeItem.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//

////                cartModelList.remove(position);
//            }
//        });

        holder.remove.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                totalView= Integer.parseInt(holder.totalQuantity.getText().toString());
                if(totalView>1){
                    totalView--;
                    holder.totalQuantity.setText(String.valueOf(totalView));
                    totalPrice= Integer.parseInt(cartModelList.get(position).getDishPrice())*Integer.parseInt(holder.totalQuantity.getText().toString());
                    holder.totalPrice.setText("$"+totalPrice);

                    update(name,totalPrice,dishDate,cartModelList.get(position).getDishTime(),durl,holder.totalQuantity.getText().
                            toString(),dishPrice,cartModelList.get(position).getDishDescription(),cartModelList.get(position).getId(),cartModelList.get(position).getChefID(),orderStatus);

//                    onItemClickListener.changed();

                }
            }
        });

        holder.add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                totalView= Integer.parseInt(holder.totalQuantity.getText().toString());
                totalView++;
                holder.totalQuantity.setText(String.valueOf(totalView));
                totalPrice= Integer.parseInt(cartModelList.get(position).getDishPrice())*Integer.parseInt(holder.totalQuantity.getText().toString());
                holder.totalPrice.setText("$"+totalPrice);
                update(name,totalPrice,dishDate,cartModelList.get(position).getDishTime(),durl,holder.totalQuantity.getText().
                        toString(),dishPrice,cartModelList.get(position).getDishDescription(),cartModelList.get(position).getId(),cartModelList.get(position).getChefID(),orderStatus);

//                onItemClickListener.changed();
            }
        });

    }

    private void update(String dishName, int totalPrice, String dishDate, String dishTime, String durl, String totalQuantity, String dishPrice, String dishDescription, String id, String chefID, String orderStatus){

        MyCartModel cartModel= new MyCartModel(dishName,totalPrice,dishDate,dishTime,durl,totalQuantity,dishPrice,dishDescription,id,chefID,orderStatus);

        db.collection("AddToCart").document(auth.getCurrentUser().getUid())
                .collection("CurrentUser").document(id).
                set(cartModel)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void unused) {
//                        Toast.makeText(getActivity(), "Profile Updated", Toast.LENGTH_SHORT).show();
                    }
                }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
//                Toast.makeText(getActivity(), "Some Error Occured", Toast.LENGTH_SHORT).show();
            }
        });


    }

    public interface OnItemClickListener {
        void onDelete();
        void changed();
        void onClick(View v);
    }

    @Override
    public int getItemCount() {
        return cartModelList.size();
    }

    public class viewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{

        TextView name,date,totalPrice,totalQuantity,dishPrice,removeItem;
        CircleImageView dishImage;
        ImageView add,remove;
        MyCartAdapter.OnItemClickListener onItemClickListener;

        private MyCartAdapter myCartAdapter;



        public viewHolder(@NonNull View itemView) {
            super(itemView);

//            this.onItemClickListener=onItemClickListener;
            dishImage=itemView.findViewById(R.id.dish_image);
            name=itemView.findViewById(R.id.dish_name);
            date=itemView.findViewById(R.id.dish_date);
            totalPrice=itemView.findViewById(R.id.dish_totalPrice);
            totalQuantity=itemView.findViewById(R.id.dish_QTY);
            dishPrice=itemView.findViewById(R.id.dish_Price);
            add=itemView.findViewById(R.id.add_item_1);
            remove=itemView.findViewById(R.id.remove_item_1);
            removeItem=itemView.findViewById(R.id.remove);

            removeItem.setOnClickListener(view -> {




                db.collection("AddToCart").document(auth.getCurrentUser().getUid())
                        .collection("CurrentUser").document(cartModelList.get(getAdapterPosition()).getId()).
                        delete()
                        .addOnSuccessListener(new OnSuccessListener<Void>() {
                            @Override
                            public void onSuccess(Void unused) {
//                        Toast.makeText(getActivity(), "Profile Updated", Toast.LENGTH_SHORT).show();
                            }
                        }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
//                Toast.makeText(getActivity(), "Some Error Occured", Toast.LENGTH_SHORT).show();
                    }
                });

                myCartAdapter.cartModelList.remove(getAdapterPosition());
                myCartAdapter.notifyDataSetChanged();
                updateBill();

                //onItemClickListener.changed();



//                UserCartFragment userCartFragment = new UserCartFragment();
//                userCartFragment.getRecyclerView();
//                userCartFragment.calculateCart();

            });


        }


        public void updateBill(){
            double percentax = 0.02;
            total_price=0;
            double delivery = 10;
            double tax;
            int size=cartModelList.size();


            if(size==0){
                totalCharge.setText("Rs 0");
                taxCharge.setText("Rs 0");
                deliveryService.setText("Rs 0");
                totalBill.setText("Rs 0");
                constrain2.setVisibility(View.GONE);
                constrain1.setVisibility(View.VISIBLE);

            }
            else {
                double price;
                for (int i = 0; i < size; i++) {
                    price = cartModelList.get(i).getTotalPrice();
                    total_price = total_price + price;
                }

                tax = Math.round((total_price * percentax) * 100.0) / 100.0;
                double total = Math.round((total_price + tax + delivery) * 100.0) / 100.0;

                totalCharge.setText("Rs " + total_price);
                taxCharge.setText("Rs " + tax);
                deliveryService.setText("Rs " + delivery);
                totalBill.setText("Rs " + total);
            }
        }


        public viewHolder linkAdapter(MyCartAdapter myCartAdapter){
            this.myCartAdapter=myCartAdapter;
            return this;
        }



        @Override
        public void onClick(View v) {
//            onItemClickListener.onDelete(getAdapterPosition());
        }

    }


}