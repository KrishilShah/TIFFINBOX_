package com.example.tiffinbox;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import com.example.tiffinbox.adapters.DisplayFoodAdapter;
import com.example.tiffinbox.models.DishData;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.List;

public class DisplayFoodActivity extends AppCompatActivity implements DisplayFoodAdapter.OnItemClickListener {
    FirebaseFirestore firestore;
    RecyclerView recyclerView;
    DisplayFoodAdapter displayFoodAdapter;
    Toolbar toolbar;
    List<DishData> dishData;
    FirebaseAuth mFirebaseAuth;
    DisplayFoodAdapter.OnItemClickListener onItemClickListener;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_display_food);

        firestore=FirebaseFirestore.getInstance();
        String chefID = getIntent().getStringExtra("id");
//        String Thali=getIntent().getStringExtra(("Thali"));
        String DishType="Thali";
        recyclerView=findViewById(R.id.recview);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);

        dishData=new ArrayList<>();
        displayFoodAdapter =new DisplayFoodAdapter(this,dishData,onItemClickListener);
        recyclerView.setAdapter(displayFoodAdapter);
        Query a =firestore.collection("dish").whereEqualTo("chefId",chefID).whereEqualTo("DishType",DishType);
         if(chefID!=null ){
             a.get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                 @SuppressLint("NotifyDataSetChanged")
                 @Override
                 public void onComplete(@NonNull Task<QuerySnapshot> task) {
                     for (DocumentSnapshot documentSnapshot:task.getResult().getDocuments()){
                         DishData dish_data=documentSnapshot.toObject(DishData.class);
//                         viewAllModels.addAll((Collection<? extends ViewAllModel>) viewAllModel);
                         dishData.add(dish_data);
                         displayFoodAdapter.notifyDataSetChanged();
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