package com.example.hop_around;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;

public class EditProfilePhotoActivity extends AppCompatActivity {

    private static final int RESULT_LOAD_IMAGE=1;
    protected ImageView toUpload;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_profile_photo);

        ActionBar actionBar = getSupportActionBar();
        actionBar.setTitle("Create Account");
        actionBar.setDisplayHomeAsUpEnabled(true);

        Button continu = (Button) findViewById(R.id.continue_btn);
        continu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //TODO need to access this ImageView's data when continue is pressed and we return to SetUpAccount activity, right now nothing is being saved/stored
                finish();
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if(requestCode == RESULT_LOAD_IMAGE && resultCode == RESULT_OK && data != null) {
            Uri selectedImage = data.getData();
            toUpload.setImageURI(selectedImage); //TODO failing here when I pick an image from gallery on tablet, but not sure if it's because I"m using emulator
            //NOTE: will we need to access variable toUpload to display the image on the user's profile??
            //or can we just pull from the firebase DB at this point?


        }
    }

    //back button is pressed, so don't save anything
    @Override
    public boolean onOptionsItemSelected(MenuItem item){

        int id = item.getItemId();

        //edit button is selected
        if(id == R.id.edit_photo) {

            Intent gallery = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
            startActivityForResult(gallery, RESULT_LOAD_IMAGE);

        }

        else { //Back button was pressed
            finish();
        }
        return true;
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu, menu);
        return true;
    }

}