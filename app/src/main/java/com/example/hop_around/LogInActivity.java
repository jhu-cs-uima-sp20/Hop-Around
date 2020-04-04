package com.example.hop_around;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toolbar;

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
                String email = emailTV.getText().toString().trim();
                String password = passwordTV.getText().toString().trim();

                //TODO check in database
                //Fow now, email = "user@jhu.edu"
                //Password = "password"

                //logged in incorrectly

                if (email.length() == 0){
                    emailTV.setError("This field cannot be empty");
                }
                if (password.length() == 0) {
                    passwordTV.setError("This field cannot be empty");
                }
                else if (!email.equals("user@jhu.edu") || !password.equals("password")){

                    emailTV.setError("Email or password incorrect");
                    passwordTV.setError("Email or password incorrect");
                }
                //logged in correctly
                else {
                    //TODO launch MAP Activity, maybe in meantime launch to their profile?

                }

            }
        });

    }



}
