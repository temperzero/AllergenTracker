package com.example.allergentrackerbeta;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

public class SignUp extends AppCompatActivity {

    Button login, signup;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);

        // action bar initialization
        ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);
        // Set BackgroundDrawable
        ColorDrawable colorDrawable = new ColorDrawable(Color.parseColor("#5C9CED"));
        actionBar.setBackgroundDrawable(colorDrawable);
        // set actionbar title
        actionBar.setTitle("הרשמה");

        login = findViewById(R.id.signup_login);
        signup = findViewById(R.id.signupButton);

        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent LoginIntent = new Intent(SignUp.this, Login.class);
                startActivity(LoginIntent);
            }
        });

        signup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(getApplicationContext(), "nice", Toast.LENGTH_SHORT).show();
            }
        });
    }

    // back button enabled
    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                this.finish();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }
}