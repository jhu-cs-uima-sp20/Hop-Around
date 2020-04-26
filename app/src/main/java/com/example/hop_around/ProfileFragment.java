package com.example.hop_around;

import android.os.Bundle;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

public class ProfileFragment extends Fragment {

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        final View view = inflater.inflate(R.layout.fragment_profile, container, false);



        //listeners for EditTexts
        final EditText displayName = view.findViewById(R.id.profile_display_name);
        final EditText description = view.findViewById(R.id.profile_description);



        //on clicks for editing display name, description, profile photo

        ImageButton editDisplayName = (ImageButton) view.findViewById(R.id.edit_display_name_btn);
        editDisplayName.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
            // make display name editable

                displayName.setEnabled(true);

                displayName.setOnFocusChangeListener(new View.OnFocusChangeListener() {
                    @Override
                    public void onFocusChange(View v, boolean hasFocus) {

                        if (!hasFocus) {
                            // Validate youredittext

                            displayName.setEnabled(false);
                        }

                    }
                });

            }
        });

        ImageButton editDescription = (ImageButton) view.findViewById(R.id.edit_description_button);
        editDescription.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                //make description editable

                description.setEnabled(true);

            }
        });


        return inflater.inflate(R.layout.fragment_profile, container, false);

    }
}
