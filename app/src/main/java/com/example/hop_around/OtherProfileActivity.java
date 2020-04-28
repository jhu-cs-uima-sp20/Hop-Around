package com.example.hop_around;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Build;
import android.os.Bundle;
import android.util.Base64;
import android.view.MenuItem;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class OtherProfileActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_other_profile);

        Intent intent = getIntent();
        String displayName = intent.getStringExtra("displayName");
        final String UID = intent.getStringExtra("uid");
        ActionBar actionBar = getSupportActionBar();
        actionBar.setTitle(displayName+"'s Profile"); //TODO set with query string passed through intent
        actionBar.setDisplayHomeAsUpEnabled(true);

        //TODO read from database same way as in Profile Fragment
        //ALL OF THIS WAS COPIED VERBATUM FROM PROFILE FRAGMENTS, only edits have been because of differences between activities/fragments

        final DatabaseReference dbRoot = FirebaseDatabase.getInstance().getReference();
        SharedPreferences sharedPreferences = this.getSharedPreferences("MySharedPref", Context.MODE_PRIVATE);

        //LOAD THE USER'S DATA: Hop points, display name, description, recently collected display!
        ValueEventListener BigListener = new ValueEventListener() {
            @RequiresApi(api = Build.VERSION_CODES.N)
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                int b = Math.toIntExact((long) dataSnapshot.child("popCount").getValue());
                if (b > 3) {
                    b = 3;
                }
                for (int i = 0; i < b; i++) {
                    if (dataSnapshot.child("users").child(UID).hasChild("" + i)) {
                        String title = (String) dataSnapshot.child("popups").child("" + i).child("title").getValue();
                        String bitmap = (String) dataSnapshot.child("popups").child("" + i).child("bitmap").getValue();
                        String viewString = "profile_popup_"+(i+1);

                        int id = getResources().getIdentifier(viewString, "id", "com.example.hop_around");
                        ImageView one = findViewById(id);
                        one.setImageBitmap(StringToBitMap(bitmap));



                    }
                }
                int pts = Math.toIntExact((long) dataSnapshot.child("users").child(UID).child("pts").getValue());
                String display = ""+dataSnapshot.child("users").child(UID).child("displayName").getValue();
                String description = ""+dataSnapshot.child("users").child(UID).child("description").getValue();
                TextView name = findViewById(R.id.profile_display_name);
                TextView desc = findViewById(R.id.profile_description);
                TextView hpts = findViewById(R.id.disp);
                TextView sentence = findViewById(R.id.display_names);
                name.setText(display);
                desc.setText(description);
                hpts.setText("Hop Points: "+pts);
                sentence.setText(display + "'s Recently Collected");
                ImageView pic = findViewById(R.id.profile_photo);
                Bitmap btm = StringToBitMap(""+dataSnapshot.child("users").child(UID).child("pfp").getValue());
                pic.setImageBitmap(btm);

            }
            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
            }
        };
        dbRoot.addListenerForSingleValueEvent(BigListener);




    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        //when user presses back button,
        finish();
        return true;
    }


    public Bitmap StringToBitMap(String encodedString){
        try{
            byte [] encodeByte = Base64.decode(encodedString,Base64.DEFAULT);
            Bitmap bitmap = BitmapFactory.decodeByteArray(encodeByte, 0, encodeByte.length);
            return bitmap;
        }
        catch(Exception e){
            e.getMessage();
            return null;
        }
    }
}
