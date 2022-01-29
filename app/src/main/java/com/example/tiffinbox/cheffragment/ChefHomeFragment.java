package com.example.tiffinbox.cheffragment;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.tiffinbox.models.DishData;
import com.example.tiffinbox.PostdishActivity;
import com.example.tiffinbox.R;
import com.example.tiffinbox.adapters.DishAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link ChefHomeFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class ChefHomeFragment extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    FloatingActionButton floatingActionButton;
    ArrayList<DishData> arrayList;
    LinearLayoutManager linearLayoutManager;

    public ChefHomeFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment ChefHomeFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static ChefHomeFragment newInstance(String param1, String param2) {
        ChefHomeFragment fragment = new ChefHomeFragment();
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

    RecyclerView recview;
    DishAdapter adapter;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_chef_home, container, false);

        recview=view.findViewById(R.id.recview);

//        recview.setLayoutManager(new LinearLayoutManager(getActivity(),RecyclerView.VERTICAL,false));

        recview.setLayoutManager(new LinearLayoutManager(getActivity() ));
        recview.setHasFixedSize(true);

        floatingActionButton = view.findViewById(R.id.floatingbtn);

//        FirebaseRecyclerOptions<ModelChef> options =
//                new FirebaseRecyclerOptions.Builder<ModelChef>()
//                        .setQuery(FirebaseDatabase.getInstance().getReference().child("Dish"), ModelChef.class)
//                        .build();

                FirebaseRecyclerOptions<DishData> options =
                new FirebaseRecyclerOptions.Builder<DishData>()
                        .setQuery(FirebaseDatabase.getInstance().getReference().child("Dish").child(FirebaseAuth.getInstance().getCurrentUser().getUid()), DishData.class)
                        .build();

        arrayList = new ArrayList<>();
//        DishAdapter = new DishAdapter(this, arrayList,dishData);
//        autopayRecyclerView.setAdapter(DishAdapter);
        adapter=new DishAdapter(getActivity(),arrayList);
        recview.setAdapter(adapter);




                floatingActionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=new Intent(getActivity(), PostdishActivity.class);
                startActivity(intent);
            }
        });
                getRecyclerView();
        // Inflate the layout for this fragment
        return view;
    }

    private void getRecyclerView() {
        DatabaseReference dref = FirebaseDatabase.getInstance().getReference("Dish").child(FirebaseAuth.getInstance().getCurrentUser().getUid());
        dref.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                    DishData dishData = dataSnapshot.getValue(DishData.class);
                    arrayList.add(dishData);
                }
                adapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }


//    @Override
//    public void onStart() {
//        super.onStart();
//        adapter.startListening();
//    }
//    @Override
//    public void onStop() {
//        super.onStop();
//        adapter.stopListening();
//    }


}