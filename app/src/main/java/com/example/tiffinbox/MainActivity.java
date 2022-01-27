package com.example.tiffinbox;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


    }

    public void user(View view) {
        Intent intent =new Intent(MainActivity.this,LoginActivity.class);
        intent.putExtra("value","user");
        startActivity(intent);
    }

    public void chef(View view) {
        Intent intent =new Intent(MainActivity.this, LoginActivity.class);
        intent.putExtra("value","chef");
        startActivity(intent);
    }
}