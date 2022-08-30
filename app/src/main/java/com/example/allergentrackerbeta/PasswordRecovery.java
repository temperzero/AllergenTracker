package com.example.allergentrackerbeta;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
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
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException;
import com.google.firebase.auth.FirebaseUser;

public class PasswordRecovery extends AppCompatActivity {

    private TextInputEditText email, password;
    private Button send;
    private FirebaseAuth profile;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_password_recovery);

        // action bar initialization
        ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);
        // Set BackgroundDrawable
        ColorDrawable colorDrawable = new ColorDrawable(Color.parseColor("#5C9CED"));
        actionBar.setBackgroundDrawable(colorDrawable);
        // set actionbar title
        actionBar.setTitle("איפוס סיסמה");

        email = findViewById(R.id.reset_email);
        password = findViewById(R.id.reset_password);
        send = findViewById(R.id.sendButton);

        send.setOnClickListener(new View.OnClickListener() {
                                    @Override
                                    public void onClick(View view) {
                                        String string_email = email.getText().toString();
                                        //String string_username = password.getText().toString();

                                        if (!LoginAndReg.checkEmail(string_email, email))
                                            return;
                                        Toast.makeText(getApplicationContext(), "yarosh sika", Toast.LENGTH_SHORT).show();
                                        resetPassword(string_email);
                                    }

                                }
        );
    }

        private void resetPassword(String email)
        {
            profile = FirebaseAuth.getInstance();
            Task<Void> resetPassTask = profile.sendPasswordResetEmail(email);
            resetPassTask.addOnCompleteListener(new ForgotPassCompleteListener());
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

    class ForgotPassCompleteListener implements OnCompleteListener<Void> {

        @Override
        public void onComplete(@NonNull Task<Void> task) {
            if(task.isSuccessful()) {
                //FirebaseUser login = fAuth.getCurrentUser();
                Toast.makeText(getApplicationContext(), "קישור לאיפוס הסיסמה נשלח אל כתובת האימייל", Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(PasswordRecovery.this, Menu.class);
            }
            else {
                try { throw task.getException(); }
                catch(FirebaseAuthInvalidCredentialsException e)
                {
                    Toast.makeText(getApplicationContext(), "בעיה בשליחת קישור לאיפוס הסיסמה לכתובת האימייל", Toast.LENGTH_SHORT).show();
                }
                catch (Exception e) { }
            }
        }

    }


}