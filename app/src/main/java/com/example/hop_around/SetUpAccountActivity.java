package com.example.hop_around;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;

public class SetUpAccountActivity extends AppCompatActivity {

    private Button next;
    private ImageView toEditProfilePhoto;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_set_up_account);

        ActionBar actionBar = getSupportActionBar();
        actionBar.setTitle("Welcome to Hop Around!");

        toEditProfilePhoto = (ImageView) findViewById(R.id.launch_editProfilePhoto);
        toEditProfilePhoto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent editPhoto = new Intent(SetUpAccountActivity.this, EditProfilePhotoActivity.class);
                startActivity(editPhoto);

            }
        });

        //TODO @Jerry when user presses next button, need to save info to DB
            //NOTE: in THIS onclick for next button, it means user did NOT add a profile photo
        next = (Button) findViewById(R.id.next_btn);
        next.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {

                //TODO ensure profile is done being created (values in all the text fields)
                //TODO then launch into map activity!
                //Intent next = new Intent(SetUpAccountActivity.this, MapsView.class);
                //startActivity(next);
            }
        });

    }

    //TODO @Jerry Database things, this will be called when user returns to the activity after EditProfileActivity.

    @Override
    public void onResume(){
        super.onResume();

        //TODO @Jerry when user presses next button, need to save info to DB
            //NOTE: in THIS onClick for next button, it means user DID add a profile photo
        next = (Button) findViewById(R.id.next_btn);
        next.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {

                //TODO ensure profile is done being created (values in all the text fields)
                //TODO then launch into map activity!
                //Intent next = new Intent(SetUpAccountActivity.this, TODOMapView.class);
                //startActivity(next);
            }
        });
    }



}
