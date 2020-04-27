package com.example.hop_around;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.SearchView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;


public class ProfileFragment extends Fragment { //implements View.OnClickListener

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_profile, container, false);

        //EditTexts
        final EditText displayName = view.findViewById(R.id.profile_display_name);
        final EditText description = view.findViewById(R.id.profile_description);
        displayName.setEnabled(false);
        description.setEnabled(false);

        final DatabaseReference dbRoot = FirebaseDatabase.getInstance().getReference();


        //TODO: NEED TO LOAD THE USER'S DATA: Hop points, display name, description, recently collected display!
            //read from firebase




        //TODO in these onclicks: WRITE to firebase!!!
        //on clicks for editing display name, description, profile photo
        ImageView editDisplayName = (ImageView) view.findViewById(R.id.edit_display_name_btn);
        editDisplayName.setOnClickListener(new View.OnClickListener(){

            @Override
            public void onClick(View v) {
                displayName.setEnabled(true);

                displayName.setOnFocusChangeListener(new View.OnFocusChangeListener() {
                    @Override
                    public void onFocusChange(View v, boolean hasFocus) {

                        if(hasFocus){
                            //do nothing
                        }
                        else{
                            displayName.setEnabled(false);
                            successfulEdit(0);
                            Toast.makeText(getContext(), "New Display Name Saved!", Toast.LENGTH_LONG).show();
                            //TODO write to Database, save data, maybe have a toast saying New Display Name Saved!

                                    //TODO put in a METHOD
                        }
                    }
                });
                displayName.setOnKeyListener(new View.OnKeyListener() {
                    @Override
                    public boolean onKey(View v, int keyCode, KeyEvent event) {

                        if ((event.getAction() == KeyEvent.ACTION_DOWN) &&
                                (keyCode == KeyEvent.KEYCODE_ENTER)) {

                            displayName.setEnabled(false);
                            successfulEdit(0);
                            //TODO call same method as above in focusChangeListener
                            return true;
                        }
                        return false;
                    }
                });
            }

        });

        ImageView editDescription = (ImageView) view.findViewById(R.id.edit_description_button);
        editDescription.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                description.setEnabled(true);
                description.setOnEditorActionListener(new TextView.OnEditorActionListener() {
                    @Override
                    public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                        description.setOnFocusChangeListener(new View.OnFocusChangeListener() {
                            @Override
                            public void onFocusChange(View v, boolean hasFocus) {
                                if(hasFocus){
                                    //do nothing
                                }
                                else{
                                    description.setEnabled(false);
                                    successfulEdit(1);
                                    //TODO write to Database, save data, maybe have a toast saying New Display Name Saved!
                                        //TODO put in a METHOD
                                }
                            }
                        });
                        return false;
                    }
                });
                description.setOnKeyListener(new View.OnKeyListener() {
                    @Override
                    public boolean onKey(View v, int keyCode, KeyEvent event) {
                        if ((event.getAction() == KeyEvent.ACTION_DOWN) &&
                                (keyCode == KeyEvent.KEYCODE_ENTER)) {

                            description.setEnabled(false);
                            successfulEdit(1);
                            return true;
                        }
                        return false;
                    }
                });
            }
        });


        ImageView profilePhoto = (ImageView) view.findViewById(R.id.profile_photo);
        profilePhoto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent editPic = new Intent(getActivity(), EditProfilePhotoActivity.class);
                startActivity(editPic);
                successfulEdit(2);
            }
        });



        //TODO: need to add SEARCH BAR/SEARCH ACTIVITY
        setHasOptionsMenu(true); //THIS MAKES SEARCH VISIBLE FOR ALL THE FRAGMENTS
        //setMenuVisibility(true);

        return view;
    }

    public void successfulEdit(int field) {

        Context context = this.getContext();
        CharSequence text = "";
        int duration = Toast.LENGTH_SHORT;

        //TODO SAVE EDITED FIELD TO FIREBASE DB

        if (field == 0) { //displayName
            text = "New Display Name Saved";
        }
        else if (field == 1) { //description
            text = "New Description Saved";
        }
        else if (field == 2) { //profilePhoto
            text = "New Profile Photo Saved";
            //TODO need to re-update profile photo/display the new one if not happening automatically
                //DON'T NEED TO SAVE TO DB, because edit profile activity already does.
        }

        Toast toast = Toast.makeText(context, text, duration);
        toast.show();
    }


    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater){
        //inflater.inflate(R.menu.search_menu, menu);
        //super.onCreateOptionsMenu(menu, inflater);

        super.onCreateOptionsMenu(menu, inflater);
        menu.clear();
        inflater.inflate(R.menu.search_menu, menu);
        MenuItem item = menu.findItem(R.id.action_search);
        SearchView searchView = new SearchView(((NavigationDrawerActivity) getContext()).getSupportActionBar().getThemedContext());

        item.setShowAsAction(MenuItem.SHOW_AS_ACTION_COLLAPSE_ACTION_VIEW | MenuItem.SHOW_AS_ACTION_IF_ROOM);
        item.setActionView(searchView);

        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {

            @Override
            public boolean onQueryTextSubmit(String query) {
                //user presses enter
                //TODO SEARCH DATABASE FOR NAME


                return false;
            }
            @Override
            public boolean onQueryTextChange(String newText) {
                //user types a new character, TODO should search suggestions should be updated?
                return false;
            }
        });

        searchView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                //don't think I need anything in here
            }
        });

    }

}
