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

                DatabaseReference dbRoot = FirebaseDatabase.getInstance().getReference();
                DatabaseReference user_found = dbRoot.child("users");

                ValueEventListener userListListener = new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        if (!dataSnapshot.hasChild(newEmail)) {
                            //If all validation passes, add user info to database
                            DatabaseReference dbRoot = FirebaseDatabase.getInstance().getReference();
                            writeNewUser(newEmail, newPass, "Temporary Placeholder");

                            Intent myIntent = new Intent(CreateAccountActivity.this, SetUpAccountActivity.class);
                            startActivity(myIntent);
                            //TODO nothing is actually launching...
                        }
                        else {
                            EditText newEmailTV = (EditText) findViewById(R.id.new_email_editText);
                            newEmailTV.setError("An Account associated with this email already exists");
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
                } else if ( newEmail.length() < 9 || !newEmail.substring(newEmail.length() - 8).equals("@jhu.edu")) {
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
                    //SEE userListListener above
                    //user_found.addListenerForSingleValueEvent(userListListener);
                    Intent intent = new Intent(CreateAccountActivity.this, SetUpAccountActivity.class);
                    startActivity(intent);
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
    private void writeNewUser(String email, String password, String displayName) {
        DatabaseReference dbRoot = FirebaseDatabase.getInstance().getReference();
        User user = new User(email, password, displayName);
        dbRoot.child("users").child(email).setValue(user);
    }


}
