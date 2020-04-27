package com.example.hop_around;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.util.Base64;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;

import com.google.android.gms.maps.model.BitmapDescriptor;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class SetUpAccountActivity extends AppCompatActivity {

    private Button next;
    private ImageView toEditProfilePhoto;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_set_up_account);

        ActionBar actionBar = getSupportActionBar();
        actionBar.setTitle("Welcome to Hop Around!");

        toEditProfilePhoto = (ImageView) findViewById(R.id.launch_editProfilePhoto);
        toEditProfilePhoto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent editPhoto = new Intent(SetUpAccountActivity.this, EditProfilePhotoActivity.class);
                startActivity(editPhoto);

            }
        });

            //NOTE: in THIS onclick for next button, it means user did NOT add a profile photo
        next = (Button) findViewById(R.id.next_btn);
        next.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {

                Intent next = new Intent(SetUpAccountActivity.this, LogInActivity.class);
                startActivity(next);
            }
        });

    }


    @Override
    public void onResume(){
        super.onResume();
        DatabaseReference dbRoot = FirebaseDatabase.getInstance().getReference();
        final ImageView pic = findViewById(R.id.launch_editProfilePhoto);
        SharedPreferences sharedPreferences = getSharedPreferences("MySharedPref", MODE_PRIVATE);
        final String UID = sharedPreferences.getString("UID", "kidPizza");
        ValueEventListener Listener = new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                Bitmap btm = StringToBitMap(""+dataSnapshot.child("users").child(UID).child("pfp").getValue());
                pic.setImageBitmap(btm);
            }
            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
            }
        };

        dbRoot.addListenerForSingleValueEvent(Listener);

        next = (Button) findViewById(R.id.next_btn);
        next.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {

                Intent next = new Intent(SetUpAccountActivity.this, LogInActivity.class);
                startActivity(next);
            }
        });
    }

    public Bitmap StringToBitMap(String encodedString) {
        try {
            byte[] encodeByte = Base64.decode(encodedString, Base64.DEFAULT);
            Bitmap bitmap = BitmapFactory.decodeByteArray(encodeByte, 0, encodeByte.length);
            return bitmap;
        } catch (Exception e) {
            e.getMessage();
            return null;
        }
    }

}
