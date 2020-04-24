package com.example.hop_around;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentActivity;
import androidx.fragment.app.FragmentManager;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.util.Base64;
import android.view.View;
import android.widget.EditText;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;

/**
MapView rough example
________________________________________________________________________________________________________________________________________________________________________________
//Lisenter - Most of code should go in here, data is only accessible inside onDataChange////////////////////////////////////////////////////////////////////////////////////////

        ValueEventListener popupsListListener = new ValueEventListener() {
@Override
public void onDataChange(DataSnapshot dataSnapshot) {
        for i in [from sharedprefs somelist.legnth for example] {
        String bitmap = dataSnapshot.child("list[i]").child("bitmap").getValue();      //change "bitmap" to "x", "y", "title", or "tag" accordingly
        String tag = ...
        String x = ...
        String y = ...

        //Then do something with this popup info
        }

        //Anything else that needs to be done once outside of the loop

        }
@Override
public void onCancelled(DatabaseError databaseError) {
final String TAG = "MapView";
        Log.w(TAG, "loadPost:onCancelled", databaseError.toException());
        }
        };


        _____________________________________________________________________________________________________________________________________________________________________________
//References - to put anywhere where needed, okay to redeclare///////////////////////////////////////////////////////////////////////////////////////////////////////////////
        DatabaseReference dbRoot = FirebaseDatabase.getInstance().getReference();
        DatabaseReference popupsRef = dbRoot.child("popups");

        _____________________________________________________________________________________________________________________________________________________________________________
//Perform onDataChange - essentially calls onDataChange exactly once//////////////////////////////////////////////////////////////////////////////////////////////////////////
        popupsRef.addListenerForSingleValueEvent(popupsListListener);

*/

public class MapsView extends AppCompatActivity implements OnMapReadyCallback {
    private GoogleMap mMap;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps_view);
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

        FloatingActionButton fab = findViewById(R.id.add_post);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //Snackbar.make(view, "Here's a Snackbar", Snackbar.LENGTH_LONG)
                       // .setAction("Action", null).show();
                showPostDialog();
            }
        });
    }

    private void showPostDialog() {
        FragmentManager fm = getSupportFragmentManager();
        PostActivity postFragmentDialog = PostActivity.newInstance("Some title");
        postFragmentDialog.show(fm, "fragment_edit");

        //inal EditText popUpName = findViewById(R.id.popUpName);

    }

    /**
     * Manipulates the map once available.
     * This callback is triggered when the map is ready to be used.
     * This is where we can add markers or lines, add listeners or move the camera. In this case,
     * we just add a marker near Sydney, Australia.
     * If Google Play services is not installed on the device, the user will be prompted to install
     * it inside the SupportMapFragment. This method will only be triggered once the user has
     * installed Google Play services and returned to the app.
     */
    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;

        // Add a marker in Sydney and move the camera
        LatLng sydney = new LatLng(-34, 151);
        mMap.addMarker(new MarkerOptions().position(sydney).title("Marker in Sydney"));
        mMap.moveCamera(CameraUpdateFactory.newLatLng(sydney));
    }

    /**
     * @param encodedString
     * @return bitmap (from given string)
     */
    public Bitmap StringToBitMap(String encodedString){
        try {
            byte [] encodeByte= Base64.decode(encodedString,Base64.DEFAULT);
            Bitmap bitmap= BitmapFactory.decodeByteArray(encodeByte, 0, encodeByte.length);
            return bitmap;
        } catch(Exception e) {
            e.getMessage();
            return null;
        }
    }
}
