package com.example.tiffinbox;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.squareup.picasso.Picasso;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class PostdishActivity extends AppCompatActivity {

    EditText dname,ddes,dprice;
    TextView cname;
    ImageView dpic;
    public Uri imageUrl;
    public int picnme=1;
    String i=Integer.toString(picnme);
    Button post;
    String chefId;
    FirebaseFirestore droot;
    FirebaseAuth mFirebaseAuth;
    DocumentReference dref;
    DatabaseReference dishRef;
    private FirebaseStorage storage;
    private StorageReference sref;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_postdish);

        dname=findViewById(R.id.dish_name);
        dpic=findViewById(R.id.dish_image);
        ddes=findViewById(R.id.dish_description);
        dprice=findViewById(R.id.dish_price);
        cname=findViewById(R.id.chef_name);
        post=findViewById(R.id.post_dish);

        storage = FirebaseStorage.getInstance();
        sref=storage.getReference();

        droot=FirebaseFirestore.getInstance();
        mFirebaseAuth=FirebaseAuth.getInstance();

        dishRef= FirebaseDatabase.getInstance().getReference().child("Dish").child(mFirebaseAuth.getCurrentUser().getUid());

        chefId=mFirebaseAuth.getCurrentUser().getUid();
        dref=droot.collection("chef").document(chefId);

        dpic.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                choosepicture();
            }
        });

        post.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                insertdata();
            }
        });



    }

    private void insertdata() {


        StorageReference fileRef = sref.child("dish/"+mFirebaseAuth.getCurrentUser().getUid());

        String dishname=dname.getText().toString().trim();
        String dishdes=ddes.getText().toString().trim();
        String dishprice=dprice.getText().toString().trim();
        String dishimg= imageUrl.toString();


        picnme++;

        if(TextUtils.isEmpty(dishname)){
            dname.setError("DishData name is required!");
            return;
        }
        else if(TextUtils.isEmpty(dishdes)){
            ddes.setError("DishData description is necessary!!");
            return;
        }
        else if(TextUtils.isEmpty(dishprice)){
            dprice.setError("DishData Price is necessary!");
            return;
        }
//        else if(imageUrl != null){
//            Toast.makeText(getApplicationContext(), "Add dish photo", Toast.LENGTH_LONG).show();
//            return;
//        }
        else {

            uploadPicture(imageUrl);

            dref.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                @Override
                public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                    String chefname = null;
                    if(task.getResult().exists()) {
                        chefname = task.getResult().getString("name");        //to get chef name from firestore collection
                        cname.setText(chefname);
                    }
                    String ruid=dishRef.push().getKey();
                    DocumentReference docref=droot.collection("dish").document(ruid);
                    //** to store dish details in database
                    Map<String, String> dish = new HashMap<>();
                    dish.put("dname", dishname);
                    dish.put("ddes", dishdes);
                    dish.put("dprice", dishprice);
                    dish.put("url", dishimg);
                    dish.put("cname", chefname);

                    //store data in realtime database
                    DishData dishData=new DishData(dishname,dishdes,Double.parseDouble(dishprice),dishimg,ruid,chefname);
                    dishRef.child(ruid).setValue(dishData).addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            if (task.isSuccessful()){ }
                            else{ }
                        }});


                    //store data in cloud firestore
                    docref.set(dish).addOnSuccessListener(new OnSuccessListener<Void>() {
                        @Override
                        public void onSuccess(Void unused) {
                            Toast.makeText(getApplicationContext(), "Dish added successfully", Toast.LENGTH_SHORT).show();
                            Intent intent= new Intent(getApplicationContext(),ChefmainActivity.class);
                            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK|Intent.FLAG_ACTIVITY_CLEAR_TASK);
                            intent.putExtra("fragment","home");
                            startActivity(intent);
                        }
                    });
                    //**
                }
            });
        }


    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode==1 && resultCode==RESULT_OK && data!=null && data.getData()!=null){
            imageUrl=data.getData();
            dpic.setImageURI(imageUrl);
//            uploadPicture();
        }
    }

//    private void uploadPicture() {
//
//        //code to store image in firestore
//    }

    private void uploadPicture(Uri Uri) {
//       ruid= UUID.randomUUID().toString();
        final ProgressDialog pd = new ProgressDialog(this );
        pd.setTitle("Uploading Image");
        pd.show();

        StorageReference fileRef = sref.child("dish/"+mFirebaseAuth.getCurrentUser().getUid());
        fileRef.putFile(imageUrl).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                pd.dismiss();
                Toast.makeText(getApplicationContext(), "Image Uploaded", Toast.LENGTH_SHORT).show();
//                fileRef.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
//                    @Override
//                    public void onSuccess(Uri uri) {
//                        Picasso.get().load(uri).into(chef_image);
//                    }
//                });
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                pd.dismiss();
                Toast.makeText(getApplicationContext(), "Image Failed to Uploaded", Toast.LENGTH_SHORT).show();
            }
        }).addOnProgressListener(new OnProgressListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onProgress(@NonNull UploadTask.TaskSnapshot snapshot) {
                double progressPercent = (100.00 * snapshot.getBytesTransferred() / snapshot.getTotalByteCount());
                pd.setMessage("Percentage: " + (int)progressPercent + "%" );
            }
        });
    }

    private void choosepicture() {
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(intent,1);
    }

    public void back(View view) {
        Intent intent= new Intent(getApplicationContext(),ChefmainActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK|Intent.FLAG_ACTIVITY_CLEAR_TASK);
        intent.putExtra("fragment","home");
        startActivity(intent);
    }
}