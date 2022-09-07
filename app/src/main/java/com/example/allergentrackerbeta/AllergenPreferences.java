package com.example.allergentrackerbeta;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;

import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.MenuItem;
import android.widget.ArrayAdapter;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;

public class AllergenPreferences extends AppCompatActivity {

    TextView settings, settingsDesc;
    ImageView settingsImg;
    ListView AllergensList;
    ArrayAdapter<String> allergenAdapter;
    String [] allergens = {"חיטה","שעורה","שיבולת שועל","אגוזים","בוטנים","חלב","ביצים","דגים","סויה","שומשום","שיפון","פירות ים"};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_allergen_preferences);

        //disable night mode display
        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);

        // action bar initialization
        ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);
        // Set BackgroundDrawable
        ColorDrawable colorDrawable = new ColorDrawable(Color.parseColor("#5C9CED"));
        actionBar.setBackgroundDrawable(colorDrawable);
        // set actionbar title
        actionBar.setTitle("התחברות");

        settings = findViewById(R.id.settings);
        settingsDesc = findViewById(R.id.settingsDesc);
        settingsImg = findViewById(R.id.settingsImg);
        AllergensList = findViewById(R.id.allergensListView);

        allergenAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_multiple_choice, allergens);
        AllergensList.setAdapter(allergenAdapter);
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