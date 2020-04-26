package com.example.hop_around;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.provider.MediaStore;
import android.text.TextUtils;
import android.util.Base64;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.DialogFragment;

import com.google.android.material.chip.Chip;
import com.google.android.material.chip.ChipGroup;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.io.ByteArrayOutputStream;
import java.lang.reflect.Array;
import java.lang.reflect.Type;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Map;
import java.util.Set;

public class PostActivity extends DialogFragment {
    ArrayList<String> arrList = new ArrayList<>();
    ImageView postImg;
    ChipGroup tags;

    final DatabaseReference dbRoot = FirebaseDatabase.getInstance().getReference();
    private static final int CAPTURE_IMAGE_ACTIVITY_REQUEST_CODE = 1888;
   /* public interface PopupNameDialogListener {
        void onFinishEditDialog(String inputText);
    }*/

    public PostActivity() {

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
        View view = getActivity().getLayoutInflater().inflate(R.layout.post_activity_layout, new LinearLayout(getActivity()), false);


        // Build dialog
        Dialog builder = new Dialog(getActivity());
        builder.requestWindowFeature(Window.FEATURE_NO_TITLE);
        builder.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
        builder.setContentView(view);
        return builder;
}

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.post_activity_layout, container, false);

    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        final SharedPreferences sharedpreferences = this.getActivity().getSharedPreferences(MapFragment.MyPREFERENCES, Context.MODE_PRIVATE);
        final SharedPreferences.Editor editor = sharedpreferences.edit();
        super.onViewCreated(view, savedInstanceState);
        Button post = view.findViewById(R.id.btnDone);
        final EditText popUpName = view.findViewById(R.id.popUpName);
        String title = getArguments().getString("title", "Post Popup");
        tags = view.findViewById(R.id.chipGroup);
        getDialog().setTitle(title);
        postImg  = view.findViewById(R.id.imageView2);

        String defaultPostIm = "/9j/4AAQSkZJRgABAQAAAQABAAD/";
        postImg.setClickable(true);
        popUpName.requestFocus();
        //popUpName.setOnEditorActionListener(this);
        getDialog().getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_VISIBLE);
        getDialog().getWindow().setLayout(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        postImg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                startActivityForResult(intent,
                        CAPTURE_IMAGE_ACTIVITY_REQUEST_CODE);

            }
        });

        //DB-Refs////////////////////////////////////////////////////////////////////////////////////
        final DatabaseReference dbRoot = FirebaseDatabase.getInstance().getReference();
        final DatabaseReference popupsRef = dbRoot.child("popups");
        ////////////////////////////////////////////////////////////////////////////////////////////


        post.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final String popUpTitle = popUpName.getText().toString();

                Set<String> set = sharedpreferences.getStringSet("key", null);
                set.add(popUpTitle);
                editor.putStringSet("key", set);
                editor.commit();

                /*
                arrList = getArrayList("sw3g");
                arrList.add(popUpTitle);
                saveArrayList(arrList, "sw3g");
                 */

                int idChip = tags.getCheckedChipId();
                Chip chip = (Chip) tags.findViewById(idChip);

                //TODO: save the string called popUpTitle right?
                popupsRef.child(popUpTitle).child("title").setValue(popUpTitle);
                System.out.println("title set");

                Bitmap bitmap = ((BitmapDrawable)postImg.getDrawable()).getBitmap();
                //final DatabaseReference dbRoot = FirebaseDatabase.getInstance().getReference();
                //final DatabaseReference popupsRef = dbRoot.child("popups");
                String bitmapString = BitMapToString(bitmap);
                popupsRef.child(popUpTitle).child("bitmap").setValue(bitmapString);

                System.out.println("Chip Set");
                popupsRef.child(popUpTitle).child("tag").setValue(chip.getText().toString());


                double maxX = 39.333977;
                double minX = 39.326170;
                double minY = -76.624140;
                double maxY = -76.618813;

                DecimalFormat df = new DecimalFormat(".######");
                double diffX = maxX - minX;
                double randomValueX = minX + Math.random( ) * diffX;

                double diffY = maxY - minY;
                double randomValueY = minY + Math.random( ) * diffY;

                System.out.println(df.format(randomValueX));
                System.out.println(df.format(randomValueY));
                //TODO: save doubles randomValueX and randomValueY right?
                popupsRef.child(popUpTitle).child("x").setValue(String.valueOf(randomValueX));
                popupsRef.child(popUpTitle).child("y").setValue(String.valueOf(randomValueY));
                //TODO create pop up with image saved, random location within hopkins parameters (longitudinal latitudinal), tags associated with popup, and popup title
                //popUpName.getText().toString()
                MapFragment.test = true;
                dismiss();
            }
        });

    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    public String BitMapToString(Bitmap bitmap){
        ByteArrayOutputStream baos=new  ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.PNG,100, baos);
        byte [] b=baos.toByteArray();
        String temp=Base64.encodeToString(b, Base64.DEFAULT);
        return temp;
    }

    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == CAPTURE_IMAGE_ACTIVITY_REQUEST_CODE) {

            if (resultCode == Activity.RESULT_OK) {

                Bitmap bmp = (Bitmap) data.getExtras().get("data");
                ByteArrayOutputStream stream = new ByteArrayOutputStream();

                bmp.compress(Bitmap.CompressFormat.PNG, 100, stream);
                byte[] byteArray = stream.toByteArray();

                // convert byte array to Bitmap

                Bitmap bitmap = BitmapFactory.decodeByteArray(byteArray, 0,
                        byteArray.length);

                //TODO:This bitmaps variable named bitmap should should be saved right?

                //final DatabaseReference dbRoot = FirebaseDatabase.getInstance().getReference();
                //final DatabaseReference popupsRef = dbRoot.child("popups");
                //String bitmapString = BitMapToString(bitmap);
                //popupsRef.child(popUpTitle).child("bitmap").setValue(bitmapString);

                postImg.setImageBitmap(bitmap);
            }
        }
    }
    public void saveArrayList(ArrayList<String> list, String key){
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(getContext());
        SharedPreferences.Editor editor = prefs.edit();
        Gson gson = new Gson();
        String json = gson.toJson(list);
        editor.putString(key, json);
        editor.apply();

    }
    public ArrayList<String> getArrayList(String key){
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(getContext());
        Gson gson = new Gson();
        String json = prefs.getString(key, null);
        Type type = new TypeToken<ArrayList<String>>() {}.getType();
        return gson.fromJson(json, type);
    }

}