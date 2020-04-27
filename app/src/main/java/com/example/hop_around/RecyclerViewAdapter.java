package com.example.hop_around;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

import de.hdodenhof.circleimageview.CircleImageView;

public class RecyclerViewAdapter extends RecyclerView.Adapter<RecyclerViewAdapter.ViewHolder> {
    private static final String TAG = "RecyclerViewAdapter";
    private ArrayList<PopupItem> mPopupList;
    private OnItemClickListener mListener;

    public interface OnItemClickListener {
        void onItemClick(int position);
    }

    public void setOnItemClickListener(OnItemClickListener listener){
        mListener = listener;
    }

    public RecyclerViewAdapter(ArrayList<PopupItem> popupList) {
        mPopupList = popupList;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.layout_popup_list, parent, false);
        ViewHolder holder = new ViewHolder(view, mListener);
        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        PopupItem currentItem = mPopupList.get(position);
        holder.popup_image.setImageBitmap(currentItem.getImageResource());
        holder.popup_name.setText(currentItem.getName());

    }

    @Override
    public int getItemCount() {
        return mPopupList.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {

        CircleImageView popup_image;
        TextView popup_name;

        public ViewHolder(@NonNull View itemView, final OnItemClickListener listener) {
            super(itemView);
            popup_image = itemView.findViewById(R.id.popup_image);
            popup_name = itemView.findViewById(R.id.popup_name);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (listener != null) {
                        int position = getAdapterPosition();
                        if (position != RecyclerView.NO_POSITION) {
                            listener.onItemClick(position);
                        }
                    }
                }
            });
        }
    }
}
