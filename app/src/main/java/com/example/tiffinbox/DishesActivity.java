package com.example.tiffinbox;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;




import android.annotation.SuppressLint;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.tiffinbox.adapters.Dishes_Adapter;
import com.example.tiffinbox.models.DishData;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.List;

public class DishesActivity extends AppCompatActivity implements Dishes_Adapter.OnItemClickListener{
    FirebaseFirestore firestore;
    RecyclerView recyclerView;
    Dishes_Adapter Dishes_Adapter;
    Toolbar toolbar;
    Button save;
    List<DishData> dishData;
    FirebaseAuth mFirebaseAuth;
    Dishes_Adapter.OnItemClickListener onItemClickListener;
    //    ImageView add_item_1, remove_item;
//    TextView view;
    int totalView=1;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dishes);

//        view=findViewById(R.id.view);
//        add_item_1=findViewById(R.id.add_item_1);
//        remove_item=findViewById(R.id.remove_item);


        firestore=FirebaseFirestore.getInstance();
        String chefID = getIntent().getStringExtra("cid");
//        String Thali=getIntent().getStringExtra(("Thali"));
        String DishType="Item";
        recyclerView=findViewById(R.id.recyclerview);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);

        save=findViewById(R.id.buy_now);

        dishData=new ArrayList<>();
        Dishes_Adapter =new Dishes_Adapter(this,dishData,onItemClickListener);
        recyclerView.setAdapter(Dishes_Adapter);
        Query a =firestore.collection("dish").whereEqualTo("chefId",chefID).whereEqualTo("dishType",DishType);
        if(chefID!=null ){
            a.get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                @SuppressLint("NotifyDataSetChanged")
                @Override
                public void onComplete(@NonNull Task<QuerySnapshot> task) {
                    for (DocumentSnapshot documentSnapshot:task.getResult().getDocuments()){
                        DishData dish_data=documentSnapshot.toObject(DishData.class);
                        dishData.add(dish_data);
                        Dishes_Adapter.notifyDataSetChanged();
//                         Toast.makeText(DisplayFoodActivity.this, "Type:"+Thali, Toast.LENGTH_SHORT).show();
                    }
                }
            }) ;
        }
    }

    public void back(View view) {
        finish();
    }
    @Override
    public void onClick(View view, int position) {
    }
}

