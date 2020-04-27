package com.example.hop_around;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.util.Base64;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.google.android.gms.maps.model.BitmapDescriptor;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.CircleOptions;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class ViewPopup extends AppCompatActivity {
    final DatabaseReference dbRoot = FirebaseDatabase.getInstance().getReference();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_popup);

        final Intent intent = getIntent();
        final int i = intent.getIntExtra("refId", 0);
        final Button collect = findViewById(R.id.collect_button);

        ValueEventListener popupListener = new ValueEventListener() {
            @RequiresApi(api = Build.VERSION_CODES.N)
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                String title = (String) dataSnapshot.child("popups").child(""+i).child("title").getValue();
                String bitmap = (String) dataSnapshot.child("popups").child(""+i).child("bitmap").getValue();
                String tag = (String) dataSnapshot.child("popups").child(""+i).child("tag").getValue();
                String x = (String) dataSnapshot.child("popups").child(""+i).child("x").getValue();
                String y = (String) dataSnapshot.child("popups").child(""+i).child("y").getValue();

                Bitmap popUpView = StringToBitMap(bitmap);
                Double latitude = Double.parseDouble(x);
                Double longitude = Double.parseDouble(y);
                LatLng position = new LatLng(latitude, longitude);

                TextView nameView = findViewById(R.id.popup_name);
                nameView.setText(title);

                TextView tagView = findViewById(R.id.popup_tag);
                tagView.setText(tag);

                ImageView popupImage = findViewById(R.id.popup_image);
                popupImage.setImageBitmap(popUpView);
                LatLng person = intent.getExtras().getParcelable("person");
                double lat = person.latitude;
                double lng = person.longitude;
                if (Math.abs(lat - latitude) > 0.00195175 && Math.abs(lng - longitude) > 0.00195175) {
                    collect.setEnabled(true);
                    collect.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            Toast.makeText(ViewPopup.this, "You're out of range!", Toast.LENGTH_LONG).show();
                        }
                    });
                }
                else {
                    collect.setEnabled(true);
                    collect.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            Toast.makeText(ViewPopup.this, "You got it!", Toast.LENGTH_LONG).show();
                        }
                    });
                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }

        };

        dbRoot.addListenerForSingleValueEvent(popupListener);


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
