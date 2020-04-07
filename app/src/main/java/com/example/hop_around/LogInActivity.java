package com.example.hop_around;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toolbar;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class LogInActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_log_in);
        getSupportActionBar().setTitle("Welcome To Hop Around");


        TextView createAccount = (TextView) findViewById(R.id.create_account_textView);
        createAccount.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {

                Intent myIntent = new Intent(LogInActivity.this, CreateAccountActivity.class);
                startActivity(myIntent);

            }
        });

        TextView forgotPass = (TextView) findViewById(R.id.forgot_password_textView);
        forgotPass.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {

                Intent myIntent = new Intent(LogInActivity.this, ForgotPasswordActivity.class);
                startActivity(myIntent);
            }
        });

        Button signIn = (Button) findViewById(R.id.sign_in_btn);
        signIn.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {

                EditText emailTV = (EditText) findViewById(R.id.email_editText);
                EditText passwordTV = (EditText) findViewById(R.id.password_editText);
                final String email = emailTV.getText().toString().trim();
                final String password = passwordTV.getText().toString().trim();



                /*
                //important references
                DatabaseReference dbRoot = FirebaseDatabase.getInstance().getReference();
                DatabaseReference user_found = dbRoot.child("users");
                // db read /////////////////////////////////////////////////////////////////////////
                ValueEventListener userListListener = new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        if (dataSnapshot.hasChild(email)) {
                            Object o = dataSnapshot.child(email).getValue();
                            User user = (User) o;
                            if (user.getPassword().equals(password)){
                                //TODO: Launch the map activity or whatever happens next here! This onDataChange is what's supposed to occur in the line marked below
                                //TODO launching isn't working
                                Intent startMap = new Intent(LogInActivity.this, MapsView.class);
                                startActivity(startMap);
                            }
                            else{
                                EditText emailTV = (EditText) findViewById(R.id.email_editText);
                                EditText passwordTV = (EditText) findViewById(R.id.password_editText);
                                emailTV.setError("Email or password incorrect");
                                passwordTV.setError("Email or password incorrect");
                            }
                        }
                        else {
                            EditText emailTV = (EditText) findViewById(R.id.email_editText);
                            EditText passwordTV = (EditText) findViewById(R.id.password_editText);
                            emailTV.setError("Email or password incorrect");
                            passwordTV.setError("Email or password incorrect");
                        }
                    }
                    @Override
                    public void onCancelled(DatabaseError databaseError) {
                        final String TAG = "LoginActivity";
                        Log.w(TAG, "loadPost:onCancelled", databaseError.toException());
                    }
                };

                */

                if (email.length() == 0){
                    emailTV.setError("This field cannot be empty");
                }
                if (password.length() == 0) {
                    passwordTV.setError("This field cannot be empty");
                }
                else if (!email.equals("user@jhu.edu")) {
                    emailTV.setError("Invalid Username or Password");
                    passwordTV.setError("Invalid Username or Password");
                }
                else if (!password.equals("password")) {
                    emailTV.setError("Invalid Username or Password");
                    passwordTV.setError("Invalid Username or Password");
                }
                //logged in correctly
                else {

                    //See the declaration of the ValueEventListener above
                    //user_found.addListenerForSingleValueEvent(userListListener);

                    //TODO uncomment here for testing
                    Intent startMap = new Intent(LogInActivity.this, CollectionActivity.class);
                    startActivity(startMap);
                }

            }
        });

    }



}
