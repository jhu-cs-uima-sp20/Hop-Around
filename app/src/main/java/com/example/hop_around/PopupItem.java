package com.example.hop_around;

public class PopupItem {
    private int mImageResource;
    private String mName;
    private int id;

    public PopupItem(int imageResource, String name, int id) {
        mImageResource = imageResource;
        mName = name;
        mid = id;
    }

    public int getImageResource() {
        return mImageResource;
    }

    public String getName() {
        return mName;
    }

    public int getId() {
        return id;
    }
}
