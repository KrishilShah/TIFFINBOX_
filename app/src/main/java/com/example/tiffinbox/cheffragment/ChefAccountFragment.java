package com.example.tiffinbox.cheffragment;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import com.example.tiffinbox.EditprofileActivity;
import com.example.tiffinbox.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.squareup.picasso.Picasso;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link ChefAccountFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class ChefAccountFragment extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    TextView chefUsername, chefPhone, chefEmail, userType, username, chefAddress;
    FirebaseAuth firebaseAuth;
    private String onlineUserID;
    FirebaseFirestore db = FirebaseFirestore.getInstance();
    DocumentReference documentReference;
    FloatingActionButton floatingActionButton;
    ImageView chefImage;
    StorageReference storageReference;


    public ChefAccountFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment ChefAccountFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static ChefAccountFragment newInstance(String param1, String param2) {
        ChefAccountFragment fragment = new ChefAccountFragment();
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

        View view = inflater.inflate(R.layout.fragment_chef_account, container, false);
        firebaseAuth = FirebaseAuth.getInstance();
        onlineUserID = FirebaseAuth.getInstance().getCurrentUser().getUid();
        documentReference = db.collection("chefs").document(firebaseAuth.getCurrentUser().getUid());
        storageReference = FirebaseStorage.getInstance().getReference();

        username = view.findViewById(R.id.userName);
        userType = view.findViewById(R.id.userType);
        chefUsername = view.findViewById(R.id.username);
        chefPhone = view.findViewById(R.id.user_phone);
        chefEmail = view.findViewById(R.id.user_email);
        chefAddress = view.findViewById(R.id.chef_address);
        chefImage = view.findViewById(R.id.chef_image);
        floatingActionButton = view.findViewById(R.id.floatingbtn);

        floatingActionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getActivity(), EditprofileActivity.class);
                startActivity(intent);
            }
        });

        getProfile();

        // Inflate the layout for this fragment
        return view;
    }

    private void getProfile() {
        documentReference.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if(task.getResult().exists()){

                    String email = task.getResult().getString("email");
                    String phone = task.getResult().getString("phone");
                    String uname = task.getResult().getString("name");
                    String address = task.getResult().getString("address");

                    username.setText(uname);
                    userType.setText("Chef");
                    chefUsername.setText(uname);
                    chefEmail.setText(email);
                    chefPhone.setText(phone);
                    chefAddress.setText(address);
                }
                else{
                    Toast.makeText(getActivity(), "No Profile Exists", Toast.LENGTH_SHORT ).show();
                }
            }
        })
                .addOnFailureListener(new OnFailureListener() {
                    @Override public void onFailure(@NonNull Exception e) {

                    }
                });

        StorageReference profileRef = storageReference.child("chefs/"+firebaseAuth.getCurrentUser().getUid()+"/chef_profile.jpg");
        profileRef.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
            @Override
            public void onSuccess(Uri uri) {
                Picasso.get().load(uri).into(chefImage);
            }
        });
    }
}