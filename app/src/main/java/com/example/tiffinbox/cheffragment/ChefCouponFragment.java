package com.example.tiffinbox.cheffragment;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.tiffinbox.AddCouponCodesActivity;
import com.example.tiffinbox.R;
import com.example.tiffinbox.adapters.coupon;
import com.example.tiffinbox.models.CouponModel;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link ChefOrderlistFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class ChefCouponFragment extends Fragment {
    private Button AddCoupon;
    private RecyclerView recyclerView;

    private ArrayList<CouponModel> couponModelArrayList;
    FirebaseFirestore db;
    //    private CouponAdapter couponAdapter;
    private coupon couponAdapter;
    private String onlineUserID;

    private FirebaseAuth firebaseAuth;




    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;


    public ChefCouponFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment ChefOrderlistFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static ChefOrderlistFragment newInstance(String param1, String param2) {
        ChefOrderlistFragment fragment = new ChefOrderlistFragment();
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
            mParam2 = getArguments().getString(ARG_PARAM2);}


    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_chef_coupon, container, false);

        db=FirebaseFirestore.getInstance();
        firebaseAuth=FirebaseAuth.getInstance();
        onlineUserID = FirebaseAuth.getInstance().getCurrentUser().getUid();


        AddCoupon = view.findViewById(R.id.AddCoupon);
        // Init Ui views
        recyclerView=view.findViewById(R.id.recyclerview);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity(),RecyclerView.VERTICAL,false));





//        allCoupons();
        getRecyclerview();




        AddCoupon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), AddCouponCodesActivity.class);
                intent.putExtra("value","add");
                //intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK|Intent.FLAG_ACTIVITY_CLEAR_TASK);
                startActivity(intent);

            }
        });
        return view;
    }
    //    private void CurrentDte(){
//        //get Current Date:
//        DecimalFormat mFormat= new DecimalFormat("00");
//        Calendar calendar=Calendar.getInstance();
//        int year = calendar.get(Calendar.YEAR);
//        int month = calendar.get(Calendar.MONTH)+1;
//        int day = calendar.get(Calendar.DAY_OF_YEAR);
//        String todayDate= day +"/"+ month +"/"+ year;
//    }
    private void allCoupons() {

        //get Current Date:
        DecimalFormat mFormat= new DecimalFormat("00");
        Calendar calendar=Calendar.getInstance();
        int year = calendar.get(Calendar.YEAR);
        int month = calendar.get(Calendar.MONTH)+1;
        int day = calendar.get(Calendar.DAY_OF_YEAR);
        String todayDate= day +"/"+ month +"/"+ year;

        //init list
        couponModelArrayList= new ArrayList<>();
        //db reference Users >current user > coupon > coupon code

        FirebaseFirestore db=FirebaseFirestore.getInstance();
        db.collection("coupon").document(FirebaseAuth.getInstance().getCurrentUser().getUid()).collection("CurrentUser")
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            for (QueryDocumentSnapshot document : task.getResult()) {
                                CouponModel myCartModel = document.toObject(CouponModel.class);
                                couponModelArrayList.add(myCartModel);
                                couponAdapter= new coupon(getActivity(),couponModelArrayList);
                                couponAdapter.notifyDataSetChanged();
                            }
                        } else {
                            Toast.makeText(getActivity(), "Error"+task.getException(), Toast.LENGTH_SHORT).show();
                        }
                    }
                });


//        DatabaseReference reference= FirebaseDatabase.getInstance().getReference("coupons");
//        reference.child(firebaseAuth.getUid())
//                .addValueEventListener(new ValueEventListener() {
//                    @Override
//                    public void onDataChange(@NonNull DataSnapshot snapshot) {
//                        //clear list before adding data
//                        couponModelArrayList.clear();
//                        for(DataSnapshot ds:snapshot.getChildren()){
//                            CouponModel couponModel=ds.getValue(CouponModel.class);
//
//                            String expiryDate= couponModel.getExpiryDate();
//
//                           // ------------Checking Expiry Date---------------
//                            try{
//                                SimpleDateFormat sdformat = new SimpleDateFormat("dd/MM/yyyy");
//                                Date currentDate = sdformat.parse(todayDate);
//                                Date expiredDate = sdformat.parse(expiryDate);
//
//                                if(expiryDate.compareTo(String.valueOf(currentDate))>0){
//                                    //date 1 occurs after date 2
//                                    couponModelArrayList.add(couponModel);
//                                }
//                                else if(expiryDate.compareTo(String.valueOf(currentDate))<0){
//                                    //date 1 occurs before date 2 (Expired)
//                                    couponModelArrayList.add(couponModel);
//                                }
//                                else if(expiryDate.compareTo(String.valueOf(currentDate))==0){
//                                    //both dates equal
//                                    couponModelArrayList.add(couponModel);
//
//                                }
//                            }
//                            catch(Exception e){
//
//                            }
//                            //add to list:
//                            couponModelArrayList.add(couponModel);
//                        }
//                        //setup adapter , add list to adapter:
//                        couponAdapter= new CouponAdapter(getActivity(),couponModelArrayList);
//
//                        //set adapter to recyclerview:
//                        recyclerView.setAdapter(couponAdapter);
//                    }
//
//                    @Override
//                    public void onCancelled(@NonNull DatabaseError error) {
//
//                    }
//                });

    }



    private void getRecyclerview() {
        couponModelArrayList=new ArrayList<>();
        couponAdapter =new coupon(getActivity(), (ArrayList<CouponModel>) couponModelArrayList);
        recyclerView.setAdapter(couponAdapter);

        db.collection("coupon").document(onlineUserID).collection("CurrentUser")
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            for (QueryDocumentSnapshot document : task.getResult()) {

                                CouponModel couponModel = document.toObject(CouponModel.class);


                                couponModelArrayList.add(couponModel);
                                couponAdapter.notifyDataSetChanged();

                                //Toast.makeText(getActivity(),couponModelArrayList.get(0).getCouponCode(), Toast.LENGTH_SHORT).show();
                            }
                        } else {
                            Toast.makeText(getActivity(), "Error"+task.getException(), Toast.LENGTH_SHORT).show();
                        }
                    }
                });


    }
}