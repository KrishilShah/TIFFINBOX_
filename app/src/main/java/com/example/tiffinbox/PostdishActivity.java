package com.example.tiffinbox;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

public class PostdishActivity extends AppCompatActivity {

    EditText dname,ddes,dprice;
    Button post;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_postdish);

        dname=findViewById(R.id.dish_name);
        ddes=findViewById(R.id.dish_description);
        dprice=findViewById(R.id.dish_price);
        post=findViewById(R.id.post_dish);


    }

    public void back(View view) {
        Intent intent= new Intent(getApplicationContext(),ChefmainActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK|Intent.FLAG_ACTIVITY_CLEAR_TASK);
//        intent.putExtra("fragments","home");
        startActivity(intent);
    }
}