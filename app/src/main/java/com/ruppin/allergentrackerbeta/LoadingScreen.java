package com.ruppin.allergentrackerbeta;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;

import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.view.WindowManager;
import android.widget.TextView;
import android.widget.Toast;


public class LoadingScreen extends AppCompatActivity {

    TextView versionName;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //disable night mode display
        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_loading_screen);

        versionName = findViewById(R.id.ver_num);
        versionName.setText("version " + BuildConfig.VERSION_NAME);

        //check internet connection on apps startup
        ConnectivityManager connectivityManager = (ConnectivityManager)getSystemService(Context.CONNECTIVITY_SERVICE);
        if (!(connectivityManager.getNetworkInfo(ConnectivityManager.TYPE_MOBILE).getState() == NetworkInfo.State.CONNECTED) &&
                !(connectivityManager.getNetworkInfo(ConnectivityManager.TYPE_WIFI).getState() == NetworkInfo.State.CONNECTED)) {
            Toast.makeText(getApplicationContext(), "אין חיבור לאינטרנט", Toast.LENGTH_SHORT).show();
        }

        new Thread(new Runnable()
        {
            @Override
            public void run()
            {
                try
                {
                    synchronized (this)
                    {
                        wait(5000);
                        Intent intent = new Intent(LoadingScreen.this, Menu.class);
                        startActivity(intent);
                        finish();
                    }
                }
                catch (InterruptedException e)
                {
                    e.printStackTrace();
                }
            }
        }).start();
    }
}