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

public class SignupUserActivity extends AppCompatActivity {
    EditText username1,email1,password1,confirmPassword1,phone1;
    Button next;
    FirebaseDatabase rootNode;
    DatabaseReference reference;
    private FirebaseAuth mFirebaseAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup_user);

        username1=(EditText) findViewById(R.id.signup_name);
        email1=(EditText) findViewById(R.id.signup_email);
        phone1=(EditText) findViewById(R.id.signup_phone);
        password1=(EditText) findViewById(R.id.signup_passcode);
        confirmPassword1=(EditText) findViewById(R.id.signup_confirm);
        next=(Button) findViewById(R.id.proceedbtn);
        final ProgressBar progressBar=findViewById(R.id.progressBar);
        mFirebaseAuth=FirebaseAuth.getInstance();

        next.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                rootNode=FirebaseDatabase.getInstance();
                reference=rootNode.getReference("customer");
//                reference.setValue()
                String username_user =username1.getText().toString();
                String email_user =email1.getText().toString();
                String password_user =password1.getText().toString();
                String confirmpass_user =confirmPassword1.getText().toString();
                String phone_user = phone1.getText().toString();

                if(TextUtils.isEmpty(username_user) || TextUtils.isEmpty(email_user) || TextUtils.isEmpty(password_user)||TextUtils.isEmpty(confirmpass_user) ||TextUtils.isEmpty(phone_user) ){
                    Toast.makeText(SignupUserActivity.this, "Please fill all the require fields", Toast.LENGTH_SHORT).show();
                    return;
                }
                if(phone_user.length()>10){
                    Toast.makeText(SignupUserActivity.this, "Mobile Number should not be more than 10 digits", Toast.LENGTH_SHORT).show();
                    return;
                }
                if(password_user.length() < 6){
                    password1.setError("Password must contain atleast 6 characters");
                    return;
                }
                if(!(TextUtils.equals(password_user, confirmpass_user))){
                    password1.setError("Passwords Do not Match");
                    return;
                }
                else{

                    progressBar.setVisibility(View.VISIBLE);
                    next.setVisibility(View.INVISIBLE);
                    PhoneAuthProvider.getInstance().verifyPhoneNumber(
                            "+91"+phone_user,
                            60,
                            TimeUnit.SECONDS,
                            SignupUserActivity.this,
                            new PhoneAuthProvider.OnVerificationStateChangedCallbacks(){
                                @Override
                                public void onVerificationCompleted(@NonNull PhoneAuthCredential phoneAuthCredential) {

                                    progressBar.setVisibility(View .GONE);
                                    next.setVisibility(View.VISIBLE);


                                }

                                @Override
                                public void onVerificationFailed(@NonNull FirebaseException e) {
                                    progressBar.setVisibility(View .GONE);
                                    next.setVisibility(View.VISIBLE);
                                    Toast.makeText(SignupUserActivity.this, e.getMessage(), Toast.LENGTH_SHORT).show();

                                }



                                @Override
                                public void onCodeSent(@NonNull String verificationId, @NonNull PhoneAuthProvider.ForceResendingToken forceResendingToken) {
                                    progressBar.setVisibility(View.GONE);
                                    next.setVisibility(View.VISIBLE);
                                    Intent intent=new Intent(getApplicationContext(),verifyOtp.class);
                                    intent.putExtra("PhoneNo",phone_user);
                                    intent.putExtra("verificationId",verificationId);
                                    intent.putExtra("value","user");
                                    intent.putExtra("username", username_user);
                                    intent.putExtra("email", email_user);
                                    intent.putExtra("password", password_user);
                                    startActivity(intent);
                                }


                            }
                    );

//                    Intent intent = new Intent(getApplicationContext(),verifyOtp.class);
//                    intent.putExtra("PhoneNo",phone_user);
//                    startActivity(intent);


//                    Chef_UserHelperClass helperClass = new Chef_UserHelperClass(username_user,email_user,password_user,confirmpass_user,phone_user);
//                    reference.child(phone_user).setValue(helperClass);
//                    Toast.makeText(admin_login.this, "SUCCESS", Toast.LENGTH_SHORT).show();
                }

            }
        });


    }
//    protected void onStart() {
//        super.onStart();
//        FirebaseUser mFirebaseUser=mFirebaseAuth.getCurrentUser();
//        if(mFirebaseUser==null){
//        }
//        else{
//            Intent intent =new Intent(SignupUserActivity.this,UserhomeActivity.class);
//            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
//            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
//            startActivity(intent);
//        }
//    }
}