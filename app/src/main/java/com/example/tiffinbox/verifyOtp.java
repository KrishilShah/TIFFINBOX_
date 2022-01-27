package com.example.tiffinbox;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.FirebaseException;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.PhoneAuthCredential;
import com.google.firebase.auth.PhoneAuthProvider;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;

public class verifyOtp extends AppCompatActivity {
    private EditText inputCode1,inputCode2,inputCode3,inputCode4,inputCode5,inputCode6;
    private String verificationId;
    String chefID, userID;
    FirebaseAuth mFirebaseAuth;
    FirebaseFirestore firestore;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_verify_otp);

        inputCode1=findViewById(R.id.inputCode1);
        inputCode2=findViewById(R.id.inputCode2);
        inputCode3=findViewById(R.id.inputCode3);
        inputCode4=findViewById(R.id.inputCode4);
        inputCode5=findViewById(R.id.inputCode5);
        inputCode6=findViewById(R.id.inputCode6);

        setupOTPInputs();

        final ProgressBar progressBar=findViewById(R.id.progressBar);
        final Button buttonVerify=findViewById(R.id.buttonVerify);
        verificationId=getIntent().getStringExtra("verificationId");

        mFirebaseAuth = FirebaseAuth.getInstance();
        firestore = FirebaseFirestore.getInstance();

        buttonVerify.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(inputCode1.getText().toString().trim().isEmpty()
                        || inputCode2.getText().toString().trim().isEmpty()
                        ||inputCode3.getText().toString().trim().isEmpty()
                        || inputCode4.getText().toString().trim().isEmpty()
                        || inputCode5.getText().toString().trim().isEmpty()
                        || inputCode6.getText().toString().trim().isEmpty()){
                    Toast.makeText(verifyOtp.this, "Please enter valid code", Toast.LENGTH_SHORT).show();
                    return;
                }
                String code=
                        inputCode1.getText().toString()+
                                inputCode2.getText().toString()+
                                inputCode3.getText().toString()+
                                inputCode4.getText().toString()+
                                inputCode5.getText().toString()+
                                inputCode6.getText().toString();

                if(verificationId!=null){
                    progressBar.setVisibility(View.VISIBLE);
                    buttonVerify.setVisibility(View.INVISIBLE);
                    PhoneAuthCredential phoneAuthCredential= PhoneAuthProvider.getCredential(
                            verificationId,
                            code
                    );
                    String email = getIntent().getStringExtra("email");
                    String password = getIntent().getStringExtra("password");
                    FirebaseAuth.getInstance().createUserWithEmailAndPassword(email, password)
                            .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                                @Override
                                public void onComplete(@NonNull Task<AuthResult> task) {
                                    progressBar.setVisibility(View.GONE);
                                    buttonVerify.setVisibility(View.VISIBLE);
                                    if(task.isSuccessful()){
                                        String value=getIntent().getStringExtra("value");
                                        String email = getIntent().getStringExtra("email");
                                        String password = getIntent().getStringExtra("password");
                                        String username = getIntent().getStringExtra("username");
                                        String phoneNo = getIntent().getStringExtra("PhoneNo");
                                        if(value.equals("chef")) {
                                            Toast.makeText(verifyOtp.this, "Account Created Successfully!", Toast.LENGTH_SHORT).show();
                                            chefID = mFirebaseAuth.getCurrentUser().getUid();
                                            DocumentReference documentReference = firestore.collection("chefs").document(chefID);
                                            Map<String,Object> chef = new HashMap<>();
                                            chef.put("name", username);
                                            chef.put("email", email);
                                            chef.put("Password", password);
                                            chef.put("phone", phoneNo);
                                            chef.put("UserType", value);
                                            documentReference.set(chef).addOnSuccessListener(new OnSuccessListener<Void>() {
                                                @Override
                                                public void onSuccess(Void unused) {

                                                }
                                            });
                                            Intent intent = new Intent(getApplicationContext(), ChefmainActivity.class);
                                            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
//                                            intent.putExtra("fragment","account");
                                            startActivity(intent);


                                        }

                                        else if (value.equals("user")){
                                            Toast.makeText(verifyOtp.this, "account Created Successfully!", Toast.LENGTH_SHORT).show();
                                            userID = mFirebaseAuth.getCurrentUser().getUid();
                                            DocumentReference documentReference = firestore.collection("customers").document(userID);
                                            Map<String,Object> user = new HashMap<>();
                                            user.put("name", username);
                                            user.put("email", email);
                                            user.put("Password", password);
                                            user.put("phone", phoneNo);
                                            user.put("UserType", value);
                                            documentReference.set(user).addOnSuccessListener(new OnSuccessListener<Void>() {
                                                @Override
                                                public void onSuccess(Void unused) {

                                                }
                                            });
                                            Intent intent = new Intent(getApplicationContext(), UserhomeActivity.class);
                                            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
//                                            intent.putExtra("fragment","account");
                                            startActivity(intent);
                                        }

                                    }
                                    else {
                                        Toast.makeText(verifyOtp.this, task.getException().getMessage() , Toast.LENGTH_SHORT).show();
                                    }
                                }
                            });
                }
            }
        });
        findViewById(R.id.textResendOtp).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                PhoneAuthProvider.getInstance().verifyPhoneNumber(
                        "+91"+getIntent().getStringExtra("PhoneNo"),
                        60,
                        TimeUnit.SECONDS,
                        verifyOtp.this,
                        new PhoneAuthProvider.OnVerificationStateChangedCallbacks(){


                            @Override
                            public void onVerificationCompleted(@NonNull PhoneAuthCredential phoneAuthCredential) {

                            }

                            @Override
                            public void onVerificationFailed(@NonNull FirebaseException e) {
                                Toast.makeText(verifyOtp.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                            }



                            @Override
                            public void onCodeSent(@NonNull String newVerificationId, @NonNull PhoneAuthProvider.ForceResendingToken forceResendingToken) {

                                verificationId=newVerificationId;
                                Toast.makeText(verifyOtp.this, "OTP Sent", Toast.LENGTH_SHORT).show();
                            }
                        }
                );
            }
        });

    }

    private void setupOTPInputs(){
        inputCode1.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                if (!charSequence.toString().trim().isEmpty()) {
                    inputCode2.requestFocus();
                }
            }

            @Override
            public void afterTextChanged(Editable charSequence) {

            }
        });
        inputCode2.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                if (!charSequence.toString().trim().isEmpty()) {
                    inputCode3.requestFocus();
                }
            }

            @Override
            public void afterTextChanged(Editable charSequence) {

            }
        });
        inputCode3.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                if (!charSequence.toString().trim().isEmpty()) {
                    inputCode4.requestFocus();
                }
            }

            @Override
            public void afterTextChanged(Editable charSequence) {

            }
        });
        inputCode4.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                if (!charSequence.toString().trim().isEmpty()) {
                    inputCode5.requestFocus();
                }
            }

            @Override
            public void afterTextChanged(Editable charSequence) {

            }
        });
        inputCode5.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                if (!charSequence.toString().trim().isEmpty()) {
                    inputCode6.requestFocus();
                }
            }

            @Override
            public void afterTextChanged(Editable charSequence) {

            }
        });
    }
}