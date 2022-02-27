package com.example.tiffinbox.userFragment;

import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.tiffinbox.R;
import com.example.tiffinbox.adapters.Chef_adapters;
import com.example.tiffinbox.models.ChefData;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link UserHomeFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class UserHomeFragment extends Fragment implements Chef_adapters.OnItemClickListener {
    public Chef_adapters.OnItemClickListener onItemClickListener;

    List<ChefData> chef_list_modelList;
    RecyclerView popularRec;
    Chef_adapters chefAdapters;
    FirebaseFirestore db ;
    EditText search_box;
    private Spinner spinner;
    String spinnerInput;
//    private List<ChefData>/










    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public UserHomeFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment UserHomeFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static UserHomeFragment newInstance(String param1, String param2) {
        UserHomeFragment fragment = new UserHomeFragment();
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

        View root = inflater.inflate(R.layout.fragment_user_home, container, false);
        db=FirebaseFirestore.getInstance();


        popularRec=root.findViewById(R.id.cheflist);
        popularRec.setLayoutManager(new LinearLayoutManager(getActivity(),RecyclerView.VERTICAL,false));
        popularRec.setHasFixedSize(true);
        chef_list_modelList =new ArrayList<>();
        chefAdapters=new Chef_adapters(getActivity(), chef_list_modelList,onItemClickListener);
        popularRec.setAdapter(chefAdapters);
        search_box =root.findViewById(R.id.search_box);

        spinner=root.findViewById(R.id.spinner);
//        String id = db.collection("chefs").document().getId();

        ArrayAdapter<CharSequence> filter = ArrayAdapter.createFromResource(getActivity(),R.array.search_filter, android.R.layout.simple_spinner_item);
        filter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(filter);



        getRecyclerview();

        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int position, long l) {
                Object item = adapterView.getItemAtPosition(position);
                if (item != null) {
                    spinnerInput=item.toString();
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {
                spinnerInput="Search by name";
            }
        });



//        recyclerViewSearch=root.findViewById(R.id.search_rec);


//        recyclerViewSearch.setLayoutManager(new LinearLayoutManager(getContext()));
//        recyclerViewSearch.setAdapter(chefAdapters);
//        recyclerViewSearch.setHasFixedSize(true);
        search_box.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

//                searchChefByName(s.toString());

            }

            @Override
            public void afterTextChanged(Editable s) {
                if(s.toString().isEmpty()){
                    chef_list_modelList.clear();
                    chef_list_modelList.clear();
                    getRecyclerview();
                }else{
                    if(spinnerInput.equals("Search by name"))
                    {
                        chef_list_modelList.clear();
                        searchChefByName(s.toString());
                    }
                    else if(spinnerInput.equals("Search by Thali type")){
                        chef_list_modelList.clear();
                        searchChefByThali(s.toString());
                    }
                }

            }
        });

        // Inflate the layout for this fragment
        return root;
    }

    private void searchChefByThali(String name) {

        if(!name.isEmpty()){
            db.collection("chefs").orderBy("speciality").startAt(name).endAt(name+"\uf8ff").get()
                    .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                        @Override
                        public void onComplete(@NonNull Task<QuerySnapshot> task) {
                            if (task.isSuccessful() && task.getResult()!=null) {
                                for (DocumentSnapshot doc : task.getResult().getDocuments()) {
                                    ChefData chefData = doc.toObject(ChefData.class);
//                                    chef_list_modelList.add(chefData);
                                    chef_list_modelList.clear();
                                    chef_list_modelList.add(chefData);
//                                    chefAdapters=new Chef_adapters(getActivity(), chef_list_modelList,onItemClickListener);
//                                    popularRec.setAdapter(chefAdapters);
                                    chefAdapters.notifyDataSetChanged();
                                }
                            }
                        }

                    });
        }
    }

    private void searchNearDelivery(String name) {



        if(!name.isEmpty()){
            db.collection("chefs").orderBy("dpin").get()
                    .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                        @Override
                        public void onComplete(@NonNull Task<QuerySnapshot> task) {
                            if (task.isSuccessful() && task.getResult()!=null) {
                                for (DocumentSnapshot doc : task.getResult().getDocuments()) {
                                    ChefData chefData = doc.toObject(ChefData.class);
//                                    chef_list_modelList.add(chefData);
                                    chef_list_modelList.clear();
                                    chef_list_modelList.add(chefData);
//                                    chefAdapters=new Chef_adapters(getActivity(), chef_list_modelList,onItemClickListener);
//                                    popularRec.setAdapter(chefAdapters);
                                    chefAdapters.notifyDataSetChanged();
                                }
                            }
                        }

                    });
        }
    }

    private void searchChefByName(String name) {



        if(!name.isEmpty()){
            db.collection("chefs").orderBy("name").startAt(name).endAt(name+"\uf8ff").get()
                    .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                        @Override
                        public void onComplete(@NonNull Task<QuerySnapshot> task) {
                            if (task.isSuccessful() && task.getResult()!=null) {
                                for (DocumentSnapshot doc : task.getResult().getDocuments()) {
                                    ChefData chefData = doc.toObject(ChefData.class);
//                                    chef_list_modelList.add(chefData);
                                    chef_list_modelList.clear();
                                    chef_list_modelList.add(chefData);
                                    chefAdapters=new Chef_adapters(getActivity(), chef_list_modelList,onItemClickListener);
                                    popularRec.setAdapter(chefAdapters);
//                                    chefAdapters.notifyDataSetChanged();
                                }
                            }
                        }

                    });
        }

    }

//    private void searchChefByName(String name) {
//        if(!name.isEmpty()){
//            db.collection("chefs").whereEqualTo("name",name).get()
//                    .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
//                        @Override
//                        public void onComplete(@NonNull Task<QuerySnapshot> task) {
//                            if(task.isSuccessful()&& task.getResult()!=null){
//                                chef_list_modelList.clear();
//                                chefAdapters.notifyDataSetChanged();
////                                List<ChefData> chef_list_modelList;
////                                RecyclerView popularRec;
////                                Chef_adapters chefAdapters;
////                                FirebaseFirestore db ;
////                                EditText search_box;
////    private List<ChefData>/
////                                private RecyclerView recyclerViewSearch;
//                                for(DocumentSnapshot doc:task.getResult().getDocuments()){
//                                    ChefData chefData = doc.toObject(ChefData.class);
//                                    chef_list_modelList.add(chefData);
//                                    chefAdapters.notifyDataSetChanged();;
//                                }
//
//                            }
//
//                        }
//                    });
//        }
//
//
//    }

    private void getRecyclerview() {
        db.collection("chefs")
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            for (QueryDocumentSnapshot document : task.getResult()) {

                                ChefData chefData = document.toObject(ChefData.class);
//                                chefListModel
                                chef_list_modelList.add(chefData);
                                chefAdapters.notifyDataSetChanged();
//                                chef_list_modelList.clear();
//                                chef_list_modelList.add(chefData);
//                                chefAdapters=new Chef_adapters(getActivity(), chef_list_modelList,onItemClickListener);
//                                popularRec.setAdapter(chefAdapters);
//                                Toast.makeText(getActivity(), "LIST DISPLAY"+task.getException(), Toast.LENGTH_SHORT).show();
                            }
                        } else {
                            Toast.makeText(getActivity(), "Error"+task.getException(), Toast.LENGTH_SHORT).show();
                        }
                    }
                });
    }

    @Override
    public void onClick(View view, int position) {

    }

}