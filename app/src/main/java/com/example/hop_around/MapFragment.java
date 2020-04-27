package com.example.hop_around;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.graphics.Rect;
import android.graphics.RectF;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.util.Base64;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.MapsInitializer;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.BitmapDescriptor;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.Circle;
import com.google.android.gms.maps.model.CircleOptions;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
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
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;


public class MapFragment extends Fragment {
    public static final String MyPREFERENCES = "MyPrefs" ;
    MapView mMapView;
    private GoogleMap googleMap;
    static ArrayList<String> arrayList;
    SharedPreferences sharedpreferences;
    SharedPreferences.Editor editor;
    final DatabaseReference dbRoot = FirebaseDatabase.getInstance().getReference();
    final DatabaseReference popupsRef = dbRoot.child("popups");
    LatLng person;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        sharedpreferences = this.getActivity().getSharedPreferences(MyPREFERENCES, Context.MODE_PRIVATE);
        editor = sharedpreferences.edit();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {


        View rootView = inflater.inflate(R.layout.fragment_map, container, false);

        mMapView = (MapView) rootView.findViewById(R.id.mapView);
        mMapView.onCreate(savedInstanceState);

        mMapView.onResume(); // needed to get the map to display immediately

        try {
            MapsInitializer.initialize(getActivity().getApplicationContext());
        } catch (Exception e) {
            e.printStackTrace();
        }

        Set<String> set = sharedpreferences.getStringSet("key", null);

        /*
        if(set == null) {
            set.add("dummy");
            editor.putStringSet("key", set);
            editor.commit();
        }*/

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


                double maxX = 39.333977;
                double minX = 39.326170;
                double minY = -76.624140;
                double maxY = -76.618813;

                DecimalFormat df = new DecimalFormat(".######");
                double diffX = maxX - minX;
                double randomValueX = minX + Math.random( ) * diffX;

                double diffY = maxY - minY;
                double randomValueY = minY + Math.random( ) * diffY;

                person = new LatLng(randomValueX, randomValueY);
                googleMap.addMarker(new MarkerOptions().position(person).title("User"));



                // For zooming automatically to the location of the marker
                CameraPosition cameraPosition = new CameraPosition.Builder().target(person).zoom(12).build();
                googleMap.animateCamera(CameraUpdateFactory.newCameraPosition(cameraPosition));

                googleMap.setOnMapClickListener(new GoogleMap.OnMapClickListener() {
                    @Override
                    public void onMapClick(LatLng latLng) {
                        ValueEventListener popupsListListener = new ValueEventListener() {
                            @RequiresApi(api = Build.VERSION_CODES.N)
                            @Override
                            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                //Set<String> set = sharedpreferences.getStringSet("key", null);
                                /*if(set == null) {
                                    set = new HashSet<String>();
                                    set.add("dummy");
                                    editor.putStringSet("key", set);
                                    editor.commit();

                                }*/
                                int b = Math.toIntExact((long) dataSnapshot.child("popCount").getValue());

                                for (int i = 0; i < b; i++) {
                                    String bitmap = (String) dataSnapshot.child("popups").child("" + i).child("bitmap").getValue();
                                    String tag = (String) dataSnapshot.child("popups").child("" + i).child("tag").getValue();
                                    String x = (String) dataSnapshot.child("popups").child("" + i).child("x").getValue();
                                    String y = (String) dataSnapshot.child("popups").child("" + i).child("y").getValue();

                                    Bitmap popUpView = StringToBitMap(bitmap);
                                    Double latitude = Double.parseDouble(x);
                                    Double longitude = Double.parseDouble(y);
                                    LatLng position = new LatLng(latitude, longitude);

                                    double lat = person.latitude;
                                    double lng = person.longitude;

                                    String color;

                                    if (Math.abs(lat - latitude) > 0.00195175 && Math.abs(lng - longitude) > 0.00195175) {
                                        color = "green";
                                    }
                                    else {
                                        color = "gray";
                                    }

                                    BitmapDescriptor icon = BitmapDescriptorFactory.fromBitmap(getCircleBitmap(popUpView));
                                    Marker m = googleMap.addMarker(new MarkerOptions().position(position).snippet(tag).icon(icon).strokeColor());
                                    m.setTag(i);




                                }
                            }

                            @Override
                            public void onCancelled(@NonNull DatabaseError databaseError) {

                            }

                        };

                        dbRoot.addListenerForSingleValueEvent(popupsListListener);
                    }
                });

                googleMap.setOnMarkerClickListener(new GoogleMap.OnMarkerClickListener() {
                    @Override
                    public boolean onMarkerClick(final Marker marker) {
                        ValueEventListener yeetListener = new ValueEventListener() {
                            @RequiresApi(api = Build.VERSION_CODES.N)
                            @Override
                            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                //Todo:do what you need to do with dataSnapshot in here
                            int refId = (int) marker.getTag();

                            String title = (String) dataSnapshot.child("popups").child("" + refId).child("title").getValue();
                            String bitmap = (String) dataSnapshot.child("popups").child("" + refId).child("bitmap").getValue();
                            String tag = (String) dataSnapshot.child("popups").child("" + refId).child("tag").getValue();
                            String x = (String) dataSnapshot.child("popups").child("" + refId).child("x").getValue();
                            String y = (String) dataSnapshot.child("popups").child("" + refId).child("y").getValue();
                                Intent myIntent = new Intent(getActivity(), ViewPopup.class);
                                myIntent.putExtra("refId", refId);
                                myIntent.putExtra("title", title);
                                myIntent.putExtra("bitmap", bitmap);
                                myIntent.putExtra("tag", tag);
                                myIntent.putExtra("x", x);
                                myIntent.putExtra("y", y);
                                myIntent.putExtra("person", person);
                                getActivity().startActivity(myIntent);
                                //editor.putInt("refId", refId);
                        //startActivity(new Intent(getActivity(), ViewPopup.class));*/
                            }

                            @Override
                            public void onCancelled(@NonNull DatabaseError databaseError) {

                            }

                        };

                        dbRoot.addListenerForSingleValueEvent(yeetListener);
                        return false;
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

    private Bitmap getCircleBitmap(Bitmap bitmap) {
        final Bitmap output = Bitmap.createBitmap(bitmap.getWidth(),
                bitmap.getHeight(), Bitmap.Config.ARGB_8888);
        final Canvas canvas = new Canvas(output);

        final int color = Color.RED;
        final Paint paint = new Paint();
        final Rect rect = new Rect(0, 0, 200, 200);
        final RectF rectF = new RectF(rect);

        paint.setAntiAlias(true);
        canvas.drawARGB(0, 0, 0, 0);
        paint.setColor(color);
        canvas.drawOval(rectF, paint);

        paint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.SRC_IN));
        canvas.drawBitmap(bitmap, rect, rect, paint);

        bitmap.recycle();

        return output;
    }
}
