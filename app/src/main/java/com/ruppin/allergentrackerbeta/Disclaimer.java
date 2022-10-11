package com.ruppin.allergentrackerbeta;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;

import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.MenuItem;
import android.widget.TextView;

public class Disclaimer extends AppCompatActivity {

    TextView disc0, disc1, disc2, disc3;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_disclaimer);

        //disable night mode display
        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);

        // action bar initialization
        ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);
        // Set BackgroundDrawable
        ColorDrawable colorDrawable = new ColorDrawable(Color.parseColor("#57B15A"));
        actionBar.setBackgroundDrawable(colorDrawable);
        // set actionbar title
        actionBar.setTitle("אחריות");

        // textviews
        disc0 = (TextView) findViewById(R.id.disc0);
        disc1 = (TextView) findViewById(R.id.disc1);
        disc2 = (TextView) findViewById(R.id.disc2);
        disc3 = (TextView) findViewById(R.id.disc3);

        disc0.setText("דבר שימוש באפליקציה");
        disc1.setText("חלק מהמידע אודות המוצרים באפליקציה מתקבל ממקורות שאינם באחריות צוות Allergen Tracker ואין בהצגתו יצירת מחויבות מצד הצוות לגבי נכונותו.\n");
        disc2.setText("באפליקציה ניתנת האפשרות למשתמשים רשומים להוסיף מוצרים באופן עצמאי ואין הצוות אחראי על פספוס של אלרגנים כשאלה יוצגו באפליקציה לאחר סריקת מוצרים.\n");
        disc3.setText("אי לכך ובהתאם לזאת, תמיד ניתן לבדוק את האלרגנים המצויים במוצר על ידי הסתכלות בגב אריזתו.\n");
    }

    @Override
    public void finish() {
        super.finish();
        //overridePendingTransition(R.anim.slide_in_left,R.anim.slide_out_right);
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
        finish();
    }
}