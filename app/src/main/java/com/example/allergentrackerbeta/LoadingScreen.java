package com.example.allergentrackerbeta;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;

import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.content.Intent;
import android.os.Bundle;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.ProgressBar;

public class LoadingScreen extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        //disable night mode display
        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);

        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_loading_screen);
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

        //ImageView aniView = (ImageView)findViewById(R.id.gifImageView);
        //ObjectAnimator mover = ObjectAnimator.ofFloat(aniView, "translationY", 600f, 0f);
        //mover.setDuration(3000);
        //ObjectAnimator fadeIn = ObjectAnimator.ofFloat(aniView, "alpha", 0f, 1f);
        //fadeIn.setDuration(3000);
        //AnimatorSet animatorSet = new AnimatorSet();
        //animatorSet.play(fadeIn).with(mover);
        //animatorSet.play(mover);
        //animatorSet.start();
    }
}