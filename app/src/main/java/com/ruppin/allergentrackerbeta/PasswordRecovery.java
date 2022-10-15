package com.ruppin.allergentrackerbeta;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;

import android.content.Context;
import android.content.Intent;
import android.content.res.Configuration;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException;

public class PasswordRecovery extends AppCompatActivity {

    private TextInputEditText email;
    private Button send;
    private FirebaseAuth profile;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_password_recovery);

        //disable night mode display
        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);

        // action bar initialization
        ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);
        // Set BackgroundDrawable
        ColorDrawable colorDrawable = new ColorDrawable(Color.parseColor("#5C9CED"));
        actionBar.setBackgroundDrawable(colorDrawable);
        // set actionbar title
        actionBar.setTitle("איפוס סיסמה");

        email = findViewById(R.id.reset_email);
        send = findViewById(R.id.sendButton);

        send.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String string_email = email.getText().toString();
                if (!CheckInput.checkEmail(string_email, email))
                    return;
                resetPassword(string_email);
            }
        });
    }

    private void resetPassword(String email)
    {
        profile = FirebaseAuth.getInstance();
        Task<Void> resetPassTask = profile.sendPasswordResetEmail(email);
        resetPassTask.addOnCompleteListener(new ForgotPassCompleteListener());
    }

    @Override
    protected void attachBaseContext(Context newBase)
    {
        final Configuration override = new Configuration(newBase.getResources().getConfiguration());
        override.fontScale = 1.0f;
        applyOverrideConfiguration(override);

        super.attachBaseContext(newBase);
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
        finish();
    }

    class ForgotPassCompleteListener implements OnCompleteListener<Void> {

        @Override
        public void onComplete(@NonNull Task<Void> task) {
            if(task.isSuccessful()) {
                Toast.makeText(getApplicationContext(), "קישור לאיפוס הסיסמה נשלח אל כתובת האימייל", Toast.LENGTH_SHORT).show();
                Intent menu = new Intent(PasswordRecovery.this, Menu.class);
                startActivity(menu);
                overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
            }
            else {
                try { throw task.getException(); }
                catch(FirebaseAuthInvalidCredentialsException e)
                {
                    Toast.makeText(getApplicationContext(), "בעיה בשליחת קישור לאיפוס הסיסמה לכתובת האימייל", Toast.LENGTH_SHORT).show();
                }
                catch (Exception e)
                {
                    Toast.makeText(getApplicationContext(), "בעיה בשליחת קישור לאיפוס הסיסמה לכתובת האימייל2", Toast.LENGTH_SHORT).show();
                }
            }
        }
    }
}