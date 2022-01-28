package com.example.tiffinbox;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;

import com.example.tiffinbox.cheffragment.ChefAccountFragment;
import com.example.tiffinbox.cheffragment.ChefHomeFragment;
import com.example.tiffinbox.cheffragment.ChefOrderlistFragment;
import com.example.tiffinbox.cheffragment.ChefPaymentFragment;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.navigation.NavigationView;
import com.google.firebase.auth.FirebaseAuth;

public class ChefmainActivity extends AppCompatActivity {

    NavigationView navigationView;
    ActionBarDrawerToggle toggle;
    DrawerLayout drawerLayout;
    Toolbar toolbar;
//    FloatingActionButton floatingActionButton;
    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chefmain);

        toolbar=(Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        navigationView = (NavigationView) findViewById(R.id.nav_draw);
        drawerLayout = (DrawerLayout) findViewById(R.id.drawer);

        toggle = new ActionBarDrawerToggle(this,drawerLayout,toolbar,R.string.navigation_drawer_open,R.string.navigation_drawer_close);
        toggle.syncState();
        String value = getIntent().getStringExtra("fragment");

        if(value.equals("home")){
        getSupportFragmentManager().beginTransaction().add(R.id.container,new ChefHomeFragment()).commit();
        }
        else if(value.equals("account")){
            getSupportFragmentManager().beginTransaction().add(R.id.container,new ChefAccountFragment()).commit();
        }

//        floatingActionButton.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                Intent intent=new Intent(getApplicationContext(),PostdishActivity.class);
//                startActivity(intent);
//            }
//        });




        navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {

                switch(item.getItemId())
                {
                    case R.id.chef_home:
                        Toast.makeText(getApplicationContext(), "Home Fragment", Toast.LENGTH_SHORT).show();
                        toolbar.setTitle("TiffinBox");
                        getSupportFragmentManager().beginTransaction().replace(R.id.container,new ChefHomeFragment()).commit();
                        drawerLayout.closeDrawer(GravityCompat.START);
                        break;
                    case R.id.chef_orderlist:
                        Toast.makeText(getApplicationContext(), "Orderlist Fragment", Toast.LENGTH_SHORT).show();
                        toolbar.setTitle("Orderlist");
                        getSupportFragmentManager().beginTransaction().replace(R.id.container,new ChefOrderlistFragment()).commit();
                        drawerLayout.closeDrawer(GravityCompat.START);
                        break;
                    case R.id.payment:
                        Toast.makeText(getApplicationContext(), "Payment Fragment", Toast.LENGTH_SHORT).show();
                        toolbar.setTitle("Payment");
                        getSupportFragmentManager().beginTransaction().replace(R.id.container,new ChefPaymentFragment()).commit();
                        drawerLayout.closeDrawer(GravityCompat.START);
                        break;

                    case R.id.chef_account:
                        Toast.makeText(getApplicationContext(), "Account Fragment", Toast.LENGTH_SHORT).show();
                        toolbar.setTitle("Profile");
                        getSupportFragmentManager().beginTransaction().replace(R.id.container,new ChefAccountFragment()).commit();
                        drawerLayout.closeDrawer(GravityCompat.START);
                        break;

                    case R.id.chef_logout:
                        FirebaseAuth.getInstance().signOut(); //logs out current user
                        startActivity(new Intent(getApplicationContext(), MainActivity.class));
                        break;

                }
//                toggle.syncState();

                return true;
            }
        });
    }

}