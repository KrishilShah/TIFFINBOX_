package com.example.tiffinbox;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SwitchCompat;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.CompoundButton;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.messaging.FirebaseMessaging;

public class activity_settings extends AppCompatActivity {
    public SwitchCompat fcmSwitch;
    private TextView notificationStatusTv;
    private ImageButton backBtn;
    private static final String enabledMessage = "Notifications are enabled";
    private static final String disabledMessage = "Notifications are disabled";
    private boolean isChecked=false;
    private FirebaseAuth firebaseAuth;
    private SharedPreferences sp;
    private SharedPreferences.Editor spEditor;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);
        fcmSwitch =findViewById(R.id.fcmSwitch);
        notificationStatusTv=findViewById(R.id.notificationstatusTv);
        backBtn=findViewById(R.id.backBtn);
        firebaseAuth =FirebaseAuth.getInstance();
        sp=getSharedPreferences("SETTINGS_SP",MODE_PRIVATE);

        isChecked=sp.getBoolean("FCM_ENABLED",false);
        fcmSwitch.setChecked(isChecked);
        if(isChecked){
            notificationStatusTv.setText(enabledMessage);
        }else{
            notificationStatusTv.setText(disabledMessage);
        }



        fcmSwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(isChecked){
                    subscribeToTopic();
                }
                else
                {
                    unSubscribeToTopic();
                }
            }
        });
        backBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

    // add switch check change listener to enable disable notifications




    private void subscribeToTopic(){
        FirebaseMessaging.getInstance().subscribeToTopic(Constant.FCM_TOPIC)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        spEditor=sp.edit();
                        spEditor.putBoolean("FCM_ENABLED",true);
                        spEditor.apply();
                        Toast.makeText(activity_settings.this, ""+enabledMessage, Toast.LENGTH_SHORT).show();
                        notificationStatusTv.setText(enabledMessage);
                    }
                }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(activity_settings.this, ""+e.getMessage(), Toast.LENGTH_SHORT).show();


            }
        });
    }
    private void unSubscribeToTopic(){
        FirebaseMessaging.getInstance().unsubscribeFromTopic(Constant.FCM_TOPIC)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        spEditor=sp.edit();
                        spEditor.putBoolean("FCM_ENABLED",false);
                        spEditor.apply();
                        Toast.makeText(activity_settings.this, ""+disabledMessage, Toast.LENGTH_SHORT).show();
                        notificationStatusTv.setText(disabledMessage);

                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(activity_settings.this, ""+e.getMessage(), Toast.LENGTH_SHORT).show();

                    }
                });


}}