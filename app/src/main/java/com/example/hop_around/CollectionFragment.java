package com.example.hop_around;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Build;
import android.os.Bundle;
import android.util.Base64;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.gms.maps.model.LatLng;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

import static android.content.Context.MODE_PRIVATE;

public class CollectionFragment extends Fragment {

    private ArrayList<PopupItem> mPopupList;
    private RecyclerView mRecyclerView;
    private RecyclerViewAdapter mAdapter;
    private RecyclerView.LayoutManager mLayoutManager;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_collection, container, false);
        mPopupList = new ArrayList<>();

        SharedPreferences sharedPreferences = this.getActivity().getSharedPreferences("MySharedPref", MODE_PRIVATE);
        final String UID = sharedPreferences.getString("UID", "kidPizza");
        final DatabaseReference dbRoot = FirebaseDatabase.getInstance().getReference();
        DatabaseReference usersRef = dbRoot.child("users");
        ValueEventListener swagListener = new ValueEventListener() {
            @RequiresApi(api = Build.VERSION_CODES.N)
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                int b = Math.toIntExact((long) dataSnapshot.child("popCount").getValue());
                for (int i = 0; i < b; i++) {
                    if (dataSnapshot.child("users").child(UID).hasChild("" + i)) {
                        String title = (String) dataSnapshot.child("popups").child("" + i).child("title").getValue();
                        String bitmap = (String) dataSnapshot.child("popups").child("" + i).child("bitmap").getValue();
                        mPopupList.add(new PopupItem(StringToBitMap(bitmap), title, i));
                        mAdapter.notifyDataSetChanged();
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
            }
        };
        dbRoot.addListenerForSingleValueEvent(swagListener);
        mPopupList.add(new PopupItem(StringToBitMap("bangohanwotabetai,ofuronihaitai,soretomo,watashi"), "title", 5));
        mRecyclerView = (RecyclerView) view.findViewById(R.id.recycler_collection);
        mRecyclerView.setHasFixedSize(true);
        mLayoutManager = new LinearLayoutManager(getActivity());
        mAdapter = new RecyclerViewAdapter(mPopupList);

        mRecyclerView.setLayoutManager(mLayoutManager);
        mRecyclerView.setAdapter(mAdapter);

        mAdapter.setOnItemClickListener(new RecyclerViewAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(int position) {
                showDetailView(position);
            }
        });

        return view;
    }

    public void showDetailView(int position) {
        Intent intent = new Intent(getActivity(), ViewPopup.class);
        PopupItem item = mPopupList.get(position);
        intent.putExtra("refId", item.getId());
        startActivity(intent);
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
