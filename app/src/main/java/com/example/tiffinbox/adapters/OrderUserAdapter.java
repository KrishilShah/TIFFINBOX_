package com.example.tiffinbox.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.FragmentActivity;
import androidx.recyclerview.widget.RecyclerView;

import com.example.tiffinbox.R;
import com.example.tiffinbox.models.MyCartModel;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.text.DateFormat;
import java.util.ArrayList;
import java.util.Calendar;

public class OrderUserAdapter extends RecyclerView.Adapter<OrderUserAdapter.HolderOrderUser>{
    private Context context;
    private ArrayList<MyCartModel> orderUserList;
    FirebaseAuth firebaseAuth = FirebaseAuth.getInstance();
    FirebaseFirestore db = FirebaseFirestore.getInstance();
    DocumentReference documentReference;
    String orderChefName;

    public OrderUserAdapter(Context context, ArrayList<MyCartModel> orderUserList) {
        this.context = context;
        this.orderUserList = orderUserList;
    }

    @NonNull
    @Override
    public HolderOrderUser onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(context).inflate(R.layout.row_order_user, parent, false);

        return new HolderOrderUser(view);
    }

    @Override
    public void onBindViewHolder(@NonNull HolderOrderUser holder, int position) {
        //getdata
        MyCartModel myCartModel = orderUserList.get(position);
        String orderId = myCartModel.getId();
        String orderCost = myCartModel.getDishPrice();
        String orderStatus = myCartModel.getOrderStatus();
        String orderDate = myCartModel.getDishDate();
        String orderChefId = myCartModel.getChefID();
        String orderTime = myCartModel.getDishTime();
        String orderQuantity = myCartModel.getTotalQuantity();

        //getChefInfo
        documentReference = db.collection("chefs").document(orderChefId);
        documentReference.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if(task.getResult().exists()){
                    orderChefName =task.getResult().getString("name");
                    holder.chefNameTv.setText("Chef Name: " +orderChefName);
                }
                else{
                    Toast.makeText(context.getApplicationContext(), "Some error occured", Toast.LENGTH_SHORT).show();
                }
            }
        });

        //Setdata
        holder.amountTv.setText("Amount Rs." +orderCost);
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

        //convert timestamp to proper format
//        Calendar calendar = Calendar.getInstance();
//        calendar.setTimeInMillis(Long.parseLong(orderTime));
//        String formattedDate = DateFormat.getInstance().format(calendar.getTime());


    }

    @Override
    public int getItemCount() {
        return orderUserList.size();
    }

    class HolderOrderUser extends RecyclerView.ViewHolder{

        private TextView orderIdTv, dateTv, chefNameTv, amountTv, statusTv;

        public HolderOrderUser(@NonNull View itemView){
            super(itemView);

            orderIdTv = itemView.findViewById(R.id.orderIdTv);
            dateTv = itemView.findViewById(R.id.dateTv);
            chefNameTv = itemView.findViewById(R.id.chefNameTv);
            amountTv = itemView.findViewById(R.id.amountTv);
            statusTv = itemView.findViewById(R.id.statusTv);
        }
    }
}
