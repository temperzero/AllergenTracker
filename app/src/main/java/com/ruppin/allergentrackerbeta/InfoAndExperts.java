package com.ruppin.allergentrackerbeta;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;

import com.ruppin.allergentrackerbeta.R;

public class InfoAndExperts extends AppCompatActivity {

    Button info, experts, disclaimer;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_info_experts);

        //disable night mode display
        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);

        // action bar initialization
        ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);
        // Set BackgroundDrawable
        ColorDrawable colorDrawable = new ColorDrawable(Color.parseColor("#57B15A"));
        actionBar.setBackgroundDrawable(colorDrawable);
        // set actionbar title
        actionBar.setTitle("מידע שימושי");

        info = findViewById(R.id.infoButton);
        info.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                Intent InfoIntent = new Intent(InfoAndExperts.this, Info.class);
                startActivity(InfoIntent);
                //overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
            }
        });

        experts = findViewById(R.id.ExpertsButton);
        experts.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                Intent ExpertsIntent = new Intent(InfoAndExperts.this, Experts.class);
                startActivity(ExpertsIntent);
                //overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
            }
        });

        disclaimer = findViewById(R.id.DisclaimerBtn);
        disclaimer.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                Intent InfoIntent = new Intent(InfoAndExperts.this, Disclaimer.class);
                startActivity(InfoIntent);
                //overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
            }
        });
    }

    @Override
    public void finish() {
        super.finish();
        overridePendingTransition(R.anim.slide_in_left,R.anim.slide_out_right);
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

    @Override
    public void onBackPressed() {
        Intent MenuIntent = new Intent(InfoAndExperts.this, Menu.class);
        startActivity(MenuIntent);
        overridePendingTransition(R.anim.slide_in_left,R.anim.slide_out_right);
    }
}