package com.example.tiffinbox;

import static android.content.ContentValues.TAG;

import android.Manifest;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.provider.Settings;
import android.text.TextUtils;
import android.util.Log;
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
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.EmailAuthProvider;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
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

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.UUID;
import java.util.stream.Collectors;

import de.hdodenhof.circleimageview.CircleImageView;

public class EditprofileActivity extends AppCompatActivity implements LocationListener {

    LocationManager locationManager;
    Toolbar toolbar;
    EditText chef_email, chef_name, chef_number, chef_address, chef_speciality, chef_about, chef_pincode, chef_deliverypin;
    RadioButton male, female, other;
    Button update_btn;
    CircleImageView chef_image;
    FirebaseAuth firebaseAuth;
    FirebaseFirestore firestore;
    private String onlineUserID;
    FirebaseFirestore db = FirebaseFirestore.getInstance();
    DocumentReference documentReference;
    StorageReference storageReference;
    public static String chefPassword;
    private Uri imageUri;
    double longitude,latitude;
    ActivityResultLauncher<String> mGetContent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_editprofile);
        toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);

        if (ContextCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_FINE_LOCATION)
                != PackageManager.PERMISSION_GRANTED
                && ActivityCompat.checkSelfPermission(getApplicationContext(),
                android.Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{
                    android.Manifest.permission.ACCESS_FINE_LOCATION,
                    android.Manifest.permission.ACCESS_COARSE_LOCATION}, 100);
        }


        chef_name = findViewById(R.id.username);
        chef_email = findViewById(R.id.email);
        chef_number = findViewById(R.id.phone);
        chef_address = findViewById(R.id.address);
//        chef_password = findViewById(R.id.password);
//        chef_confirmpassword= findViewById(R.id.confirmPassword);
        chef_speciality = findViewById(R.id.speciality);
        chef_about = findViewById(R.id.about);
        chef_deliverypin = findViewById(R.id.deliverypin);
        chef_pincode = findViewById(R.id.pincode);
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

        locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        locationEnabled();
        getLocation();

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

        String oldEmail=chef_email.getText().toString().trim();
//        String oldPassword = chef_password.getText().toString().trim();

        update_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String name = chef_name.getText().toString();
                String email = chef_email.getText().toString().trim();
                String number = chef_number.getText().toString();
                String address = chef_address.getText().toString().trim();
//                String password= chef_password.getText().toString().trim();
//                String confirmpassword = chef_confirmpassword.getText().toString().trim();
                String speciality = chef_speciality.getText().toString().trim();
                String about = chef_about.getText().toString().trim();
                String pincode = chef_pincode.getText().toString().trim();
                String dpin = chef_deliverypin.getText().toString().trim();
                String[] dpinArray = dpin.split("\\s*,\\s*");
                List<String> dpinList = Arrays.asList(dpinArray);
                String gender = "";

//                chefPassword = confirmpassword;

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
                if(TextUtils.isEmpty(speciality)){
                    chef_speciality.setError("Password is Required!");
                    return;
                }
//                if(password.length() < 6){
//                    chef_password.setError("Password must contain atleast 6 characters");
//                    return;
//                }
                if(TextUtils.isEmpty(about)){
                    chef_about.setError("Password Confirmation Required!");
                    return;
                }
//                if(!(TextUtils.equals(password, confirmpassword))){
//                    chef_password.setError("Passwords Do not Match");
//                    return;
//                }
                if(TextUtils.isEmpty(pincode)){
                    chef_pincode.setError("Pin code is required");
                    return;
                }
                if(TextUtils.isEmpty(dpin)){
                    chef_deliverypin.setError("Delivery pins are required!");
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

                FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();

                //if user update email for authentication
                if(oldEmail!=email) {
                    user.updateEmail(email)
                            .addOnCompleteListener(new OnCompleteListener<Void>() {
                                @Override
                                public void onComplete(@NonNull Task<Void> task) {
                                    if (task.isSuccessful()) {
                                        Log.d(TAG, "User email address updated.");
                                    }
                                }
                            });
                }
                //if user update password for authentication
//                if(oldPassword!=confirmpassword) {
//                    user.updatePassword(confirmpassword)
//                            .addOnCompleteListener(new OnCompleteListener<Void>() {
//                                @Override
//                                public void onComplete(@NonNull Task<Void> task) {
//                                    if (task.isSuccessful()) {
//                                        Log.d(TAG, "User password updated.");
//                                    }
//                                }
//                            });
//                }

                //updation of user info
                final DocumentReference sfDocRef = db.collection("chefs").document(firebaseAuth.getCurrentUser().getUid());

                db.runTransaction(new Transaction.Function<Void>() {

                    @Nullable
                    @Override
                    public Void apply(@NonNull Transaction transaction) throws FirebaseFirestoreException {
                        DocumentSnapshot snapshot = transaction.get(sfDocRef);

                        transaction.update(sfDocRef, "name", name);
                        transaction.update(sfDocRef, "phone", number);
                        transaction.update(sfDocRef, "email", email);
                        transaction.update(sfDocRef, "speciality", speciality);
                        transaction.update(sfDocRef, "about", about);
                        transaction.update(sfDocRef, "gender", finalGender);
                        transaction.update(sfDocRef, "address", address);
                        transaction.update(sfDocRef, "pincode", pincode);
                        transaction.update(sfDocRef, "dpin", dpinList);
                        transaction.update(sfDocRef, "lon",longitude);
                        transaction.update(sfDocRef, "lat",latitude);


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
                Intent intent= new Intent(getApplicationContext(),ChefmainActivity.class);
                intent.putExtra("fragment","account");
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK|Intent.FLAG_ACTIVITY_CLEAR_TASK);
                startActivity(intent);
//                finish();
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
//                        String url = String.valueOf(uri);
//                        store(url);
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

//    private void store(String url) {
//        DocumentReference documentReference = firestore.collection("customers").document(FirebaseAuth.getInstance().getCurrentUser().getUid());
//
//        documentReference.update("url",url);
//    }

    private void choosePicture() {
//        Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
//        intent.setType("image/*");
//        intent.setAction(Intent.ACTION_GET_CONTENT);
        mGetContent.launch("image/*");

    }


    protected void onStart(){
        super.onStart();
        documentReference.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @RequiresApi(api = Build.VERSION_CODES.O)
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if(task.getResult().exists()){
                    String dpinList = "";
                    String name = task.getResult().getString("name");
                    String email = task.getResult().getString("email");
                    String phone = task.getResult().getString("phone");
                    String gender = task.getResult().getString("gender");
//                    String password1 = task.getResult().getString("Password");
                    String speciality = task.getResult().getString("speciality");
                    String about = task.getResult().getString("about");
                    String address1= task.getResult().getString("address");
                    String pincode = task.getResult().getString("pincode");
                    List<String> dpin1 = (List<String>) task.getResult().get("dpin");
                    String dpin = String.join("," , dpin1);

                    chef_address.setText(address1);
                    chef_name.setText(name);
                    chef_email.setText(email);
                    chef_number.setText(phone);
                    chef_speciality.setText(speciality);
                    chef_about.setText(about);
                    chef_pincode.setText(pincode);
                    chef_deliverypin.setText(dpin);
//                    chef_password.setText(password1);
//                    chef_confirmpassword.setText(password1);

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
//                Toast.makeText(this , "Delete Profile", Toast.LENGTH_SHORT).show();
                //delete profile
                ShowDialog();

                break;
            default:
                break;
        }
        return true;
    }

    private void ShowDialog(){
        AlertDialog.Builder builder = new AlertDialog.Builder(EditprofileActivity.this);
        builder.setTitle("Delete");
        builder.setMessage("Are you sure to delete Profile?");
        builder.setPositiveButton("YES", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                documentReference.delete()
                        .addOnSuccessListener(new OnSuccessListener<Void>() {
                            @Override
                            public void onSuccess(Void unused) {

//                                deleteuser(chef_email.getText().toString(), chef_password.getText().toString());

                            }
                        })
                        .addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                                Toast.makeText(EditprofileActivity.this, "Error Occurred: "+e, Toast.LENGTH_SHORT);

                            }
                        });
            }
        });
        builder.setNegativeButton("NO", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                dialogInterface.dismiss();
            }
        });
        AlertDialog alertDialog = builder.create();
        alertDialog.show();

    }

    //delete the account (user cannot login again with this email and password)
    private void deleteuser(String email, String password) {

        final FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        DatabaseReference dishRef;
        dishRef= FirebaseDatabase.getInstance().getReference("Dish").child(onlineUserID);

        // Get auth credentials from the user for re-authentication. The example below shows
        // email and password credentials but there are multiple possible providers,
        // such as GoogleAuthProvider or FacebookAuthProvider.
        AuthCredential credential = EmailAuthProvider.getCredential(email, password);

        // Prompt the user to re-provide their sign-in credentials
        if (user != null) {
            user.reauthenticate(credential)
                    .addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            dishRef.removeValue();
                            user.delete()
                                    .addOnCompleteListener(new OnCompleteListener<Void>() {
                                        @Override
                                        public void onComplete(@NonNull Task<Void> task) {
                                            if (task.isSuccessful()) {
                                                Log.d("TAG", "User account deleted.");
                                                Toast.makeText(EditprofileActivity.this, "Profile Deleted", Toast.LENGTH_SHORT);
                                                Intent intent=new Intent(getApplicationContext(),LoginActivity.class);
                                                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK|Intent.FLAG_ACTIVITY_CLEAR_TASK);
                                                startActivity(intent);
                                            }
                                        }
                                    });
                        }
                    });
        }
    }


    private void locationEnabled() {
        LocationManager lm = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        boolean gps_enabled = false;
        boolean network_enabled = false;
        try {
            gps_enabled = lm.isProviderEnabled(LocationManager.GPS_PROVIDER);
        } catch (Exception e) {
            e.printStackTrace();
        }
        try {
            network_enabled = lm.isProviderEnabled(LocationManager.NETWORK_PROVIDER);
        } catch (Exception e) {
            e.printStackTrace();
        }
        if (!gps_enabled && !network_enabled) {
            new AlertDialog.Builder(EditprofileActivity.this)
                    .setTitle("Enable GPS Service")
                    .setMessage("We need your GPS location to show Near Places around you.")
                    .setCancelable(false)
                    .setPositiveButton("Enable", new
                            DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface paramDialogInterface, int paramInt) {
                                    startActivity(new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS));
                                }
                            })
                    .setNegativeButton("Cancel", null)
                    .show();

        }
    }

    void getLocation() {
        try {
            locationManager = (LocationManager) getApplicationContext().getSystemService(Context.LOCATION_SERVICE);
            locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 500, 5, (LocationListener) this);
        } catch (SecurityException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onLocationChanged(Location location) {
//        Toast.makeText(this, ""+location.getLatitude()+","+location.getLongitude(), Toast.LENGTH_SHORT).show();
//        Toast.makeText(this, latitude+" "+longitude, Toast.LENGTH_SHORT).show();

        try {
            Geocoder geocoder = new Geocoder(getApplicationContext(), Locale.getDefault());
            List<Address> addresses = geocoder.getFromLocation(location.getLatitude(), location.getLongitude(), 1);

            chef_pincode.setText(addresses.get(0).getPostalCode());
            chef_address.setText(addresses.get(0).getAddressLine(0));
            latitude=location.getLatitude();
            longitude=location.getLongitude();



//            Toast.makeText(this, "latitude:"+location.getLatitude()+",longitude:"+location.getLongitude(), Toast.LENGTH_SHORT).show();

        } catch (Exception e) {
        }
    }

    @Override
    public void onStatusChanged(String provider, int status, Bundle extras) {

    }

    @Override
    public void onProviderEnabled(String provider) {

    }

    @Override
    public void onProviderDisabled(String provider) {

    }

    public void back(View view) {

        Intent intent=new Intent(getApplicationContext(),ChefmainActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK|Intent.FLAG_ACTIVITY_CLEAR_TASK);
        intent.putExtra("fragment","account");
        startActivity(intent);
    }



}