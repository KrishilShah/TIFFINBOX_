package com.example.tiffinbox;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

public class OrderFoodActivity extends AppCompatActivity {

    ImageView dishimage;
    TextView dishdes,dishprice,dishname;
    Toolbar toolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_order_food);
        toolbar=(Toolbar) findViewById(R.id.toolbar);

        dishimage=findViewById(R.id.dish_image);
        dishdes=findViewById(R.id.dish_des);
        dishprice=findViewById(R.id.dish_price);
        dishname=findViewById(R.id.dish_name);

        String dname=getIntent().getStringExtra("dishname");
        String ddes=getIntent().getStringExtra("dishdes");
        String dprice=getIntent().getStringExtra("dishprice");
        String durl=getIntent().getStringExtra("dishurl");

        dishname.setText(dname);
        dishprice.setText(dprice);
        dishdes.setText(ddes);
        Picasso.get().load(durl).into(dishimage);

    }

    public void back(View view) {
        finish();
    }
}