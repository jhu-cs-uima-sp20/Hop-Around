package com.example.hop_around;

import android.app.Dialog;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.DialogFragment;

import com.google.android.material.chip.Chip;
import com.google.android.material.chip.ChipGroup;


public class PostActivity extends DialogFragment {

   /* public interface PopupNameDialogListener {
        void onFinishEditDialog(String inputText);
    }*/

    public void PostActivity() {

    }

    public static PostActivity newInstance(String title) {
        PostActivity frag = new PostActivity();
        Bundle args = new Bundle();
        args.putString("title", title);
        frag.setArguments(args);
        return frag;
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        return super.onCreateDialog(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.post_activity_layout, container, false);

    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        Button post = view.findViewById(R.id.btnDone);
        final EditText popUpName = view.findViewById(R.id.popUpName);
        String title = getArguments().getString("title", "Post Popup");
        final ChipGroup tags = view.findViewById(R.id.chipGroup);
        getDialog().setTitle(title);

        popUpName.requestFocus();
        //popUpName.setOnEditorActionListener(this);
        getDialog().getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_VISIBLE);
        getDialog().getWindow().setLayout(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        post.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String popUpTitle = popUpName.getText().toString();
                tags.setOnCheckedChangeListener(new ChipGroup.OnCheckedChangeListener() {
                    @Override
                    public void onCheckedChanged(ChipGroup group, int checkedId) {
                        Chip chip = group.findViewById(checkedId);
                        if(chip != null){
                            String chipTagSelected = chip.getText().toString();
                        }
                    }
                });
                //TODO create pop up with image saved, random location within hopkins parameters (longitudinal latitudinal), tags associated with popup, and popup title
                //popUpName.getText().toString()
                dismiss();
            }
        });

    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

}
