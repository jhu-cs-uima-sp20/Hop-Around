package com.example.hop_around;

public class PopupItem {
    private int mImageResource;
    private String mName;

    public PopupItem(int imageResource, String name) {
        mImageResource = imageResource;
        mName = name;
    }

    public int getImageResource() {
        return mImageResource;
    }

    public String getName() {
        return mName;
    }
}
