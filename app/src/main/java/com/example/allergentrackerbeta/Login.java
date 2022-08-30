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

    final static String USERNAME_KEY = "username";
    final static String PASSWORD_KEY = "password";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

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
            }
        });

        //go to register button
        forgotPassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent passwordIntent = new Intent(Login.this, PasswordRecovery.class);
                startActivity(passwordIntent);
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

    class LoginCompleteListener implements OnCompleteListener<AuthResult> {

        @Override
        public void onComplete(@NonNull Task<AuthResult> task) {
            if(task.isSuccessful()) {
                FirebaseUser login = fAuth.getCurrentUser();
                if (login.isEmailVerified())
                {
                    Toast.makeText(getApplicationContext(),"ההתחברות בוצעה בהצלחה" , Toast.LENGTH_SHORT).show();
                    SharedPreferences prefs = getPreferences(Context.MODE_PRIVATE);
                    String string_username = mail.getText().toString();
                    String string_password = password.getText().toString();
                    SharedPreferences.Editor editor = prefs.edit();
                    editor.putString(USERNAME_KEY, string_username);
                    editor.putString(PASSWORD_KEY, string_password);
                    editor.apply();
                    Intent MenuIntent = new Intent(Login.this, Menu.class);
                    startActivity(MenuIntent);
                }
                else
                {
                    Toast.makeText(getApplicationContext(), "יש לאמת את המשתמש לפני התחברות", Toast.LENGTH_SHORT).show();
                }
            }
            else {
                try { throw task.getException(); }
                catch(FirebaseAuthInvalidCredentialsException e)
                {
                    Toast.makeText(getApplicationContext(), "ההתחברות נכשלה, אחד או יותר מהפרטים שהזנת שגויים", Toast.LENGTH_SHORT).show();
                }
                catch (Exception e) { }
            }
        }
    }
}