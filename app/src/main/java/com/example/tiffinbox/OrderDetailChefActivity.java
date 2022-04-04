package com.example.tiffinbox;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.RecyclerView;

import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.example.tiffinbox.adapters.MyCartAdapter;
import com.example.tiffinbox.models.ChefOrderData;
import com.example.tiffinbox.models.MyCartModel;
import com.example.tiffinbox.userFragment.UserOrderlistFragment;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.firestore.Transaction;

import org.json.JSONObject;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class OrderDetailChefActivity extends AppCompatActivity {

    ImageButton editBtn, mapBtn, backBtn;
    String orderId;
    TextView orderID, orderDate, orderStatus, userEmail, userPhone, userAddress, items, amount;
    String orderBy;
    private RecyclerView itemsRv;
    FirebaseFirestore db ;
    FirebaseAuth firebaseAuth;
    MyCartAdapter cartAdapter;
    List<MyCartModel> cartModelList;


    DocumentReference documentReference;
    private String onlineUserID;
    String orderUserEmail, orderUserPhone, orderUserAddress;
    double sourceLatitude, sourceLongitude, destinationLatitude, destinationLongitude;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_order_detail_chef);

        editBtn = findViewById(R.id.editBtn);
        mapBtn = findViewById(R.id.mapBtn);
        backBtn = findViewById(R.id.backBtn);
        orderID = findViewById(R.id.orderId);
        orderDate = findViewById(R.id.orderDate);
        orderStatus = findViewById(R.id.orderStatus);
        userEmail = findViewById(R.id.userEmail);
        userPhone = findViewById(R.id.userPhone);
        userAddress = findViewById(R.id.userHome);
        items = findViewById(R.id.items);
        amount = findViewById(R.id.amount);
        itemsRv = findViewById(R.id.itemsRv);


        orderId = getIntent().getStringExtra("orderId");
        orderBy = getIntent().getStringExtra("orderBy");

        db=FirebaseFirestore.getInstance();
        firebaseAuth = FirebaseAuth.getInstance();
        onlineUserID = FirebaseAuth.getInstance().getCurrentUser().getUid();
        loadMyInfo();

        mapBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openMap();
            }
        });

        editBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //edit Order Status In Progress, Completed, Cancelled
                editOrderStatusDialog();
            }
        });

    }

    private void editOrderStatusDialog() {
        String[] options = {"In Progress", "Completed", "Cancelled"};
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Edit Order Status")
                .setItems(options, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        //handle item clicks
                        String selectedOptions = options[which];
                        editOrderStatus(selectedOptions);
                    }
                })
                .show();

    }

    private void editOrderStatus(String selectedOptions) {
        documentReference = db.collection("ChefOrders").document(onlineUserID).collection("CurrentChef").document(orderId);
        db.runTransaction(new Transaction.Function<Void>() {
            @Nullable
            @Override
            public Void apply(@NonNull Transaction transaction) throws FirebaseFirestoreException {
                DocumentSnapshot snapshot = transaction.get(documentReference);

                transaction.update(documentReference, "orderStatus", selectedOptions);

                return  null;

            }
        }).addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void unused) {
                String message = "Order is now "+selectedOptions;
                //status updated
                Toast.makeText(getApplicationContext(), message, Toast.LENGTH_SHORT).show();
                prepareNotificationMessage(orderId,message);

            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                //update failed
                Toast.makeText(getApplicationContext(), ""+e.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void openMap() {
        //saddr means soruce address
        //daddr means destination address
        String address = "https://maps.google.com/maps?saddr=" + sourceLatitude + "," +sourceLongitude + "&daddr=" + destinationLatitude + "," + destinationLongitude;
        Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(address));
        startActivity(intent);

    }

    private void loadMyInfo() {
        db.collection("chefs").document(onlineUserID)
                .get()
                .addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                        if(task.getResult().exists()){
                            sourceLatitude = task.getResult().getDouble("lat");
                            sourceLongitude = task.getResult().getDouble("lon");
                        }
                    }
                });
        documentReference = db.collection("customers").document(orderBy);
        documentReference.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if(task.getResult().exists()){
                    orderUserEmail =task.getResult().getString("email");
                    orderUserPhone = task.getResult().getString("phone");
                    orderUserAddress = task.getResult().getString("address");

                    userEmail.setText(orderUserEmail);
                    userPhone.setText(orderUserPhone);
                    userAddress.setText(orderUserAddress);
                }
                else{
                    Toast.makeText(getApplicationContext(), "Some error occured", Toast.LENGTH_SHORT).show();
                }
            }
        });

        db.collection("ChefOrders").document(onlineUserID).collection("CurrentChef").document(orderId)
                .get()
                .addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                        if(task.getResult().exists()){
                            String orderId = task.getResult().getString("id");
                            String date = task.getResult().getString("dishDate");
                            String status = task.getResult().getString("orderStatus");
                            String totalQuantity = task.getResult().getString("totalQuantity");
                            String totalPrice = task.getResult().getLong("totalPrice").toString();
                            destinationLatitude = task.getResult().getDouble("latitude");
                            destinationLongitude = task.getResult().getDouble("longitude");

                            orderID.setText(orderId);
                            orderDate.setText(date);
                            orderStatus.setText(status);
                            items.setText(totalQuantity);
                            amount.setText(totalPrice);


                        }
                        else{
                            Toast.makeText(getApplicationContext(),"No Order Exists", Toast.LENGTH_SHORT);
                        }

                    }
                });

    }
    public void back(View view) {
        Intent intent=new Intent(getApplicationContext(),ChefmainActivity.class);
        intent.putExtra("fragment","home");
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK|Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(intent);
    }
    private void prepareNotificationMessage(String orderId,String message){
        //When user places order, send notification to seller
        //prepare data for notification
        String NOTIFICATION_TOPIC = "/topics/" + Constant.FCM_TOPIC; //must be same as subscribed by user
        String NOTIFICATION_TITLE = "Your Order "+ orderId;
        String NOTIFICATION_MESSAGE = ""+message;
        String NOTIFICATION_TYPE = "OrderStatusChanged";
        //prepare json (what to send and where to send)
        JSONObject notificationJo = new JSONObject();
        JSONObject notificationBodyJo = new JSONObject();
        try {
//            String userid=orderBy;
            //what to send

            notificationBodyJo.put("notificationType", NOTIFICATION_TYPE);
            notificationBodyJo.put("buyerUid", orderBy); //since we are logged in as b
            notificationBodyJo.put("sellerUid", onlineUserID);
            notificationBodyJo.put("orderId", orderId);
            notificationBodyJo.put("notificationTitle", NOTIFICATION_TITLE);
            notificationBodyJo.put("notificationMessage", NOTIFICATION_MESSAGE);
            //where to send
            notificationJo.put("to", NOTIFICATION_TOPIC); //to all who subscribed to this topic
            notificationJo.put("data", notificationBodyJo);
        }
        catch (Exception e ){
            Toast.makeText(OrderDetailChefActivity.this, "orderdetchefact preparenoti"+e.getMessage(), Toast.LENGTH_SHORT).show();
        }

        sendFcmNotification(notificationJo);
    }

    private void sendFcmNotification(JSONObject notificationJo) {
        JsonObjectRequest jsonobjectRequest = new JsonObjectRequest("https://fcm.googleapis.com/fcm/send", notificationJo, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {



            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

            }
        }){
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                Map<String,String >headers=new HashMap<>();
                headers.put( "Content-Type", "application/json");
                headers.put( "Authorization", "key=" + Constant.FCM_KEY);
//                return super.getHeaders();
                return  headers;
            }

        };
//        Volley.newRequestQueue(getContext().add(jsonobjectRequest);
        Volley.newRequestQueue(this).add(jsonobjectRequest);


    }
}