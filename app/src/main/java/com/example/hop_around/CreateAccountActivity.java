package com.example.hop_around;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class CreateAccountActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_account);

        ActionBar actionBar = getSupportActionBar();
        actionBar.setTitle("Create Account");
        actionBar.setDisplayHomeAsUpEnabled(true);

        TextView termsAndServices = (TextView) findViewById(R.id.terms_and_services);
        termsAndServices.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {

                Intent myIntent = new Intent(CreateAccountActivity.this, TermsAndServices.class);
                startActivity(myIntent);

            }
        });


        //THIS CREATES THE ROOT REFERENCE///////////////////////////////////////////////
        DatabaseReference dbRoot = FirebaseDatabase.getInstance().getReference();
        DatabaseReference usersRef = dbRoot.child("users");
        ////////////////////////////////////////////////////////////////////////////////


        // when the Create Account button is pressed
        Button create = (Button) findViewById(R.id.create_account_btn);
        create.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {

                //TODO maybe also have a requirement for passwords, and check that req is met?


                EditText newEmailTV = (EditText) findViewById(R.id.new_email_editText);
                EditText newPassTV = (EditText) findViewById(R.id.new_password_editText);
                EditText newPassConfirmTV = (EditText) findViewById(R.id.confirm_password_editText);
                final String newEmail = newEmailTV.getText().toString().trim();
                final String newPass = newPassTV.getText().toString().trim();
                String confirmPass = newPassConfirmTV.getText().toString().trim();

                //userID exists because json database doesn't allow '.' in keys for some reason
                final String userID = newEmail.substring(0, newEmail.length() - 8);

                final DatabaseReference dbRoot = FirebaseDatabase.getInstance().getReference();
                DatabaseReference userRef = dbRoot.child("users");

                ValueEventListener userListListener = new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        if (!dataSnapshot.hasChild(userID)) {
                            writeNewUser(userID, newPass, "Temporary Placeholder", dbRoot);
                            //HAHAHAHAHAHAHA IT WORKS JESUS CHRIST. YESSSSSS YYESSSSSSSSSSSSS HAHAhAHAHAHAEFBWFBSJKBFSKHF BHKSJFWBJ
                            Intent myIntent = new Intent(CreateAccountActivity.this, SetUpAccountActivity.class);
                            startActivity(myIntent);
                        } else {
                            EditText newEmailTV = (EditText) findViewById(R.id.new_email_editText);
                            newEmailTV.setError("An account associated with this email already exists");
                        }
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {
                        final String TAG = "CreateAccountActivity";
                        Log.w(TAG, "loadPost:onCancelled", databaseError.toException());
                    }
                };


                if (newEmail.length() == 0) {
                    newEmailTV.setError("This field cannot be empty");
                } else if (newEmail.length() < 9 || !newEmail.substring(newEmail.length() - 8).equals("@jhu.edu")) {
                    newEmailTV.setError("Must be a valid @jhu.edu address");
                }
                //Passwords do not match
                else if (!newPass.equals(confirmPass)) {
                    newPassTV.setError("Passwords do not match");
                    newPassConfirmTV.setError("Passwords do not match");
                } else if (newPass.length() == 0) {
                    newPassTV.setError("This field cannot be empty");
                    newPassConfirmTV.setError("This field cannot be empty");
                } else {
                    //See userListListener
                    userRef.addListenerForSingleValueEvent(userListListener);
                }

            }
        });

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        finish();
        return true;
    }

    //adds a new user to the database
    private void writeNewUser(String email, String password, String displayName, DatabaseReference dbRoot) {
        //CODE FROM USER-OBJECT IMPLEMENTATION - HERE JUST IN CASE
        //User user = new User(email, password, displayName);
        //dbRoot.child("users").child(email).setValue(user);

        //NEW SCHEMA CODE
        dbRoot.child("users").child(email).child("password").setValue(password);
        dbRoot.child("users").child(email).child("displayName").setValue(displayName);
        dbRoot.child("users").child(email).child("pts").setValue(0);
        dbRoot.child("users").child(email).child("pfp").setValue("default");
        dbRoot.child("users").child(email).child("description").setValue("What the fuck did you just fucking say about me, you little bitch? I'll have you know I graduated top of my class in the Navy Seals, and I've been involved in numerous secret raids on Al-Quaeda, and I have over 300 confirmed kills. I am trained in gorilla warfare and I'm the top sniper in the entire US armed forces. You are nothing to me but just another target. I will wipe you the fuck out with precision the likes of which has never been seen before on this Earth, mark my fucking words. You think you can get away with saying that shit to me over the Internet? Think again, fucker. As we speak I am contacting my secret network of spies across the USA and your IP is being traced right now so you better prepare for the storm, maggot. The storm that wipes out the pathetic little thing you call your life. You're fucking dead, kid. I can be anywhere, anytime, and I can kill you in over seven hundred ways, and that's just with my bare hands. Not only am I extensively trained in unarmed combat, but I have access to the entire arsenal of the United States Marine Corps and I will use it to its full extent to wipe your miserable ass off the face of the continent, you little shit. If only you could have known what unholy retribution your little \"clever\" comment was about to bring down upon you, maybe you would have held your fucking tongue. But you couldn't, you didn't, and now you're paying the price, you goddamn idiot. I will shit fury all over you and you will drown in it. You're fucking dead, kiddo.");
    }


}
