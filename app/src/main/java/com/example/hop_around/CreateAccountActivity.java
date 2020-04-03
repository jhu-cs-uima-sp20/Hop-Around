package com.example.hop_around;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

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
            public void onClick(View view){

                Intent myIntent = new Intent(CreateAccountActivity.this, TermsAndServices.class);
                startActivity(myIntent);

            }
        });

        // when the Create Account button is pressed
        Button create = (Button) findViewById(R.id.create_account_btn);
        create.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {

                    //TODO maybe also have a requirement for passwords, and check that req is met?


                EditText newEmailTV = (EditText) findViewById(R.id.new_email_editText);
                EditText newPassTV = (EditText) findViewById(R.id.new_password_editText);
                EditText newPassConfirmTV = (EditText) findViewById(R.id.confirm_password_editText);
                String newEmail = newEmailTV.getText().toString().trim();
                String newPass = newPassTV.getText().toString().trim();
                String confirmPass = newPassConfirmTV.getText().toString().trim();

                //TODO DATABASE @Jerry, need add new account to DB/check that email hasn't already been registered (error handling)
                if (newEmail.length() == 0){
                    newEmailTV.setError("This field cannot be empty");
                }
                else if (newEmail.equals("user@jhu.edu")) {
                    newEmailTV.setError("An account with this email already exists");
                }
                //Passwords do not match
                else if (!newPass.equals(confirmPass)){
                    newPassTV.setError("Passwords do not match");
                    newPassConfirmTV.setError("Passwords do not match");
                }
                else if (newPass.length() == 0){
                    newPassTV.setError("This field cannot be empty");
                    newPassConfirmTV.setError("This field cannot be empty");
                }
                else {
                    //TODO @Jerry add to DB

                    Intent myIntent = new Intent(CreateAccountActivity.this, SetUpAccountActivity.class);
                    startActivity(myIntent);
                }

            }
        });

    }



    @Override
    public boolean onOptionsItemSelected(MenuItem item){

        finish();
        return true;
    }
}
