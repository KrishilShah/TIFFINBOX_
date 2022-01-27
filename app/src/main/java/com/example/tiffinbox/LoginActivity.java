package com.example.tiffinbox;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

public class LoginActivity extends AppCompatActivity {
    TextView register, forgot_password;
    EditText email_id, password;
    Button login_button;
    FirebaseAuth mFirebaseAuth;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        register=findViewById(R.id.registerpage);
        email_id = findViewById(R.id.login_email);
        password = findViewById(R.id.login_passcode);
        forgot_password = findViewById(R.id.forgotpass);
        login_button = findViewById(R.id.loginbtn);

        mFirebaseAuth = FirebaseAuth.getInstance();

        login_button.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                String email = email_id.getText().toString().trim();
                String Password= password.getText().toString().trim();

                if(TextUtils.isEmpty(email)){
                    email_id.setError("Email is Required!");
                    return;
                }
                if(TextUtils.isEmpty(Password)) {
                    password.setError("Password is Required!");
                    return;
                }

                //Authenticate the user
                mFirebaseAuth.signInWithEmailAndPassword(email, Password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if(task.isSuccessful()){
                            Toast.makeText(LoginActivity.this, "Logged in Successfully!", Toast.LENGTH_SHORT).show();
                            String value=getIntent().getStringExtra("value");
                            if(value.equals("chef"))
                            {
                                Intent intent = new Intent(getApplicationContext(),ChefmainActivity.class);
//                                intent.putExtra("fragment","home");
                                startActivity(intent);
                            }
                            else if(value.equals("user"))
                            {
                                startActivity(new Intent(getApplicationContext(), UserhomeActivity.class));
                            }

                        }
                        else{
                            Toast.makeText(LoginActivity.this, "Some Error Occurred" + task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    }
                });
            }

        });

        forgot_password.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                EditText resetMail = new EditText(view.getContext());
                AlertDialog.Builder passwordResetDialog = new AlertDialog.Builder(view.getContext());
                passwordResetDialog.setMessage("Enter your Email to receive reset link.");
                passwordResetDialog.setView(resetMail);

                passwordResetDialog.setPositiveButton("YES", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        //extract the email and send reset link

                        String mail = resetMail.getText().toString();
                        mFirebaseAuth.sendPasswordResetEmail(mail).addOnSuccessListener(new OnSuccessListener<Void>() {
                            @Override
                            public void onSuccess(Void unused) {
                                Toast.makeText(LoginActivity.this, "Reset Link sent to your Email", Toast.LENGTH_SHORT).show();

                            }
                        }).addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                                Toast.makeText(LoginActivity.this, "ERROR!  Reset Link is Not sent" +e.getMessage(), Toast.LENGTH_SHORT).show();
                            }
                        });


                    }
                });
                passwordResetDialog.setNegativeButton("CANCEL", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        //close the dialog
                    }
                });

                passwordResetDialog.create().show();
            }
        });


    }

    public void register(View view) {
        String value=getIntent().getStringExtra("value");
        if(value.equals("chef"))
        {
            startActivity(new Intent(getApplicationContext(), SignupChefActivity.class));
        }
        else if(value.equals("user"))
        {
            startActivity(new Intent(getApplicationContext(), SignupUserActivity.class));
        }

    }
}