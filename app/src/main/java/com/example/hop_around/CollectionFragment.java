package com.example.hop_around;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class CollectionFragment extends Fragment {

    private ArrayList<PopupItem> mPopupList;
    private RecyclerView mRecyclerView;
    private RecyclerViewAdapter mAdapter;
    private RecyclerView.LayoutManager mLayoutManager;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View view =  inflater.inflate(R.layout.fragment_collection, container, false);
        mPopupList = new ArrayList<>();
        mPopupList.add(new PopupItem(R.drawable.ic_person, "Name 1"));
        mPopupList.add(new PopupItem(R.drawable.ic_launcher_background, "Name 2"));
        mPopupList.add(new PopupItem(R.drawable.ic_map, "Name 3"));

        mRecyclerView = (RecyclerView) view.findViewById(R.id.recycler_collection);
        mRecyclerView.setHasFixedSize(true);
        mLayoutManager = new LinearLayoutManager(getActivity());
        mAdapter = new RecyclerViewAdapter(mPopupList );

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
        intent.putExtra("name", item.getName());
        intent.putExtra("image", item.getImageResource());
        intent.putExtra("position", position);
        startActivity(intent);
    }
}
