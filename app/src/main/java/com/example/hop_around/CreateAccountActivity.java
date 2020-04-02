package com.example.hop_around;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
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

                //TODO DATABASE @Jerry, need add new account to DB/check that email hasn't already been registered (error handling)
                    //TODO maybe also have a requirement for passwords, and check that req is met?

                //
            Intent myIntent = new Intent(CreateAccountActivity.this, SetUpAccountActivity.class);
            startActivity(myIntent);

            }
        });

    }



    @Override
    public boolean onOptionsItemSelected(MenuItem item){

        finish();
        return true;
    }
}
