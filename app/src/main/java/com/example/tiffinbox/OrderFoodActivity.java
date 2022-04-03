//package com.example.tiffinbox;
//
//import androidx.appcompat.app.AppCompatActivity;
//import androidx.appcompat.widget.Toolbar;
//
//import android.content.Intent;
//import android.os.Bundle;
//import android.view.View;
//import android.widget.ImageView;
//import android.widget.TextView;
//
//import com.squareup.picasso.Picasso;
//import androidx.annotation.NonNull;
//import androidx.appcompat.app.AppCompatActivity;
//
//import android.os.Bundle;
//import android.view.View;
//import android.widget.Button;
//import android.widget.TextView;
//import android.widget.Toast;
//
//import com.example.tiffinbox.adapters.DishAdapter;
//import com.example.tiffinbox.adapters.DisplayFoodAdapter;
//import com.example.tiffinbox.models.DishData;
//import com.google.android.gms.tasks.OnCompleteListener;
//import com.google.android.gms.tasks.Task;
//import com.google.firebase.auth.FirebaseAuth;
//import com.google.firebase.firestore.DocumentReference;
//import com.google.firebase.firestore.FirebaseFirestore;
//
//import java.text.SimpleDateFormat;
//import java.util.Calendar;
//import java.util.HashMap;
//import java.util.Objects;
//import java.util.SimpleTimeZone;
//
//public class OrderFoodActivity extends AppCompatActivity {
//
//    Button addToCart,increase,decrease;
//    FirebaseFirestore firestore;
//    int totalPrice=0;
//    int totalQuantity=1;
//    FirebaseAuth auth;
//    String dname,ddes,dprice,durl;
//    int Dprice;
//    DishData dishData;
//
//    ImageView dishimage;
//    TextView dishdes,dishprice,dishname,quantity;
//    Toolbar toolbar;
//
//    @Override
//    protected void onCreate(Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//        setContentView(R.layout.activity_order_food);
//        toolbar=(Toolbar) findViewById(R.id.toolbar);
//
//        dishimage=findViewById(R.id.dish_image);
//        dishdes=findViewById(R.id.dish_des);
//        dishprice=findViewById(R.id.dish_price);
//        dishname=findViewById(R.id.dish_name);
//        quantity=findViewById(R.id.quantity);
//
//
//
//        dname=getIntent().getStringExtra("dishname");
//        ddes=getIntent().getStringExtra("dishdes");
//        dprice=getIntent().getStringExtra("dishprice");
//        durl=getIntent().getStringExtra("dishurl");
//
//        totalPrice= Integer.parseInt(dprice)*Integer.parseInt(quantity.getText().toString());
//
//        firestore= FirebaseFirestore.getInstance();
//        auth=FirebaseAuth.getInstance();
//        addToCart= findViewById(R.id.add2cart);
//
//
//
//        dishname.setText(dname);
//        dishprice.setText(dprice);
//        dishdes.setText(ddes);
//        Picasso.get().load(durl).into(dishimage);
//
//
//
//        increase.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                totalQuantity++;
//                totalPrice=totalQuantity*(Integer.parseInt(dprice));
//
//                quantity.setText(String.valueOf(totalQuantity));
//                dishprice.setText(String.valueOf(totalPrice));
//
//
//            }
//        });
//
//        decrease.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                if(totalQuantity==0)
//                {
//                    return;
//                }
//                else{
//                    totalQuantity++;
//                    totalPrice=totalQuantity*(Integer.parseInt(dprice));
//
//                    quantity.setText(String.valueOf(totalQuantity));
//                    dishprice.setText(String.valueOf(totalPrice));
//                }
//
//            }
//        });
//
//        addToCart.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                addedToCart();
//            }
//        });
//
//
//    }
//    private void addedToCart(){
//        String saveCurrentDate,saveCurrentTime;
//        Calendar calForDate= Calendar.getInstance();
//
//        SimpleDateFormat currentDate = new SimpleDateFormat("MM dd, yyyy");
//        saveCurrentDate= currentDate.format(calForDate.getTime());
//
//        SimpleDateFormat currentTime = new SimpleDateFormat("HH:mm:ss a");
//        saveCurrentTime= currentTime.format(calForDate.getTime());
//
//        final HashMap<String,Object> cartMap = new HashMap<>();
//
//        cartMap.put("dishName", dname);
//        cartMap.put("dishDescription", ddes);
//        cartMap.put("dishDate", saveCurrentDate);
//        cartMap.put("dishTime", saveCurrentTime);
//        cartMap.put("dishPrice",dprice);
//        cartMap.put("totalQuantity", quantity.getText().toString());
//        cartMap.put("totalPrice", totalPrice);
//        cartMap.put("durl",durl);
//
//        firestore.collection("AddToCart").document(Objects.requireNonNull(auth.getCurrentUser()).getUid())
//                .collection("CurrentUser").add(cartMap).addOnCompleteListener(new OnCompleteListener<DocumentReference>() {
//            @Override
//            public void onComplete(@NonNull Task<DocumentReference> task) {
//                Toast.makeText(OrderFoodActivity.this,"Added To Cart", Toast.LENGTH_SHORT).show();
//                Intent intent= new Intent(getApplicationContext(),UserhomeActivity.class);
//                startActivity(intent);
//                finish();
//            }
//        });
//    }
//
//    public void back(View view) {
//
//        finish();
//    }
//}


package com.example.tiffinbox;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.tiffinbox.models.ChefOrderData;
import com.example.tiffinbox.models.MyCartModel;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.firestore.DocumentSnapshot;
import com.squareup.picasso.Picasso;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.example.tiffinbox.adapters.DishAdapter;
import com.example.tiffinbox.adapters.DisplayFoodAdapter;
import com.example.tiffinbox.models.DishData;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Objects;
import java.util.SimpleTimeZone;

public class OrderFoodActivity extends AppCompatActivity {

    TextView view;
    Double longitude;
    Double latitude;
    int totalView=1;
    Button addToCart;
    Button addMore;
    FirebaseAuth auth;
    int totalPrice=0;
    FirebaseFirestore db = FirebaseFirestore.getInstance();
    DocumentReference documentReference;
    String ruid, onlineUserID, chefID;


    String dname,ddes,dprice,durl;
    int Dprice;
    DishData dishData;
    Button increase, decrease;

    ImageView dishimage;
    TextView dishdes,dishprice,dishname,quantity;
    String orderStatus = "In Progress";
    Toolbar toolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_order_food);
        toolbar=(Toolbar) findViewById(R.id.toolbar);

        addMore=findViewById(R.id.addmore);

        addMore.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(OrderFoodActivity.this, DishesActivity.class);
                intent.putExtra("cid",getIntent().getStringExtra("cid"));
                startActivity(intent);
            }
        });

        dishimage=findViewById(R.id.dish_image);
        dishdes=findViewById(R.id.dish_des);
        dishprice=findViewById(R.id.dish_price);
        dishname=findViewById(R.id.dish_name);
        quantity=findViewById(R.id.quantity);
        increase=findViewById(R.id.increase);
        decrease=findViewById(R.id.decrease);


        dname=getIntent().getStringExtra("dishname");
        ddes=getIntent().getStringExtra("dishdes");
        dprice=getIntent().getStringExtra("dishprice");
        durl=getIntent().getStringExtra("dishurl");

        totalPrice= Integer.parseInt(dprice)*Integer.parseInt(quantity.getText().toString());

//        db= FirebaseFirestore.getInstance();
        auth=FirebaseAuth.getInstance();

        onlineUserID = auth.getCurrentUser().getUid();
        documentReference = db.collection("customers").document(auth.getCurrentUser().getUid());
        addToCart= findViewById(R.id.add2cart);

        dishname.setText(dname);
        dishprice.setText(dprice);
        dishdes.setText(ddes);
        Picasso.get().load(durl).into(dishimage);

        documentReference.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if(task.getResult().exists()){
                    latitude=task.getResult().getDouble("lat");
                    longitude=task.getResult().getDouble("lon");
                }
                else{
                    Toast.makeText(getApplicationContext(),"No Profile Exists", Toast.LENGTH_SHORT);
                }
            }
        })
                .addOnFailureListener(new OnFailureListener() {
                    @Override public void onFailure(@NonNull Exception e) {

                    }
                });

        addToCart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                addedToCart();
//                sendOrdertoChef();


            }

            private void addedToCart() {
                String saveCurrentDate, saveCurrentTime;
                Calendar calForDate = Calendar.getInstance();

                SimpleDateFormat currentDate = new SimpleDateFormat("MM dd, yyyy");
                saveCurrentDate = currentDate.format(calForDate.getTime());

                SimpleDateFormat currentTime = new SimpleDateFormat("HH:mm:ss a");
                saveCurrentTime = currentTime.format(calForDate.getTime());

                final HashMap<String, Object> cartMap = new HashMap<>();

                cartMap.put("dishName", dname);
                cartMap.put("dishDescription", ddes);
                cartMap.put("dishDate", saveCurrentDate);
                cartMap.put("dishTime", saveCurrentTime);
                cartMap.put("dishPrice", dprice);
                cartMap.put("totalQuantity", quantity.getText().toString());
                cartMap.put("totalPrice", totalPrice);
                cartMap.put("durl", durl);
                cartMap.put("orderStatus", orderStatus);


//                String ruid=cartRef.push().getKey();        //cart id
                ruid = db.collection("AddToCart").document(auth.getCurrentUser().getUid())
                        .collection("CurrentUser").document().getId();

                cartMap.put("id",ruid);
                //** to store dish details in database
                chefID = getIntent().getStringExtra("cid");
                MyCartModel cartModel= new MyCartModel(dname,totalPrice,saveCurrentDate,saveCurrentTime,durl,quantity.getText().toString(),dprice,ddes,ruid,chefID,orderStatus);

//                firestore.collection("AddToCart").document(Objects.requireNonNull(auth.getCurrentUser()).getUid())
//                        .collection("CurrentUser").document(ruid).set(cartModel).addOnCompleteListener(new OnCompleteListener<DocumentReference>() {
//                    @Override
//                    public void onComplete(@NonNull Task<DocumentReference> task) {
//                        Toast.makeText(OrderFoodActivity.this, "Added To Cart", Toast.LENGTH_SHORT).show();
//                        Intent intent = new Intent(getApplicationContext(), UserhomeActivity.class);
//                        startActivity(intent);
//                        finish();
//                    }
//                });

                db.collection("AddToCart").document(auth.getCurrentUser().getUid())
                        .collection("CurrentUser").document(ruid).set(cartModel).addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void unused) {
                        Toast.makeText(getApplicationContext(), "Dish added successfully", Toast.LENGTH_SHORT).show();


                        Intent intent = new Intent(getApplicationContext(), UserhomeActivity.class);
                        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK|Intent.FLAG_ACTIVITY_CLEAR_TASK);
                        startActivity(intent);
                        finish();
                    }
                });
            }

            private void sendOrdertoChef() {
                String saveCurrentDate, saveCurrentTime;
                Calendar calForDate = Calendar.getInstance();

                SimpleDateFormat currentDate = new SimpleDateFormat("dd/MM/yyyy");
                saveCurrentDate = currentDate.format(calForDate.getTime());

                SimpleDateFormat currentTime = new SimpleDateFormat("HH:mm:ss a");
                saveCurrentTime = currentTime.format(calForDate.getTime());

                final HashMap<String, Object> cartMap = new HashMap<>();

                cartMap.put("dishName", dname);
                cartMap.put("dishDescription", ddes);
                cartMap.put("dishDate", saveCurrentDate);
                cartMap.put("dishTime", saveCurrentTime);
                cartMap.put("dishPrice", dprice);
                cartMap.put("totalQuantity", quantity.getText().toString());
                cartMap.put("totalPrice", totalPrice);
                cartMap.put("durl", durl);
                cartMap.put("orderStatus", orderStatus);
                cartMap.put("id",ruid);
                cartMap.put("userID",onlineUserID);
                cartMap.put("chefID", chefID);
                cartMap.put("latitude", latitude);
                cartMap.put("latitude", longitude);

                ChefOrderData chefOrderData = new ChefOrderData(onlineUserID, dname, saveCurrentTime, dprice, saveCurrentDate,ruid, quantity.getText().toString(), durl, ddes, chefID, orderStatus, totalPrice, latitude, longitude);

                db.collection("ChefOrders").document(chefID)
                        .collection("CurrentChef").document(ruid).set(chefOrderData).addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void unused) {
                        Toast.makeText(getApplicationContext(), "Order Sent to Chef", Toast.LENGTH_SHORT).show();


                        Intent intent = new Intent(getApplicationContext(), UserhomeActivity.class);
                        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK|Intent.FLAG_ACTIVITY_CLEAR_TASK);
                        startActivity(intent);
                        finish();
                    }
                });


            }
        });
        increase.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                    totalView++;
                    quantity.setText(String.valueOf(totalView));
                    totalPrice= Integer.parseInt(dprice)*Integer.parseInt(quantity.getText().toString());
                    dishprice.setText(String.valueOf(totalPrice));

            }
        });

        decrease.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(totalView>1){
                    totalView--;
                    quantity.setText(String.valueOf(totalView));
                    totalPrice= Integer.parseInt(dprice)*Integer.parseInt(quantity.getText().toString());
                    dishprice.setText(String.valueOf(totalPrice));
                }

            }
        });

    }


    public void back(View view) {
        finish();

    }
}