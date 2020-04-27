package com.example.hop_around;

import android.graphics.Bitmap;

public class PopupItem {
    private Bitmap mImageResource;
    private String mName;
    private int mid;

    public PopupItem(Bitmap imageResource, String name, int id) {
        mImageResource = imageResource;
        mName = name;
        mid = id;
    }

    public Bitmap getImageResource() {
        return mImageResource;
    }

    public String getName() {
        return mName;
    }

    public int getId() {
        return mid;
    }
}
