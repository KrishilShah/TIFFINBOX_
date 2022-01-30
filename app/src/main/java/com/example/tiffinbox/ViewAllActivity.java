package com.example.tiffinbox;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;

import com.example.tiffinbox.adapters.ViewAllAdapter;
import com.example.tiffinbox.models.ViewAllModel;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.List;

public class ViewAllActivity extends AppCompatActivity {
    FirebaseFirestore firestore;
    RecyclerView recyclerView;
    ViewAllAdapter viewAllAdapter;
    List<ViewAllModel> viewAllModels;
    FirebaseAuth mFirebaseAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_all);

        firestore=FirebaseFirestore.getInstance();

//        String chefID;
//
//        chefID = mFirebaseAuth.getCurrentUser().getUid();
//        DocumentReference documentReference = firestore.collection("chefs").document(chefID);

//        String email =getIntent().getStringExtra("email");
        recyclerView=findViewById(R.id.view_all_rec);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        viewAllAdapter=new ViewAllAdapter(this,viewAllModels);
        viewAllModels=new ArrayList<>();
        recyclerView.setAdapter(viewAllAdapter);
        String ChefID = getIntent().getStringExtra("id");

//         if(chefID!=null && chefID.equalsIgnoreCase("id")){
//             firestore.collection("dish").whereEqualTo("id","ChefID").get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
//                 @Override
//                 public void onComplete(@NonNull Task<QuerySnapshot> task) {
//                     for (DocumentSnapshot documentSnapshot:task.getResult().getDocuments()){
//                         ViewAllModel viewAllModel=documentSnapshot.toObject(ViewAllModel.class);
//                         viewAllModels.add(viewAllModel);
//                         viewAllAdapter.notifyDataSetChanged();
//                     }
//                 }
//             });
//        }






    }

}