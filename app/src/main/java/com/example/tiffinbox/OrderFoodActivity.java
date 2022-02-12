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
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

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

    Button addToCart;
    FirebaseFirestore firestore;
    int totalPrice=0;
    FirebaseAuth auth;
    String dname,ddes,dprice,durl;
    int Dprice;
    DishData dishData;

    ImageView dishimage;
    TextView dishdes,dishprice,dishname,quantity;
    Toolbar toolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_order_food);
        toolbar=(Toolbar) findViewById(R.id.toolbar);

        dishimage=findViewById(R.id.dish_image);
        dishdes=findViewById(R.id.dish_des);
        dishprice=findViewById(R.id.dish_price);
        dishname=findViewById(R.id.dish_name);
        quantity=findViewById(R.id.quantity);



        dname=getIntent().getStringExtra("dishname");
        ddes=getIntent().getStringExtra("dishdes");
        dprice=getIntent().getStringExtra("dishprice");
        durl=getIntent().getStringExtra("dishurl");

        totalPrice= Integer.parseInt(dprice)*Integer.parseInt(quantity.getText().toString());

        firestore= FirebaseFirestore.getInstance();
        auth=FirebaseAuth.getInstance();
        addToCart= findViewById(R.id.add2cart);

        addToCart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                addedToCart();
            }
        });

        dishname.setText(dname);
        dishprice.setText(dprice);
        dishdes.setText(ddes);
        Picasso.get().load(durl).into(dishimage);

    }
    private void addedToCart(){
        String saveCurrentDate,saveCurrentTime;
        Calendar calForDate= Calendar.getInstance();

        SimpleDateFormat currentDate = new SimpleDateFormat("MM dd, yyyy");
        saveCurrentDate= currentDate.format(calForDate.getTime());

        SimpleDateFormat currentTime = new SimpleDateFormat("HH:mm:ss a");
        saveCurrentTime= currentTime.format(calForDate.getTime());

        final HashMap<String,Object> cartMap = new HashMap<>();

        cartMap.put("dishName", dname);
        cartMap.put("dishDescription", ddes);
        cartMap.put("dishDate", saveCurrentDate);
        cartMap.put("dishTime", saveCurrentTime);
        cartMap.put("dishPrice",dprice);
        cartMap.put("totalQuantity", quantity.getText().toString());
        cartMap.put("totalPrice", totalPrice);
        cartMap.put("durl",durl);

        firestore.collection("AddToCart").document(Objects.requireNonNull(auth.getCurrentUser()).getUid())
                .collection("CurrentUser").add(cartMap).addOnCompleteListener(new OnCompleteListener<DocumentReference>() {
            @Override
            public void onComplete(@NonNull Task<DocumentReference> task) {
                Toast.makeText(OrderFoodActivity.this,"Added To Cart", Toast.LENGTH_SHORT).show();
                Intent intent= new Intent(getApplicationContext(),UserhomeActivity.class);
                startActivity(intent);
                finish();
            }
        });
    }

    public void back(View view) {

        finish();
    }
}