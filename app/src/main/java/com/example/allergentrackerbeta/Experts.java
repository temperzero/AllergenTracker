package com.example.allergentrackerbeta;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;

public class Experts extends AppCompatActivity {
    ImageView clalit, maccabi, meuhedet, leumit, yahel;
    Button back;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_experts);

        //disable night mode display
        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);

        // action bar initialization
        ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);
        // Set BackgroundDrawable
        ColorDrawable colorDrawable = new ColorDrawable(Color.parseColor("#57B15A"));
        actionBar.setBackgroundDrawable(colorDrawable);
        // set actionbar title
        actionBar.setTitle("מומחים");

        clalit = findViewById(R.id.clalit);
        clalit.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                Intent openlinkclalit = new Intent(Intent.ACTION_VIEW, Uri.parse("https://www.clalit.co.il/he/sefersherut/Pages/services_guide.aspx"));
                startActivity(openlinkclalit);
            }
        });

        maccabi = findViewById(R.id.maccabi);
        maccabi.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                Intent openlinkmaccabi = new Intent(Intent.ACTION_VIEW, Uri.parse("https://serguide.maccabi4u.co.il/heb/doctors/doctorssearchresults/?Field=098&PageNumber=1&RequestId=00000000-0000-0001-0000-000000000098&SelectedTab=1"));
                startActivity(openlinkmaccabi);
            }
        });

        leumit = findViewById(R.id.leumit);
        leumit.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                Intent openlinkleumit = new Intent(Intent.ACTION_VIEW, Uri.parse("https://www.leumit.co.il/heb/ServiceSearch/doctorSearch/"));
                startActivity(openlinkleumit);
            }
        });

        meuhedet = findViewById(R.id.meuhedet);
        meuhedet.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                Intent openlinkmeuhedet = new Intent(Intent.ACTION_VIEW, Uri.parse("https://www.meuhedet.co.il/%D7%96%D7%9B%D7%95%D7%99%D7%95%D7%AA-%D7%A9%D7%99%D7%A8%D7%95%D7%AA%D7%99%D7%9D-%D7%95%D7%91%D7%99%D7%98%D7%95%D7%97%D7%99%D7%9D/%D7%96%D7%9B%D7%95%D7%99%D7%95%D7%AA-%D7%A9%D7%99%D7%A8%D7%95%D7%AA%D7%99%D7%9D-%D7%95%D7%91%D7%99%D7%98%D7%95%D7%97%D7%99%D7%9D/%D7%98%D7%99%D7%A4%D7%95%D7%9C%D7%99%D7%9D/%D7%91%D7%99%D7%A7%D7%95%D7%A8-%D7%90%D7%A6%D7%9C-%D7%A8%D7%95%D7%A4%D7%90-%D7%9E%D7%95%D7%9E%D7%97%D7%94-%D7%9C%D7%90%D7%9C%D7%A8%D7%92%D7%99%D7%94-%D7%95%D7%90%D7%99%D7%9E%D7%95%D7%A0%D7%95%D7%9C%D7%95%D7%92%D7%99%D7%94/?q=%D7%90%D7%9C%D7%A8%D7%92%D7%99%D7%94"));
                startActivity(openlinkmeuhedet);
            }
        });

        yahel = findViewById(R.id.yahel);
        yahel.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                Intent openlinkyahel = new Intent(Intent.ACTION_VIEW, Uri.parse("https://www.allergyisrael.org.il"));
                startActivity(openlinkyahel);
            }
        });
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
        //overridePendingTransition(R.anim.slide_in_left,R.anim.slide_out_right);
    }
}