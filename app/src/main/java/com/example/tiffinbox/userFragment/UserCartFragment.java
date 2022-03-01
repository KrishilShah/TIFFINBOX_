package com.example.tiffinbox.userFragment;

import static android.content.ContentValues.TAG;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.tiffinbox.R;
import com.example.tiffinbox.adapters.Chef_adapters;
import com.example.tiffinbox.adapters.MyCartAdapter;
import com.example.tiffinbox.models.MyCartModel;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.annotations.Nullable;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link UserCartFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class UserCartFragment extends Fragment  implements MyCartAdapter.OnItemClickListener{

    FirebaseFirestore db;
    FirebaseAuth auth;

    TextView totalCharge,taxCharge,deliveryService,totalBill;

    public MyCartAdapter.OnItemClickListener onItemClickListener;

    RecyclerView recyclerView;
    MyCartAdapter cartAdapter;
    List<MyCartModel> cartModelList;

    private double totalAmountIncome = 0;
    double total_price=0;

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public UserCartFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment UserCartFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static UserCartFragment newInstance(String param1, String param2) {
        UserCartFragment fragment = new UserCartFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View root = inflater.inflate(R.layout.fragment_user_cart, container, false);

        db= FirebaseFirestore.getInstance();
        auth = FirebaseAuth.getInstance();
        recyclerView=root.findViewById(R.id.recyclerview);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        recyclerView.setHasFixedSize(true);

        taxCharge=root.findViewById(R.id.taxCharge);
        totalCharge=root.findViewById(R.id.totalCharge);
        totalBill=root.findViewById(R.id.totalBill);
        deliveryService=root.findViewById(R.id.deliveryService);

        getRecyclerView();


//        trying();



        return root;

    }


    private void getRecyclerView(){

        cartModelList=new ArrayList<>();
        cartAdapter=new MyCartAdapter(getActivity(),cartModelList, onItemClickListener);
        recyclerView.setAdapter(cartAdapter);


        db.collection("AddToCart").document(auth.getCurrentUser().getUid())
                .collection("CurrentUser").get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if(task.isSuccessful()){
                    for(DocumentSnapshot documentSnapshot : task.getResult().getDocuments()){
                        MyCartModel cartModel=documentSnapshot.toObject(MyCartModel.class);
                        cartModelList.add(cartModel);

                        cartAdapter.notifyDataSetChanged();
                        calculateCart();

                    }
                }
            }
        });

    }


//    public void trying(){
//
//        int size=cartModelList.size();
//
//
//
//            DocumentReference docRef = db.collection("AddToCart").document(auth.getCurrentUser().getUid())
//                   ;
//
//            docRef.addSnapshotListener(new EventListener<DocumentSnapshot>() {
//                @Override
//                public void onEvent(@Nullable DocumentSnapshot snapshot,
//                                    @Nullable FirebaseFirestoreException e) {
//                    if (e != null) {
//                        Log.w(TAG, "Listen failed.", e);
//                        return;
//                    }
//
//                    if (snapshot != null && snapshot.exists()) {
//                        Log.d(TAG, "Current data: " + snapshot.getData());
//
//
//                        calculateCart();
//                    } else {
//                        Log.d(TAG, "Current data: null");
//                    }
//                }
//            });
//
//    }

    public void calculateCart(){
        double percentax = 0.02;
        double delivery = 10;
        double tax;


//        db.collection("AddToCart").document(auth.getCurrentUser().getUid())
//                .collection("CurrentUser").get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
//            @Override
//            public void onComplete(@NonNull Task<QuerySnapshot> task) {
//                if(task.isSuccessful()){
//                    for(DocumentSnapshot documentSnapshot : task.getResult().getDocuments()){
//                        MyCartModel cartModel=documentSnapshot.toObject(MyCartModel.class);
//                        cartModelList.add(cartModel);
                        int size=cartModelList.size();
                        Toast.makeText(getActivity(), "list size "+size, Toast.LENGTH_SHORT).show();


                        double price;
                        for(int i=0; i<size;i++)
                        {
                            price=cartModelList.get(i).getTotalPrice();
                            total_price=total_price+price;
                        }
//
//                    }
//                }
//            }
//        });


        tax=Math.round((total_price * percentax) * 100.0) / 100.0;
        double total= Math.round((total_price + tax + delivery)* 100.0) / 100.0 ;

        totalCharge.setText("$ "+total_price);
        taxCharge.setText("$ "+tax);
        deliveryService.setText("$ "+delivery);
        totalBill.setText("$ "+total);
    }

    public void removeItem(int position){
        cartModelList.remove(position);
        cartAdapter.notifyItemRemoved(position);

        Toast.makeText(getActivity(), "Item "+position+" removed", Toast.LENGTH_SHORT).show();
    }
//if(onItemClickListener!=null) {
//
//        if(position != RecyclerView.NO_POSITION)
//            onItemClickListener.onDelete(position);
//    }


    @Override
    public void onDelete(int position) {
            removeItem(position);
    }

    @Override
    public void changed() {
        calculateCart();
    }

    @Override
    public void onClick(View v) {

    }

}