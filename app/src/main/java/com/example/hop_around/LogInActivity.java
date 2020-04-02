package com.example.hop_around;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
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
    }
}
