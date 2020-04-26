package com.example.hop_around;

import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.util.Base64;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.MapsInitializer;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;


public class MapFragment extends Fragment {
    public static final String MyPREFERENCES = "MyPrefs" ;
    static boolean test;
    MapView mMapView;
    private GoogleMap googleMap;
    static ArrayList<String> arrayList;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
         test = false;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        final SharedPreferences sharedpreferences = this.getActivity().getSharedPreferences(MyPREFERENCES, Context.MODE_PRIVATE);
        final SharedPreferences.Editor editor = sharedpreferences.edit();
        View rootView = inflater.inflate(R.layout.fragment_map, container, false);

        mMapView = (MapView) rootView.findViewById(R.id.mapView);
        mMapView.onCreate(savedInstanceState);

        mMapView.onResume(); // needed to get the map to display immediately

        try {
            MapsInitializer.initialize(getActivity().getApplicationContext());
        } catch (Exception e) {
            e.printStackTrace();
        }

        ArrayList<String> arrList;
        if(test) {
            arrList = getArrayList("sw3g");
        }
        else {
            Set<String> set = new HashSet<String>();
            set.add("dummy");
            editor.putStringSet("key", set);
            editor.commit();

            arrList = new ArrayList<>();
            arrList.add("dummy");
            saveArrayList(arrList, "sw3g");
        }

        FloatingActionButton fab = rootView.findViewById(R.id.add_post);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //Snackbar.make(view, "Here's a Snackbar", Snackbar.LENGTH_LONG)
                // .setAction("Action", null).show();
                showPostDialog();
            }
        });


        mMapView.getMapAsync(new OnMapReadyCallback() {
            @Override
            public void onMapReady(GoogleMap mMap) {

                googleMap = mMap;

                // For showing a move to my location button
                //googleMap.setMyLocationEnabled(true);

                // For dropping a marker at a point on the Map
                LatLng sydney = new LatLng(-34, 151);
                googleMap.addMarker(new MarkerOptions().position(sydney).title("Marker Title").snippet("Marker Description"));

                // For zooming automatically to the location of the marker
                CameraPosition cameraPosition = new CameraPosition.Builder().target(sydney).zoom(12).build();
                googleMap.animateCamera(CameraUpdateFactory.newCameraPosition(cameraPosition));

                googleMap.setOnMapClickListener(new GoogleMap.OnMapClickListener() {
                    @Override
                    public void onMapClick(LatLng latLng) {

                        String bitmap = "iVBORw0KGgoAAAANSUhEUgAAADAAAAAwCAYAAABXAvmHAAAT6ElEQVR42tVZCXhU1dn+zr2z7zOZ7AnZCFlZkxBW2SJL2E0ElLJIlapVbBVU9C/8WPmtFKRaRaFQC5RFURAw7IQAAcKWfYHs22QmM5PZ95l7T89Q9dEWClp9fP7vec6TmXNPzn3f73zrGQT/D+TRjacom8MtZQJ+95k3C33ffoZ+bnD3k2e3loxvNrje9fr8mYzd+lnp5sXzf1YCnc3lfJZlBJgNIDJsielj2XutfWXXxcmnq3WHekw2kd/jx7lKz6ZjH/xm1c9KoL3xxkyvx/6Zw2JEIerQLr5I9FcyvScyfnj7t9f97t2dKVcM1JUrbS6ln8ODaJvFPWVkYuJHL8/W/awEmmrO036/p9Zu7EyVyhRYKJEhRIOLJ1AsikrIPfj1upVr3j5gPfdxgY4RAx0/rEMxvKBCxOW+uOXXk75D9GfxgdqrRRt8rr5VfD4P03we4gkk2Odxu/li9fCE1NF1x04Xj7V2Np04u/MDYVK0utydkV9QIx/ythqobX95fmLxT05g99HjaYjD5f5iWl71t+dP/umNx4Uhik+FMf3SzIb2Ci6PT0fHxQDLYsAs4xcqov9udHBew7TgRl1Le7TfbinCurZHl7/8unvSO2dvieyunUfXznzrJyWw85PPkEajOdKvf/LqX8zKr/16/uK7r2T3aXrenLNh99TP9+ygLFZnW1t1Rey8hZMruQLJ55gnP2pxo0Y3Kzhmd7rG95qslSIeZ8qiufmGVR9f4H9a3avLkHK2Hntj7qs/KYG9nx6Y0mt1fCKXiOOWPTbPGpwr2/wcx6btKuGq4oomvPreWzv/+vGosmtXL+Tm5Pw2RAJbZs7/JVN05nTQlN6va+58hiMPNaj4aOTj+RNav973VxtPvIh5nNPbVuTV/GQEtm7/q8hmMZcrQsPYp5YsSv96vuKDF57VVF55Tz54anKTi9vZ5QiUqMMi+miaKnj6mV8xwTX7i8680KY1";
                        String x = "5.0";
                        String y = "5.0";
                        String tag = "random";
                        String title = "dummy";
                        final DatabaseReference dbRoot = FirebaseDatabase.getInstance().getReference();
                        final DatabaseReference popupsRef = dbRoot.child("popups");
                        popupsRef.child(title).child("x").setValue(x);
                        popupsRef.child(title).child("y").setValue(y);
                        popupsRef.child(title).child("title").setValue(title);
                        popupsRef.child(title).child("tag").setValue(tag);
                        popupsRef.child(title).child("bitmap").setValue(bitmap);
                        ValueEventListener popupsListListener = new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                Set<String> set;
                                if(test) {
                                     set = sharedpreferences.getStringSet("key", null);
                                }
                                else {
                                    set = new HashSet<String>();
                                    set.add("dummy");
                                    editor.putStringSet("key", set);
                                    editor.commit();

                                }

                                for (Iterator<String> it = set.iterator(); it.hasNext(); ) {
                                    String element = it.next();
                                    String bitmap = (String) dataSnapshot.child(element).child("bitmap").getValue();
                                    String tag = (String) dataSnapshot.child(element).child("tag").getValue();
                                    String x = (String) dataSnapshot.child(element).child("x").getValue();
                                    String y = (String) dataSnapshot.child(element).child("y").getValue();

                                    Bitmap popUpView = StringToBitMap(bitmap);
                                    Double latitude = Double.parseDouble(x);
                                    Double longitude = Double.parseDouble(y);
                                    LatLng position = new LatLng(latitude, longitude);
                                    googleMap.addMarker(new MarkerOptions().position(position).snippet(tag).icon(BitmapDescriptorFactory.fromBitmap(popUpView)));
                                }
                                /*
                                for (int i = 0; i < set.size(); i++) {
                                    String bitmap = (String) dataSnapshot.child(arrList.get(i)).child("bitmap").getValue();
                                    String tag = (String) dataSnapshot.child(arrList.get(i)).child("tag").getValue();
                                    String x = (String) dataSnapshot.child(arrList.get(i)).child("x").getValue();
                                    String y = (String) dataSnapshot.child(arrList.get(i)).child("y").getValue();

                                    Bitmap popUpView = StringToBitMap(bitmap);
                                    Double latitude = Double.parseDouble(x);
                                    Double longitude = Double.parseDouble(y);
                                    LatLng position = new LatLng(latitude, longitude);
                                    googleMap.addMarker(new MarkerOptions().position(position).snippet(arrList.get(i)).icon(BitmapDescriptorFactory.fromBitmap(popUpView)));
                                }
                                 */


                            }

                            @Override
                            public void onCancelled(@NonNull DatabaseError databaseError) {

                            }

                        };

                        popupsRef.addListenerForSingleValueEvent(popupsListListener);
                    }
                });

            }
        });

        return rootView;
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

    public void saveArrayList(ArrayList<String> list, String key){
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(getContext());
        SharedPreferences.Editor editor = prefs.edit();
        Gson gson = new Gson();
        String json = gson.toJson(list);
        editor.putString(key, json);
        editor.apply();

    }

    public ArrayList<String> getArrayList(String key){
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(getContext());
        Gson gson = new Gson();
        String json = prefs.getString(key, null);
        Type type = new TypeToken<ArrayList<String>>() {}.getType();
        return gson.fromJson(json, type);
    }
    private void showPostDialog() {
        FragmentManager fm = getActivity().getSupportFragmentManager();
        PostActivity postFragmentDialog = PostActivity.newInstance("Some title");
        postFragmentDialog.show(fm, "fragment_edit");
    }

    @Override
    public void onResume() {
        super.onResume();
        mMapView.onResume();
    }

    @Override
    public void onPause() {
        super.onPause();
        mMapView.onPause();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        mMapView.onDestroy();
    }

    @Override
    public void onLowMemory() {
        super.onLowMemory();
        mMapView.onLowMemory();
    }
}
