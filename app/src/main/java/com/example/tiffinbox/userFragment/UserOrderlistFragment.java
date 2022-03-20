package com.example.tiffinbox.userFragment;

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
import com.example.tiffinbox.adapters.Chef_adapters;
import com.example.tiffinbox.adapters.MyCartAdapter;
import com.example.tiffinbox.adapters.OrderUserAdapter;
import com.example.tiffinbox.models.ChefData;
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
 * Use the {@link UserOrderlistFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class UserOrderlistFragment extends Fragment {
    List<MyCartModel> orderList;
    RecyclerView ordersRv;
    OrderUserAdapter orderUserAdapter;
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


    public UserOrderlistFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment UserOrderlistFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static UserOrderlistFragment newInstance(String param1, String param2) {
        UserOrderlistFragment fragment = new UserOrderlistFragment();
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
        View root= inflater.inflate(R.layout.fragment_user_orderlist, container, false);

        db=FirebaseFirestore.getInstance();
        firebaseAuth=FirebaseAuth.getInstance();
        onlineUserID = FirebaseAuth.getInstance().getCurrentUser().getUid();

        ordersRv=root.findViewById(R.id.user_order_list);
        ordersRv.setLayoutManager(new LinearLayoutManager(getActivity(),RecyclerView.VERTICAL,false));
//        ordersRv.setHasFixedSize(true);
//        ordersRv.setAdapter(orderUserAdapter);
//        Bundle bundle=this.getArguments();
//        String total_price=bundle.getString("price");
//        String userid=bundle.getString("userid");
//        Toast.makeText(getContext(), ""+total_price+""+userid, Toast.LENGTH_SHORT).show();
        getRecyclerview();
        return root;

    }

    private void getRecyclerview() {
        orderList =new ArrayList<>();
        orderUserAdapter =new OrderUserAdapter(getActivity(), (ArrayList<MyCartModel>) orderList);
        ordersRv.setAdapter(orderUserAdapter);

        db.collection("AddToCart").document(onlineUserID).collection("CurrentUser")
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            for (QueryDocumentSnapshot document : task.getResult()) {

                                MyCartModel myCartModel = document.toObject(MyCartModel.class);
                                orderList.add(myCartModel);
                                orderUserAdapter.notifyDataSetChanged();
                            }
                        } else {
                            Toast.makeText(getActivity(), "Error"+task.getException(), Toast.LENGTH_SHORT).show();
                        }
                    }
                });
    }
}