package com.example.tiffinbox.adapters;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.tiffinbox.OrderDetailChefActivity;
import com.example.tiffinbox.R;
import com.example.tiffinbox.models.ChefOrderData;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;

public class OrderChefAdapter extends RecyclerView.Adapter<OrderChefAdapter.HolderOrderChef>{
    private Context context;
    private ArrayList<ChefOrderData> orderChefList;
    FirebaseAuth firebaseAuth = FirebaseAuth.getInstance();
    FirebaseFirestore db = FirebaseFirestore.getInstance();
    DocumentReference documentReference;
    String orderUserEmail;

    public OrderChefAdapter(Context context, ArrayList<ChefOrderData> orderChefList) {
        this.context = context;
        this.orderChefList = orderChefList;
    }


    @NonNull
    @Override
    public HolderOrderChef onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.row_order_chef, parent, false);

        return new HolderOrderChef(view);
    }

    @Override
    public void onBindViewHolder(@NonNull OrderChefAdapter.HolderOrderChef holder, int position) {

        //getdata
        ChefOrderData chefOrderData = orderChefList.get(position);
        String orderId = chefOrderData.getId();
        String orderCost = chefOrderData.getDishPrice();
        String orderStatus = chefOrderData.getOrderStatus();
        String orderDate = chefOrderData.getDishDate();
        String orderUserId = chefOrderData.getUserID();
        String orderTime = chefOrderData.getDishTime();
        String orderQuantity = chefOrderData.getTotalQuantity();

        //getChefInfo
        documentReference = db.collection("customers").document(orderUserId);
        documentReference.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if(task.getResult().exists()){
                    orderUserEmail =task.getResult().getString("email");
                    holder.userEmailTv.setText("User Email: " +orderUserEmail);
                }
                else{
                    Toast.makeText(context.getApplicationContext(), "Some error occured", Toast.LENGTH_SHORT).show();
                }
            }
        });

        //Setdata
        holder.amountTv.setText("Amount Rs: " +orderCost);
        holder.statusTv.setText(orderStatus);
        holder.orderIdTv.setText("OrderId: " +orderId);
        // holder.chefNameTv.setText("Chef Name: " +orderChefName);
        holder.dateTv.setText(orderDate);

        //Change order status color
        if(orderStatus.equals("In Progress")){
            holder.statusTv.setTextColor(context.getResources().getColor(android.R.color.holo_green_light));
        }
        else if(orderStatus.equals("Completed")){
            holder.statusTv.setTextColor(context.getResources().getColor(R.color.design_default_color_secondary                                                                                                                                                                                                                                                                                                                                                                                   ));
        }
        else if(orderStatus.equals("Cancelled")){
            holder.statusTv.setTextColor(context.getResources().getColor(R.color.design_default_color_error));
        }

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Open order details
                Intent intent = new Intent(context, OrderDetailChefActivity.class);
                intent.putExtra("orderId", orderId); //To load order info
                intent.putExtra(("orderBy"), orderUserId); //To load info of user who placed the order
                context.startActivity(intent);

            }
        });
    }

    @Override
    public int getItemCount() {
        return orderChefList.size();
    }
    class HolderOrderChef extends RecyclerView.ViewHolder{

        private TextView orderIdTv, dateTv, userEmailTv, amountTv, statusTv;

        public HolderOrderChef(@NonNull View itemView){
            super(itemView);

            orderIdTv = itemView.findViewById(R.id.orderIdTv);
            dateTv = itemView.findViewById(R.id.dateTv);
            userEmailTv = itemView.findViewById(R.id.userEmailTv);
            amountTv = itemView.findViewById(R.id.amountTv);
            statusTv = itemView.findViewById(R.id.statusTv);
        }
    }
}
