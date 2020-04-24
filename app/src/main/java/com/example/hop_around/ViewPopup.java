package com.example.hop_around;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

public class ViewPopup extends AppCompatActivity {
    private String popupName;
    private int position;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_popup);

        Intent intent = getIntent();
        popupName = intent.getStringExtra("name");
        position = intent.getIntExtra("position", 0);
        TextView nameView = findViewById(R.id.popup_name);
        nameView.setText(popupName);

    }
}
