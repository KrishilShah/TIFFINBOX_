package com.example.tiffinbox.cheffragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.tiffinbox.R;
import com.example.tiffinbox.adapters.OrderChefAdapter;
import com.example.tiffinbox.adapters.OrderUserAdapter;
import com.example.tiffinbox.models.ChefOrderData;
import com.example.tiffinbox.models.MyCartModel;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link ChefOrderlistFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class ChefOrderlistFragment extends Fragment {
    List<ChefOrderData> orderList;
    RecyclerView ordersRv;
    OrderChefAdapter orderChefAdapter;
    FirebaseFirestore db ;
    FirebaseAuth firebaseAuth;
    private String onlineUserID;

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public ChefOrderlistFragment() {
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
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View root =inflater.inflate(R.layout.fragment_chef_orderlist, container, false);

        db=FirebaseFirestore.getInstance();
        firebaseAuth=FirebaseAuth.getInstance();
        onlineUserID = FirebaseAuth.getInstance().getCurrentUser().getUid();

        ordersRv=root.findViewById(R.id.chef_order_list);
        ordersRv.setLayoutManager(new LinearLayoutManager(getActivity(),RecyclerView.VERTICAL,false));

        getRecyclerview();

        return root;
    }

    private void getRecyclerview() {
        orderList =new ArrayList<>();
        orderChefAdapter =new OrderChefAdapter(getActivity(), (ArrayList<ChefOrderData>) orderList);
        ordersRv.setAdapter(orderChefAdapter);

        db.collection("ChefOrders").document(onlineUserID).collection("CurrentChef")
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            for (QueryDocumentSnapshot document : task.getResult()) {

                                ChefOrderData chefOrderData = document.toObject(ChefOrderData.class);
                                orderList.add(chefOrderData);
                                orderChefAdapter.notifyDataSetChanged();
                            }
                        } else {
                            Toast.makeText(getActivity(), "Error"+task.getException(), Toast.LENGTH_SHORT).show();
                        }
                    }
                });
    }
}