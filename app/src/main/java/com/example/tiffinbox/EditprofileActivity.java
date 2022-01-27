package com.example.tiffinbox;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

public class EditprofileActivity extends AppCompatActivity {
    
    Toolbar toolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_editprofile);
        toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
    }


    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.updateprofile, menu);
        return true;
    }
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()){
            case R.id.account_delete:
                Toast.makeText(this , "Delete Profile", Toast.LENGTH_SHORT).show();
                         //delete profile

                break;
            default:
                break;
        }
        return true;
    }

    public void back(View view) {

        Intent intent=new Intent(getApplicationContext(),ChefmainActivity.class);
        startActivity(intent);
    }


}