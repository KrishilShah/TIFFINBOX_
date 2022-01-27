package com.example.tiffinbox;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;

import com.example.tiffinbox.userFragment.UserAccountFragment;
import com.example.tiffinbox.userFragment.UserCartFragment;
import com.example.tiffinbox.userFragment.UserHomeFragment;
import com.example.tiffinbox.userFragment.UserOrderlistFragment;
import com.google.android.material.navigation.NavigationView;
import com.google.firebase.auth.FirebaseAuth;

public class UserhomeActivity extends AppCompatActivity {

    NavigationView navigationView;
    ActionBarDrawerToggle toggle;
    DrawerLayout drawerLayout;
    Toolbar toolbar;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_userhome);

        toolbar=(Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        navigationView = (NavigationView) findViewById(R.id.nav_draw);
        drawerLayout = (DrawerLayout) findViewById(R.id.drawer);

        toggle = new ActionBarDrawerToggle(this,drawerLayout,toolbar,R.string.navigation_drawer_open,R.string.navigation_drawer_close);
        toggle.syncState();

        getSupportFragmentManager().beginTransaction().add(R.id.container,new UserHomeFragment()).commit();


        navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {

                switch(item.getItemId())
                {
                    case R.id.user_home:
                        Toast.makeText(getApplicationContext(), "Home Fragment", Toast.LENGTH_SHORT).show();

                        getSupportFragmentManager().beginTransaction().replace(R.id.container,new UserHomeFragment()).commit();
                        drawerLayout.closeDrawer(GravityCompat.START);
                        break;
                    case R.id.user_orderlist:
                        Toast.makeText(getApplicationContext(), "Gallery Fragment", Toast.LENGTH_SHORT).show();

                        getSupportFragmentManager().beginTransaction().replace(R.id.container,new UserOrderlistFragment()).commit();
                        drawerLayout.closeDrawer(GravityCompat.START);
                        break;
                    case R.id.user_cart:
                        Toast.makeText(getApplicationContext(), "Slideshow Fragment", Toast.LENGTH_SHORT).show();

                        getSupportFragmentManager().beginTransaction().replace(R.id.container,new UserCartFragment()).commit();
                        drawerLayout.closeDrawer(GravityCompat.START);
                        break;

                    case R.id.user_account:
                        Toast.makeText(getApplicationContext(), "Slideshow Fragment", Toast.LENGTH_SHORT).show();

                        getSupportFragmentManager().beginTransaction().replace(R.id.container,new UserAccountFragment()).commit();
                        drawerLayout.closeDrawer(GravityCompat.START);
                        break;

                    case R.id.user_logout:
                        FirebaseAuth.getInstance().signOut(); //logs out current user
                        startActivity(new Intent(getApplicationContext(), MainActivity.class));
                        break;

                }
                toggle.syncState();

                return true;
            }
        });
    }
}