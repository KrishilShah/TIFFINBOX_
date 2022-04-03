package com.example.tiffinbox;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import android.app.DatePickerDialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;


import com.example.tiffinbox.adapters.MyCartAdapter;
import com.example.tiffinbox.cheffragment.ChefOrderlistFragment;
import com.example.tiffinbox.models.CouponModel;
import com.example.tiffinbox.models.MyCartModel;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.text.BreakIterator;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;

public class AddCouponCodesActivity extends AppCompatActivity {

    //Cart Wala:
    MyCartAdapter cartAdapter;
    List<MyCartModel> cartModelList;
    MyCartAdapter.OnItemClickListener onItemClickListener;

    private ImageButton Back;
    private EditText WriteCode,CouponDescription,MinimumOrderPrice,CouponPrice;
    private Button AddIT;
    private TextView ExpiryDate,textView;
    private String couponId;
    private boolean isUpdating= false;
    List<CouponModel> couponModelList;
    RecyclerView recyclerView;
    FirebaseAuth auth;


    //FirebaseFirestore droot;
    //DocumentReference dref;
    //firebase auth
    FirebaseFirestore db;
    FirebaseAuth firebaseAuth;
    //progress dialogue:
    ProgressDialog progressDialog;
    private String onlineUserID;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_coupon_codes);
        Back=findViewById(R.id.Back);
        WriteCode=findViewById(R.id.WriteCode);
        CouponPrice=findViewById(R.id.CouponPrice);
        CouponDescription=findViewById(R.id.CouponDescription);
        MinimumOrderPrice=findViewById(R.id.MinimumOrderPrice);
        ExpiryDate=findViewById(R.id.ExpiryDate);
        AddIT=findViewById(R.id.AddIT);
        textView=findViewById(R.id.textView);
        recyclerView = findViewById(R.id.recyclerview);

        //init firebase:
        //droot= FirebaseFirestore.getInstance();


        db= FirebaseFirestore.getInstance();
        auth= FirebaseAuth.getInstance();
        firebaseAuth=FirebaseAuth.getInstance();
        onlineUserID = FirebaseAuth.getInstance().getCurrentUser().getUid();

        String value=getIntent().getStringExtra("value");
        if(value.equals("update")){
            //Come here from adapter to update the record:
            couponId=getIntent().getStringExtra("id");

            textView.setText("Update Coupon ");
            AddIT.setText("Update");



            isUpdating=true;
            WriteCode.setText(getIntent().getStringExtra("writecode"));
            CouponDescription.setText(getIntent().getStringExtra("desc"));
            CouponPrice.setText(getIntent().getStringExtra("price"));
            MinimumOrderPrice.setText(getIntent().getStringExtra("minimumprice"));
            ExpiryDate.setText(getIntent().getStringExtra("expirydate"));

        }

        else if(value.equals("add")){
            //came here to add new coupon:
            textView.setText("Add Coupon");
            AddIT.setText("ADD");

            isUpdating=false;
        }

        Back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent(getApplicationContext(), ChefmainActivity.class);
                intent.putExtra("fragment","coupon");
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK|Intent.FLAG_ACTIVITY_CLEAR_TASK);
                startActivity(intent);
                finish();
            }
        });

        ExpiryDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                datePickDialog();
            }
        });

        AddIT.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                inputData();

            }
        });

    }


    private void datePickDialog() {
        Calendar c=Calendar.getInstance();
        int mYear = c.get(Calendar.YEAR);
        int mMonth = c.get(Calendar.MONTH);
        int mDay = c.get(Calendar.DAY_OF_MONTH);

        DatePickerDialog datePickerDialog=new DatePickerDialog(this, new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker datePicker, int year, int monthOfYear, int dayOfMonth) {
                DecimalFormat mformat= new DecimalFormat("00");
                String pDay=mformat.format(dayOfMonth);
                String pMonth=mformat.format(monthOfYear);
                String pYear = " "+ year;
                String pDate=pDay+"/"+pMonth+"/"+pYear;  //e.g. 02/03/2022
                ExpiryDate.setText(pDate);
            }
        },mYear , mMonth, mDay);

        //Show Dialog:
        datePickerDialog.show();
        //disable past Dates:
        datePickerDialog.getDatePicker().setMinDate(System.currentTimeMillis()-1000);
    }

    private String writeCode,couponDescription,minimumOrderPrice,expiryDate,couponPrice;
    private void inputData() {
        //input data
        writeCode=WriteCode.getText().toString().trim();
        couponDescription=CouponDescription.getText().toString().trim();
        minimumOrderPrice=MinimumOrderPrice.getText().toString().trim();
        expiryDate=ExpiryDate.getText().toString().trim();
        couponPrice=CouponPrice.getText().toString().trim();

        //Validate for Date
        if (TextUtils.isEmpty(writeCode)){
            Toast.makeText(this, "Enter Discount Code...", Toast.LENGTH_SHORT).show();
            return; // stop
        }
        if (TextUtils.isEmpty(couponDescription)){
            Toast.makeText(this, "Enter Description...", Toast.LENGTH_SHORT).show();
            return; // stop
        }
        if (TextUtils.isEmpty(couponPrice)){
            Toast.makeText(this, "Enter Coupon Price...", Toast.LENGTH_SHORT).show();
            return; // stop
        }
        if (TextUtils.isEmpty(minimumOrderPrice)){
            Toast.makeText(this, "Enter Minimum Order Price...", Toast.LENGTH_SHORT).show();
            return; // stop
        }
        if (TextUtils.isEmpty(minimumOrderPrice)){
            Toast.makeText(this, "Choose Expiry Date...", Toast.LENGTH_SHORT).show();
            return; // stop
        }
        //adding/updating  to datatbase

        if (isUpdating){
            //Update data:
            updateDataToDb();

        }
        else{
            //Add Data:
            addDataToDb();
        }
    }

    private void updateDataToDb() {

        String timestamp = ""+System.currentTimeMillis();
        //setup data to add it in database
        HashMap<String,Object> hashMap=new HashMap<>();

        hashMap.put("couponDescription",""+couponDescription);
        hashMap.put("price",""+couponPrice);
        hashMap.put("minimumorderprice",""+minimumOrderPrice);
        hashMap.put("couponCode",""+writeCode);
        hashMap.put("expiryDate",""+expiryDate);

        String id=getIntent().getStringExtra("id");

        //init db ref coupon > CouponId > Coupon Data
        DocumentReference documentReference= db.collection("coupon").document(firebaseAuth.getCurrentUser().getUid())
                .collection("CurrentUser").document(firebaseAuth.getCurrentUser().getUid());
        documentReference.update(hashMap)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void unused) {
                        //Updated
                        Toast.makeText(AddCouponCodesActivity.this,"Update Successful",Toast.LENGTH_SHORT).show();
                        Intent intent = new Intent(getApplicationContext(), ChefmainActivity.class);
                        intent.putExtra("fragment","coupon");
                        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK|Intent.FLAG_ACTIVITY_CLEAR_TASK);
                        startActivity(intent);
                        finish();

                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(AddCouponCodesActivity.this,"Update Failed:"+e.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                });
    }

    private void addDataToDb() {

        String timestamp = ""+System.currentTimeMillis();
        //setup data to add it in database
        HashMap<String,Object> hashMap=new HashMap<>();
        hashMap.put("Id",timestamp);
        hashMap.put("timestamp",timestamp);
        hashMap.put("couponDescription",couponDescription);
        hashMap.put("price",couponPrice);
        hashMap.put("minimumorderprice",minimumOrderPrice);
        hashMap.put("couponCode",writeCode);
        hashMap.put("expiryDate",expiryDate);

        DocumentReference documentReference = db.collection("coupon").document(firebaseAuth.getCurrentUser().getUid()).collection("CurrentUser").document(firebaseAuth.getCurrentUser().getUid());
        documentReference.set(hashMap).addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void aVoid) {
                //code added


                Toast.makeText(AddCouponCodesActivity.this,"Coupon Code Added Successfully",Toast.LENGTH_SHORT).show();

                Intent intent = new Intent(getApplicationContext(), ChefmainActivity.class);
                intent.putExtra("fragment","coupon");
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK|Intent.FLAG_ACTIVITY_CLEAR_TASK);
                startActivity(intent);
                finish();
            }
        })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        //adding failed then show this:
                        //progressDialog.dismiss();
                        Toast.makeText(AddCouponCodesActivity.this,"Failed:"+e.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                });



    }
}