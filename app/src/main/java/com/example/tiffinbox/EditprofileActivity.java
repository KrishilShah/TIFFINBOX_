package com.example.tiffinbox;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.text.TextUtils;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.Toast;

import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContract;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.Transaction;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.squareup.picasso.Picasso;

import java.util.UUID;

public class EditprofileActivity extends AppCompatActivity {
    
    Toolbar toolbar;
    EditText chef_email, chef_name, chef_number, chef_address, chef_password, chef_confirmpassword;
    RadioButton male, female, other;
    Button update_btn;
    ImageView chef_image;
    FirebaseAuth firebaseAuth;
    FirebaseFirestore firestore;
    private String onlineUserID;
    FirebaseFirestore db = FirebaseFirestore.getInstance();
    DocumentReference documentReference;
    StorageReference storageReference;
    public static String chefPassword;
    private Uri imageUri;
    ActivityResultLauncher<String> mGetContent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_editprofile);
        toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);


        chef_name = findViewById(R.id.username);
        chef_email = findViewById(R.id.email);
        chef_number = findViewById(R.id.phone);
        chef_address = findViewById(R.id.address);
        chef_password = findViewById(R.id.password);
        chef_confirmpassword= findViewById(R.id.confirmPassword);
        update_btn = findViewById(R.id.update);
        male = findViewById(R.id.updatemale);
        female = findViewById(R.id.updatefemale);
        other = findViewById(R.id.updateother);
        chef_image = findViewById(R.id.chefImage);

        mGetContent = registerForActivityResult(new ActivityResultContracts.GetContent(), new ActivityResultCallback<Uri>() {
            @Override
            public void onActivityResult(Uri result) {
                imageUri = result;
                chef_image.setImageURI(result);
                uploadPicture(imageUri);
            }
        });

        firebaseAuth = FirebaseAuth.getInstance();
        firestore = FirebaseFirestore.getInstance();
        onlineUserID = FirebaseAuth.getInstance().getCurrentUser().getUid();
        documentReference = db.collection("chefs").document(firebaseAuth.getCurrentUser().getUid());
        storageReference = FirebaseStorage.getInstance().getReference();

        StorageReference profileRef = storageReference.child("chefs/"+firebaseAuth.getCurrentUser().getUid()+"/chef_profile.jpg");
        profileRef.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
            @Override
            public void onSuccess(Uri uri) {
                Picasso.get().load(uri).into(chef_image);
            }
        });

        update_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String name = chef_name.getText().toString();
                String email = chef_email.getText().toString().trim();
                String number = chef_number.getText().toString();
                String address = chef_address.getText().toString().trim();
                String password= chef_password.getText().toString().trim();
                String confirmpassword = chef_confirmpassword.getText().toString().trim();
                String gender = "";

                chefPassword = confirmpassword;

                if(TextUtils.isEmpty(name)){
                    chef_name.setError("Name is Required!");
                    return;
                }
                if(TextUtils.isEmpty(email)){
                    chef_email.setError("Email is Required!");
                    return;
                }
                if(TextUtils.isEmpty(number)){
                    chef_number.setError("Mobile number is Required!");
                    return;
                }
                if(TextUtils.isEmpty(address)){
                    chef_address.setError("Address is Required!");
                    return;
                }
                if(TextUtils.isEmpty(password)){
                    chef_password.setError("Password is Required!");
                    return;
                }
                if(password.length() < 6){
                    chef_password.setError("Password must contain atleast 6 characters");
                    return;
                }
                if(TextUtils.isEmpty(confirmpassword)){
                    chef_confirmpassword.setError("Password Confirmation Required!");
                    return;
                }
                if(!(TextUtils.equals(password, confirmpassword))){
                    chef_password.setError("Passwords Do not Match");
                    return;
                }
                if(male.isChecked()){
                    gender = "Male";
                }
                else if(female.isChecked()){
                    gender = "Female";
                }
                else if(other.isChecked()){
                    gender = "Other";
                }
                String finalGender = gender;
                //updation of user info
                final DocumentReference sfDocRef = db.collection("chefs").document(firebaseAuth.getCurrentUser().getUid());

                db.runTransaction(new Transaction.Function<Void>() {

                    @Nullable
                    @Override
                    public Void apply(@NonNull Transaction transaction) throws FirebaseFirestoreException {
                        DocumentSnapshot snapshot = transaction.get(sfDocRef);

                        transaction.update(sfDocRef, "name", name);
                        transaction.update(sfDocRef, "phone", number);
                        transaction.update(sfDocRef, "Password", password);
                        transaction.update(sfDocRef, "gender", finalGender);
                        transaction.update(sfDocRef, "address", address);


                        return null;
                    }

                })
                        .addOnSuccessListener(new OnSuccessListener<Void>() {
                            @Override
                            public void onSuccess(Void unused) {
                                Toast.makeText(getApplicationContext(), "Profile Updated", Toast.LENGTH_SHORT).show();
                            }
                        }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(getApplicationContext(), "Some Error Occured", Toast.LENGTH_SHORT).show();
                    }
                });
                startActivity(new Intent(getApplicationContext(), ChefmainActivity.class));
                finish();
            }
        });

        chef_image.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                choosePicture();
            }
        });
    }
//
//    @Override
//    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
//        super.onActivityResult(requestCode, resultCode, data);
//        if (requestCode == 1 && resultCode== RESULT_OK){
//            imageUri = data.getData();
//            chef_image.setImageURI(imageUri);
//            uploadPicture(imageUri);
//        }
//    }

    private void uploadPicture(Uri imageUri) {
        final ProgressDialog pd = new ProgressDialog(this );
        pd.setTitle("Uploading Image");
        pd.show();
        StorageReference fileRef = storageReference.child("chefs/"+firebaseAuth.getCurrentUser().getUid()+"/chef_profile.jpg");
        fileRef.putFile(imageUri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                pd.dismiss();
                Toast.makeText(getApplicationContext(), "Image Uploaded", Toast.LENGTH_SHORT).show();
                fileRef.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                    @Override
                    public void onSuccess(Uri uri) {
                        Picasso.get().load(uri).into(chef_image);
                    }
                });
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

    private void choosePicture() {
//        Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
//        intent.setType("image/*");
//        intent.setAction(Intent.ACTION_GET_CONTENT);
        mGetContent.launch("image/*");

    }


    protected void onStart(){
        super.onStart();
        documentReference.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if(task.getResult().exists()){
                    String name = task.getResult().getString("name");
                    String email = task.getResult().getString("email");
                    String phone = task.getResult().getString("phone");
                    String gender = task.getResult().getString("gender");
                    String password1 = task.getResult().getString("Password");
                    String address1= task.getResult().getString("address");

                    chef_address.setText(address1);
                    chef_name.setText(name);
                    chef_email.setText(email);
                    chef_number.setText(phone);
                    chef_password.setText(password1);
                    chef_confirmpassword.setText(password1);
//                    if(gender.equals("Male")){
//                        male.setSelected(true);
//                        female.setSelected(false);
//                        other.setSelected(false);
//                    }
//                    else if(gender.equals("Female")){
//                        male.setSelected(false);
//                        female.setSelected(true);
//                        other.setSelected(false);
//                    }
//                    else if(gender.equals("Other")){
//                        male.setSelected(false);
//                        female.setSelected(false);
//                        other.setSelected(true);
//                    }
                }
                else{
                    Toast.makeText(getApplicationContext(),"No Profile Exists", Toast.LENGTH_SHORT);
                }
            }
        })
                .addOnFailureListener(new OnFailureListener() {
                    @Override public void onFailure(@NonNull Exception e) {

                    }
                });

    }


    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.updateprofile, menu);
        return true;
    }
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()){
            case R.id.account_delete:
                Toast.makeText(this , "Delete Profile", Toast.LENGTH_SHORT).show();
                         //delete profile

                break;
            default:
                break;
        }
        return true;
    }

    public void back(View view) {

        Intent intent=new Intent(getApplicationContext(),ChefmainActivity.class);
        intent.putExtra("fragment","account");
        startActivity(intent);
    }


}