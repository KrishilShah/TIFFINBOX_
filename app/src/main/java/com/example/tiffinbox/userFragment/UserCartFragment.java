package com.example.tiffinbox.userFragment;

import static android.content.ContentValues.TAG;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
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
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link UserCartFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class UserCartFragment extends Fragment  implements MyCartAdapter.OnItemClickListener {
    FirebaseFirestore db = FirebaseFirestore.getInstance();
    DocumentReference documentReference;
    FirebaseAuth auth;
    double myLatitude,myLongitude;
    String myPhone;
    private ProgressDialog progressDialog;


    TextView totalCharge,taxCharge,deliveryService,totalBill;

    public MyCartAdapter.OnItemClickListener onItemClickListener;

    RecyclerView recyclerView;
    MyCartAdapter cartAdapter;
    List<MyCartModel> cartModelList;
    Button btn_buynow;

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
        btn_buynow=root.findViewById(R.id.buy_now);
        auth=FirebaseAuth.getInstance();
        progressDialog=new ProgressDialog(getContext());
        progressDialog.setTitle("PLease Wait......");
        progressDialog.setCanceledOnTouchOutside(false);


        documentReference = db.collection("customers").document(auth.getCurrentUser().getUid());

        documentReference.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if(task.getResult().exists()){
                    myLatitude=task.getResult().getDouble("lat");
                    myLongitude=task.getResult().getDouble("lon");
                    myPhone=task.getResult().getString("phone");
                }
                else{
                    Toast.makeText(getActivity(),"No Profile Exists", Toast.LENGTH_SHORT).show();
                }
            }
        });

        btn_buynow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                Intent intent=new Intent(getActivity(),UserOrderlistFragment.class);
//                intent.putExtra("price",total_price);
//                intent.putExtra("userid",auth.getCurrentUser().getUid());

//                startActivity(intent);
                if (myLatitude==0 || myLatitude==0 || myLongitude==0 || myLongitude==0){
                //user didn't enter address in profile
                Toast.makeText( getActivity(), "Please enter your address in you profile before placing order...", Toast.LENGTH_SHORT). show() ;
                return;
                }

                if (myPhone.equals("")|| myPhone.equals("null")) {
                    //user didn't enter phone number in profile
                    Toast.makeText(getActivity(), "Please enter your address in you profile before placing order...", Toast.LENGTH_SHORT).show();
                    return;
                }
                if(cartModelList.size() == 0){
                    //cart list is empty
                    Toast.makeText(getActivity(), "No items in cart", Toast.LENGTH_SHORT).show();
                    return;
                }

               // submitOrder();




                Bundle bundle=new Bundle();
                bundle.putString("price", String.valueOf(total_price));
                bundle.putString("userid",auth.getCurrentUser().getUid());
                Fragment UserOrdlist=new UserOrderlistFragment();
                UserOrdlist.setArguments(bundle);
                FragmentTransaction fm=getActivity().getSupportFragmentManager().beginTransaction();
                fm.replace(R.id.container,UserOrdlist).commit();


            }
        });


        getRecyclerView();


//        trying();



        return root;

    }
//
//    private void submitOrder(){
//        progressDialog.setMessage("Placing Order....");
//        progressDialog.show();
//    }




    public void getRecyclerView(){

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
        int size=cartModelList.size();

        Toast.makeText(getActivity(), "list size "+size, Toast.LENGTH_SHORT).show();


        double price;
        for(int i=0; i<size;i++)
        {
            price=cartModelList.get(i).getTotalPrice();
            total_price=total_price+price;
        }

        tax=Math.round((total_price * percentax) * 100.0) / 100.0;
        double total= Math.round((total_price + tax + delivery)* 100.0) / 100.0 ;

        totalCharge.setText("$ "+total_price);
        taxCharge.setText("$ "+tax);
        deliveryService.setText("$ "+delivery);
        totalBill.setText("$ "+total);
    }




    @Override
    public void onDelete() {
            getRecyclerView();
    }

    @Override
    public void changed() {
        calculateCart();
    }

    @Override
    public void onClick(View v) {
        getRecyclerView();

    }

}