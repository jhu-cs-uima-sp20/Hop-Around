package com.example.hop_around;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import com.google.android.material.navigation.NavigationView;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import org.w3c.dom.Text;


public class NavigationDrawerActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {

    private DrawerLayout drawer;
    private NavigationView navigationView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_navdrawer);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        drawer = findViewById(R.id.drawer_layout);
        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        View headerView = navigationView.getHeaderView(0);

        final TextView pointCount = (TextView) headerView.findViewById(R.id.pts);
        final TextView displayName = (TextView) headerView.findViewById(R.id.disp);
        final DatabaseReference dbRoot = FirebaseDatabase.getInstance().getReference();
        SharedPreferences sharedPreferences = getSharedPreferences("MySharedPref", Context.MODE_PRIVATE);
        final String UID = sharedPreferences.getString("UID", "kidPizza");
        ValueEventListener Listener = new ValueEventListener() {
            @RequiresApi(api = Build.VERSION_CODES.N)
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                int q =  Math.toIntExact((long)dataSnapshot.child("users").child(UID).child("pts").getValue());
                String pts = "Hop Pts: " + Integer.toString(q);
                String display = "" + dataSnapshot.child("users").child(UID).child("displayName").getValue();
                pointCount.setText(pts);
                displayName.setText(display);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
            }
        };
        dbRoot.addListenerForSingleValueEvent(Listener);

        navigationView = findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this, drawer, toolbar,
                R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();


        if (savedInstanceState == null) {
            // Change to the Map Fragment later on
            getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container,
                    new MapFragment()).commit();
            navigationView.setCheckedItem(R.id.nav_map);
        }
    }

    @Override
    public void onBackPressed() {
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        }
        else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        switch(item.getItemId()) {
            case R.id.nav_map:
                getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container,
                        new MapFragment()).commit();
                break;
            case R.id.nav_collection:
                getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container,
                        new CollectionFragment()).commit();
                break;
            case R.id.nav_daily_page:
                getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container,
                        new DailyPageFragment()).commit();
                break;
            case R.id.nav_profile:
                getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container,
                        new ProfileFragment()).commit();
                break;
            //otherwise, logout was pressed
            default:
                Intent logOut = new Intent(NavigationDrawerActivity.this, LogInActivity.class);
                startActivity(logOut);
                finish();
        }
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }






}
