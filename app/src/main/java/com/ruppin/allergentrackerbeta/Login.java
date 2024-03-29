package com.ruppin.allergentrackerbeta;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.auth.AuthResult;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException;
import com.google.firebase.auth.FirebaseUser;

public class Login extends AppCompatActivity {

    Button login, goToRegister, forgotPassword;
    TextInputEditText mail, password;

    FirebaseAuth fAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

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

        mail = findViewById(R.id.login_username);
        password = findViewById(R.id.login_password);
        login = findViewById(R.id.loginButton);
        goToRegister = findViewById(R.id.loginToSignup);
        forgotPassword = findViewById(R.id.passwordRecovery);

        fAuth = FirebaseAuth.getInstance();

        // login button
        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                SharedPreferences prefs = getPreferences(Context.MODE_PRIVATE);
                String string_username = mail.getText().toString();
                String string_password = password.getText().toString();
                if(!(CheckInput.checkEmail(string_username, mail) && CheckInput.checkPassword(string_password, password)))
                    return;
                Task<AuthResult> loginTask = fAuth.signInWithEmailAndPassword(string_username, string_password);
                loginTask.addOnCompleteListener((Activity) view.getContext(), new LoginCompleteListener());
            }
        });

        //go to register button
        goToRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent SignupIntent = new Intent(Login.this, SignUp.class);
                startActivity(SignupIntent);
                overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
            }
        });

        //go to register button
        forgotPassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent passwordIntent = new Intent(Login.this, PasswordRecovery.class);
                startActivity(passwordIntent);
                overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
            }
        });
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
        Intent MenuIntent = new Intent(Login.this, Menu.class);
        startActivity(MenuIntent);
        overridePendingTransition(R.anim.slide_in_left,R.anim.slide_out_right);
    }

    class LoginCompleteListener implements OnCompleteListener<AuthResult> {

        @Override
        public void onComplete(@NonNull Task<AuthResult> task) {
            if(task.isSuccessful()) {
                FirebaseUser login = fAuth.getCurrentUser();
                if (login.isEmailVerified())
                {
                    Toast.makeText(getApplicationContext(),"ההתחברות בוצעה בהצלחה" , Toast.LENGTH_SHORT).show();
                    Intent MenuIntent = new Intent(Login.this, Menu.class);
                    startActivity(MenuIntent);
                    overridePendingTransition(R.anim.slide_in_left,R.anim.slide_out_right);
                }
                else
                {
                    fAuth.signOut();
                    Toast.makeText(getApplicationContext(), "יש לאמת את המשתמש לפני התחברות", Toast.LENGTH_SHORT).show();
                }
            }
            else {
                try { throw task.getException(); }
                catch(FirebaseAuthInvalidCredentialsException e)
                {
                    Toast.makeText(getApplicationContext(), "ההתחברות נכשלה, אחד או יותר מהפרטים שהזנת שגויים", Toast.LENGTH_SHORT).show();
                }
                catch (Exception e)
                {
                    //check internet connection
                    ConnectivityManager connectivityManager = (ConnectivityManager)getSystemService(Context.CONNECTIVITY_SERVICE);
                    if (!(connectivityManager.getNetworkInfo(ConnectivityManager.TYPE_MOBILE).getState() == NetworkInfo.State.CONNECTED) &&
                            !(connectivityManager.getNetworkInfo(ConnectivityManager.TYPE_WIFI).getState() == NetworkInfo.State.CONNECTED))
                        Toast.makeText(getApplicationContext(), "ההתחברות נכשלה, אין חיבור לאינטרנט", Toast.LENGTH_SHORT).show();
                    else
                        Toast.makeText(getApplicationContext(), "ההתחברות נכשלה, אחד או יותר מהפרטים שהזנת שגויים", Toast.LENGTH_SHORT).show();
                }
            }
        }
    }
}