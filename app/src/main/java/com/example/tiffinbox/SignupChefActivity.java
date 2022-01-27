package com.example.tiffinbox;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.google.firebase.FirebaseException;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.PhoneAuthCredential;
import com.google.firebase.auth.PhoneAuthProvider;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.concurrent.TimeUnit;

public class SignupChefActivity extends AppCompatActivity {
    private EditText username,email,password,confirmPassword,phone;
    private Button next;
    FirebaseDatabase rootNode;
    DatabaseReference reference;
    private FirebaseAuth mFirebaseAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup_chef);

        username=(EditText) findViewById(R.id.username);
        phone=(EditText) findViewById(R.id.phone);
        email=(EditText) findViewById(R.id.email);
        password=(EditText) findViewById(R.id.password);
        confirmPassword=(EditText) findViewById(R.id.confirmPassword);
        next=(Button) findViewById(R.id.next);
        final ProgressBar progressBar=findViewById(R.id.progressBar);
        mFirebaseAuth=FirebaseAuth.getInstance();




        next.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                rootNode=FirebaseDatabase.getInstance();
                reference=rootNode.getReference("chef");
//                reference.setValue()
                String username_chef =username.getText().toString();
                String email_chef =email.getText().toString();
                String password_chef =password.getText().toString();
                String confirmpass_chef =confirmPassword.getText().toString();
                String phone_chef = phone.getText().toString();

                if(TextUtils.isEmpty(username_chef) || TextUtils.isEmpty(email_chef) || TextUtils.isEmpty(password_chef)||TextUtils.isEmpty(confirmpass_chef) ||TextUtils.isEmpty(phone_chef) ){
                    Toast.makeText(SignupChefActivity.this, "Please fill all the require fields", Toast.LENGTH_SHORT).show();
                    return;
                }
                if(password.length() < 6){
                    password.setError("Password must contain atleast 6 characters");
                    return;
                }
                if(!(TextUtils.equals(password_chef, confirmpass_chef))){
                    password.setError("Passwords Do not Match");
                    return;
                }
                if(phone_chef.length()>10){
                    Toast.makeText(SignupChefActivity.this, "Mobile Number should not be more than 10 digits", Toast.LENGTH_SHORT).show();
                    return;
                }
                else {

                    progressBar.setVisibility(View.VISIBLE);
                    next.setVisibility(View.INVISIBLE);
                    PhoneAuthProvider.getInstance().verifyPhoneNumber(
                            "+91" + phone_chef,
                            60,
                            TimeUnit.SECONDS,
                            SignupChefActivity.this,
                            new PhoneAuthProvider.OnVerificationStateChangedCallbacks() {
                                @Override
                                public void onVerificationCompleted(@NonNull PhoneAuthCredential phoneAuthCredential) {

                                    progressBar.setVisibility(View.GONE);
                                    next.setVisibility(View.VISIBLE);

                                }

                                @Override
                                public void onVerificationFailed(@NonNull FirebaseException e) {
                                    progressBar.setVisibility(View.GONE);
                                    next.setVisibility(View.VISIBLE);
                                    Toast.makeText(SignupChefActivity.this, e.getMessage(), Toast.LENGTH_SHORT).show();

                                }


                                @Override
                                public void onCodeSent(@NonNull String verificationId, @NonNull PhoneAuthProvider.ForceResendingToken forceResendingToken) {
                                    progressBar.setVisibility(View.GONE);
                                    next.setVisibility(View.VISIBLE);
                                    Intent intent = new Intent(getApplicationContext(), verifyOtp.class);
                                    intent.putExtra("PhoneNo", phone_chef);
                                    intent.putExtra("verificationId", verificationId);
                                    intent.putExtra("value", "chef");
                                    intent.putExtra("username", username_chef);
                                    intent.putExtra("email", email_chef);
                                    intent.putExtra("password", password_chef);
                                    startActivity(intent);
                                }


                            }
                    );

//                    Intent intent = new Intent(getApplicationContext(),verifyOtp.class);
//                    intent.putExtra("PhoneNo",phone_chef);
//                    startActivity(intent);


//                    Chef_UserHelperClass helperClass = new Chef_UserHelperClass(username_chef,email_chef,password_chef,confirmpass_chef,phone_chef);
//                    reference.child(phone_chef).setValue(helperClass);
//                    Toast.makeText(admin_login.this, "SUCCESS", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }
//    @Override
//    protected void onStart() {
//        super.onStart();
//        FirebaseUser mFirebaseUser=mFirebaseAuth.getCurrentUser();
//        if(mFirebaseUser==null){
//        }
//        else{
//            Intent intent =new Intent(SignupChefActivity.this,chefhome.class);
//            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
//            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
//            startActivity(intent);
//        }
//    }

}